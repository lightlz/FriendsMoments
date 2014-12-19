package com.light.friendscommunity.activity;

import com.light.friendscommunity.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PhotoSelectedActivity extends Activity{
	
	private ActionBar actionBar;
	
	private static final String TAG = "PublishActivity : ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photoselector);
		initView();
	}
	
	private void initView(){
		actionBar = getActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(true); 
		actionBar.setTitle("Gallery");
		actionBar.setDisplayShowHomeEnabled(false);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_photo_selected, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
		  case android.R.id.home:
//	            Intent intent = new Intent(this, PublishActivity.class);
//	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	            startActivity(intent);
			    finish();
	            return true;
	        default:
	        	return super.onOptionsItemSelected(item);
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
}
