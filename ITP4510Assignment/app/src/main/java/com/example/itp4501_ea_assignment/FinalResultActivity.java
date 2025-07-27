package com.example.itp4501_ea_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FinalResultActivity extends AppCompatActivity {
    public static final String KEY_RESULT_MESSAGE = "RESULT_MESSAGE";
    public static final String KEY_ROUNDS_PLAYED = "ROUNDS_PLAYED";
    public static final String KEY_OPPONENT_NAME = "OPPONENT_NAME";
    public static final String KEY_PLAYER_GUESS = "PLAYER_GUESS";
    public static final String KEY_PLAYER_FINGERS = "PLAYER_FINGERS";
    public static final String KEY_OPPONENT_GUESS = "OPPONENT_GUESS";
    public static final String KEY_OPPONENT_FINGERS = "OPPONENT_FINGERS";
    public static final String KEY_ACTUAL_TOTAL = "ACTUAL_TOTAL";
    public static final String KEY_PLAYER_LEFT_HAND_IMAGE = "PLAYER_LEFT_HAND_IMAGE_ID";
    public static final String KEY_PLAYER_RIGHT_HAND_IMAGE = "RIGHT_HAND_IMAGE_ID";
    public static final String KEY_OPPONENT_LEFT_HAND_IMAGE = "OPPONENT_LEFT_HAND_IMAGE_ID";
    public static final String KEY_OPPONENT_RIGHT_HAND_IMAGE = "OPPONENT_RIGHT_HAND_IMAGE_ID";

    private DatabaseHelper dbHelper;
    private String resultMessage, opponentName;
    private int roundsPlayed, playerGuess, playerFingers, opponentGuess, opponentFingers, actualTotal;
    private int playerLeftHandImageId, playerRightHandImageId, opponentLeftHandImageId, opponentRightHandImageId;

    private TextView tvFinalResultMessage, tvRoundsPlayed, tvActualTotal, tvPlayerFingers, tvPlayerGuess, tvOpponentName, tvOpponentFingers, tvOpponentGuess;
    private ImageView ivPlayerLeftHand, ivPlayerRightHand, ivOpponentLeftHand, ivOpponentRightHand;
    private Button btnPlayAgain, btnViewRecords, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        dbHelper = new DatabaseHelper(this);

        getIntentData();
        findViews();
        populateViews();
        setupClickListeners();
        // hide the player's guess or opponent's guess based on the rounds played
        if (roundsPlayed % 2 != 0) {
            // Odd round: The player was guessing, so hide the opponent's guess.
            tvOpponentGuess.setVisibility(View.GONE);
        } else {
            // Even round: The opponent was guessing, so hide the player's guess.
            tvPlayerGuess.setVisibility(View.GONE);
        }

        // save the game record
        saveGameRecord(opponentName, resultMessage, roundsPlayed);
    }
    // get data from the intent
    private void getIntentData() {
        Intent intent = getIntent();
        // CORRECTED: Use the constants to get the data
        resultMessage = intent.getStringExtra(KEY_RESULT_MESSAGE);
        roundsPlayed = intent.getIntExtra(KEY_ROUNDS_PLAYED, 1);
        opponentName = intent.getStringExtra(KEY_OPPONENT_NAME);
        playerGuess = intent.getIntExtra(KEY_PLAYER_GUESS, 0);
        playerFingers = intent.getIntExtra(KEY_PLAYER_FINGERS, 0);
        opponentGuess = intent.getIntExtra(KEY_OPPONENT_GUESS, 0);
        opponentFingers = intent.getIntExtra(KEY_OPPONENT_FINGERS, 0);
        actualTotal = intent.getIntExtra(KEY_ACTUAL_TOTAL, 0);
        playerLeftHandImageId = intent.getIntExtra(KEY_PLAYER_LEFT_HAND_IMAGE, R.drawable.left0);
        playerRightHandImageId = intent.getIntExtra(KEY_PLAYER_RIGHT_HAND_IMAGE, R.drawable.right0);
        opponentLeftHandImageId = intent.getIntExtra(KEY_OPPONENT_LEFT_HAND_IMAGE, R.drawable.left0);
        opponentRightHandImageId = intent.getIntExtra(KEY_OPPONENT_RIGHT_HAND_IMAGE, R.drawable.right0);
    }

    // find views by their IDs
    private void findViews() {
        tvFinalResultMessage = findViewById(R.id.tvFinalResultMessage);
        tvRoundsPlayed = findViewById(R.id.tvRoundsPlayed);
        tvActualTotal = findViewById(R.id.tvActualTotal);
        tvPlayerFingers = findViewById(R.id.tvPlayerFingers);
        tvPlayerGuess = findViewById(R.id.tvPlayerGuess);
        tvOpponentName = findViewById(R.id.tvOpponentName);
        tvOpponentFingers = findViewById(R.id.tvOpponentFingers);
        tvOpponentGuess = findViewById(R.id.tvOpponentGuess);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnViewRecords = findViewById(R.id.btnViewRecords);
        btnExit = findViewById(R.id.btnExit);
        ivPlayerLeftHand = findViewById(R.id.ivPlayerLeftHand);
        ivPlayerRightHand = findViewById(R.id.ivPlayerRightHand);
        ivOpponentLeftHand = findViewById(R.id.ivOpponentLeftHand);
        ivOpponentRightHand = findViewById(R.id.ivOpponentRightHand);
    }
    // populate the views with the data received from the intent
    private void populateViews() {
        tvFinalResultMessage.setText(resultMessage);
        tvRoundsPlayed.setText("Finished in " + roundsPlayed + " round(s)");
        tvActualTotal.setText("Actual Total: " + actualTotal);
        // Player's side
        tvPlayerFingers.setText("Fingers: " + playerFingers);
        tvPlayerGuess.setText("Guess: " + playerGuess);
        ivPlayerLeftHand.setImageResource(playerLeftHandImageId);
        ivPlayerRightHand.setImageResource(playerRightHandImageId);
        // Opponent's side
        tvOpponentName.setText((opponentName != null ? opponentName : "CPU") + "'s Move");
        tvOpponentFingers.setText("Fingers: " + opponentFingers);
        tvOpponentGuess.setText("Guess: " + opponentGuess);
        ivOpponentLeftHand.setImageResource(opponentLeftHandImageId);
        ivOpponentRightHand.setImageResource(opponentRightHandImageId);
    }
    // setup click listeners for the buttons
    private void setupClickListeners() {
        btnPlayAgain.setOnClickListener(v -> {
            Intent playAgainIntent = new Intent(FinalResultActivity.this, MainActivity.class);
            startActivity(playAgainIntent);
            finish();
        });

        btnViewRecords.setOnClickListener(v -> {
            Intent recordsIntent = new Intent(FinalResultActivity.this, ViewRecord.class);
            startActivity(recordsIntent);
        });

        btnExit.setOnClickListener(v -> {
            if (getApplication() instanceof GameApplication) {
                ((GameApplication) getApplication()).releaseMusicPlayer();
            }
            finishAffinity();
        });
    }
    // Save the game record to the database
    private void saveGameRecord(String opponentName, String result, int rounds) {
        String gameDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String gameTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String winOrLost = result.contains("Win") ? "Win" : "Lost";

        boolean isInserted = dbHelper.addGameLog(gameDate, gameTime, opponentName, winOrLost, rounds);
        if (isInserted) {
            Toast.makeText(this, "Game record saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save record.", Toast.LENGTH_SHORT).show();
        }
    }
}