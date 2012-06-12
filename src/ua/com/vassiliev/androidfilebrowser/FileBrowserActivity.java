package ua.com.vassiliev.androidfilebrowser;
//Heavily based on code from
//https://github.com/mburman/Android-File-Explore
//	Version of Aug 13, 2011
//  This version is taken from the FileExplore from Android File Shareing server project.

//General Java imports 
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

//Android imports 
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


//Import of resources file for file browser
import ua.com.vassiliev.androidfilebrowser.R;


public class FileBrowserActivity extends Activity {
	//Intent Constants
	public static final String INTENT_ACTION_SELECT_DIR = 
			"ua.com.vassiliev.androidfilebrowser.SELECT_DIRECTORY_ACTION";
	public static final String INTENT_ACTION_SELECT_FILE = 
			"ua.com.vassiliev.androidfilebrowser.SELECT_FILE_ACTION";
	
	//Intent parameters constants
	public static final String startDirectoryParameter = 
			"ua.com.vassiliev.androidfilebrowser.directoryPath";
	public static final String returnDirectoryParameter = 
			"ua.com.vassiliev.androidfilebrowser.directoryPathRet";
	public static final String returnFileParameter = 
			"ua.com.vassiliev.androidfilebrowser.filePathRet";
	
	
	
	// Stores names of traversed directories
	ArrayList<String> pathDirsList = new ArrayList<String>();

	// Check if the first level of the directory structure is the one showing
	//private Boolean firstLvl = true;

	private static final String LOGTAG = "F_PATH";

	private List<Item> fileList = new ArrayList<Item>();
	private File path = null;
	private String chosenFile;
	//private static final int DIALOG_LOAD_FILE = 1000;

	ArrayAdapter<Item> adapter;
	
	//Action constants
	private static int currentAction = -1;
	private static final int SELECT_DIRECTORY = 1;
	private static final int SELECT_FILE = 2;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//In case of ua.com.vassiliev.androidfilebrowser.SELECT_DIRECTORY_ACTION
		//Expects com.mburman.fileexplore.directoryPath parameter to 
		// point to the start folder.
		// If empty or null, will start from SDcard root.
		setContentView(R.layout.ua_com_vassiliev_filebrowser_layout);
		
		//Set action for this activity
		Intent thisInt = this.getIntent();
		currentAction = SELECT_DIRECTORY;//This would be a default action in case not set by intent
//		if(thisInt.getAction().equalsIgnoreCase(INTENT_ACTION_SELECT_DIR)) {
//			currentAction = SELECT_DIRECTORY;
//		}
		if(thisInt.getAction().equalsIgnoreCase(INTENT_ACTION_SELECT_FILE)) {
			Log.d(LOGTAG, "SELECT ACTION - SELECT FILE");
			currentAction = SELECT_FILE;
		}
		
		setInitialDirectory();
		
