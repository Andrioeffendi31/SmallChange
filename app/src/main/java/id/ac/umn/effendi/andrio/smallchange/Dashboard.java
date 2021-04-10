package id.ac.umn.effendi.andrio.smallchange;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Dashboard extends Fragment {
    private ImageView upperCircle;
    private TextView welocomeText, username;
    private ConstraintLayout userInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        upperCircle = view.findViewById(R.id.upperCircle);
        welocomeText = view.findViewById(R.id.tvHello);
        username = view.findViewById(R.id.usernameDashboard);
        userInfo = view.findViewById(R.id.userInfoContainer);

        upperCircle.animate().translationY(0).alpha(1).setDuration(1200);
        userInfo.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800);
        return view;
    }
}