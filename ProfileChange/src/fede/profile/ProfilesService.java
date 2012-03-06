package fede.profile;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

public class ProfilesService extends Service{
	
	private ProfilesSet profilesSet;
	private String profileFile = "profiles.dat";
	
	private ArrayList<String> wfArray;
	private ArrayList<String> bltArray;
	private String location;
	private Context con;
	private WifiManager wfManager;
	private BluetoothAdapter bltAdapter;
	private boolean wfEnabled;
	private boolean bltEnabled;
	
	@Override
	public IBinder onBind(Intent intent) {
	    return null;
	}
	
	@Override
    public void onDestroy() {
    }
	
	public void onCreate() {
	    con = this.getBaseContext();
    	wfManager = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
        bltAdapter = BluetoothAdapter.getDefaultAdapter();
		profilesSet = new ProfilesSet();
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		showNotification("Rilevamento reti wireless, dispositivi bluetooth e locazione in corso....");
		wfArray = new ArrayList<String>();
		bltArray = new ArrayList<String>();
		readProfileToDisk();
		startDetection();
		return START_STICKY;
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
            	getWirelessNetwork();
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bltArray.add(device.getName());
            }
        }
    };
	
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
	}
	
	//Mostra una notifica
    public void showNotification(String notify){
    	Toast.makeText(this, notify, Toast.LENGTH_LONG).show();
    }
    
    //Avvia le interfacce per la scansione
    public void startDetection(){
    	startFindWirelessNetwork();
    	startFindGPS();
    	startFindBluetoothDevice();
    }
	
    //Attiva l'intefaccia wireless e inizia la scansione
    public void startFindWirelessNetwork(){
		wfEnabled = wfManager.isWifiEnabled();
		if (wfEnabled == false){
			wfManager.setWifiEnabled(true);
		}
		wfManager.startScan();
    }
    
    //Attiva l'interfaccia GPS e inizia la scansione
    public void startFindGPS(){
    	LocationManager gpsManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
		GpsStatus gpsStatus = gpsManager.getGpsStatus(null);
		gpsManager.getGpsStatus(gpsStatus);
		Iterable<GpsSatellite> satellitesList = gpsStatus.getSatellites();
		Iterator<GpsSatellite> iterator = satellitesList.iterator();
		location = "Interno";
		if (iterator.hasNext()){
			location = "Esterno";
		}
    }
    
    //Salva i dispositivi bluetooth rilevati
    public void startFindBluetoothDevice(){
        bltEnabled = bltAdapter.isEnabled();
		if (bltEnabled == false){
			bltAdapter.enable();
		}
		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter1);
        registerReceiver(mReceiver, filter2);
		bltAdapter.startDiscovery();
    }
    
    //Salva le reti wireless trovate
    public void getWirelessNetwork(){
		List<ScanResult> wfList = wfManager.getScanResults();
		for (int i = 0; i < wfList.size(); i++){
			wfArray.add(wfList.get(i).SSID); //Array con tutte le wireless rilevate
		}
    	setProfile();
    }
    
    //Imposta il profilo in base alle condizioni rilevate
	public void setProfile(){
		try{	
			Profile bestProfile = profilesSet.getDynamicProfile(wfArray, bltArray, location);
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
		}catch(Exception e){
			startDetection();

		}

	}

}
