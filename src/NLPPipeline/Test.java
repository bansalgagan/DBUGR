package NLPPipeline;

import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Scanner;
import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.CommentsRequest;
import com.gc.android.market.api.model.Market.CommentsResponse;
import com.gc.android.market.api.model.Market.ResponseContext;

public class Test{

	public static void main(String[] args){
		//appTest(args[0]);
		scrapComments();
		//commentTest(args[0], args[1]);
	}
	
	public static void scrapComments(){
		try{
		FileInputStream fstream = new FileInputStream("data/new-apps.txt");
		Scanner s = new Scanner(fstream);
		while(s.hasNext()){
			String line = s.nextLine();
			System.out.println(line);
			String[] arr = line.split("\t");
			System.out.println("Doing: "+ arr[0]+"\t"+arr[1]);
			commentTest(arr[0], arr[1]);
			//break;
		}
	}
	catch(Exception e){
		System.out.println(e);
	}
	}
	
	
		
	public static void commentTest(final  String appName,String appId){
		MarketSession session = new MarketSession();
		session.login("rsqwerty75@gmail.com", "applerocks");
		
		for( int i= 0; i<100; i++){
			try{
			System.out.println("Doing: "+ appName+"\t"+appId+ "\t"+i);
		// System.out.println("Logged in");
		CommentsRequest commentsRequest = CommentsRequest.newBuilder()
		                                .setAppId(appId)
		                                .setStartIndex(i*10)
		                                .setEntriesCount(10)
		                                .build();
			
		try {
		    Thread.sleep(1000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
          
		 session.append(commentsRequest, new Callback<CommentsResponse>() {
		  @Override
		    public void onResult(ResponseContext context, CommentsResponse response){
		        //System.out.println("Response : " + response);
				dumpToFile(appName, response);
		    }
		});
		session.flush();
		}
			catch(Exception e){
				i--;
			}
		}
		
		
	}
	
	public static void dumpToFile(final String appName, CommentsResponse comments){
		try{
		FileOutputStream fstream = new FileOutputStream(new File("raw-comments-new/"+appName+"-raw-comments.txt")	, true);
		PrintStream p = new PrintStream(fstream);
		p.println(comments);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void appTest(String appName){
		MarketSession session = new MarketSession();
		session.login("rsqwerty75@gmail.com", "applerocks");
		
		// System.out.println("Logged in");
		//session.getContext.setAndroidId("379DED138BC024CC");

		String query = appName;
		AppsRequest appsRequest = AppsRequest.newBuilder()
		                                .setQuery(query)
		                                .setStartIndex(0).setEntriesCount(10)
		                                .setWithExtendedInfo(true)
		                                .build();
       	// System.out.println("Requested");              
		session.append(appsRequest, new Callback<AppsResponse>() {
		         @Override
		         public void onResult(ResponseContext context, AppsResponse response) {
					 for(int i=0; i<10; i++){
						 App app = response.getApp(i);
						 System.out.println(app.getTitle() + "\t" + app.getId() );
					 }
					 	
		                  // Your code here
		                  // response.getApp(0).getCreator() ...
		                  // see AppsResponse class definition for more infos
		         }
		});
		// System.out.println("Append");
		session.flush();
		// System.out.println("Flush");
	}
}