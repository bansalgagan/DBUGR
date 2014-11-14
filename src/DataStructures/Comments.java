package DataStructures;

import java.util.*;
import java.io.BufferedReader;
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

	public Comments(String filename) {
		parseCommentFile(filename);
	}

	public static void main(String[] args) throws Exception {
		// String sampleCommentFile =
		// "data/raw-comments/Air Call-Accept free (Necta)-raw-comments.txt";
		// Comments testComments = new Comments(sampleCommentFile);
	}

	private void parseCommentFile(String sampleCommentFile) {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(sampleCommentFile));
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

		return cblock;
	}

}