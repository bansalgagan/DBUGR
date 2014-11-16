package Utilities;

import DataStructures.Comments;

public class APPTester {
	public static void main(String[] args){
		for(String s: Parameters.APPS)
			System.out.println(s + "\t" + (new Comments(Parameters.RAW_COMMENT_DIR + "/" + s + "-raw-comments.txt")).getCommentList().size());
		
	}
}
