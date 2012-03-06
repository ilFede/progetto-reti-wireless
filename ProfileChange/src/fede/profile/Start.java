package fede.profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Start extends Activity {
	
	private ProfilesSet profilesSet;
	private String profileFile = "profiles.dat";
	private AudioManager audioManager;
	private int ringStream;
	private int maxVolume;
	//private Context appContext;
	private Intent srvIntent;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
    	try{
	    	profilesSet = new ProfilesSet();
	    	readProfilesToDisk();
			super.onCreate(savedInstanceState);
			audioManager = (AudioManager) (getSystemService(Context.AUDIO_SERVICE));
			setContentView(R.layout.homepage);
		    ringStream = android.media.AudioManager.STREAM_RING;
		    maxVolume =  audioManager.getStreamMaxVolume(ringStream);
	    	//appContext = this.getApplicationContext();
	    	//srvIntent = new Intent(getApplicationContext(), Class.forName("fede.profile.ProfilesSetService"));
	    	srvIntent = new Intent(getApplicationContext(), ProfilesService.class);
	    	openHomepage();
	    	//showNotification("Applicazione caricata correttamente");
    	}catch(Exception e){
    		//showNotification("Errore nel caricamento dell'applicazione");
    	}
    }
    
    @Override
    public void onBackPressed() {
    }
    
    //Mostra una notifica
    private void showNotification(String notify){
    	Context context = getApplicationContext();
		CharSequence text = notify;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
    }
	
	//Ascoltatori
		
	//Apre la finestra di inserimento profili
    public void openAddView(View v){
		setContentView(R.layout.add);
		//txtProfName
		((EditText) findViewById(R.id.txtProfName)).setEnabled(true);
	    //spnRingVolune
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
    	//spnLocation
    	List<CharSequence> itemListLocation = new ArrayList<CharSequence>();
    	itemListLocation.add("Interno");
    	itemListLocation.add("Esterno");
    	itemListLocation.add("Indifferente");
    	ArrayAdapter<CharSequence> adapterLocation = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListLocation);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	((Spinner) findViewById(R.id.spnLocation)).setAdapter(adapterLocation);
	}
	
	
	//Apre la finestra per selezionare il profilo da eliminare
    public void openModifyView(View v){
		setContentView(R.layout.modify);
		List<CharSequence> itemListModifyProfile = new ArrayList<CharSequence>();
		//ArrayList<String> profNameList = profileSet.getProfNameList();
    	for (int i = 0; i < profilesSet.getSize(); i++){
    		itemListModifyProfile.add(profilesSet.getProfile(i).getProfileName());
    	}
    	ArrayAdapter<CharSequence> adapterModifyProfile = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListModifyProfile);
        adapterModifyProfile.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	((Spinner) findViewById(R.id.spnMdfProfile)).setAdapter(adapterModifyProfile);
	}
	
	//Apre la finestra di modifica profili
    public void openModifyProfileView(View v){
		String strProfile = ((Spinner) findViewById(R.id.spnMdfProfile)).getSelectedItem().toString();
		Profile profile = profilesSet.getProfile(strProfile);
		setContentView(R.layout.add);
		//txtProfName
		((EditText) findViewById(R.id.txtProfName)).setText(profile.getProfileName());
		((EditText) findViewById(R.id.txtProfName)).setEnabled(false);
		//spnRingVolune
		List<CharSequence> itemListRingVolume = new ArrayList<CharSequence>();
    	for (int i = 0; i <= maxVolume; i++){
    		itemListRingVolume.add("" + i);
    	}
    	ArrayAdapter<CharSequence> adapterRingVolume = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListRingVolume);
        adapterRingVolume.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	((Spinner) findViewById(R.id.spnRingVolume)).setAdapter(adapterRingVolume);
    	((Spinner) findViewById(R.id.spnRingVolume)).setSelection(profile.getRingVolume());
    	//spnVibrationSet
    	List<CharSequence> itemListVibrationSet = new ArrayList<CharSequence>();
    	itemListVibrationSet.add("Accesa");
    	itemListVibrationSet.add("Spenta");
    	ArrayAdapter<CharSequence> adapterVibrationSet = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListVibrationSet);
    	adapterVibrationSet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	((Spinner) findViewById(R.id.spnVibrationSet)).setAdapter(adapterVibrationSet);
    	if (!profile.getVibrationSet()){
    		((Spinner) findViewById(R.id.spnVibrationSet)).setSelection(1);
    	}
    	//spnWirelessSet
    	List<CharSequence> itemListWirelessSet = new ArrayList<CharSequence>();
    	itemListWirelessSet.add("Accesa");
    	itemListWirelessSet.add("Spenta");
    	ArrayAdapter<CharSequence> adapterWirelessSet = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListWirelessSet);
        adapterWirelessSet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	((Spinner) findViewById(R.id.spnWirelessSet)).setAdapter(adapterWirelessSet);
    	if (!profile.getWirelessSet()){
    		((Spinner) findViewById(R.id.spnWirelessSet)).setSelection(1);
    	}
    	//spnBlutoothSet
    	List<CharSequence> itemListBlutoothSet = new ArrayList<CharSequence>();
    	itemListBlutoothSet.add("Acceso");
    	itemListBlutoothSet.add("Spento");
    	ArrayAdapter<CharSequence> adapterBlutoothSet = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListBlutoothSet);
        adapterBlutoothSet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	((Spinner) findViewById(R.id.spnBlutoothSet)).setAdapter(adapterBlutoothSet);
    	if (!profile.getBluetoothSet()){
    		((Spinner) findViewById(R.id.spnBlutoothSet)).setSelection(1);
    	}
    	//cbxWirelessCondBool
    	((CheckBox) findViewById(R.id.cbxWirelessCondBool)).setChecked(profile.getWirelessCondBool());
    	//txtWirelessCond
    	((EditText) findViewById(R.id.txtWirelessCond)).setText(convArrayToString(profile.getWirelessCond()));
    	//cbxBlutoothCondBool
    	((CheckBox) findViewById(R.id.cbxBlutoothCondBool)).setChecked(profile.getBluetoothCondBool());
    	//txtBlutoothCond
    	((EditText) findViewById(R.id.txtBlutoothCond)).setText(convArrayToString(profile.getBluetoothCond()));
    	//spnLocation
    	List<CharSequence> itemListLocation = new ArrayList<CharSequence>();
    	itemListLocation.add("Interno");
    	itemListLocation.add("Esterno");
    	itemListLocation.add("Indifferente");
    	ArrayAdapter<CharSequence> adapterLocation = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListLocation);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	((Spinner) findViewById(R.id.spnLocation)).setAdapter(adapterLocation);
    	if (profile.getExternCond().equalsIgnoreCase("interno")){
    		((Spinner) findViewById(R.id.spnLocation)).setSelection(0);
    	}else if (profile.getExternCond().equalsIgnoreCase("esterno")){
    		((Spinner) findViewById(R.id.spnLocation)).setSelection(1);
    	}else{
        	((Spinner) findViewById(R.id.spnLocation)).setSelection(2);
    	}
	}
	
	//Aggiunge o modifica un profilo
    public void addProfile(View v){
		String profileName = ((TextView) findViewById(R.id.txtProfName)).getText().toString();
		int ringVolume = Integer.parseInt(((Spinner) findViewById(R.id.spnRingVolume)).getSelectedItem().toString());
		boolean vibrationSet = convStringToBool(((Spinner) findViewById(R.id.spnVibrationSet)).getSelectedItem().toString());
		boolean wirelessSet = convStringToBool(((Spinner) findViewById(R.id.spnWirelessSet)).getSelectedItem().toString());
		boolean blutoothSet = convStringToBool(((Spinner) findViewById(R.id.spnBlutoothSet)).getSelectedItem().toString());
		boolean wirelessCondBool = ((CheckBox) findViewById(R.id.cbxWirelessCondBool)).isChecked();
		ArrayList<String> wirelessCond = convStringToArray(((TextView) findViewById(R.id.txtWirelessCond)).getText().toString());
		boolean blutoothCondBool = ((CheckBox) findViewById(R.id.cbxBlutoothCondBool)).isChecked();
		ArrayList<String> blutoothCond = convStringToArray(((TextView) findViewById(R.id.txtBlutoothCond)).getText().toString());
		String externCond = (((Spinner) findViewById(R.id.spnLocation)).getSelectedItem().toString());
		//String s = profileName + ringVolume+vibrationSet+wirelessSet+blutoothSet+wirelessCondBool+wirelessCond+blutoothCondBool+blutoothCond+externCond;
		
		Profile profile = new Profile(profileName, ringVolume, vibrationSet, wirelessSet, blutoothSet, wirelessCondBool, wirelessCond, blutoothCondBool, blutoothCond, externCond);
		profilesSet.deleteProfile(profileName);
		profilesSet.insert(profile);
		
		try{
			saveProfilesToDisk();
		}catch(Exception e){
			showNotification("Errore nel salvataggio dei dati");
		}
		openHomepage();
	}
	
	//Apre la pagina per eliminare un profilo
    public void openRemoveView(View v){
		setContentView(R.layout.remove);
		List<CharSequence> itemListRemoveProfile = new ArrayList<CharSequence>();
    	for (int i = 0; i < profilesSet.getSize(); i++){
    		itemListRemoveProfile.add(profilesSet.getProfile(i).getProfileName());
    	}
    	ArrayAdapter<CharSequence> adapterRemoveProfile = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, itemListRemoveProfile);
        adapterRemoveProfile.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	((Spinner) findViewById(R.id.spnRmvProfile)).setAdapter(adapterRemoveProfile);
	}
	
	//Rimuove un profilo()
    public void removeProfile(View v){
		String profile = ((Spinner) findViewById(R.id.spnRmvProfile)).getSelectedItem().toString();
		profilesSet.deleteProfile(profile);
		try{
			saveProfilesToDisk();
		}catch(Exception e){
			showNotification("Errore nel salvataggio dei dati");
		}
		openHomepage();

	}
	
	//Apre la homepage
    public void openHomepage(View v){
		openHomepage();
	}
    
    //Chiude l'applicazione
    public void closeApplication(View v){
    	System.exit(0);
    }
    
	//Fine ascoltatori
    
    //Apre la pagina principale
	private void openHomepage(){
        setContentView(R.layout.homepage);
        if (profilesSet.isEmpty()){
        	((Button) findViewById(R.id.btnRimuovi)).setEnabled(false);
        	((Button) findViewById(R.id.btnModifica)).setEnabled(false);
        }else{
        	((Button) findViewById(R.id.btnRimuovi)).setEnabled(true);
        	((Button) findViewById(R.id.btnModifica)).setEnabled(true);
        }
    }
    
    //Salva i proili su disco, da modificare perchè bisogna passare la stinga al profile Set e si deve arrangiare
	private void saveProfilesToDisk() throws IOException, TransformerException, ParserConfigurationException{
    	FileOutputStream fOut = openFileOutput(profileFile,Context.MODE_WORLD_WRITEABLE); 
    	boolean result = profilesSet.saveProfilesToDisk(fOut);
    	fOut.flush();
    	fOut.close();
		if (result == false){
			showNotification("Errore nel salvataggio dati");
		}else{
			//showNotification("Profilo salvato");
		}
    }

    //Carica i profili salvati
	private void readProfilesToDisk(){
		try{
			FileInputStream fIn = openFileInput(profileFile);
			boolean result = profilesSet.readProfileToDisk(fIn);
			fIn.close();
			if (result == false){
				showNotification("Nessun profilo salvato");
			}else{
				showNotification("Profili caricati");
			}
		}catch(Exception e1){
			try{
				saveProfilesToDisk();
				showNotification("Nessun profilo salvato");
			}catch(Exception e2){
				showNotification("Errore nel salvataggio dati");
			}
		}
	}
	
	//Converte una Stringa in un array List spezzando la stringa
	private ArrayList<String> convStringToArray(String s){
		StringTokenizer token = new StringTokenizer(s, " ,");
		ArrayList<String> list = new ArrayList<String>();
		while(token.hasMoreTokens()){
			list.add(token.nextToken());
		}
		return list;
	}
	
	//Converte un array in una stringa separando i valori con ","
	private String convArrayToString(ArrayList<String> array){
		String result = "";
		for(int i = 0; i < array.size(); i++){
			result = result + array.get(i) + ",";
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
	
	//Metodi per il Service
	//Avvia il service
	public void serviceStart(View v){
		startService(srvIntent);
	}

}