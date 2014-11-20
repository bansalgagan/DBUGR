package NLPPipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utilities.Parameters;

import DataStructures.CommentBlock;
import DataStructures.Comments;

public class Extractor {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		for(String appName: Parameters.APPS){
			Comments comments = new Comments(appName);
			comments.readInLabels(appName);
			for(CommentBlock c: comments.getCommentList()){
				if(c.getLabelled()){
					List<List<String>> extractions = extract(c);
					System.out.println(extractions);
				}
			}
			//System.out.println(getAllEntitityMention("ONNOOPPON"));
			break;
		}
	}
	
	
	public static List<List<String>> extract(CommentBlock c){
		List<List<String>> extractions = new ArrayList<List<String>>();
		String label = "";
		for(String s: c.getLabelEmph())
			label += s;
		HashMap<String, List<Triple<Integer, Integer, String>>> mentions = getAllEntitityMention(c, 0, label);
		//extractions = applyRules(mentions, );
//		for(String s: mentions.keySet()){
//			for(Triple<Integer, Integer, String> mention: mentions.get(s)){
//				System.out.println(mention.a + " " + mention.b + " " + mention.c);
//			}
//			
//		}
		
		//BF
		if(mentions.get("B"))
		
		
		return null;
	}
	
	public static HashMap<String, List<Triple<Integer, Integer, String>>> getAllEntitityMention(CommentBlock c, int cs, String text){
		List<List<Triple<Integer, Integer, String>>> list = new ArrayList<List<Triple<Integer,Integer,String>>>();
		HashMap<String, List<Triple<Integer, Integer, String>>> map = new HashMap<String, List<Triple<Integer, Integer, String>>>();
		map.put("B", getMentions(c, cs, text, "B+"));
		map.put("D", getMentions(c, cs, text, "D+"));
		map.put("F", getMentions(c, cs, text, "F+"));
		map.put("A", getMentions(c, cs, text, "A+"));
		map.put("N", getMentions(c, cs, text, "N+"));
		map.put("P", getMentions(c, cs, text, "P+"));
		map.put("O", getMentions(c, cs, text, "O+"));

		return map;
	}
	
	public static List<Triple<Integer, Integer, String>> getMentions(CommentBlock c, int cs, String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    List<Triple<Integer, Integer, String>>  occurs = new ArrayList<Triple<Integer, Integer, String>>();
	    List<String> tokens = null;
	    if(cs == 0)
	    	tokens = c.getTokensEmph();
	    else
	    	tokens = c.getTokensText();
	    // Check all occurrences
	    while (matcher.find()) {
	    	String ext = "";
	        System.out.print("Start index: " + matcher.start());
	        System.out.print(" End index: " + matcher.end());
	        System.out.println(" Found: " + matcher.group());
	        for(int i=matcher.start(); i<matcher.end(); i++){
	        	ext += tokens.get(i) + " ";
	        }
	        
	        occurs.add(new Triple<Integer, Integer, String>(matcher.start(), matcher.end(), ext));
	        
	    }
	    return occurs;
	}

}

class Triple<A, B, C>{
	A a;
	B b;
	C c;
	
	public Triple(A a, B b, C c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
}