package id.ac.umn.leleair.kelompok.smallchange;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class UserPanel extends Fragment {
    private ImageView piggy, backWallet, backgroundBox;
    private ConstraintLayout frontWallet, photoContainer;
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
        backWallet = view.findViewById(R.id.backWallet);
        frontWallet = view.findViewById(R.id.frontWallet);
        backgroundBox = view.findViewById(R.id.backgroundBoxUP);
        photoContainer = view.findViewById(R.id.photoContainer);
        return view;
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
}