package com.example.materialmusicplayer2;


import static com.example.materialmusicplayer2.SongsFragment.artistsName;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.materialmusicplayer2.adapters.ArtistRecyclerViewAdapter;

public class ArtistsTabLayoutFragment extends Fragment {
    RecyclerView artistRecyclerView;
    public ArtistsTabLayoutFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for ArtistsTabLayout Fragment
        return inflater.inflate(R.layout.fragment_artists_tab_layout, container, false);
    }

    /**
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recycler view for Artists Name
        artistRecyclerView = view.findViewById(R.id.artistsRecyclerView);
        artistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // An ArtistRecyclerViewAdapter for artistRecyclerView
        ArtistRecyclerViewAdapter artistRecyclerViewAdapter = new ArtistRecyclerViewAdapter(artistsName,getContext());
        artistRecyclerView.setAdapter(artistRecyclerViewAdapter);
    }
}