package com.example.materialmusicplayer2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TracksTabLayoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TracksTabLayoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TracksTabLayoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TracksTabLayoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TracksTabLayoutFragment newInstance(String param1, String param2) {
        TracksTabLayoutFragment fragment = new TracksTabLayoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view =inflater.inflate(R.layout.fragment_tracks_tab_layout, container, false);
        ListView tracksListView = view.findViewById(R.id.tracksListView);

        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
        String[] items = new String[mySongs.size()];
        for (int i = 0;i < mySongs.size();i++){
            items[i] = mySongs.get(i).getName().replace(".mp3", "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_list, items);
        tracksListView.setAdapter(adapter);
        tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlaySongActivity.class);
                String currentSong = tracksListView.getItemAtPosition(position).toString();
                intent.putExtra("SongList", mySongs);
                intent.putExtra("CurrentSong", currentSong);
                intent.putExtra("Position", position);

                startActivity(intent);
            }
        });

        return view;
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