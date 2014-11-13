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
		String appFileName = Parameters.RAW_COMMENT_DIR + "/" + appName
				+ "-raw-comments.txt";
		Comments appComments = new Comments(appFileName);
		FileOutputStream ostream1 = new FileOutputStream(Parameters.POS_DIR
				+ "/" + appName + "-prepos.txt");
		PrintStream p1 = new PrintStream(ostream1);

		for (CommentBlock c : appComments.getCommentList()) {
			String emphStr = c.getEmph();
			String str = c.getText();
			if (emphStr != null)
				p1.println(emphStr);
			if (str != null)
				p1.println(str);
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
				System.out.println("Not done: " + appName);
				continue;
			}
		}
		p.close();
	}

}
