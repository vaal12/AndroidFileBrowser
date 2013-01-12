AndroidFileBrowser
==================

Provides an Activity, which can be used to select files/folders from the application.
This project is based on the Manish Burman's Android-File-Explore project (https://github.com/mburman/Android-File-Explore), but is heavily rewritten.

And example Eclipse project is included.

To use the component in your project, you would need to copy the following (to the same folders of your project):
1. ua\com\vassiliev\androidfilebrowser\FileBrowserActivity.java from src folder
2. a. folder_icon_light.png 
   b. dir_up.png 
   c. file_icon.png 
   d. folder_icon.png all from res\drawable-ldpi
3. res\layout\ua_com_vassiliev_filebrowser_layout.xml 

Change line 37 (import of ua.com.vassiliev.androidfilebrowser.R) to the R file of your project, so resources you copied would be available for the FileBrowser.

To call the activity you do it as alwasy (see AndroidFileBrowserExampleActivity.java):
```
   Intent fileExploreIntent = new Intent(
   	ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
   	null,
        activityForButton,
        ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.class
    );
//  fileExploreIntent.putExtra(
//  	ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.startDirectoryParameter, 
//      "/sdcard"
//  );//Here you can add optional start directory parameter, and file browser will start from that directory.
    startActivityForResult(
    	fileExploreIntent,
    	REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL
    );
```

To get result overrider onActivityResult method of calling Activity for example (from AndroidFileBrowserExampleActivity.java):
```
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
        			Toast.LENGTH_LONG
        		).show(); 
        	} else {//if(resultCode == this.RESULT_OK) {
        		Toast.makeText(
       				this, 
       				"Received NO result from file browser",
       				Toast.LENGTH_LONG)
       			.show(); 
        	}//END } else {//if(resultCode == this.RESULT_OK) {
        }//if (requestCode == REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL) {
		super.onActivityResult(requestCode, resultCode, data);
}
```
	
Features
At the moment only pick directory activity is provided, though more options are planned to be added in the future.
	
Sometimes it is necessary to run Project/Clean for both this project and project(s) which rely on this to regenerate correct files/jars.
	
License
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

Thanks. 
This project is inspired and initially based on the project of Manish Burman - https://github.com/mburman/Android-File-Explore.


 						
						
