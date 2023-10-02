package com.example.materialmusicplayer2;

import static com.example.materialmusicplayer2.SongsFragment.artistsName;
import static com.example.materialmusicplayer2.SongsFragment.songsName;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.materialmusicplayer2.adapters.TracksRecyclerViewAdapter;

public class TracksTabLayoutFragment extends Fragment {
    TracksRecyclerViewAdapter tracksRecyclerViewAdapter;
    RecyclerView tracksRecyclerView;
    public TracksTabLayoutFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tracks_tab_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tracks RecyclerView
        tracksRecyclerView = view.findViewById(R.id.tracksRecyclerView);
        tracksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Tracks RecyclerView Adapter
        tracksRecyclerViewAdapter = new TracksRecyclerViewAdapter(songsName,artistsName,getContext());
        tracksRecyclerView.setAdapter(tracksRecyclerViewAdapter);
    }
}