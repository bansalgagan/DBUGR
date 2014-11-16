package NLPPipeline.Preprocess;

import java.io.FileOutputStream;
import java.io.PrintStream;

import DataStructures.CommentBlock;
import DataStructures.Comments;
import Utilities.Parameters;

/**
 * Generates a bash file in src/scripts/generatePOS.sh that calls twitter NLP
 * for POS tags.
 * 
 * @author bansal
 *
 */
public class POSPreprocessor {

	static FileOutputStream ostream;
	static PrintStream p;

	public static void generatePOSTags(String appName) throws Exception {

		String prePosFile = (Parameters.POS_DIR + "/" + appName + "-prepos.txt")
				//.replace(" ", "\\ ").replace("(", "\\(").replace(")", "\\)");
		;
		String posFile = (Parameters.POS_DIR + "/" + appName + "-pos.txt")
				//.replace(" ", "\\ ").replace("(", "\\(").replace(")", "\\)")
				;
		p.println("cat '" + prePosFile + "' | python " + Parameters.TWITTER_NLP
				+ "/python/ner/extractEntities2.py --pos --chunk >>'" + posFile + "'");

	}

	public static void preprocessForPOS(String appName) throws Exception {
		Comments appComments = new Comments(appName);
		FileOutputStream ostream1 = new FileOutputStream(Parameters.POS_DIR
				+ "/" + appName + "-prepos.txt");
		PrintStream p1 = new PrintStream(ostream1);

		for (CommentBlock c : appComments.getCommentList()) {
			
			for(int i=0; i < c.getTokensEmph().size(); i++){
				if(i== c.getTokensEmph().size()-1)
					p1.println(c.getTokensEmph().get(i));
				else
					p1.print(c.getTokensEmph().get(i) + " ");
			}
			
			for(int i=0; i < c.getTokensText().size(); i++){
				if(i== c.getTokensText().size()-1)
					p1.println(c.getTokensText().get(i));
				else
					p1.print(c.getTokensText().get(i) + " ");
			}
		}

		p1.close();
		generatePOSTags(appName);
	}

	public static void main(String[] args) throws Exception {
		ostream = new FileOutputStream("src/scripts/generatePOS.sh");
		p = new PrintStream(ostream);
		p.println("#!/bin/bash");
		p.println("export TWITTER_NLP=" + Parameters.TWITTER_NLP);
		for (String appName : Parameters.APPS) {
			System.out.println("Doing " + appName);
			p.println("echo Doing '" + appName + "'");
			try {
				preprocessForPOS(appName);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Not done: " + appName);
				continue;
			}
			break;
		}
		p.close();
	}

}
