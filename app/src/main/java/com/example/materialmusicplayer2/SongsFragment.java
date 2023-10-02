package com.example.materialmusicplayer2;

import static com.example.materialmusicplayer2.MainActivity.mySongs;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.materialmusicplayer2.adapters.SongsAdapterViewPager2;
import com.google.android.material.search.SearchView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {
    static ArrayList<String> artistsName;
    static ArrayList<String> songsName = new ArrayList<>();
    static ArrayList<String> genresNames;
    TabLayout tabLayout;
    SongsAdapterViewPager2 songsAdapterViewPager2;
    ViewPager2 viewPager2Songs;

    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs, container, false);// Inflate the layout for this fragment
    }

    /**
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2Songs = view.findViewById(R.id.viewPager2Songs);

        // search suggestions views
        SearchView searchView = view.findViewById(R.id.searchViewSongs);
        ListView scrollViewSuggestions = view.findViewById(R.id.scrollViewSuggestions);

        // hack: extracting the textview from the search view( please change it to native implementation of search view in future)
        EditText searchText = searchView.getEditText();

        // ArrayList<String> of all the tracks in mySongs
        for (int i = 0;i < mySongs.size();i++){
            songsName.add(i,mySongs.get(i).getName());
        }

        // ArrayList<String> of all the artist in mySongs
        artistsName = getArtistNamesFromMP3Files(mySongs);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSongs(s.toString(), scrollViewSuggestions, songsName);
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
        songsAdapterViewPager2 = new SongsAdapterViewPager2(getChildFragmentManager(), getLifecycle());
        songsAdapterViewPager2.addFragment(new TracksTabLayoutFragment());
        songsAdapterViewPager2.addFragment(new ArtistsTabLayoutFragment());
        songsAdapterViewPager2.addFragment(new GenresTabLayoutFragment());

        viewPager2Songs.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2Songs.setAdapter(songsAdapterViewPager2);

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
    }

    private void filterSongs(String query, ListView listView, ArrayList<String> allSongs) {
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

    /**
     * @param mp3Files ArrayList<File> containing all the mp3 files
     * @return ArrayList<String> containing all the Artists Name
     */
    public ArrayList<String> getArtistNamesFromMP3Files(@NonNull ArrayList<File> mp3Files) {
        ArrayList<String> artistNames = new ArrayList<>();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        for (File mp3File : mp3Files) {
            try {
                retriever.setDataSource(mp3File.getAbsolutePath());
                String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                artistNames.add(artist != null ? artist : "Unknown Artist");
            } catch (Exception e) {
                // Handle exceptions or errors when processing the file
                e.printStackTrace();
                artistNames.add("Unknown Artist");
            }
        }
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return artistNames;
    }
}