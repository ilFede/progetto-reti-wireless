package fede.profile; 

import java.util.ArrayList;

public class Profile {
	private String profileName;
	private int ringVolume;
	private boolean wirelessSet;
	private boolean blutoothSet;
	private boolean vibrationSet;
	private ArrayList<String> wirelessCond; //Wireless che devono essere ricevute per attivare il profilo
	private ArrayList<String> blutoothCond; //Dispositivi che devono essere ricevuti per attivare il profilo
	private boolean externCond; //Luogo (interno o esterno) in cui si deve essere per attivare il profilo
	
	public Profile(String profileName, int ringVolume, boolean wirelessSet, boolean blutoothSet, boolean vibrationSet, ArrayList<String> wirelessCond, ArrayList<String> blutoothCond, boolean externCond){
		this.profileName = profileName;
		this.ringVolume = ringVolume;
		this.blutoothSet = blutoothSet;
		this.wirelessSet = wirelessSet;
		this.vibrationSet = vibrationSet;
		this.wirelessCond = wirelessCond;
		this.blutoothCond = blutoothCond;
		this.externCond = externCond;
	}	
	
	public void setProfileName(String newProfileName){
		profileName = newProfileName;
	}
	public void setRingVolume(int newRingVolume){
		ringVolume = newRingVolume;
	}
	
	public void setWireless(boolean newWirelessSet){
		wirelessSet = newWirelessSet; 
	}
	
	public void setBlutoothSet(boolean newBlutoothSet){
		blutoothSet = newBlutoothSet;
	}
	
	public void setVibratioSet(boolean newVibrationSet){
		vibrationSet = newVibrationSet;
	}
	
	public String getProfileName(){
		return profileName;
	}
	
	public int getRingVolume(){
		return ringVolume;
	}
	
	public boolean getBlutoothSet(){
		return blutoothSet;
	}
	
	public boolean getWirelessSet(){
		return wirelessSet;
	}
	
	public boolean getVibrationSet(){
		return vibrationSet;
	}
	
	public ArrayList<String> getBluetoothCond(){
		return blutoothCond;
	}
	
	public ArrayList<String> getWirelessCond(){
		return wirelessCond;
	}
	
	public boolean getExternCond(){
		return externCond;
	}
}
