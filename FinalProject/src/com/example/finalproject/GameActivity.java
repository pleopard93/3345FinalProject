package com.example.finalproject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends ActionBarActivity implements Animation.AnimationListener {

	String[] gameDataArray;
	public static final String GAME_KEY = "GameKey";
	public static final String GAME_OVER_KEY = "GameOverKey";
	private TextView mAnswerButton0;
	private TextView mAnswerButton1;
	private TextView mAnswerButton2;
	private TextView mAnswerButton3;
	private TextView mQuestionTextView;
	private TextView mMultiplierTextView;
	private TextView mScoreTextView;
	private TextView mStreakTextView;
	private TextView mCountdownTextView;
	private ProgressBar mTimerBar;
	private RelativeLayout mMultiplierLayout;
	private RelativeLayout mScoreLayout;
	private RelativeLayout mStreakLayout;
	private RelativeLayout mTimerLayout;
	private int questionIndex;
	private int score;
	private int multiplier;
	private int streak;
	private ArrayList<QuestionObject> questionList;

	private static final int READ_TIMEOUT = 10000;
	private static final int CONNECTION_TIMEOUT = 15000;
	private static String JSON_LOCATION;
	
	Animation fadeout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Intent i = getIntent();
		gameDataArray = i.getStringArrayExtra(GAME_KEY);

		mAnswerButton0 = (TextView) findViewById(R.id.answer0);
		mAnswerButton1 = (TextView) findViewById(R.id.answer1);
		mAnswerButton2 = (TextView) findViewById(R.id.answer2);
		mAnswerButton3 = (TextView) findViewById(R.id.answer3);

		mQuestionTextView = (TextView) findViewById(R.id.questionTextView);
		mMultiplierTextView = (TextView) findViewById(R.id.multiplierTextView);
		mScoreTextView = (TextView) findViewById(R.id.scoreTextView);
		mStreakTextView = (TextView) findViewById(R.id.streakTextView);
		mCountdownTextView = (TextView) findViewById(R.id.countdownTextView);

		mMultiplierLayout = (RelativeLayout) findViewById(R.id.multiplier);
		mScoreLayout = (RelativeLayout) findViewById(R.id.score);
		mStreakLayout = (RelativeLayout) findViewById(R.id.streak);
		mTimerLayout = (RelativeLayout) findViewById(R.id.timer);
		
		mTimerBar = (ProgressBar) findViewById(R.id.timerBar);
		mTimerBar.setProgress(100);
		
		questionIndex = 0;
		score = 0;
		multiplier = 1;
		streak = 0;

		JSON_LOCATION = "http://192.168.56.1:8888/QuizApp/api/index.php/questions/"
				+ gameDataArray[0] + "/" + gameDataArray[1];

		new HttpGetData().execute(null, null, null);
		setCountdownTimer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    private void launchGameOver() {
		Intent i = new Intent(this, GameOverActivity.class);
		i.putExtra(GameActivity.GAME_OVER_KEY, 0);
		startActivityForResult(i, 0);
	}
	
	private void setNewQuestion() {
		if (questionIndex < questionList.size()) {
			setTextView(questionIndex);
			setAnswerButtons(questionIndex);
			questionIndex++;
		} else {
			setGameOver();
		}
	}

	private void setTextView(int questionNumber) {
		mQuestionTextView.setText(questionList.get(questionNumber).question);
	}

	private void setAnswerButtons(final int questionNumber) {
		// Set the text and listeners for the radio buttons
		mAnswerButton0.setText(questionList.get(questionNumber).answer0);
		mAnswerButton1.setText(questionList.get(questionNumber).answer1);
		mAnswerButton2.setText(questionList.get(questionNumber).answer2);
		mAnswerButton3.setText(questionList.get(questionNumber).answer3);

		mAnswerButton0.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					v.setBackgroundColor(getResources().getColor(
							R.color.darkerblue));
					return true;
				case MotionEvent.ACTION_UP:
					checkAnswer(questionList.get(questionNumber).answer0,
							questionList.get(questionNumber).correctAnswer);
					v.setBackgroundColor(getResources().getColor(
							R.color.darkblue));
					return true;
				}
				return false;
			}
		});

		mAnswerButton1.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					v.setBackgroundColor(getResources().getColor(
							R.color.darkerblue));
					return true;
				case MotionEvent.ACTION_UP:
					checkAnswer(questionList.get(questionNumber).answer1,
							questionList.get(questionNumber).correctAnswer);
					v.setBackgroundColor(getResources().getColor(
							R.color.darkblue));
					return true;
				}
				return false;
			}
		});

		mAnswerButton2.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					v.setBackgroundColor(getResources().getColor(
							R.color.darkerblue));
					return true;
				case MotionEvent.ACTION_UP:
					checkAnswer(questionList.get(questionNumber).answer2,
							questionList.get(questionNumber).correctAnswer);
					v.setBackgroundColor(getResources().getColor(
							R.color.darkblue));
					return true;
				}
				return false;
			}
		});

		mAnswerButton3.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					v.setBackgroundColor(getResources().getColor(
							R.color.darkerblue));
					return true;
				case MotionEvent.ACTION_UP:
					checkAnswer(questionList.get(questionNumber).answer3,
							questionList.get(questionNumber).correctAnswer);
					v.setBackgroundColor(getResources().getColor(
							R.color.darkblue));
					return true;
				}
				return false;
			}
		});
	}

	private void startGame() {
		setNewQuestion();
		setCountdownTimer();
	}
	
	private void setTimer(final int totalTime) {
		new CountDownTimer(totalTime, 10) {

			public void onTick(long millisUntilFinished) {
				float progressPercentage = (((float) millisUntilFinished / (float) totalTime) * 100);
				mTimerBar.setProgress((int) progressPercentage);
			}

			public void onFinish() {
				setGameOver();
				launchGameOver();
			}
		}.start();
	}
	
	private void setCountdownTimer() {
		new CountDownTimer(4000, 100) {

			public void onTick(long millisUntilFinished) {
				int time = (int) millisUntilFinished/1000;
				if (time != 0) {
				mCountdownTextView.setText(Integer.toString(time));
				} else {
					mCountdownTextView.setText("Go!");
				}	
			}

			public void onFinish() {
				fadeout= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
				mCountdownTextView.setAnimation(fadeout);
				setTimer(15000);
			}
		}.start();
	}

	private void checkAnswer(String correctAnswer, String selectedAnswer) {
		if (selectedAnswer.equals(correctAnswer)) {
			score += multiplier * 5;
			streak++;
			multiplier++;
			mScoreTextView.setText(Integer.toString(score));
			mMultiplierTextView.setText("x" + Integer.toString(multiplier));
			mStreakTextView.setText(Integer.toString(streak));
			setNewQuestion();
		} else {
			multiplier = 1;
			streak = 0;
			mMultiplierTextView.setText("x" + Integer.toString(multiplier));
			mStreakTextView.setText(Integer.toString(streak));
			setIncorrectAnswer();
			setNewQuestion();
		}
	}

	private void setIncorrectAnswer() {
		new CountDownTimer(500, 10) {

			public void onTick(long millisUntilFinished) {
				mMultiplierLayout.setBackgroundColor(getResources().getColor(
						R.color.red));
				mScoreLayout.setBackgroundColor(getResources().getColor(
						R.color.red));
				mStreakLayout.setBackgroundColor(getResources().getColor(
						R.color.red));
				mTimerLayout.setBackgroundColor(getResources().getColor(
						R.color.red));
			}

			public void onFinish() {
				mMultiplierLayout.setBackgroundColor(getResources().getColor(
						R.color.lightblue));
				mScoreLayout.setBackgroundColor(getResources().getColor(
						R.color.orange));
				mStreakLayout.setBackgroundColor(getResources().getColor(
						R.color.green));
				mTimerLayout.setBackgroundColor(getResources().getColor(
						R.color.lightblue));
			}
		}.start();
	}

	private void setGameOver() {
		Toast.makeText(getApplicationContext(), "Game Over!",
				Toast.LENGTH_SHORT).show();
	}

	private static class QuestionObject {
		String question;
		String answer0;
		String answer1;
		String answer2;
		String answer3;
		String correctAnswer;

		@Override
		public String toString() {
			return "Question: " + question + "\nAnswer 1: " + answer0
					+ "\nAnswer 2: " + answer1 + "\nAnswer 3: " + answer2
					+ "\nAnswer 4: " + answer3 + "\nCorrect Answer: "
					+ correctAnswer;
		}
	}

	private void parseJSON(InputStream in) throws JSONException {
		JSONArray questions = new JSONObject(getResponseText(in))
				.getJSONArray("questions");
		questionList = new ArrayList<QuestionObject>();
		for (int i = 0; i < questions.length(); i++) {
			// updateProgress(questions.length(), i);
			JSONObject obj = questions.getJSONObject(i);
			QuestionObject quest = new QuestionObject();
			quest.question = obj.getString("question");
			quest.answer0 = obj.getString("0");
			quest.answer1 = obj.getString("1");
			quest.answer2 = obj.getString("2");
			quest.answer3 = obj.getString("3");
			quest.correctAnswer = obj.getString("answer");
			questionList.add(quest);
		}

		startGame();
	}

	private static String getResponseText(InputStream stream) {
		Scanner scanner = new Scanner(stream);
		final String result = scanner.useDelimiter("\\A").next();
		scanner.close();
		return result;
	}
	
	@Override
	public void onAnimationEnd(Animation animation) { }
	public void onAnimationRepeat(Animation animation) { }
	public void onAnimationStart(Animation animation) { }

	/*
	 * public void updateProgress(Integer size, Integer index) {
	 * mProgress.setProgress((index+1)*(100/size)); }
	 */

	// Use a background thread to retrieve the JSON objects
	private class HttpGetData extends
			AsyncTask<Void, Void, BufferedInputStream> {

		@Override
		protected BufferedInputStream doInBackground(Void... params) {

			HttpURLConnection connection = null;
			try {
				URL requestURL = new URL(JSON_LOCATION);
				connection = (HttpURLConnection) requestURL.openConnection();
				connection.setReadTimeout(READ_TIMEOUT);
				connection.setConnectTimeout(CONNECTION_TIMEOUT);

				final int statusCode = connection.getResponseCode();

				if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
					Log.d("PBL", "Unauthorized");
					return null;
				} else if (statusCode != HttpURLConnection.HTTP_OK) {
					Log.d("PBL", "Error: " + statusCode);
					return null;
				}

				return new BufferedInputStream(connection.getInputStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(BufferedInputStream in) {
			try {
				parseJSON(in);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
