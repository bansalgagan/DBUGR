package DataStructures;

import java.util.ArrayList;
import java.util.List;

public class CommentBlock {
	
	private String emph;
	private String text;
	private int rating;
	private List<String> tokensEmph;
	private List<String> tokensText;
	private String timestamp;
	
	public CommentBlock() {
		emph = "";
		text = "";
		tokensEmph = new ArrayList<String>();
		tokensText = new ArrayList<String>();
		setRating(-1);
		setTimestamp("");
	}
	
	public String getEmph(){
		if(emph == null){
			System.out.println("Error emph");
			System.exit(1);
		}
		return emph;
	}
	public String getText(){
		if(emph == null){
			System.out.println("Error text");
			System.exit(1);
		}
		return text;
	}
	
	public int getRating(){
		return rating;
	}
	public List<String> getTokensEmph(){
		if(emph == null){
			System.out.println("Error emph tkn");
			System.exit(1);
		}
		return tokensEmph;
	}
	public List<String> getTokensText(){
		if(emph == null){
			System.out.println("Error text tkn");
			System.exit(1);
		}
		return tokensText;
	}
	
	public String getTimeStamp(){
		return getTimestamp();
	}
	
	public void setEmph(String s){
		emph = s;
	}
	
	public void setText(String s){
		text = s;
	}
	
	public void setTokensEmph(List<String> tkns){
		tokensEmph = tkns;
	}
	
	public void setTokensText(List<String> tkns){
		tokensText = tkns;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}	
}