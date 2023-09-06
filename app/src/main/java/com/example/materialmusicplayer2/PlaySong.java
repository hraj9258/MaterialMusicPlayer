package com.example.materialmusicplayer2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    private TextView textView;
    private ImageView play,previous,next,mediaArt;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateseekBar;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseekBar.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        // hide the action bar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);
        mediaArt = findViewById(R.id.albumArt);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs =(ArrayList)bundle.getParcelableArrayList("SongList");
        textContent = intent.getStringExtra("CurrentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("Position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, uri);
        byte[] artBytes = mediaMetadataRetriever.getEmbeddedPicture();

        // Album Art Retrieval
        if (artBytes != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
            mediaArt.setImageBitmap(bm);
        } else {
            mediaArt.setImageResource(R.drawable.twotone_music_note_24);
        }


        // media Player Started
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateseekBar = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while (currentPosition<mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseekBar.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });



        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != 0){
                    position = position-1;
                }
                else{
                    position = songs.size() - 1 ;
                }
                songsChange(mediaMetadataRetriever,uri);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != songs.size() - 1 ){
                    position = position + 1;
                }
                else{
                    position = 0 ;
                }
                songsChange(mediaMetadataRetriever, uri);
            }

        });
    }

    public void songsChange(MediaMetadataRetriever mmr, Uri uri){
        uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        // setting the media art
        mmr.setDataSource(this, uri);
        byte[] artBytes = mmr.getEmbeddedPicture();

        // Album Art Retrieval
        if (artBytes != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
            mediaArt.setImageBitmap(bm);
        } else {
            mediaArt.setImageResource(R.drawable.twotone_music_note_24);
        }


        play.setImageResource(R.drawable.pause); // play button

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition()); // reset the seekbar
        textContent = songs.get(position).getName().toString(); // current song
        textView.setText(textContent);
    }
}
