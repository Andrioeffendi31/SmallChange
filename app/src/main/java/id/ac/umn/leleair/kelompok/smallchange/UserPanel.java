package id.ac.umn.leleair.kelompok.smallchange;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class UserPanel extends Fragment {
    public ImageView piggy;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_panel, container, false);
        piggy = view.findViewById(R.id.piggy);
        view.post(new Runnable() {
            @Override
            public void run() {
                piggy.animate().translationY(100).setDuration(1000).setStartDelay(600);
            }
        });
        return view;
    }
}