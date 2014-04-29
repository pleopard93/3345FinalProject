package com.example.finalproject;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
	String[] questionsArray;
	String[][] answersArray;
	public static final String GAME_KEY = "GameKey";
	private RadioButton mRadio1;
	private RadioButton mRadio2;
	private RadioButton mRadio3;
	private RadioButton mRadio4;
	private TextView mQuestionTextView;
	private TextView mMultiplierTextView;
	private TextView mScoreTextView;
	private TextView mStreakTextView;
	private int questionIndex;
	private int score;
	private int multiplier;
	private int streak;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Intent i = getIntent();
		gameDataArray = i.getStringArrayExtra(GAME_KEY);

		mRadio1 = (RadioButton) findViewById(R.id.radioButton1);
		mRadio2 = (RadioButton) findViewById(R.id.radioButton2);
		mRadio3 = (RadioButton) findViewById(R.id.radioButton3);
		mRadio4 = (RadioButton) findViewById(R.id.radioButton4);

		mQuestionTextView = (TextView) findViewById(R.id.questionTextView);
		mMultiplierTextView = (TextView) findViewById(R.id.multiplierTextView);
		mScoreTextView = (TextView) findViewById(R.id.scoreTextView);
		mStreakTextView = (TextView) findViewById(R.id.streakTextView);

		getQuestionResources();
		
		questionIndex = 0;
		score = 0;
		multiplier = 1;
		streak = 0;
		
		setNewQuestion();
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

	private void getQuestionResources() {
		Resources res = getResources();

		questionsArray = res.getStringArray(R.array.stateCapitalQuestions);

		TypedArray ta = res.obtainTypedArray(R.array.stateCapitalAnswers);
		int n = ta.length();
		int id;
		answersArray = new String[n][];
		for (int j = 0; j < n; ++j) {
			id = ta.getResourceId(j, 0);
			answersArray[j] = res.getStringArray(id);
		}
		ta.recycle();
	}

	private void setNewQuestion() {
		if (questionIndex < answersArray.length) {
			setTextView(questionIndex);
			setRadioButtons(questionIndex);
			questionIndex++;
		}
	}

	private void setTextView(int questionNumber) {
		mQuestionTextView.setText(questionsArray[questionNumber]);
	}

	private void setRadioButtons(final int questionNumber) {
		// Set the text and listeners for the radio buttons
		mRadio1.setText(answersArray[questionNumber][0]);
		mRadio2.setText(answersArray[questionNumber][1]);
		mRadio3.setText(answersArray[questionNumber][2]);
		mRadio4.setText(answersArray[questionNumber][3]);

		mRadio1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRadio1.setChecked(false);
				checkAnswer(Integer.parseInt(answersArray[questionNumber][4]), 0);
			}
		});

		mRadio2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRadio2.setChecked(false);
				checkAnswer(Integer.parseInt(answersArray[questionNumber][4]), 1);
			}
		});

		mRadio3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRadio3.setChecked(false);
				checkAnswer(Integer.parseInt(answersArray[questionNumber][4]), 2);
			}
		});

		mRadio4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRadio4.setChecked(false);
				checkAnswer(Integer.parseInt(answersArray[questionNumber][4]), 3);
			}
		});
	}
	
	private void checkAnswer(int correctAnswer, int selectedAnswer) {
		if (selectedAnswer == correctAnswer) {
			score += multiplier*5;
			streak++;
			multiplier++;
			Log.d("PBL", "Score: " + Integer.toString(score));
			mScoreTextView.setText(Integer.toString(score));
			mMultiplierTextView.setText("x"+Integer.toString(multiplier));
			mStreakTextView.setText(Integer.toString(streak));
			setNewQuestion();
		} else {
			multiplier = 1;
			streak = 0;
			mMultiplierTextView.setText("x"+Integer.toString(multiplier));
			mStreakTextView.setText(Integer.toString(streak));
		}
	}
}
