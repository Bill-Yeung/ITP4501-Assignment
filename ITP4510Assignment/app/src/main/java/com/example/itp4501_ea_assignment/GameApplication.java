package com.example.itp4501_ea_assignment;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

public class GameApplication extends Application {

    private MediaPlayer mediaPlayer;
    // on create than the application is created, we will create the media player
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        mediaPlayer.setLooping(true);
        setVolume();
    }
    // startMusic method to start the music
    public void startMusic() {
        // If the player is null (destroyed or first launch), create it.
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                setVolume();
            } else {
                Log.e("GameApplication", "Error creating MediaPlayer. Check your music file.");
                return;
            }
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
    // change the volume of the music player
    public void setVolume() {
        if (mediaPlayer != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
            int savedVolume = sharedPreferences.getInt("volume", 50);// Default volume is 50 if not set
            boolean isMuted = sharedPreferences.getBoolean("isMuted", false);

            // change to the saved volume
            if (isMuted) {
                mediaPlayer.setVolume(0, 0);
            } else {
                float volume = (float) savedVolume / 100f;
                mediaPlayer.setVolume(volume, volume); // Set to the saved volume
            }
        }
    }

    // stopMusic method to stop the music
    public void releaseMusicPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}