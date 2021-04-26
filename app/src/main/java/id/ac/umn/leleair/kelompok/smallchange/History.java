package id.ac.umn.leleair.kelompok.smallchange;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class History extends Fragment {
    private ImageView backgroundBox;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mOutcomeDatabase;

    //Recycler View
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerOutcome;

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
        mRecyclerIncome = view.findViewById(R.id.recyclerIncome);
        mRecyclerOutcome = view.findViewById(R.id.recyclerOutcome);

        //Setup RecyclerView
        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerOutcome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerOutcome.setStackFromEnd(true);
        layoutManagerOutcome.setReverseLayout(true);
        mRecyclerOutcome.setHasFixedSize(true);
        mRecyclerOutcome.setLayoutManager(layoutManagerOutcome);

        return  view;
    }
    public void playAnimOut(){
        backgroundBox.animate().translationY(300).alpha(0).setDuration(200);
    }

    public void playAnimIn(){
        backgroundBox.animate().translationY(0).alpha(1).setDuration(600);
    }
}