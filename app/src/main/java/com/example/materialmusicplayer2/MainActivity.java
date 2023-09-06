package com.example.materialmusicplayer2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new SongsFragment(),false);
    }

    public void loadFragment(Fragment fragment, boolean isReplace){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isReplace){
            fragmentTransaction.replace(R.id.framelayout, fragment);
        }
        else {
            fragmentTransaction.add(R.id.framelayout, fragment);
        }
        fragmentTransaction.commit();
    }
}