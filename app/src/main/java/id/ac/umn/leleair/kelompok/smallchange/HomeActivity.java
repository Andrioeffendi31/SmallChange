package id.ac.umn.leleair.kelompok.smallchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    //Initialize Variable
    public static TabLayout tabLayoutHome;
    public static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Assign Variable
        tabLayoutHome = findViewById(R.id.homeTabLayout);
        viewPager = findViewById(R.id.viewpagerHome);

        //Initialize View Pager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Income());
        adapter.addFrag(new UserPanel());
        adapter.addFrag(new Dashboard());
        adapter.addFrag(new History());
        adapter.addFrag(new Outcome());
        viewPager.setAdapter(adapter);

        tabLayoutHome.setupWithViewPager(viewPager);
        setCustomTabs();
        viewPager.setCurrentItem(2);
        tabLayoutHome.getTabAt(2).select();

        tabLayoutHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0) {
                    Fragment income = adapter.getItem(0);
                    ((Income) income).playAnimIn();
                }
                if(tab.getPosition() == 1) {
                    Fragment userpanel = adapter.getItem(1);
                    ((UserPanel) userpanel).playAnimIn();
                }
                if(tab.getPosition() == 2) {
                    Fragment dashboard = adapter.getItem(2);
                    ((Dashboard) dashboard).playAnimIn();
                }
                if(tab.getPosition() == 3) {
                    Fragment history = adapter.getItem(3);
                    ((History) history).playAnimIn();
                }
                if(tab.getPosition() == 4) {
                    Fragment outcome = adapter.getItem(4);
                    ((Outcome) outcome).playAnimIn();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    Fragment income = adapter.getItem(0);
                    ((Income) income).playAnimOut();
                }
                if(tab.getPosition() == 1){
                    Fragment userpanel = adapter.getItem(1);
                    ((UserPanel) userpanel).playAnimOut();
                }
                if(tab.getPosition() == 2) {
                    Fragment dashboard = adapter.getItem(2);
                    ((Dashboard) dashboard).playAnimOut();
                }
                if(tab.getPosition() == 3) {
                    Fragment history = adapter.getItem(3);
                    ((History) history).playAnimOut();
                }
                if(tab.getPosition() == 4) {
                    Fragment outcome = adapter.getItem(4);
                    ((Outcome) outcome).playAnimOut();
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    private void setCustomTabs() {

        View view1 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_income_13);
        tabLayoutHome.getTabAt(0).setCustomView(view1);

        View view2 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_userpanel_13);
        tabLayoutHome.getTabAt(1).setCustomView(view2);

        View view3 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_dashboardicon_13);
        tabLayoutHome.getTabAt(2).setCustomView(view3);

        View view4 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_history_13);
        tabLayoutHome.getTabAt(3).setCustomView(view4);

        View view5 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view5.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_outcome_13);
        tabLayoutHome.getTabAt(4).setCustomView(view5);
    }
}