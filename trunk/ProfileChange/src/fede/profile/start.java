package fede.profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class start extends Activity {
	
	private ProfileSet profileSet = new ProfileSet();
	private String profileFile = "setting.dat";
	
	//Ascoltatori
	public void ciao(View v){
		try{
			((TextView) findViewById(R.id.textView2)).setText("ciao");
		}catch(Exception e){
			String s = e.getMessage() + e;
			TextView tv = new TextView(this);
	        tv.setText(s);
	        setContentView(tv);
		}
	}
	//Fine ascoltatori
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	try{
    		readProfileToDisk();
    	}catch(Exception e){
    	}
    	openHomepage();        
    }
    
    public void openHomepage(){
        setContentView(R.layout.homepage);
    }
    
    //Salva i proili su disco, da modificare perchè bisogna passare la stinga al profile Set e si deve arrangiare
    public void saveProfilesToDisck(){
    	try{
	    	String profileStr = profileSet.saveProfilesToString();
	    	FileOutputStream fOut = openFileOutput(profileFile,MODE_PRIVATE); 
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(profileStr);
			osw.flush();
			osw.close();
			fOut.close();
    	}catch(Exception e){
    	}
    }

	public void readProfileToDisk(){
		try{
			FileInputStream fIn = openFileInput(profileFile);
			InputStreamReader osr = new InputStreamReader(fIn);
			String profileStr = "";
			while(osr.ready()){
				profileStr = profileStr + (char)osr.read();
			}
			profileSet.readProfileToString(profileStr);
			osr.close();
			fIn.close();
		}catch(Exception e){
		}
		}

}