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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity {

	private String[] difficultiesArray;
	private String[] gameDataArray;
	private ArrayList<String> categoryList;

	private Spinner mDifficultySpinner;
	private Spinner mGameTypeSpinner;
	private Button mGoButton;
	
	private static final int READ_TIMEOUT = 10000;
	private static final int CONNECTION_TIMEOUT = 15000;
	private static String JSON_LOCATION;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Retrieve the difficulty types
        difficultiesArray = getResources().getStringArray(R.array.difficulties);
        
        // Spinners that contains the different difficulties and game types
     	mDifficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);
     	mGameTypeSpinner = (Spinner) findViewById(R.id.gameTypeSpinner);
     	
     	// Button to launch game
     	mGoButton = (Button) findViewById(R.id.goButton);
     	
     	gameDataArray = new String[2];
     	
     	JSON_LOCATION = "http://192.168.56.1:8888/QuizApp/api/index.php/categories";
     	new HttpGetData().execute(null, null, null);
     	
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
		i.putExtra(GameActivity.GAME_KEY, gameDataArray);
		startActivityForResult(i, 0);
	}
    
    private void setSpinners() {
		// Set the data to the sauces spinner
		// Create an adapter that retrieves the data and set it to the spinner
		final ArrayAdapter<CharSequence> difficultiesAdapter = ArrayAdapter
				.createFromResource(this, R.array.difficulties, android.R.layout.simple_spinner_item);
		difficultiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDifficultySpinner.setAdapter(difficultiesAdapter);
		
		final ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
		categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mGameTypeSpinner.setAdapter(categoriesAdapter);
		

		mDifficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				gameDataArray[0] = difficultiesArray[pos];
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		mGameTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				gameDataArray[1] = categoryList.get(pos);
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
    
    private void parseJSON(InputStream in) throws JSONException {
		JSONArray categoriesJSON = new JSONArray(getResponseText(in));
		categoryList = new ArrayList<String>();
		for (int i = 0; i < categoriesJSON.length(); i++) {
			categoryList.add(categoriesJSON.getString(i));
		}
		
		setSpinners();
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

