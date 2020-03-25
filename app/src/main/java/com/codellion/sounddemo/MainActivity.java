package com.codellion.sounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager mAudioManager;
    SeekBar scrubControl;
    Timer timer = new Timer();;

    public void onClickPlay(View view){
        mediaPlayer.start();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubControl.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 500);
    }

    public void onClickPause(View view){
        mediaPlayer.pause();
        timer.purge();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer = MediaPlayer.create(this, R.raw.heartbreaker);
        SeekBar volumeContol = (SeekBar) findViewById(R.id.volumeSeekBar);
        volumeContol.setMax(maxVolume);
        volumeContol.setProgress(currentVolume);

        scrubControl = (SeekBar) findViewById(R.id.scrubSeekBar);
        scrubControl.setMax(mediaPlayer.getDuration());


        volumeContol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Seekbar changed", Integer.toString(progress));
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scrubControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int prg;
            int dragProgress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Scrub Seekbar: ", Integer.toString(progress));
                prg = progress;
                prg = dragProgress;
//                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                onClickPause(seekBar);
                mediaPlayer.seekTo(dragProgress);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                onClickPlay(seekBar);
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        timer.purge();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        timer.cancel();
    }

}
