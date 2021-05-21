package id.ac.umn.leleair.kelompok.smallchange;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignIn extends Fragment {
    private TextInputLayout mEmailSignInWrapper, mPassSignInWrapper;
    private Button btnLogin;
    private TextView mForgetPassword;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern EmailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private Pattern PassPattern = Pattern.compile(PASSWORD_PATTERN);
    private Matcher matcherEmail, matcherPass;

    private ProgressDialog mDialog;

    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set Auth instance
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(getActivity());

        //Inflate view
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mEmailSignInWrapper = (TextInputLayout) view.findViewById(R.id.editEmailSignInWrapper);
        mPassSignInWrapper = (TextInputLayout) view.findViewById(R.id.editPasswordSignInWrapper);
        btnLogin = (Button) view.findViewById(R.id.btnSignIn);
        mForgetPassword = (TextView) view.findViewById(R.id.ForgetPassword);

        mEmailSignInWrapper.setHint("E-mail");
        mPassSignInWrapper.setHint("Password");


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailSignIn = mEmailSignInWrapper.getEditText().getText().toString().trim();
                String passSignIn = mPassSignInWrapper.getEditText().getText().toString().trim();

                if(!TextUtils.isEmpty(emailSignIn) && !TextUtils.isEmpty(passSignIn)){
                    if (!validateEmail(emailSignIn)) {
                        mEmailSignInWrapper.setError("Not a valid email address!");
                    }
                    //The password must be at least 8 characters long and include a number, lowercase letter, uppercase letter and special character
                    else if (!validatePassword(passSignIn)) {
                        mPassSignInWrapper.setError("Not a valid password!\n" +
                                "Must be 8 characters (number, lowercase, uppercase and special character)");
                    }
                    else {
                        mEmailSignInWrapper.setErrorEnabled(false);
                        mPassSignInWrapper.setErrorEnabled(false);

                        mDialog.setMessage("Processing...");
                        mDialog.show();
                        mAuth.signInWithEmailAndPassword(emailSignIn,passSignIn).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mDialog.dismiss();
                                    moveToHome();
                                    Toast.makeText(getActivity().getApplicationContext(), "Sign In Succesfull", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    mDialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), "Incorect E-mail or Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else {
                    if(TextUtils.isEmpty(emailSignIn)){
                        mEmailSignInWrapper.setError("Email Required...");
                        return;
                    }
                    if(TextUtils.isEmpty(passSignIn)){
                        mPassSignInWrapper.setError("Password Required...");
                        return;
                    }
                }
            }
        });

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        return view;
    }


    public void forgotPassword() {
        //Change Password Form
        Dialog forgotPassDialog = new Dialog(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myviewm = inflater.inflate(R.layout.forgot_password, null);
        forgotPassDialog.setContentView(myviewm);
        forgotPassDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.41);

        forgotPassDialog.getWindow().setLayout(width, height);

        EditText editConfirmEmail = myviewm.findViewById(R.id.tvEmailConfirm);

        Button btnSend = myviewm.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ConfirmEmail = editConfirmEmail.getText().toString().trim();

                if(TextUtils.isEmpty(ConfirmEmail)){
                    editConfirmEmail.setError("Required Field");
                    return;
                }
                else if (!validateEmail(ConfirmEmail)){
                    editConfirmEmail.setError("Not a valid email address!");
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(ConfirmEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                                    forgotPassDialog.dismiss();
                                    Log.d("Status", "Email sent.");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Email not registered", Toast.LENGTH_SHORT).show();
                                forgotPassDialog.dismiss();
                                Log.d("Status", "Failed to sent.");
                            }
                });
            }
        });
        forgotPassDialog.show();
    }


    public boolean validateEmail(String email) {
        matcherEmail = EmailPattern.matcher(email);
        return matcherEmail.matches();
    }

    public boolean validatePassword(String password) {
        matcherPass = PassPattern.matcher(password);
        return matcherPass.matches();
    }
    private void moveToHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}