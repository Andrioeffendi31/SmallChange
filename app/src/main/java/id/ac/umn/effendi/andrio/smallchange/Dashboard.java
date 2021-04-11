package id.ac.umn.effendi.andrio.smallchange;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Dashboard extends Fragment {
    private ImageView upperBox, upperBox2;
    private TextView welocomeText, username, incomeText, outcomeText;
    private ConstraintLayout userInfo;
    private ProgressBar incomeProgress, outcomeProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        upperBox = view.findViewById(R.id.upperBox);
        upperBox2 = view.findViewById(R.id.upperBox2);
        welocomeText = view.findViewById(R.id.tvHello);
        username = view.findViewById(R.id.usernameDashboard);
        userInfo = view.findViewById(R.id.userInfoContainer);
        incomeText = view.findViewById(R.id.income_progressText);
        incomeProgress = view.findViewById(R.id.income_progressBar);
        outcomeText = view.findViewById(R.id.outcome_progressText);
        outcomeProgress = view.findViewById(R.id.outcome_progressBar);

        ProgressBarAnimation anim1 = new ProgressBarAnimation(incomeProgress, incomeText, 0, 80);
        ProgressBarAnimation anim2 = new ProgressBarAnimation(outcomeProgress, outcomeText, 0, 40);
        anim1.setDuration(1400);
        anim2.setDuration(1400);
        incomeProgress.startAnimation(anim1);
        outcomeProgress.startAnimation(anim2);

        upperBox.animate().translationX(0).alpha(1).setDuration(800);
        upperBox2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1000);
        userInfo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600);
        return view;
    }
}