package com.example.materialmusicplayer2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.search.SearchView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2SongsAdapter viewPager2SongsAdapter;
    ViewPager2 viewPager2Songs;

    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);// Inflate the layout for this fragment
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2Songs = view.findViewById(R.id.viewPager2Songs);

        // search suggestions views
        SearchView searchView = view.findViewById(R.id.searchViewSongs);
        ListView scrollViewSuggestions = view.findViewById(R.id.scrollViewSuggestions);


        // hack: extracting the textview from the search view( please change it to native implementation of search view in future)
        EditText searchText = searchView.getEditText();

        // alphabetically sorted arraylist of all songs
        ArrayList<File> mySongs = MainActivity.fetchSongs(Environment.getExternalStorageDirectory());
        String[] items = new String[mySongs.size()]; // string array containing names of all songs
        for (int i = 0;i < mySongs.size();i++){
            items[i] = mySongs.get(i).getName().replace(".mp3", "");
        }
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSongs(s.toString(), scrollViewSuggestions, items);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // click listeners for the listview results
        scrollViewSuggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlaySongActivity.class);
                String currentSong = scrollViewSuggestions.getItemAtPosition(position).toString();
                intent.putExtra("SongList", mySongs);
                intent.putExtra("CurrentSong", currentSong);
                intent.putExtra("Position", position);
                startActivity(intent);

            }
        });

        // tab layout and view pager2
        viewPager2SongsAdapter = new ViewPager2SongsAdapter(getChildFragmentManager(), getLifecycle());
        viewPager2SongsAdapter.addFragment(new TracksTabLayoutFragment());
        viewPager2SongsAdapter.addFragment(new ArtistsTabLayoutFragment());
        viewPager2SongsAdapter.addFragment(new GenresTabLayoutFragment());

        viewPager2Songs.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2Songs.setAdapter(viewPager2SongsAdapter);


        new TabLayoutMediator(tabLayout, viewPager2Songs, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0){
                    tab.setText("Tracks");
                }
                else if(position == 1){
                    tab.setText("Artists");
                }else{
                    tab.setText("Genres");
                }
            }
        }).attach();
        return view;
    }

    private void filterSongs(String query, ListView listView, String[] allSongs) {
        List<String> filteredSongs = new ArrayList<>();

        for (String song : allSongs) {
            if (song.toLowerCase().contains(query.toLowerCase())) {
                filteredSongs.add(song);
            }
        }

        // Update the ListView with the filtered results
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.simple_list, filteredSongs);
        listView.setAdapter(adapter);
    }
}