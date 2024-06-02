package com.example.trivia;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;

public class Activity_Trivia extends AppCompatActivity {

    private AppCompatImageView trivia_IMG_question;
    private AppCompatImageView[] trivia_IMG_hearts;
    private MaterialTextView trivia_LBL_score;
    private MaterialButton trivia_BTN_green;
    private MaterialButton trivia_BTN_red;
    private MaterialTextView timer_LBL_info;
    private LinearProgressIndicator trivia_PRG_progress;
    private GameManager gameManager;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, gameManager.getDELAY());
            tick();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        findViews();
        gameManager = new GameManager(12, 4);
        updateLivesUI();
        nextQuestion();

        trivia_BTN_green.setOnClickListener(v -> answered(true, true));
        trivia_BTN_red.setOnClickListener(v -> answered(false, true));
        trivia_PRG_progress.setMax(gameManager.getNumOfQuestions());
    }

    @Override
    protected void onStart() {
        Log.d("p", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("p", "onResume");
        super.onResume();
        handler.postDelayed(runnable, gameManager.getDELAY());
    }

    @Override
    protected void onPause() {
        Log.d("p", "onPause");
        super.onPause();
    }


    @Override
    protected void onStop() {
        Log.d("p", "onStop");
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        Log.d("p", "onDestroy");
        super.onDestroy();
    }
    private void tick() {
        if (gameManager.getTimer() == 0) {
            answered(true, false);
        }
        gameManager.decreaseTimer();
        timer_LBL_info.setText(String.valueOf(gameManager.getTimer()));
    }
    private void openAdvertisementDialog() {
        handler.removeCallbacks(runnable);
        new MaterialAlertDialogBuilder(this)
                .setTitle("No lives")
                .setMessage("watch ad for extra live")
                .setPositiveButton("Yes", (dialog, which) -> showVideoAd())
                .setNegativeButton("No", (dialog, which) -> noVideoAd())
        .show();
    }

    private void showVideoAd() {
        gameManager.addExtraLive();
        updateLivesUI();
        updateQuestionUI();
        gameManager.setTimer(11);
        handler.postDelayed(runnable, gameManager.getDELAY());
    }

    private void noVideoAd() {
        gameDone();
    }


    private void answered(boolean greenClicked, boolean isClicked) {
        Log.d("p", "answered clicked: " + greenClicked);

        if (gameManager.isCorrect(greenClicked) && isClicked) {
            playSound("correct");
            gameManager.incrementScore();
        } else {
            playSound("wronganswer");
            gameManager.decreaseLive();
        }

        updateLivesUI();
        nextQuestion();
        trivia_LBL_score.setText(String.valueOf(gameManager.getScore()));
        gameManager.setTimer(11);
    }

    private void updateLivesUI() {
        int SZ = trivia_IMG_hearts.length;

        for (AppCompatImageView triviaImgHeart : trivia_IMG_hearts) {
            triviaImgHeart.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < SZ - gameManager.getLives(); i++) {
            trivia_IMG_hearts[SZ - i - 1].setVisibility(View.INVISIBLE);
        }
    }

    private void nextQuestion() {
        if (gameManager.getLives() == 0) {
            lose();
            return;
        }

        gameManager.nextQuestion();

        if (gameManager.noMoreQuestions()) {
            win();
            return;
        }
        updateQuestionUI();
    }


    private void updateQuestionUI() {
        int image = gameManager.getCurrentImage();
        trivia_IMG_question.setImageResource(image);
        trivia_PRG_progress.setProgress(gameManager.getCurrentIndex() + 1);
    }

    private void lose() {
        playSound("brassfail");
        Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show();
        openAdvertisementDialog();
    }

    private void win() {
        playSound("goodresult");
        Toast.makeText(this, "You win " + gameManager.getScore(), Toast.LENGTH_SHORT).show();
        gameDone();
    }

    private void gameDone() {
        Log.d("p", "Game Done");
        trivia_IMG_question.setVisibility(View.INVISIBLE);
        trivia_BTN_green.setEnabled(false);
        trivia_BTN_red.setEnabled(false);
        finish();
    }


    private void findViews() {
        trivia_IMG_question = findViewById(R.id.trivia_IMG_question);
        trivia_LBL_score = findViewById(R.id.trivia_LBL_score);
        trivia_BTN_green = findViewById(R.id.trivia_BTN_green);
        trivia_BTN_red = findViewById(R.id.trivia_BTN_red);
        trivia_PRG_progress = findViewById(R.id.trivia_PRG_progress);
        timer_LBL_info = findViewById(R.id.timer_LBL_info);

        trivia_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.trivia_IMG_heart1),
                findViewById(R.id.trivia_IMG_heart2),
                findViewById(R.id.trivia_IMG_heart3),
                findViewById(R.id.trivia_IMG_heart4),
        };
    }

    private void playSound(String path) {
        try {
            int resId = getResources().getIdentifier(path, "raw", getPackageName());
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + resId);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), uri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}