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
	
	public ProfileSet(){
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
			size -= 1;
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
	
	public String saveProfilesToString() throws TransformerException, ParserConfigurationException{
    	Element profileEl = convProfilesToXml();
    	String profileStr = convXmlToString(profileEl);
    	return profileStr;
    }
	
	public void readProfileToString(String profileStr) throws SAXException, IOException, ParserConfigurationException{
		Document doc;
		doc = convStringToXml(profileStr);
		readProfilesFromXml(doc);

	}
	
	//Crea il set di profili da un Document XML
	public void readProfilesFromXml(Document xmlProfile){
		NodeList list = xmlProfile.getElementsByTagName("profile");
		for(int i = 0; i < list.getLength(); i++){
			insert(convNodeToProfile(list.item(i)));
		}
	}
	
	//Converet i profili in un file xml
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
			//Creo il nodo externalCond
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
	
	//Converte un nodo in un profilo
	public Profile convNodeToProfile(Node profileNode){
		NodeList nodeList = profileNode.getChildNodes();
		//Nome profilo
		String profileName = nodeList.item(0).getChildNodes().item(0).getNodeValue();
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
		boolean externalCond = true;
		if (tmp6.equals("no")){
			externalCond = false;
		}
		return new Profile(profileName, ringVolume, vibrationSet,  wirelessSet, blutoothSet, wirelessCondBool, wirelessCond, blutoothCondBool, blutoothCond, externalCond);
	}
	
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
		StringTokenizer token = new StringTokenizer(s, " ");
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
	
	
}
