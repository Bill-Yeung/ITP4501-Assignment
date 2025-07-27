package com.example.itp4501_ea_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    public static final String KEY_RESULT_MESSAGE = "RESULT_MESSAGE";
    public static final String KEY_ACTUAL_TOTAL = "ACTUAL_TOTAL";
    public static final String KEY_ROUND_NUMBER = "ROUND_NUMBER";
    public static final String KEY_OPPONENT_NAME = "OPPONENT_NAME";
    public static final String KEY_PLAYER_GUESS = "PLAYER_GUESS";
    public static final String KEY_PLAYER_FINGERS = "PLAYER_FINGERS";
    public static final String KEY_OPPONENT_GUESS = "OPPONENT_GUESS";
    public static final String KEY_OPPONENT_FINGERS = "OPPONENT_FINGERS";
    public static final String KEY_PLAYER_LEFT_HAND_IMAGE = "PLAYER_LEFT_HAND_IMAGE_ID";
    public static final String KEY_PLAYER_RIGHT_HAND_IMAGE = "PLAYER_RIGHT_HAND_IMAGE_ID";
    public static final String KEY_OPPONENT_LEFT_HAND_IMAGE = "OPPONENT_LEFT_HAND_IMAGE_ID";
    public static final String KEY_OPPONENT_RIGHT_HAND_IMAGE = "OPPONENT_RIGHT_HAND_IMAGE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // get the Intent that started this activity
        Intent intent = getIntent();
        String resultMessage = intent.getStringExtra(KEY_RESULT_MESSAGE);
        int actualTotal = intent.getIntExtra(KEY_ACTUAL_TOTAL, 0);
        int currentRound = intent.getIntExtra(KEY_ROUND_NUMBER, 1);
        String opponentName = intent.getStringExtra(KEY_OPPONENT_NAME);
        int playerGuess = intent.getIntExtra(KEY_PLAYER_GUESS, 0);
        int playerFingers = intent.getIntExtra(KEY_PLAYER_FINGERS, 0);
        int opponentGuess = intent.getIntExtra(KEY_OPPONENT_GUESS, 0);
        int opponentFingers = intent.getIntExtra(KEY_OPPONENT_FINGERS, 0);
        int playerLeftHandImageId = intent.getIntExtra(KEY_PLAYER_LEFT_HAND_IMAGE, R.drawable.left0_unchecked);
        int playerRightHandImageId = intent.getIntExtra(KEY_PLAYER_RIGHT_HAND_IMAGE, R.drawable.right0_unchecked);
        int opponentLeftHandImageId = intent.getIntExtra(KEY_OPPONENT_LEFT_HAND_IMAGE, R.drawable.left0_unchecked);
        int opponentRightHandImageId = intent.getIntExtra(KEY_OPPONENT_RIGHT_HAND_IMAGE, R.drawable.right0_unchecked);

        // Find all views
        TextView tvResultMessage = findViewById(R.id.tvResultMessage);
        TextView tvActualTotal = findViewById(R.id.tvActualTotal);
        TextView tvPlayerFingers = findViewById(R.id.tvPlayerFingers);
        TextView tvPlayerGuess = findViewById(R.id.tvPlayerGuess);
        TextView tvOpponentName = findViewById(R.id.tvOpponentName);
        TextView tvOpponentFingers = findViewById(R.id.tvOpponentFingers);
        TextView tvOpponentGuess = findViewById(R.id.tvOpponentGuess);
        Button btnNextRound = findViewById(R.id.btnNextRound);
        ImageView ivPlayerLeftHand = findViewById(R.id.ivPlayerLeftHand);
        ImageView ivPlayerRightHand = findViewById(R.id.ivPlayerRightHand);
        ImageView ivOpponentLeftHand = findViewById(R.id.ivOpponentLeftHand);
        ImageView ivOpponentRightHand = findViewById(R.id.ivOpponentRightHand);

        // change the text and images based on the Intent data
        tvResultMessage.setText(resultMessage);
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

        // hide the player's guess or opponent's guess based on the rounds played
        if (currentRound % 2 != 0) {
            // Odd round: The player was guessing, so hide the opponent's guess.
            tvOpponentGuess.setVisibility(View.GONE);
        } else {
            // Even round: The opponent was guessing, so hide the player's guess.
            tvPlayerGuess.setVisibility(View.GONE);
        }

        // set on click listener for the Next Round button
        btnNextRound.setOnClickListener(v -> {
            // --- CHANGE: The Intent now goes to MathQuizActivity ---
            Intent quizIntent = new Intent(ResultActivity.this, MathQuizActivity.class);

            // Pass the data that GameActivity will need for the *next* round
            quizIntent.putExtra("ROUND_NUMBER", currentRound + 1);
            quizIntent.putExtra("PLAYER_GUESS_VALUE", opponentGuess);
            quizIntent.putExtra("OPPONENT_NAME", opponentName);

            startActivity(quizIntent);
            finish(); // Close the result screen
        });
    }
}