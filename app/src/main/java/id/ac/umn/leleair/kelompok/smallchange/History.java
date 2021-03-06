package id.ac.umn.leleair.kelompok.smallchange;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.ac.umn.leleair.kelompok.smallchange.Model.Data;
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout;

public class History extends Fragment {
    private ImageView backgroundBox;
    private TextView tvIncome, tvOutcome;
    private Button showFilterBtn;

    //Firebase
    private FirebaseAuth mAuth;
    private Query mIncomeDatabase;
    private Query mOutcomeDatabase;

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
        showFilterBtn = view.findViewById(R.id.btnFilter);

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

        //init popup menu
        Context wrapper = new ContextThemeWrapper(getActivity().getApplicationContext(), R.style.popupMenuStyle);
        PopupMenu popupMenu = new PopupMenu(wrapper,showFilterBtn);
        popupMenu.getMenuInflater().inflate(R.menu.filter_popup, popupMenu.getMenu());

        //handle popup menu item clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                Query incomeQuery, outcomeQuery;
                String dateTarget;

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                String mDate = df.format(date);
                FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter;
                FirebaseRecyclerAdapter<Data, OutcomeViewHolder> outcomeAdapter;

                if (id == R.id.showAll){
                    Toast.makeText(getActivity().getApplicationContext(), "Show all transaction history", Toast.LENGTH_SHORT).show();

                    incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                            (
                                    Data.class,
                                    R.layout.history_income_item,
                                    History.IncomeViewHolder.class,
                                    mIncomeDatabase
                            ) {
                        @Override
                        protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data model, int i) {
                            //parse date format
                            String unFormattedDate = model.getDate();
                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = inFormat.parse(unFormattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String formattedDate = outFormat.format(date);

                            incomeViewHolder.setIncomeType(model.getType());
                            incomeViewHolder.setIncomeAmount(model.getAmount());
                            incomeViewHolder.setIncomeDate(formattedDate);
                        }
                    };



                    outcomeAdapter = new FirebaseRecyclerAdapter<Data, OutcomeViewHolder>
                            (
                                    Data.class,
                                    R.layout.history_outcome_item,
                                    History.OutcomeViewHolder.class,
                                    mOutcomeDatabase
                            ) {
                        @Override
                        protected void populateViewHolder(OutcomeViewHolder outcomeViewHolder, Data model, int i) {
                            //parse date format
                            String unFormattedDate = model.getDate();
                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = inFormat.parse(unFormattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String formattedDate = outFormat.format(date);

                            outcomeViewHolder.setOutcomeType(model.getType());
                            outcomeViewHolder.setOutcomeAmount(model.getAmount());
                            outcomeViewHolder.setOutcomeDate(formattedDate);
                        }
                    };
                    mRecyclerIncome.setAdapter(incomeAdapter);
                    mRecyclerOutcome.setAdapter(outcomeAdapter);
                    return true;
                }

                else if (id == R.id.today) {
                    Toast.makeText(getActivity().getApplicationContext(), "Today transaction history", Toast.LENGTH_SHORT).show();

                    incomeQuery = mIncomeDatabase.orderByChild("date").startAt(mDate).endAt(mDate);
                    outcomeQuery = mOutcomeDatabase.orderByChild("date").startAt(mDate).endAt(mDate);

                    incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                            (
                                    Data.class,
                                    R.layout.history_income_item,
                                    History.IncomeViewHolder.class,
                                    incomeQuery
                            ) {
                        @Override
                        protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data model, int i) {
                            //parse date format
                            String unFormattedDate = model.getDate();
                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = inFormat.parse(unFormattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String formattedDate = outFormat.format(date);

                            incomeViewHolder.setIncomeType(model.getType());
                            incomeViewHolder.setIncomeAmount(model.getAmount());
                            incomeViewHolder.setIncomeDate(formattedDate);
                        }
                    };

                    outcomeAdapter = new FirebaseRecyclerAdapter<Data, OutcomeViewHolder>
                            (
                                    Data.class,
                                    R.layout.history_outcome_item,
                                    History.OutcomeViewHolder.class,
                                    outcomeQuery
                            ) {
                        @Override
                        protected void populateViewHolder(OutcomeViewHolder outcomeViewHolder, Data model, int i) {
                            //parse date format
                            String unFormattedDate = model.getDate();
                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = inFormat.parse(unFormattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String formattedDate = outFormat.format(date);

                            outcomeViewHolder.setOutcomeType(model.getType());
                            outcomeViewHolder.setOutcomeAmount(model.getAmount());
                            outcomeViewHolder.setOutcomeDate(formattedDate);
                        }
                    };
                    mRecyclerIncome.setAdapter(incomeAdapter);
                    mRecyclerOutcome.setAdapter(outcomeAdapter);
                    return true;
                }

                else if (id == R.id.sevenDays) {
                    cal.add(Calendar.DATE,-7);
                    date = cal.getTime();
                    dateTarget = df.format(date);

                    Toast.makeText(getActivity().getApplicationContext(), "7 days transaction history", Toast.LENGTH_SHORT).show();

                    incomeQuery = mIncomeDatabase.orderByChild("date").startAt(dateTarget).endAt(mDate);
                    outcomeQuery = mOutcomeDatabase.orderByChild("date").startAt(dateTarget).endAt(mDate);

                    incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                            (
                                    Data.class,
                                    R.layout.history_income_item,
                                    History.IncomeViewHolder.class,
                                    incomeQuery
                            ) {
                        @Override
                        protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data model, int i) {
                            //parse date format
                            String unFormattedDate = model.getDate();
                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = inFormat.parse(unFormattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String formattedDate = outFormat.format(date);

                            incomeViewHolder.setIncomeType(model.getType());
                            incomeViewHolder.setIncomeAmount(model.getAmount());
                            incomeViewHolder.setIncomeDate(formattedDate);
                        }
                    };

                    outcomeAdapter = new FirebaseRecyclerAdapter<Data, OutcomeViewHolder>
                            (
                                    Data.class,
                                    R.layout.history_outcome_item,
                                    History.OutcomeViewHolder.class,
                                    outcomeQuery
                            ) {
                        @Override
                        protected void populateViewHolder(OutcomeViewHolder outcomeViewHolder, Data model, int i) {
                            //parse date format
                            String unFormattedDate = model.getDate();
                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = inFormat.parse(unFormattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String formattedDate = outFormat.format(date);

                            outcomeViewHolder.setOutcomeType(model.getType());
                            outcomeViewHolder.setOutcomeAmount(model.getAmount());
                            outcomeViewHolder.setOutcomeDate(formattedDate);
                        }
                    };
                    mRecyclerIncome.setAdapter(incomeAdapter);
                    mRecyclerOutcome.setAdapter(outcomeAdapter);
                    return true;
                }

                else if (id == R.id.thirtyOneDays) {
                    cal.add(Calendar.DATE,-31);
                    date = cal.getTime();
                    dateTarget = df.format(date);

                    Toast.makeText(getActivity().getApplicationContext(), "7 days transaction history", Toast.LENGTH_SHORT).show();

                    incomeQuery = mIncomeDatabase.orderByChild("date").startAt(dateTarget).endAt(mDate);
                    outcomeQuery = mOutcomeDatabase.orderByChild("date").startAt(dateTarget).endAt(mDate);

                    incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                            (
                                    Data.class,
                                    R.layout.history_income_item,
                                    History.IncomeViewHolder.class,
                                    incomeQuery
                            ) {
                        @Override
                        protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data model, int i) {
                            //parse date format
                            String unFormattedDate = model.getDate();
                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = inFormat.parse(unFormattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String formattedDate = outFormat.format(date);

                            incomeViewHolder.setIncomeType(model.getType());
                            incomeViewHolder.setIncomeAmount(model.getAmount());
                            incomeViewHolder.setIncomeDate(formattedDate);
                        }
                    };

                    outcomeAdapter = new FirebaseRecyclerAdapter<Data, OutcomeViewHolder>
                            (
                                    Data.class,
                                    R.layout.history_outcome_item,
                                    History.OutcomeViewHolder.class,
                                    outcomeQuery
                            ) {
                        @Override
                        protected void populateViewHolder(OutcomeViewHolder outcomeViewHolder, Data model, int i) {
                            //parse date format
                            String unFormattedDate = model.getDate();
                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = inFormat.parse(unFormattedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                            String formattedDate = outFormat.format(date);

                            outcomeViewHolder.setOutcomeType(model.getType());
                            outcomeViewHolder.setOutcomeAmount(model.getAmount());
                            outcomeViewHolder.setOutcomeDate(formattedDate);
                        }
                    };
                    mRecyclerIncome.setAdapter(incomeAdapter);
                    mRecyclerOutcome.setAdapter(outcomeAdapter);
                    return true;
                }
                return false;
            }
        });

        showFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

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
                //parse date format
                String unFormattedDate = model.getDate();
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = inFormat.parse(unFormattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                String formattedDate = outFormat.format(date);

                incomeViewHolder.setIncomeType(model.getType());
                incomeViewHolder.setIncomeAmount(model.getAmount());
                incomeViewHolder.setIncomeDate(formattedDate);
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
                //parse date format
                String unFormattedDate = model.getDate();
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = inFormat.parse(unFormattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat outFormat = new SimpleDateFormat("MMM dd, yyyy");
                String formattedDate = outFormat.format(date);

                outcomeViewHolder.setOutcomeType(model.getType());
                outcomeViewHolder.setOutcomeAmount(model.getAmount());
                outcomeViewHolder.setOutcomeDate(formattedDate);
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