package com.example.itp4501_ea_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Start the music player when the main activity is created
        ((GameApplication) getApplication()).startMusic();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    // change the pages
    public void startGame(View view) {
        // Your code to start the game
        Intent intent = new Intent(this, chooseOpponentActivity.class); // Or GameActivity
        startActivity(intent);
    }
    // change the pages
    public void howToPlay(View View) {
        Intent intent = new Intent(MainActivity.this, manual.class);
        startActivity(intent);
    }
    // change the pages
    public void credits(View View) {
        Intent intent = new Intent(MainActivity.this, Credits.class);
        startActivity(intent);
    }
    // change the pages
    public void viewRecord(View view) {
        // This button will now start the ViewRecord activity

        Intent intent = new Intent(MainActivity.this, ViewRecord.class);
        startActivity(intent);
    }
    // change the pages
    public void settings(View view) {
        // This button will now start the Settings activity
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
    // change the pages
    public void exitGame(View view) {
        ((GameApplication) getApplication()).releaseMusicPlayer();
        finishAffinity();
    }
}