		parceDirectoryPath();
		loadFileList();
		this.createFileListAdapter();
		this.initializeButtons();
		this.initializeFileListView();
		updateCurrentDirectoryTextView();
		Log.d(LOGTAG, path.getAbsolutePath());
	}
	
	
	private void setInitialDirectory() {
		//this.path = Environment.getExternalStorageDirectory();
		Intent thisInt = this.getIntent();
		String requestedStartDir = 
				thisInt.getStringExtra(startDirectoryParameter);
		
		if(requestedStartDir!=null 
				&& 
		   requestedStartDir.length()>0
		) {//if(requestedStartDir!=null
			File tempFile = new File(requestedStartDir);
			if(tempFile.isDirectory())
				this.path = tempFile;
		}//if(requestedStartDir!=null
		
		if(this.path==null) {//No or invalid directory supplied in intent parameter
			if(Environment.getExternalStorageDirectory().isDirectory()
					&&
			   Environment.getExternalStorageDirectory().canRead())
				path = Environment.getExternalStorageDirectory();
			else
				path = new File("/");
		}//if(this.path==null) {//No or invalid directory supplied in intent parameter
	}//private void setInitialDirectory() {
	
	
	private void parceDirectoryPath() {
		pathDirsList.clear();
		String pathString = path.getAbsolutePath();
		String[] parts = pathString.split("/");
		int i=0;
		while(i<parts.length) {
			pathDirsList.add(parts[i]);
			i++;
		}
//		if(pathDirsList.size()>0) 
//			firstLvl = false;
//		else	firstLvl = true;
	}
	
	private void initializeButtons() {
		Button upDirButton = (Button)this.findViewById(R.id.upDirectoryButton);
		upDirButton.setOnClickListener(
			new OnClickListener() {
				public void onClick(View v) {
					Log.d(LOGTAG, "onclick for upDirButton");
					loadDirectoryUp();
					loadFileList();
					adapter.notifyDataSetChanged();
					updateCurrentDirectoryTextView();
				}
			});//upDirButton.setOnClickListener(
		
		Button selectFolderButton = (Button)this.findViewById(R.id.selectCurrentDirectoryButton);
		if(currentAction == this.SELECT_DIRECTORY) {
			selectFolderButton.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						Log.d(LOGTAG, "onclick for selectFolderButton");
						returnDirectoryFinishActivity();
					}
				});//upDirButton.setOnClickListener(
		} else {//if(currentAction == this.SELECT_DIRECTORY) {
			selectFolderButton.setVisibility(View.GONE);
			((TextView)this.findViewById(
					R.id.currentDirectoryTextView)).setVisibility(View.GONE);
		}//} else {//if(currentAction == this.SELECT_DIRECTORY) {
		
		
	}
	
	private void loadDirectoryUp() {
		// present directory removed from list
		String s = pathDirsList.remove(pathDirsList.size() - 1);
		// path modified to exclude present directory
		path = new File(path.toString().substring(0,
				path.toString().lastIndexOf(s)));
		//fileList = null;
		fileList.clear();
		// if there are no more directories in the list, then
		// its the first level
//		if (pathDirsList.isEmpty()) {
//			firstLvl = true;
//		}
	}
	
	private void updateCurrentDirectoryTextView() {
		int i=0;
		String curDirString = "";
		while(i<pathDirsList.size()) {
			curDirString +=pathDirsList.get(i)+"/";
			i++;
		}
		if(pathDirsList.size()==0) {
			((Button)this.findViewById(R.id.upDirectoryButton)).setEnabled(false);
			curDirString = "/";
		}
		else ((Button)this.findViewById(R.id.upDirectoryButton)).setEnabled(true);
		
		//Log.d(TAG, "Will set curr dir to:"+curDirString);
		((TextView)this.findViewById(
				R.id.currentDirectoryTextView)).setText(
				"Current directory:\n"+curDirString);
	}//private void updateCurrentDirectoryTextView() {
	
	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	
	
	private void initializeFileListView() {
		ListView lView = (ListView)this.findViewById(R.id.fileListView);
		lView.setBackgroundColor(Color.LTGRAY);
		LinearLayout.LayoutParams lParam =
				new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		lParam.setMargins(15, 5, 15, 5);
		lView.setAdapter(this.adapter);
		lView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView parent, View view, int position, long id) {
						chosenFile = fileList.get(position).file;
						File sel = new File(path + "/" + chosenFile);
						Log.d(LOGTAG, "Clicked:"+chosenFile);
						if (sel.isDirectory()) {
							if(sel.canRead()) {
								//firstLvl = false;
								// Adds chosen directory to list
								pathDirsList.add(chosenFile);
								path = new File(sel + "");
								Log.d(LOGTAG, "Just reloading the list");
								//returnDirectoryFinishActivity();
								loadFileList();
								adapter.notifyDataSetChanged();
								updateCurrentDirectoryTextView();
								Log.d(LOGTAG, path.getAbsolutePath());
							} else {//if(sel.canRead()) {
								showToast("Path does not exist or cannot be read");
							}//} else {//if(sel.canRead()) {
						}//if (sel.isDirectory()) {
						// Checks if 'up' was clicked
						else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {
							//Below is not needed as there is a separate "Up" button
//							loadDirectoryUp();
//							loadFileList();
//							adapter.notifyDataSetChanged();
//							Log.d(TAG, path.getAbsolutePath());
						}
						// File picked
						else {
							Log.d(LOGTAG, "File select action is not defined");
						}
						//Log.d(TAG, "onClick finished");
					}//public void onClick(DialogInterface dialog, int which) {
				}//new OnItemClickListener() {
		);//lView.setOnClickListener(
	}//private void initializeFileListView() {
	
	private void returnDirectoryFinishActivity() {
		Intent retIntent = new Intent();
		retIntent.putExtra(
				returnDirectoryParameter, 
				path.getAbsolutePath()
		);
		this.setResult(RESULT_OK, retIntent);
		this.finish();
	}//END private void returnDirectoryFinishActivity() {

	private void loadFileList() {
		try {
			path.mkdirs();
		} catch (SecurityException e) {
			Log.e(LOGTAG, "unable to write on the sd card ");
		}
		fileList.clear();

		// Checks whether path exists
		if (path.exists() && path.canRead()) {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					if(currentAction==SELECT_DIRECTORY) {
						return (sel.isDirectory());
					}
					if(currentAction == SELECT_FILE) {
						return true;
					}
					return (sel.isFile() || sel.isDirectory())
							&& !sel.isHidden();
				}//public boolean accept(File dir, String filename) {
			};//FilenameFilter filter = new FilenameFilter() {

			String[] fList = path.list(filter);
			for (int i = 0; i < fList.length; i++) {
				
				// Convert into file path
				File sel = new File(path, fList[i]);
				
				Log.d(LOGTAG, "File:"+fList[i]+" readable:"+
						(new Boolean(sel.canRead())).toString()
						);
				int drawableID = R.drawable.file_icon;
				boolean canRead = sel.canRead();
				// Set drawables
				if (sel.isDirectory()) {
					if(canRead) {
						drawableID = R.drawable.folder_icon;
					}
					else { 
						drawableID = R.drawable.folder_icon_light;
					}
				} else {
					//Log.d("FILE", fileList[i].file);
					//Log.d("FILE", fileList.get(i).file);
					//Nothing to do here - file drawable is already added
				}
				
				fileList.add(i, 
						new Item(
								fList[i], 
								drawableID,
								canRead));
				
				Log.d("DIRECTORY", fileList.get(i).file);
				Log.d(LOGTAG, "Drawable:"+ 
						(new Integer(fileList.get(i).icon)).toString());
			}
			if(fileList.size()==0) {
				Log.d(LOGTAG, "This directory is empty");
				fileList.add(0, 
						new Item("Directory is empty", -1, true));
			}
			else {//sort non empty list
				Collections.sort(fileList, new ItemFileNameComparator());
			}
		} else {
			Log.e(LOGTAG, "path does not exist or cannot be read");
		}
		//Log.d(TAG, "loadFileList finished");
	}//private void loadFileList() {
	
	private void createFileListAdapter(){
		adapter = new ArrayAdapter<Item>(this,
				android.R.layout.select_dialog_item, android.R.id.text1,
				fileList)
			{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// creates view
				//Log.d(TAG, "getView start");
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				// put the image on the text view
				int drawableID = 0;
				if(fileList.get(position).icon != -1) {
					//Log.d(TAG, "Putting image");
					//If icon == -1, then directory is empty
					drawableID = fileList.get(position).icon;
				}
				textView.setCompoundDrawablesWithIntrinsicBounds(
						drawableID, 0, 0, 0);
				
				textView.setEllipsize(null);

				// add margin between image and text (support various screen
				// densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				int dp3 = (int) (3 * getResources().getDisplayMetrics().density + 0.5f);
				//TODO: change next line for empty directory, so text will be centered
				textView.setCompoundDrawablePadding(dp3);
				textView.setBackgroundColor(Color.LTGRAY);
				return view;
			}//public View getView(int position, View convertView, ViewGroup parent) {
		};//adapter = new ArrayAdapter<Item>(this,
		//adapter.setNotifyOnChange(true);
		
		
		
	}//private createFileListAdapter(){

	private class Item {
		public String file;
		public int icon;
		public boolean canRead;

		public Item(String file, Integer icon, boolean canRead) {
			this.file = file;
			this.icon = icon;
		}

		@Override
		public String toString() {
			return file;
		}
	}
	
	private class ItemFileNameComparator implements Comparator<Item> {
		@Override
		public int compare(Item lhs, Item rhs) {
			return lhs.file.toLowerCase().compareTo(rhs.file.toLowerCase());
		}
		
	}//private class ItemComparator implements Comparator<Item> {

}
