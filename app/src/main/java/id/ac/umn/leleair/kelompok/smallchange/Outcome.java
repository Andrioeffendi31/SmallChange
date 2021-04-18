package id.ac.umn.leleair.kelompok.smallchange;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class Outcome extends Fragment {
    private Spinner filter;
    private ConstraintLayout PageTitle;
    private ImageView backgroundBox;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        filter = view.findViewById(R.id.filterOutcome);
        PageTitle = view.findViewById(R.id.PageTitleOutcome);
        backgroundBox = view.findViewById(R.id.backgroundBoxOutcome);

        initializeFilter();

        return view;
    }

    public void playAnimIn(){
        backgroundBox.animate().translationY(0).alpha(1).setDuration(600);
        PageTitle.animate().translationY(0).alpha(1).setDuration(400);
        filter.animate().alpha(1).setDuration(400).setStartDelay(600);
    }

    public void playAnimOut(){
        backgroundBox.animate().translationY(300).alpha(0).setDuration(200);
        PageTitle.animate().translationY(-130).alpha(0).setDuration(200);
        filter.animate().alpha(0).setDuration(200);
    }

    private void initializeFilter() {
        String[] value = {"Show All", "Today", "7 days ago", "31 days ago"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.customview_spinner,arrayList){

            @Override
            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = (TextView)view.findViewById(R.id.tvFilter);
                text.setTextColor(getResources().getColor(R.color.biru));

                return view;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = super.getView(position, convertView, parent);

                TextView text = (TextView)view.findViewById(R.id.tvFilter);
                text.setTextColor(getResources().getColor(R.color.white));

                return view;

            }
        };
        filter.setAdapter(arrayAdapter);
    }
}