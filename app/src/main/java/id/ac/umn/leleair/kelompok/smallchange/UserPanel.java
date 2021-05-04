package id.ac.umn.leleair.kelompok.smallchange;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.ac.umn.leleair.kelompok.smallchange.Model.Data;
import id.ac.umn.leleair.kelompok.smallchange.Model.User;

public class UserPanel extends Fragment {
    private TextView username, email;
    private Button btnChangePass, btnLogout;
    private ImageView piggy, backWallet, backgroundBox, photoProfile;
    private ConstraintLayout frontWallet, photoContainer;
    private String stUsername;
    private CardView btnChangeProfileImg;

    //URI for profile image
    private Uri imageUri;
    private String myUri = "";

    //Pass Regex Auth
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private Pattern PassPattern = Pattern.compile(PASSWORD_PATTERN);
    private Matcher matcherPass;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mUsername;
    private DatabaseReference mImage;
    private StorageReference storageProfileImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_panel, container, false);

        //Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        String uid = mUser.getUid();

        mUsername = FirebaseDatabase.getInstance().getReference().child("Username").child(uid);
        mImage = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfileImg = FirebaseStorage.getInstance().getReference().child("Profile Img");

        piggy = view.findViewById(R.id.piggy);
        backWallet = view.findViewById(R.id.backWallet);
        frontWallet = view.findViewById(R.id.frontWallet);
        backgroundBox = view.findViewById(R.id.backgroundBoxUP);
        photoContainer = view.findViewById(R.id.photoContainer);
        username = view.findViewById(R.id.tvUsernameUP);
        email = view.findViewById(R.id.tvEmailUP);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnLogout = view.findViewById(R.id.btnLogout);
        photoProfile = view.findViewById(R.id.photoProfileUP);
        btnChangeProfileImg = view.findViewById(R.id.changeImgBtn);

        checkDatabaseUpdate();

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mAuth.signOut();
                    moveToLogin();
                } catch (Exception e) {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Logout Failed, please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnChangeProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setAspectRatio(1,1)
                        .getIntent(getContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                imageUri = result.getUri();
                photoProfile.setImageURI(imageUri);
                uploadProfileImage();
            }
        }
        else {
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Error, Please Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we are setting your data ");
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileRef = storageProfileImg
                    .child(mAuth.getCurrentUser().getUid()+ ".jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            myUri = uri.toString();

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("image",myUri);

                            mImage.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);

                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }
        else {
            progressDialog.dismiss();
        }
    }


    public void changePass() {
        //Change Password Form
        Dialog mdialog = new Dialog(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myviewm = inflater.inflate(R.layout.change_pass_form, null);
        mdialog.setContentView(myviewm);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.44);

        mdialog.getWindow().setLayout(width, height);

        EditText editOldPass = myviewm.findViewById(R.id.tvOldPass);
        EditText editNewPass = myviewm.findViewById(R.id.tvNewPass);

        Button btnChange = myviewm.findViewById(R.id.btnChangePass);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = editOldPass.getText().toString().trim();
                String newPass = editNewPass.getText().toString().trim();

                if(TextUtils.isEmpty(oldPass)){
                    editOldPass.setError("Required Field");
                    return;
                }
                else if (!validatePassword(newPass)){
                    editOldPass.setError("Not a valid password!\n" +
                            "Must be 8 characters (number, lowercase, uppercase and special character)");
                }

                if(TextUtils.isEmpty(newPass)){
                    editNewPass.setError("Required Field");
                    return;
                }
                else if (!validatePassword(newPass)){
                    editNewPass.setError("Not a valid password!\n" +
                            "Must be 8 characters (number, lowercase, uppercase and special character)");
                }

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(mUser.getEmail(), oldPass);

                // Prompt the user to re-provide their sign-in credentials
                mUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                try {
                                                    mAuth.signOut();
                                                    Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_SHORT).show();
                                                    moveToLogin();
                                                } catch (Exception e) {
                                                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Logout Failed, please check your internet connection", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), "Error password not updated", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "Error Failed Authentification", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        mdialog.show();
    }

    private void checkDatabaseUpdate() {
        String authEmail;

        //get auth Email
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            FirebaseUser mUser = mAuth.getCurrentUser();
            authEmail = mUser.getEmail();
            email.setText(authEmail);
        }

        mUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stUsername = "";

                for(DataSnapshot mSnapshot:snapshot.getChildren()){
                    User user = mSnapshot.getValue(User.class);
                    assert  user != null;
                    stUsername = user.getUsername();
                }
                username.setText(stUsername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mImage.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    if (dataSnapshot.hasChild("image"))
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(photoProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void playAnimOut(){
        photoContainer.animate().alpha(0).setDuration(200);
        backgroundBox.animate().translationY(300).alpha(0).setDuration(200);
        piggy.animate().translationY(450).alpha(0).setDuration(200);
        backWallet.animate().translationY(300).alpha(0).setDuration(300);
        frontWallet.animate().translationY(300).alpha(0).setDuration(300);
    }

    public void playAnimIn(){
        photoContainer.animate().alpha(1).setDuration(400);
        backgroundBox.animate().translationY(0).alpha(1).setDuration(600);
        piggy.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(400);
        backWallet.animate().translationY(0).alpha(1).setDuration(600);
        frontWallet.animate().translationY(0).alpha(1).setDuration(600);
    }

    private void moveToLogin() {
        Intent intent = new Intent(getActivity(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean validatePassword(String password) {
        matcherPass = PassPattern.matcher(password);
        return matcherPass.matches();
    }
}