package com.example.materialmusicplayer2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlaySongActivity extends AppCompatActivity {
    MediaMetadataRetriever mediaMetadataRetriever ;
    TextView songName, artistName, currentTime, totalTime;
    private Button play;
    private ImageView mediaArt;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateseekBar;
    Boolean isRepeat = false;

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

        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.artistName);
        play = findViewById(R.id.play);
        Button previous = findViewById(R.id.previous);
        Button next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);
        mediaArt = findViewById(R.id.albumArt);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        Chip shuffle = findViewById(R.id.shuffle);
        Chip repeat = findViewById(R.id.repeat);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs =(ArrayList)bundle.getParcelableArrayList("SongList");
        textContent = intent.getStringExtra("CurrentSong");
        position = intent.getIntExtra("Position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);


        // Initial Setup
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, uri);
        byte[] artBytes = mediaMetadataRetriever.getEmbeddedPicture();

        // Album Art Retrieval
        if (artBytes != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
            mediaArt.setImageBitmap(bm);
        } else {
            mediaArt.setImageResource(R.drawable.twotone_music_note_24);
        }
        songName.setText(textContent);
        songName.setSelected(true);
        artistName.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        totalTime.setText(convertToMMSS((String.valueOf(mediaPlayer.getDuration()))));


        // media Player Started
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isRepeat){
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                    seekBar.setProgress(mediaPlayer.getCurrentPosition()); // reset the seekbar
                    mediaPlayer.start();
                }
                else{
                    mediaPlayer.stop();
                    if (position != songs.size() - 1 ){
                        position = position + 1;
                    }
                    else{
                        position = 0 ;
                    }
                    songsChange(mediaMetadataRetriever, uri);
                }
            }
        });

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentTime.setText(convertToMMSS(String.valueOf(mediaPlayer.getCurrentPosition())));
                            }
                        });
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
                    play.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });



        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
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
                if (position != songs.size() - 1 ){
                    position = position + 1;
                }
                else{
                    position = 0 ;
                }
                songsChange(mediaMetadataRetriever, uri);
            }

        });

        repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isRepeat){
                    isRepeat = false;
                }
                else{
                    isRepeat=true;
                }
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

        // update the play button icons
        play.setBackgroundResource(R.drawable.pause);

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition()); // reset the seekbar
        textContent = songs.get(position).getName(); // current song name
        songName.setText(textContent);
        artistName.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        totalTime.setText(convertToMMSS(String.valueOf(mediaPlayer.getDuration())));
    }

    public String convertToMMSS(String duration){
        long millis = Long.parseLong(duration);

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}
