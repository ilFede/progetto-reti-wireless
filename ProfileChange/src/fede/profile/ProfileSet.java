package fede.profile;

import java.util.ArrayList;

public class ProfileSet {
	private ArrayList<Profile> profileSet;
	private int size;
	
	private ProfileSet(){
		profileSet = new ArrayList<Profile>();
		size = 0;
	}
	
	public boolean isEmpty(){
		return profileSet.isEmpty();
	}
	
	public void insert(Profile newProfile){
		profileSet.add(newProfile);
		size += 1;
	}
	
	public void deleteProfile(int index){
		if (index < size){
			profileSet.remove(index);
		}
	}
	
	public Profile getProfile(int index){
		if (index < size){
			return profileSet.get(index);
		}else{
			return null;
		}
	}
	
	public Profile getDinamicProfile(){
		return null;
	}
}
