package NLPPipeline.Preprocess;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import DataStructures.CommentBlock;
import DataStructures.Comments;
import Utilities.Parameters;

public class BrownClusteringProprocessor {

	static FileOutputStream ostream;
	static PrintStream p;

	public static void preprocessForBrown(String appName) throws Exception {
		String appFileName = Parameters.RAW_COMMENT_DIR + "/" + appName
				+ "-raw-comments.txt";
		Comments appComments = new Comments(appFileName);
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

	public static void main(String[] args) throws Exception {
		FileOutputStream ostream = new FileOutputStream(Parameters.BROWN_DIR
				+ "/" + "brownClustering.txt");
		p = new PrintStream(ostream);

		for (String appName : Parameters.APPS) {
			System.out.println("Doing " + appName);
			p.println("echo Doing '" + appName + "'");
			try {
				preprocessForBrown(appName);
			} catch (Exception e) {
				System.out.println("Not done: " + appName);
				continue;
			}
		}

		p.close();
	}

}
