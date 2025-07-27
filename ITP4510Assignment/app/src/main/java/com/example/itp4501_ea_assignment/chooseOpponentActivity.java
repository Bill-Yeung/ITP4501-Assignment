package com.example.itp4501_ea_assignment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class chooseOpponentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_opponent);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new ChooseOpponentFragment())
                    .commit();
        }
    }
}
