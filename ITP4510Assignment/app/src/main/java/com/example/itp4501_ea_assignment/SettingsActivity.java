package com.example.itp4501_ea_assignment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    private SeekBar seekBarVolume;
    private SwitchCompat switchMute;
    private SharedPreferences sharedPreferences;

    // This activity allows users to adjust game settings such as volume and mute state.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        switchMute = findViewById(R.id.switchMute); // Find the Switch

        // Load the settings
        loadSettings();

        // Listener for the volume SeekBar
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    saveVolume(progress);
                    updateMusicVolume();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Listener for the Mute Switch
        switchMute.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveMuteState(isChecked);
            updateMusicVolume();
        });
    }

    // load the settiings
    private void loadSettings() {
        int savedVolume = sharedPreferences.getInt("volume", 70);
        boolean isMuted = sharedPreferences.getBoolean("isMuted", false);

        seekBarVolume.setProgress(savedVolume);
        switchMute.setChecked(isMuted);
    }
    // save the volume
    private void saveVolume(int volume) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("volume", volume);
        editor.apply();
    }
    // save the mute state
    private void saveMuteState(boolean isMuted) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isMuted", isMuted);
        editor.apply();
    }
    // update the music volume based on the saved settings
    private void updateMusicVolume() {
        // Tell the central music player to update its volume
        ((GameApplication) getApplication()).setVolume();
    }
}