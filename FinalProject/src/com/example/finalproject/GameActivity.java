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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class GameActivity extends ActionBarActivity {

	String[] gameDataArray;
	public static final String GAME_KEY = "GameKey";
	private RadioButton mRadio0;
	private RadioButton mRadio1;
	private RadioButton mRadio2;
	private RadioButton mRadio3;
	private TextView mQuestionTextView;
	private TextView mMultiplierTextView;
	private TextView mScoreTextView;
	private TextView mStreakTextView;
	private int questionIndex;
	private int score;
	private int multiplier;
	private int streak;
	private ArrayList<QuestionObject> questionList;

	private static final int READ_TIMEOUT = 10000;
	private static final int CONNECTION_TIMEOUT = 15000;
	private static String JSON_LOCATION;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Intent i = getIntent();
		gameDataArray = i.getStringArrayExtra(GAME_KEY);

		Log.d("PBL", gameDataArray[1]);
		
		mRadio0 = (RadioButton) findViewById(R.id.radioButton0);
		mRadio1 = (RadioButton) findViewById(R.id.radioButton1);
		mRadio2 = (RadioButton) findViewById(R.id.radioButton2);
		mRadio3 = (RadioButton) findViewById(R.id.radioButton3);

		mQuestionTextView = (TextView) findViewById(R.id.questionTextView);
		mMultiplierTextView = (TextView) findViewById(R.id.multiplierTextView);
		mScoreTextView = (TextView) findViewById(R.id.scoreTextView);
		mStreakTextView = (TextView) findViewById(R.id.streakTextView);

		questionIndex = 0;
		score = 0;
		multiplier = 1;
		streak = 0;
		
		JSON_LOCATION = "http://192.168.56.1:8888/QuizApp/api/index.php/questions/" + gameDataArray[1];

		new HttpGetData().execute(null, null, null);
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

	private void setNewQuestion() {
		Log.d("PBL", Integer.toString(questionList.size()));
		if (questionIndex < questionList.size()) {
			setTextView(questionIndex);
			setRadioButtons(questionIndex);
			questionIndex++;
		}
	}

	private void setTextView(int questionNumber) {
		mQuestionTextView.setText(questionList.get(questionNumber).question);
	}

	private void setRadioButtons(final int questionNumber) {
		// Set the text and listeners for the radio buttons
		mRadio0.setText(questionList.get(questionNumber).answer0);
		mRadio1.setText(questionList.get(questionNumber).answer1);
		mRadio2.setText(questionList.get(questionNumber).answer2);
		mRadio3.setText(questionList.get(questionNumber).answer3);

		mRadio0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRadio0.setChecked(false);
				checkAnswer(questionList.get(questionNumber).answer0,
						questionList.get(questionNumber).correctAnswer);
			}
		});

		mRadio1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRadio1.setChecked(false);
				checkAnswer(questionList.get(questionNumber).answer1,
						questionList.get(questionNumber).correctAnswer);
			}
		});

		mRadio2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRadio2.setChecked(false);
				checkAnswer(questionList.get(questionNumber).answer2,
						questionList.get(questionNumber).correctAnswer);
			}
		});

		mRadio3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRadio3.setChecked(false);
				checkAnswer(questionList.get(questionNumber).answer3,
						questionList.get(questionNumber).correctAnswer);
			}
		});
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
		}
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
		JSONArray questions = new JSONObject(getResponseText(in)).getJSONArray("questions");
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

		setNewQuestion();
	}

	private static String getResponseText(InputStream stream) {
		Scanner scanner = new Scanner(stream);
		final String result = scanner.useDelimiter("\\A").next();
		scanner.close();
		return result;
	}

	/*
	 * public void updateProgress(Integer size, Integer index) { 
	 * mProgress.setProgress((index+1)*(100/size)); }
	 */

	// Use a background thread to retrieve the JSON objects
	private class HttpGetData extends AsyncTask<Void, Void, BufferedInputStream> {

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
