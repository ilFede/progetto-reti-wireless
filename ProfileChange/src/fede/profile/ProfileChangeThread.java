package fede.profile;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

public class ProfileChangeThread extends Thread implements Runnable{
	
	private ProfilesSet profiles;
	
	public ProfileChangeThread(ProfilesSet profiles){
		this.profiles = profiles;
	}
	
	public void run(){
		while (true){
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
