package NLPPipeline;

import java.io.BufferedReader;

import DataStructures.CommentBlock;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Class for generating features for NER
 * @author bansal
 *
 */
public class FeatureFactory {
	
	//uses Ritter's POS tagger for noisy text
	static boolean POS = false;
	//If is capital
	static boolean CASE = false;
	// Preceding and next word
	static boolean PREV_NEXT = false;
	
	//if is a technical term
	static boolean TECHI = false;
	//os version
	static boolean OS = false;
	//if is a phone brand
	static boolean PHONE = false;
	//sentiment
	static boolean SENTIMENT = false;
	
	
	
	static void test(CommentBlock testCBlock, List<Boolean> features) throws IOException{
		
		POS = features.get(0);
		CASE = features.get(1);
		PREV_NEXT = features.get(2);
		TECHI = features.get(3);
		OS = features.get(4);
		PHONE = features.get(5);
		SENTIMENT = features.get(6);
		
		
		List<String> POS_tags;
		List<String> testEmphTokens = testCBlock.getTokensEmph();
		List<String> testEmphFeatures = getFeatures(testEmphTokens, POS_tags);
		
		List<String> testTextTokens = testCBlock.getTokensText();
		List<String> testTextFeatures = getFeatures(testTextTokens, POS_tags);
		
		
	}
	
	public static List<String> getFeatures(List<String> testCommentTokens, List<String> POS_tags) throws IOException {
		
		String [] featureTokens = new String[testCommentTokens.size()];
		
		List<String> osList = new ArrayList<String>();
		List<String> techList = new ArrayList<String>();
		List<String> deviceList = new ArrayList<String>();
		if (OS)
			osList = getOSList();
		if (TECHI)
			techList = getTechList();
		if (PHONE)
			deviceList = getDeviceList();
			
		for (int i = 0; i < testCommentTokens.size(); i++){
			featureTokens[i] = testCommentTokens.get(i);
			if (POS)
				featureTokens[i] = featureTokens[i] + " POS_" + POS_tags.get(i);
			if (CASE)
				featureTokens[i] = featureTokens[i]  + " " + isCapital(testCommentTokens.get(i));
			if (SENTIMENT)
				featureTokens[i] = featureTokens[i] + " " + getSentiLexiconLabel(testCommentTokens.get(i).toLowerCase(), POS_tags.get(i));
			if (OS && osList.contains(testCommentTokens.get(i).toLowerCase()))
				featureTokens[i] = featureTokens[i] + " OS" ;
			if (TECHI && techList.contains(testCommentTokens.get(i).toLowerCase()))
				featureTokens[i] = featureTokens[i] + " TECHNICAL";
			if (PHONE && deviceList.contains(testCommentTokens.get(i).toLowerCase()))
				featureTokens[i] = featureTokens[i] + " PHONE";
			if (PREV_NEXT)
			{
				if (i != 0)
					featureTokens[i] = featureTokens[i] + " " + testCommentTokens.get(i-1);
				if (i != testCommentTokens.size()-1)
					featureTokens[i] = featureTokens[i] + " " + testCommentTokens.get(i+1);				
			}			
			System.out.println(featureTokens[i] + "\n");
		}	
		return Arrays.asList(featureTokens);
	}

	public static ArrayList<String> getTechList() throws IOException{
		BufferedReader techFile = new BufferedReader(new FileReader("data/lexicons/technical.txt"));
		String line;
		List<String> techlist = new ArrayList<String>();
		while ((line = techFile.readLine())!=null)
			techlist.add(line.toLowerCase());
		techFile.close();
		return (ArrayList<String>) techlist;
	}
	
	public static ArrayList<String> getOSList() throws IOException{
		BufferedReader osFile = new BufferedReader(new FileReader("data/lexicons/osversions.txt"));
		String line;
		List<String> oslist = new ArrayList<String>();
		while ((line = osFile.readLine())!=null)
			oslist.add(line.toLowerCase());
		osFile.close();
		return (ArrayList<String>) oslist;
	}
	
	public static ArrayList<String> getDeviceList() throws IOException{
		BufferedReader deviceFile = new BufferedReader(new FileReader("data/lexicons/devices.txt"));
		String line;
		List<String> devices = new ArrayList<String>();
		while ((line = deviceFile.readLine())!=null)
			devices.add(line.toLowerCase());
		deviceFile.close();
		return (ArrayList<String>) devices;
	}
	
	public static String getSentiLexiconLabel(String token, String POS_tag ) throws IOException{
		SentiLexicon sentiwordnet = new SentiLexicon("data/lexicons/SentiWordNet_3.0.0_20130122.txt");
		
		Double sentiScore = sentiwordnet.extract(token, POS_tag.toLowerCase());
		
		if (sentiScore == null)
			return "";
		if (sentiScore < -0.5)
			return "VERY_NEGATIVE";
		else if (sentiScore < -0.1)
			return "NEGATIVE";
		else if (sentiScore < 0.1)
			return "NEUTRAL";
		else if (sentiScore < 0.5)
			return "POSITIVE";
		else
			return "VERY_POSITIVE";		
	}
	
	public static String isCapital(String s){
		if(s.substring(0,1).toUpperCase().equals(s.substring(0, 1)))
			return "CAPITAL";
		return "";
	}
	
//	public static boolean isTechi(String s){
//		
//	}
	
	
	public static void main(String[] args) throws IOException {
		
		String[] testComment = {"This", "is", "a", "NEXUS", "sucks", "blue", "comment"};
		char[] POS_tags = {'A', 'V', 'D', 'N', 'V', 'A', 'N'};
		CommentBlock cblock = null;
		cblock.setText("This is a NEXUS sucks blue comment");
		List<String> cblock_tokens = Arrays.asList("This", "is", "a", "NEXUS", "sucks", "blue", "comment");
		cblock.setTokensText(cblock_tokens);
		List<Boolean> features = Arrays.asList(true, true, true, true, true, true, true);
		test(cblock, features);

	}

}
