package ua.com.vassiliev.androidfilebrowser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AndroidFileBrowserExampleActivity extends Activity {
    /** Called when the activity is first created. */
	private final String LOGTAG = "AndroidFileBrowserExampleActivity";
	
	private final int REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL = 1;
	//Arbitrary constant to discriminate against values returned to onActivityResult
	// as requestCode
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Button startBrowserButton = (Button) findViewById(R.id.startFileBrowserButtonID);
        final Activity activityForButton = this;
        startBrowserButton.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Log.d(LOGTAG, "Start browsing button pressed");
    			Intent fileExploreIntent = new Intent(
    					ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
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
        				REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL
        				);
    		}//public void onClick(View v) {
    	});//startBrowserButton.setOnClickListener(new View.OnClickListener() {
    }//public void onCreate(Bundle savedInstanceState) {
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL) {
        	if(resultCode == this.RESULT_OK) {
        		String newDir = data.getStringExtra(
        				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.returnDirectoryParameter);
        		Toast.makeText(
        				this, 
        				"Received path from file browser:"+newDir, 
        				Toast.LENGTH_LONG).show(); 
	        	
        	} else {//if(resultCode == this.RESULT_OK) {
        		Toast.makeText(
        				this, 
        				"Received NO result from file browser",
        				Toast.LENGTH_LONG).show(); 
        	}//END } else {//if(resultCode == this.RESULT_OK) {
        }//if (requestCode == REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}