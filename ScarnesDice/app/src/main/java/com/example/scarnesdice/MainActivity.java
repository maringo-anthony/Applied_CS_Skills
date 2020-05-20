package com.example.scarnesdice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private boolean userTurn = true;
    private int userScore = 0;
    private int userTurnScore = 0;
    private int computerScore = 0;
    private int computerTurnScore = 0;
    private int[] diceImages = {
            R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void rollClickHandler(View view) {
        int roll = getRoll();

        if (userTurn) {
            userTurnScore = roll + 1 + userTurnScore;
            if (roll == 0) {
                userTurnScore = 0;
                userTurn = false;
            }
            TextView turnScoreText = findViewById(R.id.userTurnScoreText);
            String turnScoreString = "Your turn score: " + userTurnScore;
            turnScoreText.setText(turnScoreString);
        } else {
            computerTurn(view, roll);
        }

    }

    private int getRoll() {
        Random random = new Random();
        int roll = random.nextInt(6);
        ImageView die = findViewById(R.id.imageView);
        die.setImageResource(diceImages[roll]);
        return roll;
    }

    private void computerTurn(View view, int roll) {
        Button rollButton = findViewById(R.id.roll_button);
        rollButton.setEnabled(false);
        Button holdButton = findViewById(R.id.hold_button);
        holdButton.setEnabled(false);


        computerTurnScore = roll + 1 + computerTurnScore;
        if (roll == 0) {
            computerTurnScore = 0;
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
            userTurn = true;
        } else if (computerTurnScore > 15) {
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
            holdClickHandler(view);
        } else {
            rollClickHandler(view);
        }
    }

    public void holdClickHandler(View view) {
        if (userTurn) {
            userScore += userTurnScore;
            TextView userScoreText = findViewById(R.id.userScoreText);
            String userScoreString = "Your score: " + userScore;
            userScoreText.setText(userScoreString);
            userTurnScore = 0;

            TextView turnScoreText = findViewById(R.id.userTurnScoreText);
            String turnScoreString = "Your turn score: 0";
            turnScoreText.setText(turnScoreString);

            userTurn = false;
            rollClickHandler(view);


        } else {
            computerScore += computerTurnScore;
            TextView computerScoreText = findViewById(R.id.computerScoreText);
            String computerScoreString = "Computer score: " + computerScore;
            computerScoreText.setText(computerScoreString);
            computerTurnScore = 0;
            userTurn = true;
        }
    }

    public void resetClickHandler(View view) {
        userScore = 0;
        computerScore = 0;
        userTurnScore = 0;
        computerTurnScore = 0;
        userTurn = true;

        TextView turnScoreText = findViewById(R.id.userTurnScoreText);
        String turnScoreString = "Your turn score: 0";
        turnScoreText.setText(turnScoreString);

        TextView userScoreText = findViewById(R.id.userScoreText);
        String userScoreString = "Your score: 0";
        userScoreText.setText(userScoreString);

        TextView computerScoreText = findViewById(R.id.computerScoreText);
        String computerScoreString = "Computer score: 0";
        computerScoreText.setText(computerScoreString);


    }

}
