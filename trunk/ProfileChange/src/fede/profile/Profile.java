package fede.profile; 

public class Profile {
	private String profileName;
	private int ringVolume;
	private boolean wirelessState;
	private boolean blutoothState;
	private boolean vibrationState;
	
	public Profile(int ringVolume, boolean wirelessState, boolean blutoothState, boolean vibrationState){
		this.ringVolume = ringVolume;
		this.blutoothState = blutoothState;
		this.wirelessState = wirelessState;
		this.vibrationState = vibrationState;
	}	
	
	public void setProfileName(String newProfileName){
		profileName = newProfileName;
	}
	public void setRingVolume(int newRingVolume){
		ringVolume = newRingVolume;
	}
	
	public void setWireless(boolean newWirelessState){
		wirelessState = newWirelessState; 
	}
	
	public void setBlutoothState(boolean newBlutoothState){
		blutoothState = newBlutoothState;
	}
	
	public void setVibratioState(boolean newVibrationState){
		vibrationState = newVibrationState;
	}
	
	public String getProfileName(){
		return profileName;
	}
	
	public int getRingVolume(){
		return ringVolume;
	}
	
	public boolean getBlutoothState(){
		return blutoothState;
	}
	
	public boolean getWirelessState(){
		return wirelessState;
	}
	
	public boolean getVibrationState(){
		return vibrationState;
	}
}
