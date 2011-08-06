package fede.profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Element;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class start extends Activity {
	private ProfileSet profileSet = new ProfileSet();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	System.out.println("start!!");
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Hello, Android!!!");
        setContentView(tv);
       // try{	
			try{
				FileOutputStream fOut = openFileOutput("settings.dat",MODE_PRIVATE); 
				OutputStreamWriter osw = new OutputStreamWriter(fOut);
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

			/**char[] inputBuffer = new char[255];
			String data = null;*/

			try{
				FileInputStream fIn = openFileInput("settings.dat");
				InputStreamReader osr = new InputStreamReader(fIn);
				String tmp = "";
				while(osr.ready()){
					tmp = tmp + (char)osr.read();
				}
				
				/**Scanner sc = new Scanner(new File("/data/data/fede.profile/file/settings.dat"));
				sc.useDelimiter("\n");
				String tmp = "";
				while (sc.hasNext()){
					tmp = tmp + sc.next();
				}
				fIn = openFileInput("settings.dat"); 
				isr = new InputStreamReader(fIn);
				isr.read(inputBuffer);
				data = new String(inputBuffer) + "adsda";*/
				tv.setText("prova" + tmp);
			}
			catch (Exception e){ 
			    tv.setText(e.getMessage());
			
			}
			//while(true){}
       

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
    
    //Salva i proili su disco, da modificare perchè bisogna passare la stinga al profile Set e si deve arrangiare
    public void saveProfilesToDisck(){
    	try{
	    	Element profileEl = profileSet.convProfilesToXml();
	    	String profileStr = profileSet.convXmlToString(profileEl);
	    	FileOutputStream fOut = openFileOutput("settings.dat",MODE_PRIVATE); 
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
			FileInputStream fIn = openFileInput("settings.dat");
			InputStreamReader osr = new InputStreamReader(fIn);
			String tmp = "";
			while(osr.ready()){
				tmp = tmp + (char)osr.read();
			}
		}catch(Exception e){
		}
		}

}