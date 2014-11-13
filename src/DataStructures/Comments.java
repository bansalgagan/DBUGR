package DataStructures;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

import NLPPipeline.TwokenizerWrapper;
import Utilities.Parameters;

public class Comments {

	public List<CommentBlock> comBlocks =new ArrayList<CommentBlock>();
	public HashSet<String> timestampList= new HashSet<String>();
	
	public List<CommentBlock> getCommentList(){
		return comBlocks;
	}
	
	public int getUniqueCount(){
		return timestampList.size();
	}
	
	public Comments(String filename) throws Exception{
		parseCommentFile(filename);
	}
	
	public static void main(String[] args) throws Exception {				
		String sampleCommentFile="data/raw-comments/Air Call-Accept free (Necta)-raw-comments.txt";
		Comments testComments = new Comments(sampleCommentFile);
	}

	private void parseCommentFile(String sampleCommentFile) throws Exception {
		
		BufferedReader br = new BufferedReader(new FileReader(sampleCommentFile));
		String line; 
		
		List<String> comment = new ArrayList<String>();
		while ((line=br.readLine())!=null)
		{
			if (!line.isEmpty() && !line.substring(0,1).equals("e"))
			{
				//System.out.println(line);
				comment.add(line);
				if (line.equals("}"))
				{
					CommentBlock cblock=generateCommentBlock(comment);
					if (cblock!=null)
						comBlocks.add(cblock);
					comment.clear();
				}
			}
		}
		
		br.close();
	}

	private CommentBlock generateCommentBlock(List<String> comment) {
		
		String line;
		CommentBlock cblock=new CommentBlock();
		for (int i = 1; i<comment.size()-1;i++)
		{
			line = comment.get(i);
			String[] words= line.split(" ");
			if (words[2].equals("text:"))
				{
					String validText = line.substring(9, line.length()-1);
					int tIndex=validText.indexOf("\\t");
					if (tIndex==-1)
						{
							cblock.emph="";
							cblock.text=validText.replaceAll("\\\\[0-9][0-9][0-9]","").replaceAll("\\\\","");
						}
					else
						{
							cblock.emph=validText.substring(0,tIndex).replaceAll("\\\\[0-9][0-9][0-9]","").replaceAll("\\\\","");
							cblock.text=validText.substring(tIndex+2).replaceAll("\\\\[0-9][0-9][0-9]","").replaceAll("\\\\","");
						}
					//System.out.println(cblock.emph);
					//System.out.println(cblock.text);
					
					cblock.tokensEmph=TwokenizerWrapper.tokenize(cblock.emph);
					cblock.tokensText=TwokenizerWrapper.tokenize(cblock.text);
					if (cblock.emph.trim().length() <= Parameters.MINLEN )
						cblock.emph = null;
					if (cblock.text.trim().length() <= Parameters.MINLEN ) {
						cblock.text = null;
					}
					if (cblock.text == null && cblock.emph == null){
						return null;
					}
						
				}
			else if (words[2].equals("rating:"))
				cblock.rating=Integer.parseInt(line.substring(10,11));
			else if (words[2].equals("creationTime:"))
				{
					cblock.timestamp=line.substring(16);
					if (timestampList.contains(cblock.timestamp))
					{
						cblock=null;
						break;
					}
					else
						timestampList.add(cblock.timestamp);
				}
			
		}
		if (cblock!=null)
			{
			//System.out.println(cblock.emph);
			//	System.out.println(cblock.text);
				//System.out.println(cblock.timestamp);
				//System.out.println();
			}
		return cblock;
	}
	
	

}