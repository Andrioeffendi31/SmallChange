package id.ac.umn.effendi.andrio.smallchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    TabLayout tabLayoutHome;
    ViewPager2 pager2Home;
    HomeFragmentAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayoutHome = findViewById(R.id.homeTabLayout);
        pager2Home = findViewById(R.id.viewpagerHome);

        FragmentManager fm = getSupportFragmentManager();
        homeAdapter = new HomeFragmentAdapter(fm, getLifecycle());
        pager2Home.setAdapter(homeAdapter);

        setCustomTabs();

        pager2Home.setCurrentItem(2, true);
        tabLayoutHome.getTabAt(2).select();
        tabLayoutHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2Home.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2Home.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayoutHome.selectTab(tabLayoutHome.getTabAt(position));
            }
        });
    }

    private void setCustomTabs() {

        View view1 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_income_13);
        tabLayoutHome.addTab(tabLayoutHome.newTab().setCustomView(view1));

        View view2 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_userpanel_13);
        tabLayoutHome.addTab(tabLayoutHome.newTab().setCustomView(view2));

        View view3 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_dashboardicon_13);
        tabLayoutHome.addTab(tabLayoutHome.newTab().setCustomView(view3));

        View view4 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view4.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_history_13);
        tabLayoutHome.addTab(tabLayoutHome.newTab().setCustomView(view4));

        View view5 = getLayoutInflater().inflate(R.layout.tab_icon_custom, null);
        view5.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_outcome_13);
        tabLayoutHome.addTab(tabLayoutHome.newTab().setCustomView(view5));
    }
}