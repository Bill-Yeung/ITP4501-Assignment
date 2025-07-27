package com.example.itp4501_ea_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MathQuizActivity extends AppCompatActivity {

    private int correctAnswer;
    private int nextRoundNumber;
    private int nextPlayerGuessValue;
    private String opponentName;

    private TextView tvMathQuestion;
    private EditText etMathAnswer;
    private Button btnSubmitAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz);

        tvMathQuestion = findViewById(R.id.tvMathQuestion);
        etMathAnswer = findViewById(R.id.etMathAnswer);
        btnSubmitAnswer = findViewById(R.id.btnSubmitAnswer);
        // get the date forme intent
        Intent intent = getIntent();
        nextRoundNumber = intent.getIntExtra("ROUND_NUMBER", 2);
        nextPlayerGuessValue = intent.getIntExtra("PLAYER_GUESS_VALUE", 0);
        opponentName = intent.getStringExtra("OPPONENT_NAME");

        // call gen math questuinn
        generateMathQuestion();
        // check ans ans set on click listener
        btnSubmitAnswer.setOnClickListener(v -> {
            checkAnswer();
        });
    }

    private void generateMathQuestion() {
        Random random = new Random();
        int num1, num2;
        // if roond number is less than 5, use simple single-digit numbers, otherwise use double-digit numbers
        if (nextRoundNumber < 5) {
            // Simple question: single-digit numbers
            num1 = random.nextInt(9) + 1; // 1-9
            num2 = random.nextInt(9) + 1; // 1-9
        } else {
            // Harder question: double-digit numbers
            num1 = random.nextInt(90) + 10; // 10-99
            num2 = random.nextInt(90) + 10; // 10-99
        }
        correctAnswer = num1 + num2;
        tvMathQuestion.setText(num1 + " + " + num2 + " = ?");
    }
    // check the answer
    private void checkAnswer() {
        String userAnswerStr = etMathAnswer.getText().toString();
        if (userAnswerStr.isEmpty()) {
            Toast.makeText(this, "Please enter an answer.", Toast.LENGTH_SHORT).show();
            return;
        }

        int userAnswer = Integer.parseInt(userAnswerStr);

        if (userAnswer == correctAnswer) {
            Toast.makeText(this, "Correct! Starting next round...", Toast.LENGTH_SHORT).show();

            // --- 4. If correct, navigate to the next GameActivity round ---
            Intent gameIntent = new Intent(this, GameActivity.class);
            gameIntent.putExtra("ROUND_NUMBER", nextRoundNumber);
            gameIntent.putExtra("PLAYER_GUESS_VALUE", nextPlayerGuessValue);
            gameIntent.putExtra("OPPONENT_NAME", opponentName);

            startActivity(gameIntent);
            finish(); // Close the quiz screen
        } else {
            Toast.makeText(this, "Wrong answer, try again!", Toast.LENGTH_SHORT).show();
        }
    }
}