package NLPPipeline;

/**
 * Class for generating features for NER
 * @author bansal
 *
 */
public class FeatureFactory {
	
	//uses Ritter's POS tagger for noisy text
	static final boolean POS = false;
	//Uses sentiLexicon to classify positive or negative
	static final boolean LEXICON = false;
	//If is capital
	static final boolean CASE = false;
	//if contained in vulgar dictionary
	static final boolean VULGAR = false;
	//if is a technical term
	static final boolean TECHI = false;
	//if is a phone brand
	static final boolean PHONE = false;
	//sentiment
	static final boolean SENTIMENT = false;
	
	static void test(){
		
	}
	
	
	
	public static void main(String[] args) {
		test();

	}

}
