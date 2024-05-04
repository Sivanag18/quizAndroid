package com.example.quizandroid;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView totalquestion;
    TextView question;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;
    TextView timerTextView;
    CountDownTimer countDownTimer;

    int score = 0;
    int totalQuestions1 = QuestionAnswer.question.length;
    int cur = 0;
    String selected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalquestion = findViewById(R.id.totalQuestions);
        question = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);
        submitBtn = findViewById(R.id.submit);
        timerTextView = findViewById(R.id.timerTextView);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        totalquestion.setText("Total questions : " + totalQuestions1);

        loadNewQuestion();
        startTimer();
    }

    @Override
    public void onClick(View view) {
        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);

        Button click = (Button) view;
        if (click.getId() == R.id.submit) {
            if (selected.equals(QuestionAnswer.correctAnswers[cur])) {
                score++;
            }
            cur++;
            loadNewQuestion();
            startTimer();
        } else {
            selected = click.getText().toString();
            click.setBackgroundColor(Color.MAGENTA);
        }
    }

    void loadNewQuestion() {
        if (cur == totalQuestions1) {
            finishQuiz();
            return;
        }
        question.setText(QuestionAnswer.question[cur]);
        ansA.setText(QuestionAnswer.choices[cur][0]);
        ansB.setText(QuestionAnswer.choices[cur][1]);
        ansC.setText(QuestionAnswer.choices[cur][2]);
        ansD.setText(QuestionAnswer.choices[cur][3]);
    }

    void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

                timerTextView.setText("Time left: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {

                cur++;
                loadNewQuestion();
                startTimer();
            }
        }.start();
    }

    void finishQuiz() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        String passStatus = "";
        if (score > totalQuestions1 * 0.60) {
            passStatus = "Passed";
        } else {
            passStatus = "Failed";
        }

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Score is " + score + " out of " + totalQuestions1)
                .setPositiveButton("Restart", (dialogInterface, i) -> restartQuiz())
                .setCancelable(false)
                .show();
    }

    void restartQuiz() {
        score = 0;
        cur = 0;
        loadNewQuestion();
        startTimer(); // Restart the timer when restarting the quiz
    }
}
