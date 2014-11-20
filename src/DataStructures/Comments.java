package DataStructures;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import NLPPipeline.TwokenizerWrapper;
import Utilities.Parameters;

public class Comments {

	public List<CommentBlock> comBlocks = new ArrayList<CommentBlock>();
	public HashSet<String> timestampList = new HashSet<String>();

	public List<CommentBlock> getCommentList() {
		return comBlocks;
	}

	public int getUniqueCount() {
		return timestampList.size();
	}

	public Comments(String appName) {
		parseCommentFile(appName);
	}

	/**
	 * Stores the POS from Twokenizer in the comments
	 * 
	 * @param appName
	 */

	public void parsePOSFile(String appName) {
		String posFile = Parameters.POS_DIR + "/" + appName + "-pos.txt";
		try {
			FileInputStream fstream = new FileInputStream(posFile);
			Scanner s = new Scanner(fstream);
			int i = 0;
			while (s.hasNext()) {
				String emphLine = s.nextLine();
				String textLine = s.nextLine();
				List<String> posEmph = new ArrayList<String>();
				List<String> posText = new ArrayList<String>();
				String[] tempPOSEmph = emphLine.split(" ");
				String[] tempPOSText = textLine.split(" ");

				// e.g. It/O/PRP/B-NP
				for (String str : tempPOSEmph) {
					String[] posSplit = str.split("/");
					posEmph.add(posSplit[2]);
				}

				for (String str : tempPOSText) {
					String[] posSplit = str.split("/");
					posText.add(posSplit[2]);
				}
				if (comBlocks.get(i).getTokensEmph().size() != posEmph.size()
						|| comBlocks.get(i).getTokensText().size() != posText
								.size()) {
					System.err
							.println("Error wrong number of tokens and pos tags");
					System.exit(0);
				}

				comBlocks.get(i).setPosEmph(posEmph);
				comBlocks.get(i).setPosText(posText);

				i++;
			}
			s.close();
		} catch (FileNotFoundException e) {
			System.err.println("Warning: cannot find pos file for: " + appName);
			return;
		}

	}

	public static void main(String[] args) throws Exception {
		// String sampleCommentFile =
		// "data/raw-comments/Air Call-Accept free (Necta)-raw-comments.txt";
		// Comments testComments = new Comments(sampleCommentFile);
	}

