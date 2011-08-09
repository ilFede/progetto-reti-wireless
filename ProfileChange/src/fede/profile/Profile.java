package fede.profile; 

import java.util.ArrayList;

public class Profile {
	private String profileName;
	private int ringVolume;
	private boolean wirelessSet;
	private boolean blutoothSet;
	private boolean vibrationSet;
	private ArrayList<String> wirelessCond; //Wireless che devono essere ricevute per attivare il profilo
	private boolean wirelessCondBool; //true se le bisogna considerare le wireless trovate
	private ArrayList<String> blutoothCond; //Dispositivi che devono essere ricevuti per attivare il profilo
	private boolean blutoothCondBool; //true se bisogna considerare i dispositivi blutooth trovati
	private boolean externCond; //Luogo (interno o esterno) in cui si deve essere per attivare il profilo
	
	public Profile(String profileName, int ringVolume, boolean vibrationSet, boolean wirelessSet, boolean blutoothSet, boolean wirelessCondBool, ArrayList<String> wirelessCond, boolean blutoothCondBool, ArrayList<String> blutoothCond, boolean externCond){
		this.profileName = profileName;
		this.ringVolume = ringVolume;
		this.vibrationSet = vibrationSet;
		this.blutoothSet = blutoothSet;
		this.wirelessSet = wirelessSet;
		this.wirelessCondBool = wirelessCondBool;
		this.wirelessCond = wirelessCond;
		this.blutoothCondBool = blutoothCondBool;
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
	
	public boolean getVibrationSet(){
		return vibrationSet;
	}
	
	public boolean getBlutoothSet(){
		return blutoothSet;
	}
	
	public boolean getWirelessSet(){
		return wirelessSet;
	}
	
	public boolean getWirelessCondBool(){
		return wirelessCondBool;
	}
	
	public ArrayList<String> getBlutoothCond(){
		return blutoothCond;
	}
	
	public boolean getBlutoothCondBool(){
		return blutoothCondBool;
	}
	
	public ArrayList<String> getWirelessCond(){
		return wirelessCond;
	}
	
	public boolean getExternCond(){
		return externCond;
	}
}
