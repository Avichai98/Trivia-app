package com.example.trivia;

import java.util.ArrayList;

public class GameManager {

    private ArrayList<Question> questions;
    private int lives = 3;
    private int score = 0;
    private int timer = 10;
    private int currentQuestion = -1;

    private final int DELAY = 1000;


    public GameManager(int numOfQuestions, int initialLives) {
        if (initialLives > 0  &&  initialLives <= 4) {
            lives = initialLives;
        }

        questions = new ArrayList<>();
        questions.add(new Question().setImage(R.drawable.img_watermelon_juice).setHealthy(true));
        questions.add(new Question().setImage(R.drawable.img_tacos).setHealthy(false));
        questions.add(new Question().setImage(R.drawable.img_soup).setHealthy(true));
        questions.add(new Question().setImage(R.drawable.img_soup_mushrooms).setHealthy(true));
        questions.add(new Question().setImage(R.drawable.img_sausages).setHealthy(false));
        questions.add(new Question().setImage(R.drawable.img_salad).setHealthy(true));
        questions.add(new Question().setImage(R.drawable.img_pasta).setHealthy(false));
        questions.add(new Question().setImage(R.drawable.img_nachos).setHealthy(false));
        questions.add(new Question().setImage(R.drawable.img_ice_cream).setHealthy(false));
        questions.add(new Question().setImage(R.drawable.img_hot_dog).setHealthy(false));
        questions.add(new Question().setImage(R.drawable.img_eggs).setHealthy(true));
        questions.add(new Question().setImage(R.drawable.img_chicken).setHealthy(true));
        questions.add(new Question().setImage(R.drawable.img_burger).setHealthy(false));

        if (questions.size() > numOfQuestions) {
            questions.subList(numOfQuestions, questions.size()).clear();
        }
    }

    public void incrementScore() {
        score += 10;
    }

    public void decreaseLive() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getDELAY() {
        return DELAY;
    }

    public void nextQuestion() {
        currentQuestion++;
    }

    public boolean noMoreQuestions() {
        return currentQuestion >= questions.size();
    }

    public int getCurrentImage() {
        return questions.get(currentQuestion).getImage();
    }

    public boolean isCorrect(boolean answer) {
        return getCurrent().isHealthy() == answer;
    }

    private Question getCurrent() {
        return questions.get(currentQuestion);
    }

    public int getNumOfQuestions() {
        return questions.size();
    }

    public int getCurrentIndex() {
        return currentQuestion;
    }

    public void addExtraLive() {
        lives++;
    }

    public void decreaseTimer() {
        timer--;
    }
}
