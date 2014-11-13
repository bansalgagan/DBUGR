package Utilities;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import DataStructures.CommentBlock;
import DataStructures.Comments;

public class DataLabeller {

	public static void label(String appName) throws Exception{
		String rawFile = Parameters.RAW_COMMENT_DIR + "/" + appName + "-raw-comments.txt";
		Comments comments = new Comments(rawFile);
		
		String labelFile = Parameters.LABEL_DIR + "/" + appName + "-label.txt";
		FileOutputStream ostream = new FileOutputStream(labelFile);
		PrintStream p = new PrintStream(ostream);
		String labelInstruction  = "BUG -> B, DEVICE -> D, FEATURE -> F, APP -> A";
		Scanner userScanner = new Scanner(System.in);
		for(CommentBlock cb: comments.getCommentList()){
			System.out.println(labelInstruction);
			
			if(cb.getEmph() != null){
				System.out.println("Comment-> " + cb.getEmph());
				for(String tkn: cb.getTokensEmph()){
					System.out.println("Token: " + tkn);
					String label = userScanner.nextLine();
					while(!(label.equals("B") || label.equals("D") || label.equals("F") || label.equals("A"))){
						System.err.println("Invalid tag" + label);
						label = userScanner.nextLine();
					}
					p.print(tkn + "/"+ label + " ");
				}
				p.println();
			}
			if(cb.getText() != null){
				System.out.println("Comment-> " + cb.getText());
				for(String tkn: cb.getTokensText()){
					System.out.println("Token: " + tkn);
					String label = userScanner.nextLine();
					while(!(label.equals("B") || label.equals("D") || label.equals("F") || label.equals("A"))){
						System.err.println("Invalid tag " + label);
						label = userScanner.nextLine();
					}
					p.print(tkn + "/"+ label + " ");
				}
				p.println();
			}
		}
		p.close();
		userScanner.close();
	}
	
	public static void main(String[] args) throws Exception {
		for(String app: Parameters.APPS){
			label(app);
			break;
		}
	}

}
