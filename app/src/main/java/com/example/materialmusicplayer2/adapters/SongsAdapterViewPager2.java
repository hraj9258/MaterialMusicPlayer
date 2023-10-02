package com.example.materialmusicplayer2.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class SongsAdapterViewPager2 extends FragmentStateAdapter {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    public SongsAdapterViewPager2(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    /**
     * @param fragment Object of the Fragment you want to add
     */
    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
