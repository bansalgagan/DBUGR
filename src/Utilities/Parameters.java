package Utilities;

public class Parameters {

	/**
	 * data
	 */
	public final static String HPATH = "/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/";
	public final static String HEAD_PATH = "";
	public final static String RAW_COMMENT_DIR = HEAD_PATH + "data/raw-comments";
	public static final String LABEL_DIR = HEAD_PATH + "data/labels";
	public static final String BROWN_DIR = HEAD_PATH + "data/brown";
	public final static String MALLET_DIR = HEAD_PATH + "data/mallet";
	public final static String POS_DIR = HPATH + "data/POS";
	public final static String ACT_DIR = HPATH + "data/act";

	/**
	 * libs
	 */
	public final static String TWITTER_NLP = HEAD_PATH + "libs/twitter_nlp";
	public final static String LIBS = HPATH + "libs";

	public final static String[] APPS = {
		"ASTRO File Manager with Cloud",
		"OvuView: Ovulation & Fertility", "Parking King",
			"Air Call-Accept free (Necta)", "AntiVirus Security - FREE",
			"Audible for Android", "Battle.net Authenticator",
			"Best Apps Market - for Android", "Burger", "Camera illusion",
			"Camera magic", "Cardiograph", "ColorNote Notepad Notes",
			"Craigs Race", "DiDi - Free Calls & Texts", "Face Mood Scanner",
			"Funny Call", "Funny Camera", "Glympse - Share GPS location",
			"HeyTell", "Hootsuite (Social Media Mgmt)", "Job Search",
			"LED Scroller 3 - FREE", "Little Girl Magic",
			"Mobile Hidden Camera", "My Days - Period & Ovulation â¢",
			"My Tracks", "Noom CardioTrainer", "Note Everything",
			"Opera Mini â Fast web browser",
			"Parking Manijak", "Race 2 Free", "Raging Thunder 2 - FREE",
			"Rebtel: Cheap and Free Calls", "SMS Popup", "SoundHound",
			"Text-To-Speech Extended", "TiKL Touch Talk Walkie Talkie",
			"Unblock Parking Jam - Free", "WikiMobile (Wikipedia Browser)",
			"Zello PTT Walkie-Talkie", 
			 };
	// public final static String[] APPS = { "ASTRO File Manager with Cloud",
	// "Air Call-Accept free (Necta)", "Antarctic Adventure Free",
	// "Call Dispatcher", "Call-Timer", "Calming Music to Tranquilize",
	// "Cardiograph", "DiDi - Free Calls & Texts", "Easy Browser",
	// "Fast Food Calorie Counter", "File Browser", "Finger Dance Pro",
	// "Floating Browser", "Fun Cinemas", "Fun Math Tricks Lite",
	// "Funny Call", "Keys Jumper Adventure",
	// "Music Therapy for Refreshment", "My Tracks", "Noom CardioTrainer",
	// "OvuView: Ovulation & Fertility", "Phone2Phone Internet Calling",
	// "Rebtel: Cheap and Free Calls", "Santa Fun Jokes",
	// "Street Dancer Deluxe Free", "Stretch Exercises",
	// "WikiMobile (Wikipedia Browser)" };

	/**
	 * DataStructures/Comments.java
	 */
	// min number of tokens that should be there
	public static final int MINTOKENS = 3;
	// minimum length of emph or text in a comment
	public final static int MINLEN = 2;

}
