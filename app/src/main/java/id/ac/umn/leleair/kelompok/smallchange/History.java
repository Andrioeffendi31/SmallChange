package id.ac.umn.leleair.kelompok.smallchange;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.ac.umn.leleair.kelompok.smallchange.Model.Data;
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout;

public class History extends Fragment {
    private ImageView backgroundBox;
    private TextView tvIncome, tvOutcome;

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

        //Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mOutcomeDatabase = FirebaseDatabase.getInstance().getReference().child("OutcomeData").child(uid);

        backgroundBox = view.findViewById(R.id.backgroundBoxHistory);
        mRecyclerIncome = view.findViewById(R.id.recyclerIncome);
        mRecyclerOutcome = view.findViewById(R.id.recyclerOutcome);
        tvIncome = view.findViewById(R.id.tvIncome);
        tvOutcome = view.findViewById(R.id.tvOutcome);

        //Setup RecyclerView
        LinearLayoutManager layoutManagerIncome = new ZoomRecyclerLayout(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelperIncome = new LinearSnapHelper();
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        snapHelperIncome.attachToRecyclerView(mRecyclerIncome);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setNestedScrollingEnabled(false);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerOutcome = new ZoomRecyclerLayout(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelperOutcome = new LinearSnapHelper();
        layoutManagerOutcome.setStackFromEnd(true);
        layoutManagerOutcome.setReverseLayout(true);
        snapHelperOutcome.attachToRecyclerView(mRecyclerOutcome);
        mRecyclerOutcome.setHasFixedSize(true);
        mRecyclerOutcome.setNestedScrollingEnabled(false);
        mRecyclerOutcome.setLayoutManager(layoutManagerOutcome);

        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                (
                        Data.class,
                        R.layout.history_income_item,
                        History.IncomeViewHolder.class,
                        mIncomeDatabase
                ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data model, int i) {
                incomeViewHolder.setIncomeType(model.getType());
                incomeViewHolder.setIncomeAmount(model.getAmount());
                incomeViewHolder.setIncomeDate(model.getDate());
            }
        };

        FirebaseRecyclerAdapter<Data, OutcomeViewHolder> outcomeAdapter = new FirebaseRecyclerAdapter<Data, OutcomeViewHolder>
                (
                        Data.class,
                        R.layout.history_outcome_item,
                        History.OutcomeViewHolder.class,
                        mOutcomeDatabase
                ) {
            @Override
            protected void populateViewHolder(OutcomeViewHolder outcomeViewHolder, Data model, int i) {
                outcomeViewHolder.setOutcomeType(model.getType());
                outcomeViewHolder.setOutcomeAmount(model.getAmount());
                outcomeViewHolder.setOutcomeDate(model.getDate());
            }
        };

        mRecyclerIncome.setAdapter(incomeAdapter);
        mRecyclerOutcome.setAdapter(outcomeAdapter);
    }

    // Income Data
    public static class IncomeViewHolder extends RecyclerView.ViewHolder {
        View mIncomeView;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            mIncomeView = itemView;
        }

        public void setIncomeType(String type) {
            TextView mtype = mIncomeView.findViewById(R.id.IncomeTitle);
            mtype.setText(type);
        }

        public void setIncomeAmount(Integer amount) {
            TextView mAmount = mIncomeView.findViewById(R.id.IncomeAmount);
            String stAmount = String.valueOf(amount);
            mAmount.setText(stAmount);
        }

        public void setIncomeDate(String date) {
            TextView mDate = mIncomeView.findViewById(R.id.IncomeDate);
            mDate.setText(date);
        }
    }


    // Outcome Data
    public static class OutcomeViewHolder extends RecyclerView.ViewHolder {
        View mOutcomeView;

        public OutcomeViewHolder(View itemView) {
            super(itemView);
            mOutcomeView = itemView;
        }

        public void setOutcomeType(String type) {
            TextView mtype = mOutcomeView.findViewById(R.id.OutcomeTitle);
            mtype.setText(type);
        }

        public void setOutcomeAmount(Integer amount) {
            TextView mAmount = mOutcomeView.findViewById(R.id.OutcomeAmount);
            String stAmount = String.valueOf(amount);
            mAmount.setText(stAmount);
        }

        public void setOutcomeDate(String date) {
            TextView mDate = mOutcomeView.findViewById(R.id.OutcomeDate);
            mDate.setText(date);
        }
    }

    public void playAnimOut(){
        backgroundBox.animate().translationY(300).alpha(0).setDuration(200);
        tvIncome.animate().scaleX(0.5f).scaleY(0.5f).alpha(0).setDuration(200);
        mRecyclerIncome.animate().scaleX(0.5f).scaleY(0.5f).alpha(0).setDuration(200);
        tvOutcome.animate().scaleX(0.5f).scaleY(0.5f).alpha(0).setDuration(200);
        mRecyclerOutcome.animate().scaleX(0.5f).scaleY(0.5f).alpha(0).setDuration(200);
    }

    public void playAnimIn(){
        backgroundBox.animate().translationY(0).alpha(1).setDuration(600);
        tvIncome.animate().scaleX(1).scaleY(1).alpha(1).setDuration(400);
        mRecyclerIncome.animate().scaleX(1).scaleY(1).alpha(1).setDuration(400);
        tvOutcome.animate().scaleX(1).scaleY(1).alpha(1).setDuration(400).setStartDelay(400);
        mRecyclerOutcome.animate().scaleX(1).scaleY(1).alpha(1).setDuration(500).setStartDelay(400);
    }
}