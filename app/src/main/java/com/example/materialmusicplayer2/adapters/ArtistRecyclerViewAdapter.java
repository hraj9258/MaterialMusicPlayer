package com.example.materialmusicplayer2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.materialmusicplayer2.R;

import java.util.ArrayList;

public class ArtistRecyclerViewAdapter extends RecyclerView.Adapter<ArtistRecyclerViewAdapter.ViewHolder>{

    ArrayList<String> artistsName;
    Context context;

    public ArtistRecyclerViewAdapter(ArrayList<String> artistsName, Context context) {
        this.artistsName = artistsName;
        this.context = context;
    }

    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public ArtistRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View artistsViewHolder = LayoutInflater.from(context).inflate(R.layout.simple_list,parent,false);
        return new ViewHolder(artistsViewHolder);
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ArtistRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.artistName.setText(artistsName.get(holder.getAdapterPosition()));
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        if (artistsName != null) {
            return artistsName.size();
        }
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.text1);
        }
    }
}
