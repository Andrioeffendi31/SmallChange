package id.ac.umn.effendi.andrio.smallchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

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

        tabLayoutHome.addTab(tabLayoutHome.newTab().setIcon(R.drawable.ic_income_13));
        tabLayoutHome.addTab(tabLayoutHome.newTab().setIcon(R.drawable.ic_userpanel_13));
        tabLayoutHome.addTab(tabLayoutHome.newTab().setIcon(R.drawable.ic_dashboardicon_13));
        tabLayoutHome.addTab(tabLayoutHome.newTab().setIcon(R.drawable.ic_history_13));
        tabLayoutHome.addTab(tabLayoutHome.newTab().setIcon(R.drawable.ic_outcome_13));

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
}