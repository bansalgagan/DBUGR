package NLPPipeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import NLPPipeline.Twokenize;

public class TwokenizerWrapper {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		test();
	}
	
	public static void test2(){
		String line = "we've srk sucks";
		List<String> toks = Twokenize.tokenizeRawTweetText(line);
		System.out.println(toks);
	}
	
	public static List<String> tokenize(String line){
		return  Twokenize.tokenizeRawTweetText(line);
	}
	
	public static void test() throws IOException{
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
	        PrintStream output = new PrintStream(System.out, true, "UTF-8");
	    	String line;
	    	while ( (line = input.readLine()) != null) {
	    		List<String> toks = Twokenize.tokenizeRawTweetText(line);
	    		for (int i=0; i<toks.size(); i++) {
	    			output.print(toks.get(i));
	    			if (i < toks.size()-1) {
	    				output.print(" ");
	    			}
	    		}
	    		output.print("\n");
	    	}
	}

}
