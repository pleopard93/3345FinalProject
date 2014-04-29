package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity {

	private String[] difficultiesArray;
	private String[] gameTypesArray;

	private Spinner mDifficultySpinner;
	private Spinner mGameTypeSpinner;
	private Button mGoButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Retrieve the difficulty types
        difficultiesArray = getResources().getStringArray(R.array.difficulties);

        // Retrieve the game tyoes
        gameTypesArray = getResources().getStringArray(R.array.gameTypes);
        
        // Spinners that contains the different difficulties and game types
     	mDifficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);
     	mGameTypeSpinner = (Spinner) findViewById(R.id.gameTypeSpinner);
     	
     	// Button to launch game
     	mGoButton = (Button) findViewById(R.id.goButton);
     	
     	setSpinners();
     	setGoButton();
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
    
    private void launchGame() {
		Intent i = new Intent(this, GameActivity.class);
		//i.putIntegerArrayListExtra(Toppings.TOPPING_KEY, mSavedToppings);
		startActivityForResult(i, 0);
	}
    
    private void setSpinners() {
		// Set the data to the sauces spinner
		// Create an adapter that retrieves the data and set it to the spinner
		final ArrayAdapter<CharSequence> difficultiesAdapter = ArrayAdapter
				.createFromResource(this, R.array.difficulties, android.R.layout.simple_spinner_item);
		difficultiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDifficultySpinner.setAdapter(difficultiesAdapter);
		
		ArrayAdapter<CharSequence> gameTypesAdapter = ArrayAdapter
				.createFromResource(this, R.array.gameTypes, android.R.layout.simple_spinner_item);
		gameTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mGameTypeSpinner.setAdapter(gameTypesAdapter);

		mDifficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Log.d("PBL", "Selected: "+ difficultiesArray[pos]);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		mGameTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Log.d("PBL", "Selected: "+ gameTypesArray[pos]);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
    
    private void setGoButton() {
    	// Add the click listener to the plus icon
		mGoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				launchGame();
			}
		});
    }
    
}
