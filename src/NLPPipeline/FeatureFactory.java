package NLPPipeline;

public class FeatureFactory {
	
	//uses Ritter's POS tagger for noisy text
	public static final boolean POS = false;
	//Uses sentiLexicon to classify positive or negative
	public static final boolean LEXICON = false;
	//If is capital
	public static final boolean CASE = false;
	//if contained in vulgar dictionary
	public static final boolean VULGAR = false;
	//if is a technical term
	public static final boolean TECHI = false;
	//if is a phone brand
	public static final boolean PHONE = false;
	//sentiment
	public static final boolean SENTIMENT = false;
	
	public static void test(){
		
	}
	
	public static void main(String[] args) {
		test();

	}

}
