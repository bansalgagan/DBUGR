package NLPPipeline.Preprocess.mallet;

import java.io.FileOutputStream;
import java.io.PrintStream;
import DataStructures.CommentBlock;
import DataStructures.Comments;
import Utilities.Parameters;

public class MalletGEPreprocessor {
	/**
	 * Takes an app and generates a mallet SimpleTagger style input file
	 * 
	 * @param appName
	 * @throws Exception
	 */
	public static void preprocessForMallet(String appName) throws Exception {
		Comments appComments = new Comments(appName);
		appComments.readInLabels(appName);
		// System.out.println(appName + "\t"+appComments.getUniqueCount());
		FileOutputStream ostream = new FileOutputStream(Parameters.MALLET_DIR
				+ "/" + appName + "-mallet-labelled.txt");
		PrintStream p = new PrintStream(ostream);

		for (CommentBlock c : appComments.getCommentList()) {
			if(c.getLabelEmph().size() < c.getTokensEmph().size())
				break;
			for (int i = 0; i < c.getTokensEmph().size(); i++) {	
				p.println(c.getTokensEmph().get(i) + " "
						+ c.getLabelEmph().get(i));
			}
			p.println();
			
			for (int i = 0; i < c.getTokensText().size(); i++)
				p.println(c.getTokensText().get(i) + " "
						+ c.getLabelText().get(i));
			p.println();
		}
		p.close();
	}

	public static void main(String[] args) {
		try {
			for (String appName : Parameters.APPS) {
				preprocessForMallet(appName);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
