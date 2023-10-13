package com.example.materialmusicplayer2.adapters;

import static com.example.materialmusicplayer2.MainActivity.mySongs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.materialmusicplayer2.PlaySongActivity;
import com.example.materialmusicplayer2.R;

import java.util.ArrayList;

public class TracksRecyclerViewAdapter extends RecyclerView.Adapter<TracksRecyclerViewAdapter.ViewHolder> {
    ArrayList<String> songsName;
    ArrayList<String> artistsName;
    Context context;

    /**
     * @param songsName ArrayList<String> containing all the Songs
     * @param artistsName ArrayList<String>
     * @param context Context
     */
    public TracksRecyclerViewAdapter(ArrayList<String> songsName,ArrayList<String> artistsName,Context context) {
        this.songsName = songsName;
        this.artistsName = artistsName;
        this.context = context;
    }


    @NonNull
    @Override
    public TracksRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.songs_row_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.songName.setText(songsName.get(holder.getAdapterPosition()));
        holder.artistName.setText(artistsName.get(holder.getAdapterPosition()));
        holder.albumArt.setImageResource(R.drawable.twotone_music_note_24);

        // Starting the PlaySongActivity
        holder.songsRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaySongActivity.class);
                intent.putExtra("Position", holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });
    }

    /**
     * @return no of items to show in RecyclerView
     */
    @Override
    public int getItemCount() {
        return songsName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumArt;
        TextView songName, artistName;
        ConstraintLayout songsRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumArt = itemView.findViewById(R.id.albumArtRecyclerViewRow);
            songName = itemView.findViewById(R.id.songNameRecyclerViewRow);
            artistName = itemView.findViewById(R.id.artistNameRecyclerViewRow);
            songsRow = itemView.findViewById(R.id.songsRow);
        }
    }
}
