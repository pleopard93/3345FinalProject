package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class GameOverActivity extends ActionBarActivity {
	
	private TextView scoreTextView;
	private TextView streakTextView;
	private TextView newGameTextView;
	
	int gameScoreArray[];
	public static final String GAME_OVER_KEY = "GameOverKey";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		
		Intent i = getIntent();
		gameScoreArray = i.getIntArrayExtra(GAME_OVER_KEY);

		scoreTextView = (TextView) findViewById(R.id.scoreTextView);
		streakTextView = (TextView) findViewById(R.id.streakTextView);
		newGameTextView = (TextView) findViewById(R.id.newGameTextView);
		
		scoreTextView.setText("Score: " + Integer.toString(gameScoreArray[0]));
		streakTextView.setText("Max streak: " + Integer.toString(gameScoreArray[1]));
		
		// Add the click listener to the new game icon
		newGameTextView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					v.setBackgroundColor(getResources().getColor(
							R.color.darkerblue));
					return true;
				case MotionEvent.ACTION_UP:
					goToHomeScreen();
					return true;
				}
				return false;
			}
		});
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
	
	private void goToHomeScreen() {
		// Send the user back to the home screen
		Intent i = new Intent(this, MainActivity.class);
		startActivityForResult(i, 0);
	}
	
	@Override
    public void onBackPressed() {
		// Send the user back to the home screen instead of
		// back to the game screen
		this.startActivity(new Intent(GameOverActivity.this, MainActivity.class));
    }
}
