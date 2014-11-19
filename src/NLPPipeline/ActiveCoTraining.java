package NLPPipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DataStructures.CommentBlock;
import DataStructures.Comments;
import Utilities.Parameters;

public class ActiveCoTraining {
	public static final int N = 1;
	public static final int n = 10;
	public static final int p = 10;

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

		for (int i = 0; i < N; i++) {
			@SuppressWarnings("unchecked")
			List<List<String>>[] labels = new List[2];
			
			//Train two classifiers on two orthogonal feature sets
			for (int j = 0; j < 2; j++)
				labels[j] = trainClassifierAndLabel(i, labelledComments);
			
			//Figure out the informative and non-informative examples
			HashMap<CommentBlock, String> map = new HashMap<CommentBlock, String>();
			for (int k = 0; k < labelledComments.size(); k++) {
				List<String> predEmphLabels0 = labels[0].get(2 * k);
				List<String> predTextLabels0 = labels[0].get(2 * k + 1);
				List<String> predEmphLabels1 = labels[0].get(2 * k);
				List<String> predTextLabels1 = labels[0].get(2 * k + 1);

				if (predEmphLabels0.size() > predTextLabels0.size()) {
					if (areSame(predEmphLabels0, predEmphLabels1))
						map.put(labelledComments.get(k), "NotInformative");
					else
						map.put(labelledComments.get(k), "Informative");
				} else {
					if (areSame(predTextLabels0, predTextLabels1))
						map.put(labelledComments.get(k), "NotInformative");
					else
						map.put(labelledComments.get(k), "Informative");
				}
			}
			
			//classify the un-labelled comments as informative or non-informative using the union of two features sets
			List<CommentBlock> unlabelledInformative = classifyIMF(map,
					unlabelledComments);

			List<List<Pair<Pair<List<String>, List<String>>, Integer>>> confidentExamplesByModel = new ArrayList<List<Pair<Pair<List<String>, List<String>>, Integer>>>();
			
			//figure out the un-labelled informative examples about which we are confident
			for (int k = 0; k < 2; k++)
				confidentExamplesByModel.add(confidentClassifier(k,
						unlabelledInformative));
			
			//store the index, emph and text labels of the informative examples about which we are confident
			HashMap<Integer, Pair<List<String>, List<String>>> confidentMap = new HashMap<Integer, Pair<List<String>, List<String>>>();
			for(List<Pair<Pair<List<String>, List<String>>, Integer>> modelExamples: confidentExamplesByModel)
				for(Pair<Pair<List<String>, List<String>>, Integer> example: modelExamples)
					confidentMap.put(example.b, example.a);
			
			//remove the comments we are confident about from the unlabelled, and store them in the labelled list
			for(int key: confidentMap.keySet()){
				CommentBlock c = unlabelledComments.remove(key);
				c.setLabelEmph(confidentMap.get(key).a);
				c.setLabelText(confidentMap.get(key).b);
				labelledComments.add(c);
			}
		}

	}

	private static List<Pair<Pair<List<String>, List<String>>, Integer>> confidentClassifier(int k,
			List<CommentBlock> unlabelledInformative) {
		// TODO Auto-generated method stub
		return null;
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
			List<CommentBlock> labelledComments) {
				return null;
		// TODO Auto-generated method stub

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
