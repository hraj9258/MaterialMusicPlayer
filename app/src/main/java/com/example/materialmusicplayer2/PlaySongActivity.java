package com.example.materialmusicplayer2;

import static com.example.materialmusicplayer2.MainActivity.mySongs;

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
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.button.MaterialButton;
import java.util.concurrent.TimeUnit;

public class PlaySongActivity extends AppCompatActivity {
    private MediaMetadataRetriever mediaMetadataRetriever ;
    private TextView songName, artistName, currentTime, totalTime;
    private MaterialButton play,previous,next,shuffle,repeat;
    private ImageView mediaArt;
    private MediaPlayer mediaPlayer;
    int position;
    private SeekBar seekBar;
    private Thread updateSeekBar;
    private Boolean isRepeat = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeekBar.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        // hide the action bar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        // Find Views for all the objects
        findViewsPlaySongsActivity();

        Intent intent = getIntent();
        position = intent.getIntExtra("Position", 0);
        Uri uri = Uri.parse(mySongs.get(position).toString());
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
        songName.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        songName.setSelected(true);
        artistName.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        totalTime.setText(convertToMMSS((String.valueOf(mediaPlayer.getDuration()))));

        // media Player Started
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.pause();
                if (isRepeat){
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                    seekBar.setProgress(mediaPlayer.getCurrentPosition()); // reset the seekbar
                }
                else{
                    if (position != mySongs.size() - 1 ){
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

        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while (currentPosition <= mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentTime.setText(convertToMMSS(String.valueOf(mediaPlayer.getCurrentPosition())));
                            }
                        });
                        sleep(500);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeekBar.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    play.setIcon(AppCompatResources.getDrawable(PlaySongActivity.this,R.drawable.play));
                    mediaPlayer.pause();
                }
                else{
                    play.setIcon(AppCompatResources.getDrawable(PlaySongActivity.this,R.drawable.pause));
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
                    position = mySongs.size() - 1 ;
                }
                songsChange(mediaMetadataRetriever,uri);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if (position != mySongs.size() - 1 ){
                    position = position + 1;
                }
                else{
                    position = 0 ;
                }
                songsChange(mediaMetadataRetriever, uri);
            }

        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRepeat){
                    isRepeat=false;
                }
                else isRepeat = true;
            }
        });
    }

    public void songsChange(MediaMetadataRetriever mmr, Uri uri){
        uri = Uri.parse(mySongs.get(position).toString());
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
        play.setIcon(AppCompatResources.getDrawable(PlaySongActivity.this,R.drawable.pause));

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition()); // reset the seekbar
        songName.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)); // current song name
        artistName.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)); // current Artist name
        totalTime.setText(convertToMMSS(String.valueOf(mediaPlayer.getDuration()))); // Total time of the song
    }

    public String convertToMMSS(String duration){
        long millis = Long.parseLong(duration);

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public void findViewsPlaySongsActivity(){
        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.artistName);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);
        mediaArt = findViewById(R.id.albumArt);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        shuffle = findViewById(R.id.shuffle);
        repeat = findViewById(R.id.repeat);
    }
}
