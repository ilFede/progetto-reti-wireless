package fede.profile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ProfilesSet {
	private ArrayList<Profile> profileSet;
	private int size;
	
	//Costruttore
	public ProfilesSet(){
		profileSet = new ArrayList<Profile>();
		size = 0;
	}
	
	//Controlla se non ci sono profili
	public boolean isEmpty(){
		return size == 0;
	}
	
	//Inserisce un nuovo profilo
	public void insert(Profile newProfile){
		profileSet.add(newProfile);
		size += 1;
	}
	
	//Cancella un profilo dato l'indice
	private void deleteProfile(int index){
		if ((index < size)&&(index >= 0)){
			profileSet.remove(index);
			size -= 1;
		}
	}
	
	//Cancella un profilo dato il nome
	public void deleteProfile(String profName){
		for(int i = 0; i < size; i++){
			if (profileSet.get(i).getProfileName().equalsIgnoreCase(profName)){
				deleteProfile(i);
			}
		}
	}
	
	//Restituisce l'elenco dei nomi dei profili
	public ArrayList<String> getProfNameList(){
		ArrayList<String> profNameList = new ArrayList<String>();
		for(int i = 0; i < size; i++){
			profNameList.add(profileSet.get(i).getProfileName());
		}
		return profNameList;
	}
	
	//Restituisce un profilo dato l'indice
	public Profile getProfile(int index){
		if (index < size){
			return profileSet.get(index);
		}else{
			return null;
		}
	}
	
	//Restituisce un profilo dato il nome
	public Profile getProfile(String profName){
		for(int i = 0; i < size; i++){
			if (profileSet.get(i).getProfileName().equalsIgnoreCase(profName)){
				return profileSet.get(i);
			}
		}
		return null;
	}
	
	//Restituisce il profilo da impostare in base alle condizioni, questo codice invec sembra più per ottenere le informazioni, metterlo nel servizio 
	public Profile getDynamicProfile(ArrayList<String> wirelessDect, ArrayList<String> bluetoothDect, String externCond){
		int score = 0;
		int maxScore = 0;
		int bestProfile = -1;
		//Controllo tutti i profili per trovare il migliore
		for (int i = 0; i < size; i++){
			score = 0;
			boolean wirelessOk = false;
			boolean bluetoothOk = false;
			boolean locationOk = false;
			Profile temp = profileSet.get(i);
			//Controllo punteggio per wireless
			if (temp.getWirelessCondBool() == true){
				ArrayList<String> wirelessCond = temp.getWirelessCond();
				for(int j = 0; j < wirelessCond.size(); j++){
					for(int k = 0; k < wirelessDect.size(); k++){
						if ((wirelessCond.get(j)).equalsIgnoreCase(wirelessDect.get(k))){
							wirelessOk = true;
							score+= 1;
						}
					}
				}
			}else{
				wirelessOk = true;
			}
			//Controllo punteggio per bluetooth
			if (temp.getBluetoothCondBool() == true){
				ArrayList<String> bluetoothCond = temp.getBluetoothCond();
				for(int j = 0; j < bluetoothCond.size(); j++){
					for(int k = 0; k < bluetoothDect.size(); k++){
						if ((bluetoothCond.get(j)).equalsIgnoreCase(bluetoothDect.get(k))){
							bluetoothOk = true;
							score+= 1;
						}
					}
				}
			}else{
				bluetoothOk = true;
			}
			//Controllo punteggio per location
			if (temp.getExternCond().equalsIgnoreCase(externCond)){
				locationOk = true;
				score+= 1;
			}else if(temp.getExternCond().equalsIgnoreCase("Indifferente")){
				locationOk = true;
			}
			if ((maxScore < score)&&(wirelessOk == true)&&(bluetoothOk == true)&&(locationOk == true)){
				bestProfile = i;
			}
		}
		if (bestProfile == -1){
			return null;
		}else{
			return profileSet.get(bestProfile);
		}
	}
	
	//Restituisce il numero di profili salvati
	public int getSize(){
		return size;
	}
	
	//Svuota i profili
	public void cancellAllProfiles(){
		profileSet = new ArrayList<Profile>();
		size = 0;
	}
	
	 //Salva i proili su disco, da modificare perchè bisogna passare la stinga al profile Set e si deve arrangiare
    public boolean saveProfilesToDisk(FileOutputStream fOut) throws IOException, TransformerException, ParserConfigurationException{
    	try{
    		String profileStr = saveProfilesToString();
    		OutputStreamWriter osw = new OutputStreamWriter(fOut);
    		osw.write(profileStr);
    		osw.flush();
    		osw.close();
    		return true;
    	}catch(Exception e){
    		return false;
    	}
    }

    //Carica i profili salvati
	public boolean readProfileToDisk(FileInputStream fIn){
		try{
			//Cancello tutti i profili
			cancellAllProfiles();
			//FileInputStream fIn = openFileInput(profileFile);
			InputStreamReader osr = new InputStreamReader(fIn);
			String profileStr = "";
			while(osr.ready()){
				profileStr = profileStr + (char)osr.read();
			}
			readProfileToString(profileStr);
			osr.close();
			return true;
		}catch(Exception e){
			return false;
		}   
	}
	
	//Salva il mprofilo in una stringa xml
	private String saveProfilesToString() throws TransformerException, ParserConfigurationException{
    	Element profileEl = convProfilesToXml();
    	String profileStr = convXmlToString(profileEl);
    	return profileStr;
    }
	
	//Carica i profili da una stringa xml
	private void readProfileToString(String profileStr) throws SAXException, IOException, ParserConfigurationException{
		try{
			Document doc;
			doc = convStringToXml(profileStr);
			readProfilesFromXml(doc);
		}catch(TransformerException e){
		}
	}
	
	//Crea il set di profili da un Document XML
	@SuppressWarnings("unchecked")
	private void readProfilesFromXml(Document xmlProfile) throws ParserConfigurationException, TransformerException{
		NodeList profileList = xmlProfile.getElementsByTagName("profile");
		int numProf = profileList.getLength(); //Numero di profili trovati
		//Creo degli array con le impostazioni dei profili
		String profileNameArray[] = new String[numProf];
		int ringVolumeArray[] = new int[numProf];
		boolean vibrationSetArray[] = new boolean[numProf];
		boolean wirelessSetArray[] = new boolean[numProf];
		boolean blutoothSetArray[] = new boolean[numProf];
		boolean wirelessCondBoolArray[] = new boolean[numProf];
		ArrayList<String> wirelessCondArray[] = new ArrayList[numProf];
		boolean blutoothCondBoolArray[] = new boolean[numProf];
		ArrayList<String> blutoothCondArray[] = new ArrayList[numProf];
		String externalCondBoolArray[] = new String[numProf];
		
		NodeList profileNameList = xmlProfile.getElementsByTagName("profileName");
		NodeList ringVolumeList = xmlProfile.getElementsByTagName("ringVolume");
		NodeList vibrationSetList = xmlProfile.getElementsByTagName("vibrationSet");
		NodeList wirelessSetList = xmlProfile.getElementsByTagName("wirelessSet");
		NodeList blutoothSetList = xmlProfile.getElementsByTagName("bluetoothSet");
		NodeList wirelessCondBoolList = xmlProfile.getElementsByTagName("wirelessCondBool");
		NodeList wirelessCondList = xmlProfile.getElementsByTagName("wirelessCond");
		NodeList blutoothCondBoolList = xmlProfile.getElementsByTagName("bluetoothCondBool");
		NodeList blutoothCondList = xmlProfile.getElementsByTagName("bluetoothCond");
		NodeList externalCondList = xmlProfile.getElementsByTagName("externalCond");
		
		for(int i = 0; i < numProf; i++){
			try{
				profileNameArray[i] = profileNameList.item(i).getFirstChild().getTextContent();
			}catch(Exception e){
				profileNameArray[i] = "";
			}
	    	ringVolumeArray[i] = Integer.parseInt(ringVolumeList.item(i).getFirstChild().getTextContent());
	    	vibrationSetArray[i] = convStringToBool(vibrationSetList.item(i).getFirstChild().getTextContent());
	    	wirelessSetArray[i] = convStringToBool(wirelessSetList.item(i).getFirstChild().getTextContent());
	    	blutoothSetArray[i] = convStringToBool(blutoothSetList.item(i).getFirstChild().getTextContent());
	    	wirelessCondBoolArray[i] = convStringToBool(wirelessCondBoolList.item(i).getFirstChild().getTextContent());
	    	try{
	    		wirelessCondArray[i] = convStringToArray(wirelessCondList.item(i).getFirstChild().getTextContent());
	    	}catch(Exception e){
	    		wirelessCondArray[i] = convStringToArray("");
	    	}
	    	blutoothCondBoolArray[i] = convStringToBool(blutoothCondBoolList.item(i).getFirstChild().getTextContent());
	    	try{
	  	    	blutoothCondArray[i] = convStringToArray(blutoothCondList.item(i).getFirstChild().getTextContent());
	    	}catch(Exception e){
	    		blutoothCondArray[i] =  convStringToArray("");
	    	}
	    	try{
	    		externalCondBoolArray[i] = (externalCondList.item(i).getFirstChild().getTextContent());
			}catch(Exception e){
				externalCondBoolArray[i] = "";
			}
	    	Profile newProfile = new Profile(profileNameArray[i], ringVolumeArray[i], vibrationSetArray[i], wirelessSetArray[i], blutoothSetArray[i], wirelessCondBoolArray[i], wirelessCondArray[i], blutoothCondBoolArray[i], blutoothCondArray[i], externalCondBoolArray[i]);
	    	insert(newProfile);

		}
	}
	
	//Converte i profili in un file xml
	private Element convProfilesToXml() throws ParserConfigurationException{
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		//create the root element and add it to the document
		Element root = doc.createElement("profileSet");
		
		for(int i= 0; i<profileSet.size(); i++){
			Profile prof = profileSet.get(i);
			Element profileNode = doc.createElement("profile");
			//Creo il nodo profileName
			Element profileName = doc.createElement("profileName");
			profileName.appendChild(doc.createTextNode(prof.getProfileName()));
			//Creo il nodo ringVolum
			Element ringVolume = doc.createElement("ringVolume");
			ringVolume.appendChild(doc.createTextNode("" + prof.getRingVolume()));
			//Creo il nodo vibrationSet
			Element vibrationSet = doc.createElement("vibrationSet");
			vibrationSet.appendChild(doc.createTextNode("" + prof.getVibrationSet()));
			//Creo il nodo wirelessSet
			Element wirelessSet = doc.createElement("wirelessSet");
			wirelessSet.appendChild(doc.createTextNode("" + prof.getWirelessSet()));
		    //Creo il nodo blutoothSet
			Element blutoothSet = doc.createElement("bluetoothSet");
			blutoothSet.appendChild(doc.createTextNode("" + prof.getBluetoothSet()));
			//Creo il nodo wirelessCondBool
			Element wirelessCondBool = doc.createElement("wirelessCondBool");
			wirelessCondBool.appendChild(doc.createTextNode("" + prof.getWirelessCondBool()));
			//Creo il nodo wirelessCond
			Element wirelessCond = doc.createElement("wirelessCond");
			wirelessCond.appendChild(doc.createTextNode(convArrayToString(prof.getWirelessCond())));
			//Creo il nodo blutoothCondBool
			Element blutoothCondBool = doc.createElement("bluetoothCondBool");
			blutoothCondBool.appendChild(doc.createTextNode("" + prof.getBluetoothCondBool()));
			//Creo il nodo blutoothCond
			Element blutoothCond = doc.createElement("bluetoothCond");
			blutoothCond.appendChild(doc.createTextNode(convArrayToString(prof.getBluetoothCond())));
			//Creo il nodo externalCondBool
			Element externalCond = doc.createElement("externalCond");
			externalCond.appendChild(doc.createTextNode("" + prof.getExternCond()));
			//Appendo i nodi
			profileNode.appendChild(profileName);
			profileNode.appendChild(ringVolume);
			profileNode.appendChild(vibrationSet);
			profileNode.appendChild(wirelessSet);
			profileNode.appendChild(blutoothSet);
			profileNode.appendChild(wirelessCondBool);
			profileNode.appendChild(wirelessCond);
			profileNode.appendChild(blutoothCondBool);
			profileNode.appendChild(blutoothCond);
			profileNode.appendChild(externalCond);
			root.appendChild(profileNode);
		}
		return root;		
	}
	
	//Converte una stringa in un Document XML
	private Document convStringToXml(String s) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder parser = factory.newDocumentBuilder();
	    Document d = parser.parse(new ByteArrayInputStream(s.getBytes()));
	    return d;
	}
	
	//Converte un Document XML in una stringa
	private String convXmlToString (Element doc) throws TransformerException{
		TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        String xmlString = sw.toString();
        return xmlString;
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
	
	//Converte un array in una stringa separando i valori con " ,"
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
		if ((s.equalsIgnoreCase("acceso")) ||  (s.equalsIgnoreCase("accesa")) ||  (s.equalsIgnoreCase("esterno")) || (s.equalsIgnoreCase("true"))){
			result = true;
		}
		return result;
	}
	
	
}
