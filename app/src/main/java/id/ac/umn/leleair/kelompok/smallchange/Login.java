package id.ac.umn.leleair.kelompok.smallchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Method;

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

        checkUserSession();

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

    private void checkUserSession(){
        //Initialize Firebase Auth & User
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //check if user already signed in
        if(firebaseUser != null){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


}