package com.example.itp4501_ea_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameActivity extends AppCompatActivity {
    private static final String API_URL = "https://assign-mobileasignment-ihudikcgpf.cn-hongkong.fcapp.run";
    private int round;
    private int playerGuess;
    private int opponentFingers = -1;
    private int opponentGuess = -1;
    private String opponentName;
    private String fetchedJsonString;
    private int cheatTapCount = 0;
    private boolean isCheatModeEnabled = false;
    private int opponentLeftHandImageId;
    private int opponentRightHandImageId;
    private TextView tvtitle;
    private TextView tvOpponent;
    private TextView tvCheatJson;
    private RecyclerView rvGuess;
    private TextView tvGuessTheNumber;
    private Button submitButton;
    private GuessAdapter guessAdapter;
    private RadioGroup rgRightHand;
    private RadioGroup rgLeftHand;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViews();
        getIntentData();
        updateStaticUI();
        setupListeners();
        setupRoundUI();
        fetchOpponentMove();
    }

    // find view by id
    private void findViews() {
        tvtitle = findViewById(R.id.tvtitle);
        tvOpponent = findViewById(R.id.tvOpponent);
        tvCheatJson = findViewById(R.id.tvCheatJson);
        rvGuess = findViewById(R.id.rvGuess);
        tvGuessTheNumber = findViewById(R.id.tvGuessTheNumber);
        submitButton = findViewById(R.id.button);
        rgRightHand = findViewById(R.id.rgRightHand);
        rgLeftHand = findViewById(R.id.rgLeftHand);
    }
    // get the intent data
    private void getIntentData() {
        Intent intent = getIntent();
        round = intent.getIntExtra("ROUND_NUMBER", 1);
        playerGuess = intent.getIntExtra("PLAYER_GUESS_VALUE", -1);
        opponentName = intent.getStringExtra("OPPONENT_NAME");
    }

    // update the static UI elements
    private void updateStaticUI() {
        tvtitle.setText("Round " + round);
        tvOpponent.setText(opponentName != null && !opponentName.isEmpty() ? "Opponent: " + opponentName : "Opponent: CPU");
    }
    // set the on click listeners for the views
    private void setupListeners() {
        tvtitle.setOnClickListener(view -> {
            cheatTapCount++;
            if (!isCheatModeEnabled && cheatTapCount < 5) {
                int remaining = 5 - cheatTapCount;
                String message = "You are " + remaining + " step" + (remaining == 1 ? "" : "s") + " away from enabling cheat mode";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else if (!isCheatModeEnabled && cheatTapCount >= 5) {
                isCheatModeEnabled = true;
                Toast.makeText(this, "Cheat Mode ENABLED!", Toast.LENGTH_SHORT).show();
                if (fetchedJsonString != null) {
                    tvCheatJson.setText("Cheat JSON: " + fetchedJsonString);
                    tvCheatJson.setVisibility(View.VISIBLE);
                }
            } else if (isCheatModeEnabled) {
                isCheatModeEnabled = false;
                cheatTapCount = 0;
                Toast.makeText(this, "Cheat Mode DISABLED!", Toast.LENGTH_SHORT).show();
                tvCheatJson.setVisibility(View.GONE);
            }
        });

        submitButton.setOnClickListener(view -> processGameResult());
    }

    // if the roound is odd, than show the guess options, otherwise show the locked guess
    private void setupRoundUI() {
        if (round % 2 != 0) {
            tvGuessTheNumber.setText("Guess the Total Number");
            tvGuessTheNumber.setVisibility(View.VISIBLE);
            rvGuess.setVisibility(View.VISIBLE);
            List<Integer> guessOptions = Arrays.asList(0, 5, 10, 15, 20);
            guessAdapter = new GuessAdapter(guessOptions);
            rvGuess.setAdapter(guessAdapter);
            rvGuess.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else {
            tvGuessTheNumber.setText("Your guess is locked at: " + playerGuess);
            rvGuess.setVisibility(View.GONE);
        }
    }

    // get the json data from the server
    private void fetchOpponentMove() {
        submitButton.setEnabled(false);
        Request request = new Request.Builder().url(API_URL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            // This method is called when the request fails
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(GameActivity.this, "Failed to connect.", Toast.LENGTH_SHORT).show());
            }
            // This method is called when the request is successful
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) return;
                fetchedJsonString = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(fetchedJsonString);
                    int opponentLeft = jsonObject.getInt("left");
                    int opponentRight = jsonObject.getInt("right");
                    opponentFingers = opponentLeft + opponentRight;
                    opponentGuess = jsonObject.getInt("guess");
                    opponentLeftHandImageId = (opponentLeft == 5) ? R.drawable.left5 : R.drawable.left0;
                    opponentRightHandImageId = (opponentRight == 5) ? R.drawable.right5 : R.drawable.right0;

                    runOnUiThread(() -> {
                        if (isCheatModeEnabled) {
                            tvCheatJson.setText("Cheat JSON: " + fetchedJsonString);
                            tvCheatJson.setVisibility(View.VISIBLE);
                        }
                        submitButton.setEnabled(true);
                        Toast.makeText(GameActivity.this, "Opponent has moved. Your turn!", Toast.LENGTH_SHORT).show();
                    });
                } catch (JSONException e) {
                    runOnUiThread(() -> Toast.makeText(GameActivity.this, "Invalid data from server.", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
    // This method processes the game result based on the player's and opponent's moves.
    private void processGameResult() {
        if (opponentFingers == -1) {
            Toast.makeText(this, "Still waiting for opponent's move...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (round % 2 != 0) {
            Integer selectedGuess = guessAdapter.getSelectedValue();
            if (selectedGuess == null) {
                Toast.makeText(this, "Please select a number to guess.", Toast.LENGTH_SHORT).show();
                return;
            }
            playerGuess = selectedGuess;
        }

        int playerLeftHandValue;
        int playerLeftHandImageId;
        int leftHandId = rgLeftHand.getCheckedRadioButtonId();

        if (leftHandId == R.id.RBleft5) {
            playerLeftHandValue = 5;
            playerLeftHandImageId = R.drawable.left5;
        } else {
            playerLeftHandValue = 0;
            playerLeftHandImageId = R.drawable.left0;
        }

        int playerRightHandValue;
        int playerRightHandImageId;
        int rightHandId = rgRightHand.getCheckedRadioButtonId();

        if (rightHandId == R.id.RBright5) {
            playerRightHandValue = 5;
            playerRightHandImageId = R.drawable.right5;
        } else {
            playerRightHandValue = 0;
            playerRightHandImageId = R.drawable.right0;
        }

        int playerFingers = playerLeftHandValue + playerRightHandValue;
        int actualTotal = playerFingers + opponentFingers;
        String resultMessage = determineResultMessage(actualTotal);

        navigateToResultScreen(resultMessage, actualTotal, playerFingers, playerLeftHandImageId, playerRightHandImageId);
    }
    // process the result message win or lost
    private String determineResultMessage(int actualTotal) {
        if (round % 2 != 0) {
            return (playerGuess == actualTotal) ? "You Win! 🎉" : "It's a Draw!";
        } else {
            return (opponentGuess == actualTotal) ? "You Lose! 😞" : "It's a Draw!";
        }
    }

    // This method now correctly populates the Intent for both result screens.
    private void navigateToResultScreen(String resultMessage, int actualTotal, int playerFingers, int playerLeftHandImageId, int playerRightHandImageId) {
        Class<?> destinationActivity = "It's a Draw!".equals(resultMessage) ? ResultActivity.class : FinalResultActivity.class;
        Intent intent = new Intent(this, destinationActivity);

        // It's better to use constants defined in the receiving activities
        intent.putExtra("RESULT_MESSAGE", resultMessage);
        intent.putExtra("ACTUAL_TOTAL", actualTotal);
        intent.putExtra("ROUND_NUMBER", round);
        intent.putExtra("PLAYER_RIGHT_HAND_IMAGE_ID", playerRightHandImageId);
        intent.putExtra("OPPONENT_NAME", opponentName);
        intent.putExtra("PLAYER_GUESS", playerGuess);
        intent.putExtra("PLAYER_FINGERS", playerFingers);
        intent.putExtra("OPPONENT_GUESS", opponentGuess);
        intent.putExtra("OPPONENT_FINGERS", opponentFingers);
        intent.putExtra("PLAYER_LEFT_HAND_IMAGE_ID", playerLeftHandImageId);
        intent.putExtra(FinalResultActivity.KEY_PLAYER_RIGHT_HAND_IMAGE, playerRightHandImageId);
        intent.putExtra("OPPONENT_LEFT_HAND_IMAGE_ID", opponentLeftHandImageId);
        intent.putExtra("OPPONENT_RIGHT_HAND_IMAGE_ID", opponentRightHandImageId);
        // it for the final result activity
        if (destinationActivity == FinalResultActivity.class) {
            intent.putExtra("ROUNDS_PLAYED", round);
        }
        startActivity(intent);
        finish();
    }
}