	private void parseCommentFile(String appName) {
		String commentFile = Parameters.RAW_COMMENT_DIR + "/" + appName
				+ "-raw-comments.txt";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(commentFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;

		List<String> comment = new ArrayList<String>();
		try {
			while ((line = br.readLine()) != null) {
				if (!line.isEmpty() && !line.substring(0, 1).equals("e")) {
					// System.out.println(line);
					comment.add(line);
					if (line.equals("}")) {
						CommentBlock cblock = generateCommentBlock(comment);
						if (cblock != null)
							comBlocks.add(cblock);
						comment.clear();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private CommentBlock generateCommentBlock(List<String> comment) {
		String line;
		CommentBlock cblock = new CommentBlock();
		for (int i = 1; i < comment.size() - 1; i++) {
			line = comment.get(i);
			String[] words = line.split(" ");
			if (words[2].equals("text:")) {
				String validText = line.substring(9, line.length() - 1);
				int tIndex = validText.indexOf("\\t");
				if (tIndex == -1) {
					cblock.setEmph("");
					cblock.setText(validText.replaceAll("\\\\[0-9][0-9][0-9]",
							"").replaceAll("\\\\", ""));
				} else {
					cblock.setEmph(validText.substring(0, tIndex)
							.replaceAll("\\\\[0-9][0-9][0-9]", "")
							.replaceAll("\\\\", ""));
					cblock.setText(validText.substring(tIndex + 2)
							.replaceAll("\\\\[0-9][0-9][0-9]", "")
							.replaceAll("\\\\", ""));
				}

				if (cblock.getEmph().trim().length() < Parameters.MINLEN)
					cblock.setEmph("");

				if (cblock.getText().trim().length() < Parameters.MINLEN)
					cblock.setText("");

				if (cblock.getText().isEmpty() && cblock.getEmph().isEmpty()) {
					return null;
				}

				if (!cblock.getEmph().isEmpty())
					cblock.setTokensEmph(TwokenizerWrapper.tokenize(cblock
							.getEmph()));

				if (!cblock.getText().isEmpty())
					cblock.setTokensText(TwokenizerWrapper.tokenize(cblock
							.getText()));

			} else if (words[2].equals("rating:")) {
				cblock.setRating(Integer.parseInt(line.substring(10, 11)));
			} else if (words[2].equals("creationTime:")) {
				cblock.setTimestamp(line.substring(16));
				if (timestampList.contains(cblock.getTimestamp())) {
					return null;
				} else
					timestampList.add(cblock.getTimestamp());
			}
		}
		if (cblock != null) {
			// System.out.println(cblock.emph);
			// System.out.println(cblock.text);
			// System.out.println(cblock.timestamp);
			// System.out.println();
		}

		// remove comment blocks with very short length
		if ((cblock.getTokensText().size() + cblock.getTokensEmph().size()) < Parameters.MINTOKENS)
			return null;

		if (cblock.getRating() >= 3)
			return null;

		return cblock;
	}
	
	
	public void readInPOS(String appName) throws Exception {
		String labelFile = Parameters.POS_DIR + "/" + appName + "-pos.txt";
		FileInputStream fstream = new FileInputStream(labelFile);
		Scanner s = new Scanner(fstream);
		int comNum = 0;
		for (CommentBlock c : this.getCommentList()) {
			//System.out.println("Read " + comNum++);
			if (!s.hasNext())
				break;
			String line = s.nextLine();
			if (!line.isEmpty()) {
				String[] arr = line.split(" ");
				List<String> labelsEmph = new ArrayList<String>();
				for (int i = 0; i < arr.length; i++)
					labelsEmph.add(arr[i].charAt(arr[i].length() - 1) + "");
				c.setPosEmph(labelsEmph);
			}
			else{
				c.setPosEmph(new ArrayList<String>());
			}
			assert c.getPosEmph().size() == c.getTokensEmph().size();

			line = s.nextLine();
			assert line!=null;
			String[] arr = line.split(" ");
			List<String> labelsText = new ArrayList<String>();
			for (int i = 0; i < arr.length; i++)
				labelsText.add(arr[i].charAt(arr[i].length() - 1) + "");
			c.setPosText(labelsText);
			assert c.getTokensText().size() == c.getPosText().size();
		}
		s.close();
	}

	public void readInLabels(String appName) throws Exception {
		String labelFile = Parameters.LABEL_DIR + "/" + appName + "-label.txt";
		FileInputStream fstream = new FileInputStream(labelFile);
		Scanner s = new Scanner(fstream);
		int comNum = 0;
		for (CommentBlock c : this.getCommentList()) {
			System.out.println("Read " + comNum++);
			if (!s.hasNext())
				break;
			String line = s.nextLine();
			if (!line.isEmpty()) {
				String[] arr = line.split(" ");
				List<String> labelsEmph = new ArrayList<String>();
				for (int i = 0; i < arr.length; i++)
					labelsEmph.add(arr[i].charAt(arr[i].length() - 1) + "");
				c.setLabelEmph(labelsEmph);
			}
			
			assert c.getLabelEmph().size() == c.getTokensEmph().size();

			line = s.nextLine();
			assert line!=null;
			String[] arr = line.split(" ");
			List<String> labelsText = new ArrayList<String>();
			for (int i = 0; i < arr.length; i++)
				labelsText.add(arr[i].charAt(arr[i].length() - 1) + "");
			c.setLabelText(labelsText);
			
			c.setLabelled(true);
			
			assert c.getTokensText().size() == c.getLabelText().size();
		}
		s.close();
	}

}