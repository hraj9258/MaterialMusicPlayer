package com.example.materialmusicplayer2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerSongsAdapter extends FragmentPagerAdapter {
    public ViewPagerSongsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new TracksTabLayoutFragment();
        }
        else if (position == 1) {
            return  new ArtistsTabLayoutFragment();
        }
        else{
            return new GenresTabLayoutFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Tracks";
        }
        else if (position == 1) {
            return  "Artists";
        }
        else return "Genres";
    }
}

