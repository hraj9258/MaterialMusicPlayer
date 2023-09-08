package com.example.materialmusicplayer2;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistsTabLayoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistsTabLayoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArtistsTabLayoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistsTabLayoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistsTabLayoutFragment newInstance(String param1, String param2) {
        ArtistsTabLayoutFragment fragment = new ArtistsTabLayoutFragment();
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
        View view = inflater.inflate(R.layout.fragment_artists_tab_layout, container, false);
        ListView artistsListView = view.findViewById(R.id.artistsListView);

        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
        Uri uri = Uri.parse(mySongs.toString());

        ArrayList<String> artistNames = getArtistNamesFromMP3Files(mySongs);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.simple_list, artistNames);
        artistsListView.setAdapter(adapter);


        return view;

    }

    public ArrayList<String> getArtistNamesFromMP3Files(ArrayList<File> mp3Files) {
        ArrayList<String> artistNames = new ArrayList<>();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        for (File mp3File : mp3Files) {
            try {
                retriever.setDataSource(mp3File.getAbsolutePath());
                String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                artistNames.add(artist != null ? artist : "Unknown");
            } catch (Exception e) {
                // Handle exceptions or errors when processing the file
                e.printStackTrace();
                artistNames.add("Unknown");
            }
        }

        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return artistNames;
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