package NLPPipeline;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

public class MalletPreprocessor {

	/**
	 * Takes an app and generates a mallet SimpleTagger style input file
	 * @param appName
	 * @throws Exception 
	 */
	public static void preprocessForMallet(String appName) throws Exception{
		String appFileName = "data/raw-comments/" + appName + "-raw-comments.txt";
		Comments appComments = new Comments(appFileName); 
		FileOutputStream ostream = new FileOutputStream("data/mallet/"+appName+"-prepro.txt");
		PrintStream p = new PrintStream(ostream);
		
		for(CommentBlock c: appComments.getCommentList()){
			List<String> boldTokens = c.getTokensEmph();
			List<String> tokens = c.getTokensText();
			for(String s: boldTokens)
				p.println(s + " BOLD");
			for(String s: tokens)
				p.println(s);
			p.println();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String appName = "Keys Jumper Adventure";
		preprocessForMallet(appName);
	}

}
