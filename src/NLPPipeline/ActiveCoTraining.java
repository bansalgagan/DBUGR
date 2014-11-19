package NLPPipeline;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import DataStructures.CommentBlock;
import DataStructures.Comments;
import Utilities.Parameters;

public class ActiveCoTraining {
	public static final int N = 1;
	public static final int neg = 10;
	public static final int pos = 10;

	public static void test() {
		for (String appName : Parameters.APPS) {
			try {
				act(appName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private static void act(String appName) throws Exception {

		Comments comments = new Comments(appName);
		long seed = System.nanoTime();
		Collections.shuffle((List<CommentBlock>) comments, new Random(seed));

		comments.readInLabels(appName);
		List<CommentBlock> allComments = comments.getCommentList();
		List<CommentBlock> unlabelledComments = new ArrayList<CommentBlock>();
		List<CommentBlock> labelledComments = new ArrayList<CommentBlock>();
		for (CommentBlock c : allComments) {
			if (c.isLabelled())
				labelledComments.add(c);
			else
				unlabelledComments.add(c);
		}
		List<CommentBlock> labelledCommentsTrain = labelledComments.subList(0,
				labelledComments.size() * 7 / 10);
		List<CommentBlock> labelledCommentsTest = labelledComments.subList(
				labelledComments.size() * 7 / 10, labelledComments.size());

		for (int i = 0; i < N; i++) {
			@SuppressWarnings("unchecked")
			List<List<String>>[] labels = new List[2];

			// Train two classifiers on two orthogonal feature sets
			for (int j = 0; j < 2; j++)
				labels[j] = trainClassifierAndLabel(i, labelledCommentsTrain,
						labelledCommentsTest);

			// Figure out the informative and non-informative examples
			HashMap<CommentBlock, String> map = new HashMap<CommentBlock, String>();
			for (int k = 0; k < labelledCommentsTest.size(); k++) {
				List<String> predEmphLabels0 = labels[0].get(2 * k);
				List<String> predTextLabels0 = labels[0].get(2 * k + 1);
				List<String> predEmphLabels1 = labels[1].get(2 * k);
				List<String> predTextLabels1 = labels[1].get(2 * k + 1);

				if (predEmphLabels0.size() > predTextLabels0.size()) {
					if (areSame(predEmphLabels0, predEmphLabels1))
						map.put(labelledCommentsTest.get(k), "NotInformative");
					else
						map.put(labelledCommentsTest.get(k), "Informative");
				} else {
					if (areSame(predTextLabels0, predTextLabels1))
						map.put(labelledCommentsTest.get(k), "NotInformative");
					else
						map.put(labelledCommentsTest.get(k), "Informative");
				}
			}

			// classify the un-labelled comments as informative or
			// non-informative using the union of two features sets
			List<CommentBlock> unlabelledInformative = classifyIMF(map,
					unlabelledComments);

			List<List<Pair<Pair<List<String>, List<String>>, Integer>>> confidentExamplesByModel = new ArrayList<List<Pair<Pair<List<String>, List<String>>, Integer>>>();

			// figure out the un-labelled informative examples about which we
			// are confident
			for (int k = 0; k < 2; k++)
				confidentExamplesByModel.add(confidentClassifier(k,
						unlabelledInformative));

			// store the index, emph and text labels of the informative examples
			// about which we are confident
			HashMap<Integer, Pair<List<String>, List<String>>> confidentMap = new HashMap<Integer, Pair<List<String>, List<String>>>();
			for (List<Pair<Pair<List<String>, List<String>>, Integer>> modelExamples : confidentExamplesByModel)
				for (Pair<Pair<List<String>, List<String>>, Integer> example : modelExamples)
					confidentMap.put(example.b, example.a);

			// remove the comments we are confident about from the unlabelled,
			// and store them in the labelled list
			for (int key : confidentMap.keySet()) {
				CommentBlock c = unlabelledComments.remove(key);
				c.setLabelEmph(confidentMap.get(key).a);
				c.setLabelText(confidentMap.get(key).b);
				labelledComments.add(c);
			}
		}

	}

	private static List<Pair<Pair<List<String>, List<String>>, Integer>> confidentClassifier(
			int i, List<CommentBlock> unlabelledInformative) {
		dumpCommentsToFileMalletStyle(unlabelledInformative, Parameters.ACT_DIR
				+ "/temp.txt");

		executeCommand("java -cp '/Users/bansal/Desktop/current/NLP_PROJECT/DBUGR/libs/mallet-2.0.7/class:/Users/bansal/Desktop/current/NLP_PROJECT/DBUGR/libs/mallet-2.0.7/lib/mallet-deps.jar' cc.mallet.fst.SimpleTagger --threads 2 --model-file model-"
				+ i
				+ ".crf "
				+ Parameters.ACT_DIR
				+ "/temp.txt >>"
				+ Parameters.ACT_DIR
				+ "/temp-labelled-classified-"
				+ i
				+ ".txt");

		List<Pair<Pair<List<String>, List<String>>, Integer>> result = new ArrayList<Pair<Pair<List<String>, List<String>>, Integer>>();
		try {
			FileInputStream fstream = new FileInputStream(Parameters.ACT_DIR
					+ "/temp-labeled-classified-" + i + ".txt");
			Scanner s = new Scanner(fstream);
			HashMap<Double, Pair<Pair<List<String>, List<String>>, Integer>> map = new HashMap<Double, Pair<Pair<List<String>, List<String>>, Integer>>();
			int index = 0;
			for (CommentBlock c : unlabelledInformative) {
				List<String> emphLabels = new ArrayList<String>();
				// do emph
				for (int i1 = 0; i1 < c.getTokensEmph().size(); i1++) {
					String label = s.nextLine();
					emphLabels.add(label);
				}

				double score = Double.parseDouble(s.nextLine());// score
				s.nextLine();// empty line
				List<String> textLabels = new ArrayList<String>();
				// do text
				for (int i1 = 0; i1 < c.getTokensEmph().size(); i1++) {
					String label = s.nextLine();
					textLabels.add(label);
				}
				Pair<Pair<List<String>, List<String>>, Integer> example = null;
				Pair<List<String>, List<String>> labels = new Pair(
						new ArrayList<String>(emphLabels),
						new ArrayList<String>(textLabels));
				example = new Pair(labels, index++);
				map.put(score, example);
			}

			List<Double> keys = new ArrayList<Double>(map.keySet());
			Collections.sort(keys);
			for (int k = 0; k < pos; k++) {
				result.add(map.get(keys.get(k)));
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return result;
	}

	private static List<CommentBlock> classifyIMF(
			HashMap<CommentBlock, String> map,
			List<CommentBlock> unlabelledComments) {
		// TODO Auto-generated method stub
		return null;
	}

	private static boolean areSame(List<String> predEmphLabels0,
			List<String> predEmphLabels1) {
		for (int i = 0; i < predEmphLabels0.size(); i++) {
			if (!predEmphLabels0.get(i).equals(predEmphLabels1.get(i)))
				return false;
		}
		return true;
	}

	private static List<List<String>> trainClassifierAndLabel(int i,
			List<CommentBlock> labelledCommentsTrain,
			List<CommentBlock> labelledCommentsTest) {
		dumpCommentsToFileMalletStyleWithLabels(labelledCommentsTrain,
				Parameters.ACT_DIR + "/temp-labelled.txt");

		// creating the model
		executeCommand("java -cp '/Users/bansal/Desktop/current/NLP_PROJECT/DBUGR/libs/mallet-2.0.7/class:/Users/bansal/Desktop/current/NLP_PROJECT/DBUGR/libs/mallet-2.0.7/lib/mallet-deps.jar' cc.mallet.fst.SimpleTagger --train true --threads 2 --model-file model-"
				+ i + ".crf " + Parameters.ACT_DIR + "/temp-labelled.txt");

		dumpCommentsToFileMalletStyle(labelledCommentsTest, Parameters.ACT_DIR
				+ "/temp.txt");
		// testing on the model
		executeCommand("java -cp '/Users/bansal/Desktop/current/NLP_PROJECT/DBUGR/libs/mallet-2.0.7/class:/Users/bansal/Desktop/current/NLP_PROJECT/DBUGR/libs/mallet-2.0.7/lib/mallet-deps.jar' cc.mallet.fst.SimpleTagger --threads 2 --model-file model-"
				+ i
				+ ".crf "
				+ Parameters.ACT_DIR
				+ "/temp.txt >>"
				+ Parameters.ACT_DIR + "/temp-labeled-classified-" + i + ".txt");

		List<List<String>> result = new ArrayList<List<String>>();
		try {
			FileInputStream fstream = new FileInputStream(Parameters.ACT_DIR
					+ "/temp-labeled-classified-" + i + ".txt");
			Scanner s = new Scanner(fstream);
			for (CommentBlock c : labelledCommentsTest) {
				List<String> labels = new ArrayList<String>();
				// do emph
				for (int i1 = 0; i1 < c.getTokensEmph().size(); i1++) {
					String label = s.nextLine();
					labels.add(label);
				}
				result.add(labels);
				s.nextLine();// score
				s.nextLine();// empty line
				labels = new ArrayList<String>();
				// do text
				for (int i1 = 0; i1 < c.getTokensEmph().size(); i1++) {
					String label = s.nextLine();
					labels.add(label);
				}
				result.add(labels);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return result;
	}

	private static void dumpCommentsToFileMalletStyleWithLabels(
			List<CommentBlock> labelledComments, String string) {
		// TODO Auto-generated method stub

	}

	private static void dumpCommentsToFileMalletStyle(
			List<CommentBlock> labelledComments, String fileName) {
		// TODO Auto-generated method stub
		try {
			FileOutputStream ostream = new FileOutputStream(fileName);
			PrintStream p = new PrintStream(ostream);
			for (CommentBlock c : labelledComments) {
				for (String s : c.getTokensEmph()) {
					p.println();
				}
			}

			p.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}

	public static void main(String[] args) {
		test();
	}

}

class Pair<A, B> {
	A a;
	B b;

	public Pair(A a1, B b1) {
		a = a1;
		b = b1;
	}
}
