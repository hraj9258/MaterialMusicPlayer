package com.example.materialmusicplayer2;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.search.SearchView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SongsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFragment newInstance(String param1, String param2) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        // search suggestions views
        SearchView searchView = view.findViewById(R.id.searchViewSongs);
        ListView listView = view.findViewById(R.id.scrollViewSuggestions);


        // hack extracting the textview from the search view( please change it to native implementation of search view in future)
        EditText editText = searchView.getEditText();

        // alphabetically sorted arraylist of all songs
        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
        String[] items = new String[mySongs.size()]; // string array containing names of all songs
        for (int i = 0;i < mySongs.size();i++){
            items[i] = mySongs.get(i).getName().replace(".mp3", "");
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSongs(s.toString(), listView, items);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // click listeners for the listview results
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlaySongActivity.class);
                String currentSong = listView.getItemAtPosition(position).toString();
                intent.putExtra("SongList", mySongs);
                intent.putExtra("CurrentSong", currentSong);
                intent.putExtra("Position", position);
                startActivity(intent);

            }
        });

        // tab layout and view pager
        ViewPagerSongsAdapter viewPagerSongsAdapter = new ViewPagerSongsAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerSongsAdapter);

        tabLayout.setupWithViewPager(viewPager);
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

    public ArrayList<File> fetchSongs(File file){
        ArrayList arrayList = new ArrayList();
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