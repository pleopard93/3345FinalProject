package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class GameOverActivity extends ActionBarActivity {
	
	int gameScoreArray[];
	public static final String GAME_OVER_KEY = "GameOverKey";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		
		Intent i = getIntent();
		gameScoreArray = i.getIntArrayExtra(GAME_OVER_KEY);
		Log.d("PBL", Integer.toString(gameScoreArray[0]));
		Log.d("PBLstreak", Integer.toString(gameScoreArray[1]));
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
	
	@Override
    public void onBackPressed() {
		// Send the user back to the home screen instead of
		// back to the game screen
		this.startActivity(new Intent(GameOverActivity.this, MainActivity.class));
    }
}
