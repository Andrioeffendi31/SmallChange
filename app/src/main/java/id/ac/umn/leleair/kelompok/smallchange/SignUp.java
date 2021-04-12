package id.ac.umn.leleair.kelompok.smallchange;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends Fragment {
    private TextInputLayout mEmailSignUpWrapper, mUsernameSignUpWrapper, mPassSignUpWrapper;
    private Button btnReg;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern EmailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private Pattern PassPattern = Pattern.compile(PASSWORD_PATTERN);
    private Matcher matcherEmail, matcherPass;

    private ProgressDialog mDialog;

    //Firebase Authentification
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(getActivity());
        //Inflate View
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mEmailSignUpWrapper = (TextInputLayout) view.findViewById(R.id.editEmailSignUpWrapper);
        mPassSignUpWrapper = (TextInputLayout) view.findViewById(R.id.editPasswordSignUpWrapper);
        mUsernameSignUpWrapper = (TextInputLayout) view.findViewById(R.id.editUsernameSignUpWrapper);
        btnReg = (Button) view.findViewById(R.id.btnSignUp);

        mEmailSignUpWrapper.setHint("E-mail");
        mPassSignUpWrapper.setHint("Password");
        mUsernameSignUpWrapper.setHint("Username");

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailSignUp = mEmailSignUpWrapper.getEditText().getText().toString().trim();
                String usernameSignUp = mUsernameSignUpWrapper.getEditText().getText().toString().trim();
                String passSignUp = mPassSignUpWrapper.getEditText().getText().toString().trim();

                if(!TextUtils.isEmpty(emailSignUp) && !TextUtils.isEmpty(usernameSignUp) && !TextUtils.isEmpty(passSignUp)){
                    if (!validateEmail(emailSignUp)) {
                        mEmailSignUpWrapper.setError("Not a valid email address!");
                    }
                    else if (!validateUsername(usernameSignUp)) {
                        mUsernameSignUpWrapper.setError("Username too long!");
                    }
                    //The password must be at least 8 characters long and include a number, lowercase letter, uppercase letter and special character
                    else if (!validatePassword(passSignUp)) {
                        mPassSignUpWrapper.setError("Not a valid password!\n" +
                                "Must be 8 characters (number, lowercase, uppercase and special character)");
                    }
                    else {
                        mEmailSignUpWrapper.setErrorEnabled(false);

                        mUsernameSignUpWrapper.setErrorEnabled(false);

                        mDialog.setMessage("Processing...");
                        mDialog.show();
                        mAuth.createUserWithEmailAndPassword(emailSignUp,passSignUp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mDialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), "Registration Complete", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity().getApplicationContext(),HomeActivity.class));
                                }
                                else {
                                    mDialog.dismiss();
                                    mPassSignUpWrapper.setErrorEnabled(true);
                                    mPassSignUpWrapper.setError("Incorrect e-mail or password");
                                    Toast.makeText(getActivity().getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else {
                    if(TextUtils.isEmpty(emailSignUp)){
                        mEmailSignUpWrapper.setError("Email Required...");
                        return;
                    }
                    if(TextUtils.isEmpty(usernameSignUp)){
                        mUsernameSignUpWrapper.setError("Username Required...");
                        return;
                    }
                    if(TextUtils.isEmpty(passSignUp)){
                        mPassSignUpWrapper.setError("Password Required...");
                    }
                }

            }
        });

        return view;
    }

    public boolean validateEmail(String email) {
        matcherEmail = EmailPattern.matcher(email);
        return matcherEmail.matches();
    }

    public boolean validateUsername(String username) {
        return username.length() <= 15;
    }

    public boolean validatePassword(String password) {
        matcherPass = PassPattern.matcher(password);
        return matcherPass.matches();
    }
}