 package id.ac.umn.leleair.kelompok.smallchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.ac.umn.leleair.kelompok.smallchange.Model.Data;
import id.ac.umn.leleair.kelompok.smallchange.Model.User;

public class Dashboard extends Fragment {
    private ImageView upperBox, upperBox2, user_photo, backgroundBox;
    private TextView welocomeText, username, incomeText, outcomeText, currentBalance, tvViewHistory;
    private ConstraintLayout userInfo;
    private ProgressBar incomeProgress, outcomeProgress;
    private CardView photoContainer;
    private int sumOutcome, sumIncome, totIncome, totOutcome;
    private String stTotalValue, stUsername;
    private RadioGroup radioGroup;
    private RadioButton checkedRadioButton;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mOutcomeDatabase;
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mUsername = FirebaseDatabase.getInstance().getReference().child("Username").child(uid);
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mOutcomeDatabase = FirebaseDatabase.getInstance().getReference().child("OutcomeData").child(uid);
        mImage = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfileImg = FirebaseStorage.getInstance().getReference().child("Profile Img");

        upperBox = view.findViewById(R.id.upperBox);
        upperBox2 = view.findViewById(R.id.upperBox2);
        welocomeText = view.findViewById(R.id.tvHello);
        username = view.findViewById(R.id.usernameDashboard);
        userInfo = view.findViewById(R.id.userInfoContainer);
        incomeText = view.findViewById(R.id.income_progressText);
        incomeProgress = view.findViewById(R.id.income_progressBar);
        outcomeText = view.findViewById(R.id.outcome_progressText);
        outcomeProgress = view.findViewById(R.id.outcome_progressBar);
        user_photo = view.findViewById(R.id.photoProfile);
        backgroundBox = view.findViewById(R.id.backgroundBox);
        photoContainer = view.findViewById(R.id.photoProfileContainer);
        currentBalance = view.findViewById(R.id.tvCurrentBalance);
        tvViewHistory = view.findViewById(R.id.tvViewHistory);
        radioGroup = view.findViewById(R.id.RadioFilter);
        checkedRadioButton = (RadioButton)radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

        checkDatabaseUpdate();

        statisticFilter();
        radioGroup.check(R.id.RadioToday);

        upperBox.animate().translationX(0).alpha(1).setDuration(800);
        upperBox2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1000);
        userInfo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600);
        backgroundBox.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(600);
        photoContainer.animate().alpha(1).setDuration(600).setStartDelay(1400);

        tvViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.viewPager.setCurrentItem(3);
                HomeActivity.tabLayoutHome.getTabAt(3).select();
            }
        });

        return view;
    }

    private void statisticFilter() {
        // This overrides the radiogroup onCheckListener
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Query incomeQuery, outcomeQuery;
                String dateTarget;

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                String mDate = df.format(date);

                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    if(checkedId == R.id.RadioToday) {
                        incomeQuery = mIncomeDatabase.orderByChild("date").startAt(mDate).endAt(mDate);
                        outcomeQuery = mOutcomeDatabase.orderByChild("date").startAt(mDate).endAt(mDate);

                        incomeQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                totIncome = 0;

                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    Data data = dataSnapshot.getValue(Data.class);
                                    assert data != null;
                                    totIncome += data.getAmount();
                                }

                                ProgressBarAnimation anim1 = new ProgressBarAnimation(incomeProgress, incomeText, 0, calculatePercentage((float) totIncome, (float) totOutcome));
                                ProgressBarAnimation anim2 = new ProgressBarAnimation(outcomeProgress, outcomeText, 0, calculatePercentage((float) totOutcome, (float) totIncome));
                                anim1.setDuration(1400);
                                anim2.setDuration(1400);
                                incomeProgress.startAnimation(anim1);
                                outcomeProgress.startAnimation(anim2);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });

                        outcomeQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                totOutcome = 0;

                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    Data data = dataSnapshot.getValue(Data.class);
                                    assert data != null;
                                    totOutcome += data.getAmount();
                                }

                                ProgressBarAnimation anim1 = new ProgressBarAnimation(incomeProgress, incomeText, 0, calculatePercentage((float) totIncome, (float) totOutcome));
                                ProgressBarAnimation anim2 = new ProgressBarAnimation(outcomeProgress, outcomeText, 0, calculatePercentage((float) totOutcome, (float) totIncome));
                                anim1.setDuration(1400);
                                anim2.setDuration(1400);
                                incomeProgress.startAnimation(anim1);
                                outcomeProgress.startAnimation(anim2);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                    else if(checkedId == R.id.Radio7D) {
                        Toast.makeText(getActivity().getApplicationContext(), "7 days", Toast.LENGTH_SHORT).show();
                    }
                    else if(checkedId == R.id.Radio31D) {
                        Toast.makeText(getActivity().getApplicationContext(), "31 days", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void checkDatabaseUpdate() {
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

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sumIncome = 0;

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    assert data != null;
                    sumIncome += data.getAmount();
                }

                stTotalValue = String.valueOf(sumIncome-sumOutcome);
                currentBalance.setText(stTotalValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        mOutcomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sumOutcome = 0;

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    assert data != null;
                    sumOutcome += data.getAmount();
                }

                //Debug Only
                Log.i("Income",String.valueOf(sumIncome));
                Log.i("Outcome",String.valueOf(sumOutcome));

                stTotalValue = String.valueOf(sumIncome-sumOutcome);
                currentBalance.setText(stTotalValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        mImage.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    if (dataSnapshot.hasChild("image"))
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(user_photo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public float calculatePercentage(float x, float y){
        float percentage;
        Log.i("xValue", String.valueOf(x));
        Log.i("yValue", String.valueOf(y));
        percentage = Math.round ((x / (x+y)) * 100);
        Log.i("xValue", String.valueOf(x));
        Log.i("yValue", String.valueOf(y));
        return percentage;
    }


    public void playAnimIn(){
        ProgressBarAnimation anim1 = new ProgressBarAnimation(incomeProgress, incomeText, 0, calculatePercentage((float) totIncome, (float) totOutcome));
        ProgressBarAnimation anim2 = new ProgressBarAnimation(outcomeProgress, outcomeText, 0, calculatePercentage((float) totOutcome, (float) totIncome));
        anim1.setDuration(1400);
        anim2.setDuration(1400);
        incomeProgress.startAnimation(anim1);
        outcomeProgress.startAnimation(anim2);

        backgroundBox.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(600);
    }

    public void playAnimOut(){
        ProgressBarAnimation anim1 = new ProgressBarAnimation(incomeProgress, incomeText, calculatePercentage((float) totIncome, (float) totOutcome), 0);
        ProgressBarAnimation anim2 = new ProgressBarAnimation(outcomeProgress, outcomeText, calculatePercentage((float) totOutcome, (float) totIncome), 0);
        anim1.setDuration(1400);
        anim2.setDuration(1400);
        incomeProgress.startAnimation(anim1);
        outcomeProgress.startAnimation(anim2);

        backgroundBox.animate().translationY(400).alpha(0).setDuration(800).setStartDelay(600);
    }
}