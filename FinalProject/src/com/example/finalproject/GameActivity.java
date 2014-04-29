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
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Intent i = getIntent();
		gameDataArray = i.getStringArrayExtra(GAME_KEY);

		Log.d("PBL", "Difficulty: " + gameDataArray[0]);
		Log.d("PBL", "Game Type:  " + gameDataArray[1]);

		mRadio1 = (RadioButton) findViewById(R.id.radioButton1);
		mRadio2 = (RadioButton) findViewById(R.id.radioButton2);
		mRadio3 = (RadioButton) findViewById(R.id.radioButton3);
		mRadio4 = (RadioButton) findViewById(R.id.radioButton4);

		mTextView = (TextView) findViewById(R.id.questionTextView);

		getQuestionResources();

		setNewQuestion(0);
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

	private void setNewQuestion(int questionNumber) {
		if (questionNumber < answersArray.length) {
			setTextView(questionNumber);
			setRadioButtons(questionNumber);
		}
	}

	private void setTextView(int questionNumber) {
		mTextView.setText(questionsArray[questionNumber]);
	}

	private void setRadioButtons(final int questionNumber) {
		// Set the text and listeners for the radio buttons
		mRadio1.setText(answersArray[questionNumber][0]);
		mRadio2.setText(answersArray[questionNumber][1]);
		mRadio3.setText(answersArray[questionNumber][2]);
		mRadio4.setText(answersArray[questionNumber][3]);

		final int newQuestion = questionNumber + 1;

		mRadio1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNewQuestion(newQuestion);
				mRadio1.setChecked(false);
			}
		});

		mRadio2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNewQuestion(newQuestion);
				mRadio2.setChecked(false);
			}
		});

		mRadio3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNewQuestion(newQuestion);
				mRadio3.setChecked(false);
			}
		});

		mRadio4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setNewQuestion(newQuestion);
				mRadio4.setChecked(false);
			}
		});
	}
}
