package com.wifisecurity.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.example.wifisecurity.R;

public class AboutActivity extends ActionBarActivity {
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    	this.getSupportActionBar().setIcon(R.color.green_pass);
	    	getSupportActionBar().setTitle(R.string.about);
	    	setContentView(R.layout.activity_about);
	    }
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
}
