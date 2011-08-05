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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.sax.Element;

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
	
	//Crea il set di profili da un file XML
	public void profileFromXML(Document xmlProfile){
		NodeList list = xmlProfile.getElementsByTagName("profile");
		for(int i = 0; i < list.getLength(); i++){
			insert(convNodeToProfile(list.item(i)));
		}
	}
	
	//Converte un nodo in un profilo
	public Profile convNodeToProfile(Node profileNode){
		NodeList nodeList = profileNode.getChildNodes();
		//Nome profilo
		String profileName = nodeList.item(0).getNodeValue();
		//Volume suoneria
		int ringVolume = Integer.parseInt(nodeList.item(1).getNodeValue());
		//Setup della wireless
		String tmp1 = nodeList.item(2).getNodeValue();
		boolean wirelessSet = true;
		if (tmp1.equals("off")){
			wirelessSet = false;
		}
		//Setup del blutooth
		String tmp2 = nodeList.item(3).getNodeValue();
		boolean blutoothSet = true;
		if (tmp2.equals("off")){
			blutoothSet = false;
		}
		//Setup della vibrazione
		String tmp3 = nodeList.item(4).getNodeValue();
		boolean vibrationSet = true;
		if (tmp3.equals("off")){
			vibrationSet = false;
		}
		//Condizioni della wireless
		String tmp4 = nodeList.item(5).getNodeValue();
		ArrayList<String> wirelessCond = convStringToArray(tmp4);
		//Condizioni del blutoth
		String tmp5 = nodeList.item(6).getNodeValue();
		ArrayList<String> blutoothCond = convStringToArray(tmp5);
		//Condizioni del gps
		String tmp6 = nodeList.item(7).getNodeValue();
		boolean externalCond = true;
		if (tmp6.equals("no")){
			externalCond = false;
		}
		return new Profile(profileName, ringVolume, wirelessSet, blutoothSet, vibrationSet, wirelessCond, blutoothCond, externalCond);
	}
	
	//Converte una stringa in un Document XML
	protected Document convStringToXml(String s) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder parser = factory.newDocumentBuilder();
	    Document d = parser.parse(new ByteArrayInputStream(s.getBytes()));
	    return d;
	}
	
	//Converte un Document XML in una stringa
	protected String convXMLToString (Element doc) throws TransformerException{
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource();
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
}
