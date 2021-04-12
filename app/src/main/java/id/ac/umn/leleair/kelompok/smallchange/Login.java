package id.ac.umn.leleair.kelompok.smallchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
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

    private void checkSession() {
        //check if user is logged in
        //if user is logged in --> move to mainActivity

        SessionManagement sessionManagement = new SessionManagement(Login.this);
        int userID = sessionManagement.getSession();

        if(userID != -1){
            //user id logged in and so move to mainActivity
            moveToHome();
        }
        else{
            //do nothing
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        checkSession();
    }

    private void moveToHome() {
        Intent intent = new Intent(Login.this, HomeActivity.class);
        intent.putExtra("Login", 1);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}