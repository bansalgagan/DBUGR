package DataStructures;

import java.util.List;

public class CommentBlock {
	
	String emph;
	String text;
	int rating;
	List<String> tokensEmph;
	List<String> tokensText;
	String timestamp;
	
	public String getEmph(){
		return emph;
	}
	public String getText(){
		return text;
	}
	
	public int getRating(){
		return rating;
	}
	public List<String> getTokensEmph(){
		return tokensEmph;
	}
	public List<String> getTokensText(){
		return tokensText;
	}
	
	public String getTimeStamp(){
		return timestamp;
	}
	
}