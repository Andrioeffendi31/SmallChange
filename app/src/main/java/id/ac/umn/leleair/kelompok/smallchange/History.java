package id.ac.umn.leleair.kelompok.smallchange;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class History extends Fragment {
    private ImageView backgroundBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        backgroundBox = view.findViewById(R.id.backgroundBoxHistory);

        return  view;
    }
    public void playAnimOut(){
        backgroundBox.animate().translationY(300).alpha(0).setDuration(200);
    }

    public void playAnimIn(){
        backgroundBox.animate().translationY(0).alpha(1).setDuration(600);
    }
}