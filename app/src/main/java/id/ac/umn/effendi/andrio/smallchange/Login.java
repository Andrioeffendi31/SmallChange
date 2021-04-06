package id.ac.umn.effendi.andrio.smallchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class Login extends AppCompatActivity {
    TabLayout tabLayoutLogin;
    ViewPager2 pager2Login;
    LoginFragmentAdapter loginAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayoutLogin = findViewById(R.id.loginTabLayout);
        pager2Login = findViewById(R.id.viewpagerLogin);

        FragmentManager fm = getSupportFragmentManager();
        loginAdapter = new LoginFragmentAdapter(fm, getLifecycle());
        pager2Login.setAdapter(loginAdapter);

        tabLayoutLogin.addTab(tabLayoutLogin.newTab().setText("Sign In"));
        tabLayoutLogin.addTab(tabLayoutLogin.newTab().setText("Sign Up"));

        tabLayoutLogin.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2Login.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2Login.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayoutLogin.selectTab(tabLayoutLogin.getTabAt(position));
            }
        });
    }
}