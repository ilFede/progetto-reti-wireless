package fede.profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class ProfilesSetService extends Service{
	
	private ProfilesSet profilesSet;
	private String profileFile = "profiles.dat";
	
	@Override
	public IBinder onBind(Intent intent) {
	    return null;
	}
	
	@Override
    public void onDestroy() {
    }
	
	public void onCreate() {
		profilesSet = new ProfilesSet();
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		readProfileToDisk();
		return START_STICKY;
	}
	
    //Carica i profili salvati
	private void readProfileToDisk(){
		try{
			FileInputStream fIn = openFileInput(profileFile);
			boolean result = profilesSet.readProfileToDisk(fIn);
			fIn.close();
			if (result == false){
				showNotification("Nessun profilo salvato");
			}
		}catch(Exception e){
			showNotification("Errore nel caricamento dati da parte del service");
		}
		setProfile();

	}
	
	//Mostra una notifica
    public void showNotification(String notify){
    	Toast.makeText(this, notify, Toast.LENGTH_SHORT).show();
    }
	
	public void setProfile(){
		Context con = this.getBaseContext();
		
		//Avvio ricerca dei dispositivi bluetooth
		BluetoothAdapter bltAdapter = BluetoothAdapter.getDefaultAdapter();
		boolean bltEnabled = bltAdapter.isEnabled();
		if (bltEnabled == false){
			bltAdapter.enable();
		}
		bltAdapter.startDiscovery();
		
		//Avvio ricerca delle reti wireless
		//Context con = Context.getApplicationContext();
		WifiManager wfManager = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
		boolean wfEnabled = wfManager.isWifiEnabled();
		if (wfEnabled == false){
			wfManager.setWifiEnabled(true);
		}
		wfManager.startScan();
		
		//Avvio la ricerca della posizione
		LocationManager gpsManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
		GpsStatus gpsStatus = gpsManager.getGpsStatus(null);
		gpsManager.getGpsStatus(gpsStatus);
		
		//Attendo il completamento delle ricerche
		while(bltAdapter.isDiscovering()){
			SystemClock.sleep(2000);
		}
		
		List<ScanResult> wfList = wfManager.getScanResults();
		ArrayList<String> wfArray = new ArrayList<String>();
				
		for (int i = 0; i < wfList.size(); i++){
			wfArray.add(wfList.get(i).SSID); //Array con tutte le wireless rilevate
		}
		
		Set<BluetoothDevice> bltDevices = bltAdapter.getBondedDevices();
		Object[] bltObj = bltDevices.toArray();
		ArrayList<String> bltArray = new ArrayList<String>();
		for (int i = 0; i < bltDevices.size(); i++){
			bltArray.add(((BluetoothDevice) bltObj[i]).getName());
		}		
		Iterable<GpsSatellite> satellitesList = gpsStatus.getSatellites();
		Iterator<GpsSatellite> iterator = satellitesList.iterator();
		String location = "Interno";
		if (iterator.hasNext()){
			location = "Esterno";
		}
		
		//A questo punto ho terminato le ricerche		
		Profile bestProfile = profilesSet.getDinamicProfile(wfArray, bltArray, location);
		
		//Impostare il profilo con bestProfile
		try{
			AudioManager audioManager = (AudioManager) this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
			if (bestProfile.getVibrationSet() == true){
				audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
			}else{
				audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
			}
			
			audioManager.setStreamVolume(AudioManager.STREAM_RING, bestProfile.getRingVolume(), 0);
			
			wfManager.setWifiEnabled(bestProfile.getWirelessSet());
			
			if (bestProfile.getBluetoothSet() == true){
				bltAdapter.enable();
			}else{
				bltAdapter.disable();
			}
			showNotification("Caricato profilo " + bestProfile.getProfileName());
		}catch(Exception e){
			showNotification("Nessun profilo compatibile");
			//Ripristino il bluetooth
			if (bltEnabled == true){
				bltAdapter.enable();
			}else{
				bltAdapter.disable();
			}
			
			//Riprisitno il wireless
			wfManager.setWifiEnabled(wfEnabled);
		}

	}

}
