package ua.com.vassiliev.androidfilebrowser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AndroidFileBrowserExampleActivity extends Activity {
    /** Called when the activity is first created. */
	private final String LOGTAG = "AndroidFileBrowserExampleActivity";
	
	private final int REQUEST_CODE_PICK_DIR = 1;
	private final int REQUEST_CODE_PICK_FILE = 2;
	//Arbitrary constant to discriminate against values returned to onActivityResult
	// as requestCode
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        final Activity activityForButton = this;
        
        final Button startBrowserButton = (Button) findViewById(R.id.startFileBrowserButtonID);
        
        startBrowserButton.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.d(LOGTAG, "Start browsing button pressed");
    			Intent fileExploreIntent = new Intent(
    					ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
        				null,
        				activityForButton,
        				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.class
        				);
    			//If the parameter below is not provided the Activity will try to start from sdcard(external storage),
    			// if fails, then will start from roor "/"
    			// Do not use "/sdcard" instead as base address for sdcard use Environment.getExternalStorageDirectory() 
//        		fileExploreIntent.putExtra(
//        				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.startDirectoryParameter, 
//        				"/sdcard"
//        				);
        		startActivityForResult(
        				fileExploreIntent,
        				REQUEST_CODE_PICK_DIR
        				);
    		}//public void onClick(View v) {
    	});//startBrowserButton.setOnClickListener(new View.OnClickListener() {
        
        final Button startBrowser4FileButton = (Button) findViewById(R.id.startFileBrowser4FileButtonID);
        startBrowser4FileButton.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.d(LOGTAG, "StartFileBrowser4File button pressed");
    			Intent fileExploreIntent = new Intent(
    					ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.INTENT_ACTION_SELECT_FILE,
        				null,
        				activityForButton,
        				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.class
        				);
//        		fileExploreIntent.putExtra(
//        				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.startDirectoryParameter, 
//        				"/sdcard"
//        				);
        		startActivityForResult(
        				fileExploreIntent,
        				REQUEST_CODE_PICK_FILE
        				);
    		}//public void onClick(View v) {
        });
        
        final Button start4FileHideNonReadButton = 
        		(Button) findViewById(R.id.startBrowse4FileHideNonReadButtonID);
        start4FileHideNonReadButton.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.d(LOGTAG, "StartFileBrowser4File button pressed");
    			Intent fileExploreIntent = new Intent(
    					ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.INTENT_ACTION_SELECT_FILE,
        				null,
        				activityForButton,
        				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.class
        				);
    			fileExploreIntent.putExtra(
    					ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.showCannotReadParameter, 
    					false);
        		startActivityForResult(
        				fileExploreIntent,
        				REQUEST_CODE_PICK_FILE
        				);
    		}//public void onClick(View v) {
        });//start4FileHideNonReadButton.setOnClickListener(new View.OnClickListener() {
        
    }//public void onCreate(Bundle savedInstanceState) {
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PICK_DIR) {
        	if(resultCode == RESULT_OK) {
        		String newDir = data.getStringExtra(
        				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.returnDirectoryParameter);
        		Toast.makeText(
        				this, 
        				"Received DIRECTORY path from file browser:\n"+newDir, 
        				Toast.LENGTH_LONG).show(); 
	        	
        	} else {//if(resultCode == this.RESULT_OK) {
        		Toast.makeText(
        				this, 
        				"Received NO result from file browser",
        				Toast.LENGTH_LONG).show(); 
        	}//END } else {//if(resultCode == this.RESULT_OK) {
        }//if (requestCode == REQUEST_CODE_PICK_DIR) {
		
		if (requestCode == REQUEST_CODE_PICK_FILE) {
        	if(resultCode == RESULT_OK) {
        		String newFile = data.getStringExtra(
        				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.returnFileParameter);
        		Toast.makeText(
        				this, 
        				"Received FILE path from file browser:\n"+newFile, 
        				Toast.LENGTH_LONG).show(); 
	        	
        	} else {//if(resultCode == this.RESULT_OK) {
        		Toast.makeText(
        				this, 
        				"Received NO result from file browser",
        				Toast.LENGTH_LONG).show(); 
        	}//END } else {//if(resultCode == this.RESULT_OK) {
        }//if (requestCode == REQUEST_CODE_PICK_FILE) {
		
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}