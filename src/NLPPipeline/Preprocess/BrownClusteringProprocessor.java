package NLPPipeline.Preprocess;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import DataStructures.CommentBlock;
import DataStructures.Comments;
import Utilities.Parameters;

public class BrownClusteringProprocessor {

	static FileOutputStream ostream;
	static PrintStream p;

	public static void preprocessForBrown(String appName){
		Comments appComments = new Comments(appName);
		System.out.println("Num comments: " + appComments.getCommentList().size());
		for (CommentBlock c : appComments.getCommentList()) {
			List<String> emphTkns = c.getTokensEmph();
			List<String> textTkns = c.getTokensText();

			for (String s : emphTkns)
				p.print(s + " ");

			if (emphTkns.size() > 0)
				p.println();

			for (String s : textTkns)
				p.print(s + " ");

			p.println();
		}
	}

	public static void main(String[] args){
		FileOutputStream ostream = null;
		try {
			ostream = new FileOutputStream(Parameters.BROWN_DIR
					+ "/" + "brownClustering.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		p = new PrintStream(ostream);

		for (String appName : Parameters.APPS) {
			appName = "ASTRO File Manager with Cloud";
			System.out.println("Doing " + appName);
			p.println("echo Doing '" + appName + "'");
			try {
				preprocessForBrown(appName);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("\nNot done: " + appName);
				continue;
			}
			break;
		}

		p.close();
	}

}
