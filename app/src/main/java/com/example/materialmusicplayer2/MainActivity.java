package com.example.materialmusicplayer2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<File> mySongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide the action bar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        // ArrayList<File> of all the songs in internal storage
        mySongs = MainActivity.fetchSongs(Environment.getExternalStorageDirectory());

        // Load the Songs fragment by default
        loadFragment(new SongsFragment(),false);

    }
    /**
     * @param fragment Fragment you want to load
     * @param isReplace Weather to Replace the fragment or add a fragment when loading
     **/
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

    /**
     *  @param file Object of File class
     *  @return a sorted ArrayList<File> containing all the songs wit
     **/
    public static ArrayList<File> fetchSongs(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] songs = file.listFiles();
        if (songs != null){
            for (File myFile: songs){
                if (!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchSongs(myFile));
                }
                else{
                    if ((myFile.getName().endsWith(".mp3") || myFile.getName().endsWith(".opus")) && !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }
}