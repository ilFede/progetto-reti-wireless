package fede.profile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class ProfileSet {
	private ArrayList<Profile> profileSet;
	private int size;
	
	//Costruttore
	public ProfileSet(){
		profileSet = new ArrayList<Profile>();
		size = 0;
	}
	
	//Controlla se non ci sono profili
	public boolean isEmpty(){
		return profileSet.isEmpty();
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
	
	//Restituisce il profilo da impostare in base alle condizioni
	public Profile getDinamicProfile(){
		return null;
	}
	
	//Restituisce il numero di profili salvati
	public int getSize(){
		return size;
	}
	
	//Salva il mprofilo in una stringa xml
	public String saveProfilesToString() throws TransformerException, ParserConfigurationException{
    	Element profileEl = convProfilesToXml();
    	String profileStr = convXmlToString(profileEl);
    	return profileStr;
    }
	
	//Carica i profili da una stringa xml
	public void readProfileToString(String profileStr) throws SAXException, IOException, ParserConfigurationException{
		try{
			Document doc;
			doc = convStringToXml(profileStr);
			readProfilesFromXml(doc);
		}catch(TransformerException e){
		}

	}
	
	//Crea il set di profili da un Document XML
	@SuppressWarnings("unchecked")
	public void readProfilesFromXml(Document xmlProfile) throws ParserConfigurationException, TransformerException{
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
		boolean externalCondBoolArray[] = new boolean[numProf];
		
		NodeList profileNameList = xmlProfile.getElementsByTagName("profileName");
		NodeList ringVolumeList = xmlProfile.getElementsByTagName("ringVolume");
		NodeList vibrationSetList = xmlProfile.getElementsByTagName("vibrationSet");
		NodeList wirelessSetList = xmlProfile.getElementsByTagName("wirelessSet");
		NodeList blutoothSetList = xmlProfile.getElementsByTagName("blutoothSet");
		NodeList wirelessCondBoolList = xmlProfile.getElementsByTagName("wirelessCondBool");
		NodeList wirelessCondList = xmlProfile.getElementsByTagName("wirelessCond");
		NodeList blutoothCondBoolList = xmlProfile.getElementsByTagName("blutoothCondBool");
		NodeList blutoothCondList = xmlProfile.getElementsByTagName("blutoothCond");
		NodeList externalCondBoolList = xmlProfile.getElementsByTagName("externalCondBool");
		
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
	    	wirelessCondBoolArray[i] = convStringToBool(blutoothCondBoolList.item(i).getFirstChild().getTextContent());
	    	try{
	  	    	blutoothCondArray[i] = convStringToArray(blutoothCondList.item(i).getFirstChild().getTextContent());
	    	}catch(Exception e){
	    		blutoothCondArray[i] =  convStringToArray("");
	    	}
	    	externalCondBoolArray[i] = convStringToBool(externalCondBoolList.item(i).getFirstChild().getTextContent());
	    	
	    	Profile newProfile = new Profile(profileNameArray[i], ringVolumeArray[i], vibrationSetArray[i], wirelessSetArray[i], blutoothSetArray[i], wirelessCondBoolArray[i], wirelessCondArray[i], blutoothCondBoolArray[i], blutoothCondArray[i], externalCondBoolArray[i]);
	    	insert(newProfile);

		}
	}
	
	//Converte i profili in un file xml
	public Element convProfilesToXml() throws ParserConfigurationException{
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
			Element blutoothSet = doc.createElement("blutoothSet");
			blutoothSet.appendChild(doc.createTextNode("" + prof.getBlutoothSet()));
			//Creo il nodo wirelessCondBool
			Element wirelessCondBool = doc.createElement("wirelessCondBool");
			wirelessCondBool.appendChild(doc.createTextNode("" + prof.getWirelessCondBool()));
			//Creo il nodo wirelessCond
			Element wirelessCond = doc.createElement("wirelessCond");
			wirelessCond.appendChild(doc.createTextNode(convArrayToString(prof.getWirelessCond())));
			//Creo il nodo blutoothCondBool
			Element blutoothCondBool = doc.createElement("blutoothCondBool");
			blutoothCondBool.appendChild(doc.createTextNode("" + prof.getBlutoothCondBool()));
			//Creo il nodo blutoothCond
			Element blutoothCond = doc.createElement("blutoothCond");
			blutoothCond.appendChild(doc.createTextNode(convArrayToString(prof.getBlutoothCond())));
			//Creo il nodo externalCondBool
			Element externalCondBool = doc.createElement("externalCondBool");
			externalCondBool.appendChild(doc.createTextNode("" + prof.getExternCond()));
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
			profileNode.appendChild(externalCondBool);
			root.appendChild(profileNode);
		}
		return root;		
	}
	
	//Converte un nodo in un profilo
	/**public Profile convNodeToProfile(Node profileNode) throws ParserConfigurationException, TransformerException{
		//profileNode.
		//profileNode.normalize();
		NodeList nodeList = profileNode.getChildNodes();
		//Nome profilo
		//Node a = nodeList.item(0);
	    //Node b = a.getChildNodes().item(0);
	    //Creo il document per un lettura più semplice 
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
		Element root = doc.createElement("singleProfile");
		Node node = doc.createTextNode("sfdaf");
		root.appendChild(node);
		try{
			root.appendChild(profileNode);
		}catch(Exception e){
		}
	    //Ho cerato un documento, ora posso ottenere nodi tramite il nome
    	Node a = doc.getElementsByTagName("profileName").item(0);
    	Node b = a.getChildNodes().item(0);
    	String profileName = b.getTextContent();
		//Volume suoneria
		int ringVolume = Integer.parseInt(nodeList.item(1).getChildNodes().item(0).getNodeValue());
		//Setup della vibrazione
		String tmp3 = nodeList.item(2).getChildNodes().item(0).getNodeValue();
		boolean vibrationSet = true;
		if (tmp3.equals("false")){
			vibrationSet = false;
		}
		//Setup della wireless
		String tmp1 = nodeList.item(3).getChildNodes().item(0).getNodeValue();
		boolean wirelessSet = true;
		if (tmp1.equals("false")){
			wirelessSet = false;
		}
		//Setup del blutooth
		String tmp2 = nodeList.item(4).getChildNodes().item(0).getNodeValue();
		boolean blutoothSet = true;
		if (tmp2.equals("false")){
			blutoothSet = false;
		}
		//Condizioni se considerare la wireless (se false non considero la condizione)
		String tmp7 = nodeList.item(5).getChildNodes().item(0).getNodeValue();
		boolean wirelessCondBool = true;
		if (tmp7.equals("false")){
			wirelessCondBool = false;
		}
		//Condizioni della wireless (elenco wireless da rilevare pe attivare il profilo)
		String tmp4 = nodeList.item(6).getChildNodes().item(0).getNodeValue();
		ArrayList<String> wirelessCond = convStringToArray(tmp4);
		//Condizioni se considerare il blutooth
		String tmp8 = nodeList.item(7).getChildNodes().item(0).getNodeValue();
		boolean blutoothCondBool = true;
		if (tmp8.equals("false")){
			blutoothCondBool = false;
		}
		//Condizioni del blutooth (se false non considero la condizione)
		String tmp5 = nodeList.item(8).getChildNodes().item(0).getNodeValue();
		ArrayList<String> blutoothCond = convStringToArray(tmp5);
		//Condizioni del gps (elenco dispositivi da rilevare per attivare il profilo)
		String tmp6 = nodeList.item(9).getChildNodes().item(0).getNodeValue();
		boolean externalCondBool = true;
		if (tmp6.equals("no")){
			externalCondBool = false;
		}
		return new Profile(profileName, ringVolume, vibrationSet,  wirelessSet, blutoothSet, wirelessCondBool, wirelessCond, blutoothCondBool, blutoothCond, externalCondBool);
	}*/
	
	//Converte una stringa in un Document XML
	protected Document convStringToXml(String s) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder parser = factory.newDocumentBuilder();
	    Document d = parser.parse(new ByteArrayInputStream(s.getBytes()));
	    return d;
	}
	
	//Converte un Document XML in una stringa
	protected String convXmlToString (Element doc) throws TransformerException{
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
		StringTokenizer token = new StringTokenizer(s, " ,;");
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
		if ((s.equalsIgnoreCase("acceso")) ||  (s.equalsIgnoreCase("accesa")) ||  (s.equalsIgnoreCase("esterno")) || (s.equalsIgnoreCase("true"))){
			result = true;
		}
		return result;
	}
	
	
}
