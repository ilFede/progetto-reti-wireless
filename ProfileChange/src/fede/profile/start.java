package fede.profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class start extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	System.out.println("start!!");
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Hello, Android!!!");
        setContentView(tv);
       // try{
		
			FileOutputStream fOut = null;
			OutputStreamWriter osw = null;
	
			try{
				fOut = openFileOutput("settings.dat",MODE_PRIVATE); 
				osw = new OutputStreamWriter(fOut);
				osw.write("ciao");
				osw.flush();
				osw.close();
				fOut.close();
				tv.setText("ciao");
			}
			catch (Exception e) { 
				e.printStackTrace();
				tv.setText("errore1");
			}
						
			
			FileInputStream fIn = null;
			InputStreamReader isr = null;

			char[] inputBuffer = new char[255];
			String data = null;

			try{
			fIn = openFileInput("settings.dat"); 
			isr = new InputStreamReader(fIn);
			isr.read(inputBuffer);
			data = new String(inputBuffer) + "adsda";
			tv.setText(data);
			}
			catch (Exception e) { 
			e.printStackTrace();tv.setText("errore4");
			
			}
			finally {
			try {
			isr.close();
			fIn.close();
			} catch (IOException e) {
			e.printStackTrace();
			tv.setText("errore5");
			}
			}
			while(true){}
       

        	/*
        	File sdcardDir = Environment.getDataDirectory();
        	File file = new File(sdcardDir, "/Profile/prova.txt");
        	FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeUTF("Ho scito questo");
            dos.close();
        	tv.setText("ciao");
        }catch(Exception e){
        	tv.setText(e.toString());
        }*/
    }
}