package NLPPipeline;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

class CommentBlock {
	
	String text;
	int rating;
	List<String> tokens;
	String timestamp;
	
	public String getText(){
		return text;
	}
	
	public int getRating(){
		return rating;
	}
	
	public List<String> getTokens(){
		return tokens;
	}
	
	public String getTimeStamp(){
		return timestamp;
	}
	
}

public class Comments {

	public static List<CommentBlock> comBlocks =new ArrayList<CommentBlock>();
	
	public static void main(String[] args) throws Exception {				
		String sampleCommentFile="data/raw-comments/Keys Jumper Adventure-raw-comments.txt";		
		parseCommentFile(sampleCommentFile);
	}

	private static void parseCommentFile(String sampleCommentFile) throws Exception {
		
		BufferedReader br = new BufferedReader(new FileReader(sampleCommentFile));
		String line; 
		
		List<String> comment = new ArrayList<String>();
		while ((line=br.readLine())!=null)
		{
			if (!line.isEmpty() && !line.substring(0,1).equals("e"))
			{
				System.out.println(line);
				comment.add(line);
				if (line.equals("}"))
				{
					CommentBlock cblock=generateCommentBlock(comment);
					comBlocks.add(cblock);
					comment.clear();
				}
			}
		}
		
		br.close();
	}

	private static CommentBlock generateCommentBlock(List<String> comment) {
		
		String line;
		CommentBlock cblock=new CommentBlock();
		for (int i = 1; i<comment.size()-1;i++)
		{
			line = comment.get(i);
			String[] words= line.split(" ");
			if (words[2].equals("text:"))
				{
					cblock.text=line.substring(9, line.length()-1).replaceAll("\\\\",""); 
					cblock.tokens=TwokenizerWrapper.tokenize(cblock.text);
				}
			else if (words[2].equals("rating:"))
				cblock.rating=Integer.parseInt(line.substring(10,11));
			else if (words[2].equals("creationTime:"))
				cblock.timestamp=line.substring(16);		
		}
		return cblock;
	}
	
	

}
