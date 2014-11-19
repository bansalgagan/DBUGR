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
	private List<String> posEmph;
	private List<String> posText;
	private List<String> labelEmph;
	private List<String> labelText;
	
	public CommentBlock() {
		emph = "";
		text = "";
		tokensEmph = new ArrayList<String>();
		tokensText = new ArrayList<String>();
		setRating(-1);
		setTimestamp("");
		setPosEmph(new ArrayList<String>());
		setPosText(new ArrayList<String>());
		setLabelEmph(new ArrayList<String>());
		setLabelText(new ArrayList<String>());
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

	public List<String> getPosEmph() {
		return posEmph;
	}

	public void setPosEmph(List<String> posEmph) {
		this.posEmph = posEmph;
	}

	public List<String> getPosText() {
		return posText;
	}

	public void setPosText(List<String> posText) {
		this.posText = posText;
	}

	public List<String> getLabelEmph() {
		return labelEmph;
	}

	public void setLabelEmph(List<String> labelEmph) {
		this.labelEmph = labelEmph;
	}

	public List<String> getLabelText() {
		return labelText;
	}

	public void setLabelText(List<String> labelText) {
		this.labelText = labelText;
	}
	
	public boolean isLabelled(){
		if(this.getLabelEmph().size() < this.getTokensEmph().size())
			return false;
		else
			return true;
	}
}