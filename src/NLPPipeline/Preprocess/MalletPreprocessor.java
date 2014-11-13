package NLPPipeline.Preprocess;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import DataStructures.CommentBlock;
import DataStructures.Comments;
import Utilities.Parameters;

public class MalletPreprocessor {

	/**
	 * Takes an app and generates a mallet SimpleTagger style input file
	 * @param appName
	 * @throws Exception 
	 */
	public static void preprocessForMallet(String appName) throws Exception{
		String appFileName = Parameters.RAW_COMMENT_DIR + "/" + appName + "-raw-comments.txt";
		//System.out.println(appName);
		Comments appComments = new Comments(appFileName); 
		//System.out.println(appName + "\t"+appComments.getUniqueCount());
		FileOutputStream ostream = new FileOutputStream(Parameters.MALLET_DIR+"/"+appName+"-mallet.txt");
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
		p.close();
	}
	
	public static void main(String[] args) throws Exception {
		for(String appName: Parameters.APPS)
			preprocessForMallet(appName);
			
	}

}
