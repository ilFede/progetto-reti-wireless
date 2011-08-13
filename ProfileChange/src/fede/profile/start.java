package fede.profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class start extends Activity {
	
	private ProfileSet profileSet;
	private String profileFile = "prova.dat";
	private AudioManager audioManager;
	private int ringStream;
	private int maxVolume;
	
	//Ascoltatori
	/**public void ciao(View v){
		try{
			((TextView) findViewById(R.id.textView2)).setText("ciao");
		}catch(Exception e){
			String s = e.getMessage() + e;
			TextView tv = new TextView(this);
	        tv.setText(s);
	        setContentView(tv);
		}
	}*/
	
	//Apre la finestra di inserimento e modifica profili
	public void openAddView(View v){
		setContentView(R.layout.add);
		//try{
			List<CharSequence> itemListRingVolume = new ArrayList<CharSequence>();
	    	for (int i = 0; i <= maxVolume; i++){
	    		itemListRingVolume.add("" + i);
	    	}
	    	ArrayAdapter<CharSequence> adapterRingVolume = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListRingVolume);
	        adapterRingVolume.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	((Spinner) findViewById(R.id.spnRingVolume)).setAdapter(adapterRingVolume);
	    	//spnVibrationSet
	    	List<CharSequence> itemListVibrationSet = new ArrayList<CharSequence>();
	    	itemListVibrationSet.add("Accesa");
	    	itemListVibrationSet.add("Spenta");
	    	ArrayAdapter<CharSequence> adapterVibrationSet = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListVibrationSet);
	    	adapterVibrationSet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	((Spinner) findViewById(R.id.spnVibrationSet)).setAdapter(adapterVibrationSet);
	    	//spnWirelessSet
	    	List<CharSequence> itemListWirelessSet = new ArrayList<CharSequence>();
	    	itemListWirelessSet.add("Accesa");
	    	itemListWirelessSet.add("Spenta");
	    	ArrayAdapter<CharSequence> adapterWirelessSet = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListWirelessSet);
	        adapterWirelessSet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	((Spinner) findViewById(R.id.spnWirelessSet)).setAdapter(adapterWirelessSet);
	    	//spnBlutoothSet
	    	List<CharSequence> itemListBlutoothSet = new ArrayList<CharSequence>();
	    	itemListBlutoothSet.add("Acceso");
	    	itemListBlutoothSet.add("Spento");
	    	ArrayAdapter<CharSequence> adapterBlutoothSet = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListBlutoothSet);
	        adapterBlutoothSet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	((Spinner) findViewById(R.id.spnBlutoothSet)).setAdapter(adapterBlutoothSet);
	    	List<CharSequence> itemListLocation = new ArrayList<CharSequence>();
	    	itemListLocation.add("Interno");
	    	itemListLocation.add("Esterno");
	    	ArrayAdapter<CharSequence> adapterLocation = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListLocation);
	        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	((Spinner) findViewById(R.id.spnLocation)).setAdapter(adapterLocation);
	}
	
	//Aggiunge un profilo
	public void addProfile(View v){
		String profileName = ((TextView) findViewById(R.id.txtProfName)).getText().toString();
		int ringVolume = Integer.parseInt(((Spinner) findViewById(R.id.spnRingVolume)).getSelectedItem().toString());
		boolean vibrationSet = convStringToBool(((Spinner) findViewById(R.id.spnWirelessSet)).getSelectedItem().toString());
		boolean wirelessSet = convStringToBool(((Spinner) findViewById(R.id.spnWirelessSet)).getSelectedItem().toString());
		boolean blutoothSet = convStringToBool(((Spinner) findViewById(R.id.spnBlutoothSet)).getSelectedItem().toString());
		boolean wirelessCondBool = ((CheckBox) findViewById(R.id.cbxWirelessCondBool)).isChecked();
		ArrayList<String> wirelessCond = convStringToArray(((TextView) findViewById(R.id.txtWirelessCond)).getText().toString());
		boolean blutoothCondBool = ((CheckBox) findViewById(R.id.cbxBlutoothCondBool)).isChecked();
		ArrayList<String> blutoothCond = convStringToArray(((TextView) findViewById(R.id.txtBlutoothCond)).getText().toString());
		boolean externCond = convStringToBool(((Spinner) findViewById(R.id.spnLocation)).getSelectedItem().toString());
		//String s = profileName + ringVolume+vibrationSet+wirelessSet+blutoothSet+wirelessCondBool+wirelessCond+blutoothCondBool+blutoothCond+externCond;
		
		Profile profile = new Profile(profileName, ringVolume, vibrationSet, wirelessSet, blutoothSet, wirelessCondBool, wirelessCond, blutoothCondBool, blutoothCond, externCond);
		profileSet.insert(profile);
		
		try{
			saveProfilesToDisk();
		}catch(Exception e){
		}
		openHomepage();
	}
	
	//Apre la pagina per eliminare un profilo
	public void openRemoveView(View v){
		setContentView(R.layout.remove);
		List<CharSequence> itemListRemoveProfile = new ArrayList<CharSequence>();
		//ArrayList<String> profNameList = profileSet.getProfNameList();
    	for (int i = 0; i < profileSet.getSize(); i++){
    		itemListRemoveProfile.add(profileSet.getProfile(i).getProfileName());
    	}
    	ArrayAdapter<CharSequence> adapterRemoveProfile = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListRemoveProfile);
        adapterRemoveProfile.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	((Spinner) findViewById(R.id.spnProfile)).setAdapter(adapterRemoveProfile);
	}
	
	//Rimuove un profilo()
	public void removeProfile(View v){
		String profile = ((Spinner) findViewById(R.id.spnProfile)).getSelectedItem().toString();
		profileSet.deleteProfile(profile);
		try{
			saveProfilesToDisk();
		}catch(Exception e){
		}
		openHomepage();

	}
	
	//Apre la homepage
	public void openHomepage(View v){
		openHomepage();
	}
	//Fine ascoltatori
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
    	profileSet = new ProfileSet();
    	readProfileToDisk();
		super.onCreate(savedInstanceState);
		//audioManager = (AudioManager) (getSystemService(Context.AUDIO_SERVICE));
		//setContentView(R.layout.homepage);
	    //ringStream = android.media.AudioManager.STREAM_RING;
	    //maxVolume =  audioManager.getStreamMaxVolume(ringStream);
    	//try{
    		//readProfileToDisk();
    	//}catch(Exception e){
    	//}
    	openHomepage();        
    }
    
    //Apre la pagina principale
    public void openHomepage(){
        setContentView(R.layout.homepage);
        if (profileSet.isEmpty()){
        	((Button) findViewById(R.id.btnAggiungi)).setClickable(false);
        }else{
        	((Button) findViewById(R.id.btnAggiungi)).setClickable(true);
        }
    }
    
    //Salva i proili su disco, da modificare perchè bisogna passare la stinga al profile Set e si deve arrangiare
    public void saveProfilesToDisk() throws IOException, TransformerException, ParserConfigurationException{
    	String profileStr = profileSet.saveProfilesToString();
    	FileOutputStream fOut = openFileOutput("prova.dat",MODE_PRIVATE); 
		OutputStreamWriter osw = new OutputStreamWriter(fOut);
		osw.write(profileStr);
		osw.flush();
		osw.close();
		fOut.close();
    }

    //Carica i profili salvati
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
		}catch(IOException e){
			String s = e.getMessage() + e +e.getCause();
			TextView tv = new TextView(this);
	        tv.setText(s);
	        setContentView(tv);
	        System.out.print(s);
		}catch(ParserConfigurationException e){
			String s = e.getMessage() + e +e.getCause();
			TextView tv = new TextView(this);
	        tv.setText(s);
	        setContentView(tv);
	        System.out.print(s);
		}catch(SAXException e){
			String s = e.getMessage() + e +e.getCause();
			TextView tv = new TextView(this);
	        tv.setText(s);
	        setContentView(tv);
	        System.out.print(s);
		}
	        
	}
	//Converte una Stringa in un array List spezzando la stringa
	private ArrayList<String> convStringToArray(String s){
		StringTokenizer token = new StringTokenizer(s, " ");
		ArrayList<String> list = new ArrayList<String>();
		while(token.hasMoreTokens()){
			list.add(token.nextToken());
		}
		return list;
	}
	
	//Converte un array in una stringa separando i valori con " "
	private String convArrayToString(ArrayList<String> array){
		String result = "";
		for(int i = 0; i < array.size(); i++){
			result = result + array.get(i);
		}
		return result;
	}
	
	//Converte una stringa in un boolean
	private boolean convStringToBool(String s){
		boolean result = false;
		if ((s.equalsIgnoreCase("acceso")) ||  (s.equalsIgnoreCase("accesa")) ||  (s.equalsIgnoreCase("esterno"))){
			result = true;
		}
		return result;
	}

}