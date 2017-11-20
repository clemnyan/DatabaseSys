import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



public class Blog {
	
	public static int entryID =0;
	
	public static void main(String [] args) throws IOException {
		try {
				MongoClientURI uri = new MongoClientURI(
						"mongodb://Team14:9XJMVkQCGCzxKLjH@cluster0-shard-00-00-ppp7l.mongodb.net:27017,cluster0-shard-00-01-ppp7l.mongodb.net:27017,cluster0-shard-00-02-ppp7l.mongodb.net:27017/Team14DB?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin");

	        		MongoClient mongo = new MongoClient(uri);
	        		MongoDatabase db = mongo.getDatabase("Team14DB");
				
	            MongoCollection<Document> postCollection = db.getCollection("post");
	            MongoCollection<Document> commentCollection = db.getCollection("comment");
	            
	            // empty the database to start over
	            postCollection.drop();
	            commentCollection.drop();
	            
	            // create collections
	            postCollection = db.getCollection("post");
	            commentCollection = db.getCollection("comment");
	            
	            System.out.println("Connection Successful");
	            startQueries(postCollection, commentCollection);
	            
		} catch (MongoException e){
			System.out.println("Error Connecting");
		}

	}
		

	   /** Method accepts user input and starts the queries that the user wants.
	    * Some decent error checking is implemented      
	    * @param postCollection
	    * @param commentCollection
	    */
	    public static void startQueries (MongoCollection<Document> postCollection, MongoCollection<Document> commentCollection){   
                promptCommand();
	    	
	    	    // create reader from stdin
	            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	            String s;
	            
	            // read from stdin until ctr-z
	            System.out.println("Enter a command");
           	    try {
	            while ((s = in.readLine()) != null && s.length() != 0) {
	             String[] words = s.split("\\s+");
	              if (words[0].equals("post")) {
	            	 ArrayList<String>  store = parsePost(s);
 	            	  	if (store.size()== 3) {
 	            	  		String one = store.get(0);
 	            	  		String two = store.get(1);
 	            	  		String three = store.get(2);
 	            	  		words=one.split("\\s+");
	            	  		postBlog(words[1], words[2], words[3],two,three, postCollection);
	            	  	} else {
		            	  	System.out.println("command not inputted properly");
	            	  	}
	            	  	System.out.println("enter another command");

	              } else if (words[0].equals("comment")) {
		            	 ArrayList<String>  store = parsePost(s);
	            	  		if (store.size() == 3) {
	 	            	  		String one = store.get(0);
	 	            	  		String two = store.get(1);
	 	            	  		words=one.split("\\s+");
	            	  			commentBlog(words[1], Integer.parseInt(words[2]), words[3], two, commentCollection, postCollection);
	            	  		} else {
			            	  	System.out.println("command not inputted properly");
		            	  	}
		            	  	System.out.println("enter another command");

	              } else if (words[0].equals("delete")) {
	          	  		if (words.length == 4) {
	          	  			deleteEntry(words[1], Integer.parseInt(words[2]), words[3], commentCollection, postCollection);
	          	  		} else {
	          	  			System.out.println("command not inputted properly");
	          	  		}
	          	  		System.out.println("enter another command");
	              } else if (words[0].equals("show")) {
	            	  		  if (words.length == 2) {
	            	  			showBlogs (words[1], postCollection, commentCollection);
	            	  		  }
	  	            	  	System.out.println("enter another command");
	              } else {
	            	  	System.out.println("that is not a proper command");
	            	  	promptCommand();
	              }
	            }	
           	 } catch(Exception ec){
        		 System.out.println ("Input Error");
        		 startQueries(postCollection, commentCollection);
        	 }
	    }

	
	/** Parses user input to correctly insert the right parameters to 
	 * to qeury methods
	 * @param s
	 * @return
	 */
	public static ArrayList<String> parsePost (String s) {
	
	ArrayList<String> store = new ArrayList<>();

		int count=0;
		int tick =0;
		while (count==0){
			tick = tick+1;
			if (s.charAt(tick)=='"'){
				count=1;
				String m=s.substring(0,tick);
				store.add(m);
				break;
			}
		}
	
	for (int i=0; i<s.length()-1; i++){
		if (s.charAt(i)=='"'){
			int counter = 0;
			int end=i-1;
			while (counter==0){
				end=end+1;
				if (s.charAt(end+1)=='"'){
					counter=1;
					String m=s.substring(i+1,end+1);
					store.add(m);
				}
			}
		}
	}
	if (store.size()==4){
		store.remove(2);
	}
	
	if (store.size()==2){
		store.add("");
	}
	return store;
	}
	
	
	
	public static void promptCommand() {
        System.out.println("");

        System.out.println("Enter one of the following commands in EXACT FORMAT:");
        System.out.println("		post blogName userName title \"postBody\" \"tags\"");
        System.out.println("		comment blogname entryID userName \"commentBody\"");
        System.out.println("		delete blogname entryID userName");
        System.out.println("		show blogName");
        
        System.out.println("");
	}
	
	public static void postBlog (String blogName, String username, String title, String postBody, String tags, MongoCollection<Document> postCollection) {
		
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 LocalDateTime now = LocalDateTime.now();
		 entryID =entryID+1;
		 
		 Document document = new Document();
		 
		 document.put("_id", entryID);
		 document.put("blogName", blogName);
		 document.put("userName", username);
		 document.put("title", title);
		 document.put("body",postBody );
		 document.put("date", dtf.format(now));
		 document.put("tags", tags);
		 
		 postCollection.insertOne(document);

	}
	
	public static void commentBlog (String blogName, int id, String username, String commentBody, MongoCollection<Document> commentCollection, MongoCollection<Document> postCollection) {
		
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 LocalDateTime now = LocalDateTime.now();
		 entryID =entryID+1;
		 int order = 1;
		 
		 if (isCommentEntryPresent(id, blogName, postCollection)){
			System.out.println("entry is present");
			order = 1;
		 } else if (isCommentEntryPresent(id, blogName, commentCollection)){
			System.out.println("entry is present");
			order = getCommentOrder(id, blogName,commentCollection) + 1;
		 } else {
	        System.err.println("Referenced Entry is not present for comment");
	        startQueries(postCollection, commentCollection);
	        return;
		 }

		 Document document = new Document();
		 document.put("_id", entryID);
		 document.put("ref", id);
		 document.put("blogName", blogName);
		 document.put("userName", username);
		 document.put("body",commentBody );
		 document.put("commentOrder",order );
		 document.put("date", dtf.format(now));
		 commentCollection.insertOne(document);
	}
	
	public static void deleteEntry (String blogName, int id, String username, MongoCollection<Document> commentCollection, MongoCollection<Document> postCollection) {
		MongoCollection<Document> collection = postCollection; 
		if (isEntryPresent(id, blogName,username, postCollection)){
			System.out.println("entry is present for delete");
			collection = postCollection;
		 } else if (isCommentEntryPresent(id, blogName, commentCollection)){
			System.out.println("entry is present for delete");
			collection = commentCollection;
		 } else {
	        System.err.println("Referenced Entry is not present for delete");
	        return;
		 }
		 
		BasicDBObject selectBlog = new BasicDBObject();
        selectBlog.put("_id", id);
        Document current = collection.find(selectBlog).first();
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		 
		Document replacement = new Document();
		 
		replacement.put("_id", id);
		replacement.put("date", dtf.format(now));
		replacement.put("blogName", blogName);
		replacement.put("userName", username);
		replacement.put("title", "Deleted");
		replacement.put("tags", "Deleted");		
		replacement.put("body","deleted by " + username + " on " + dtf.format(now));
		 
        collection.replaceOne(current, replacement);
	}
	
	/** Checks if a comment is present
	 * 
	 * @param entryIntID
	 * @param blogName
	 * @param collection
	 * @return
	 */
	public static boolean isCommentEntryPresent (int entryIntID, String blogName,MongoCollection<Document> collection) {
        BasicDBObject selectBlog = new BasicDBObject();
        selectBlog.put("blogName", blogName); 
        selectBlog.put("_id", entryIntID); 
        return collection.find(selectBlog).first()!=null;
	}
	
	/**Helper method to check is blog is present
	 * 
	 * @param entryIntID
	 * @param blogName
	 * @param username
	 * @param collection
	 * @return
	 */
	public static boolean isEntryPresent (int entryIntID, String blogName,String username, MongoCollection<Document> collection) {
        BasicDBObject selectBlog = new BasicDBObject();
        selectBlog.put("blogName", blogName); 
        selectBlog.put("_id", entryIntID); 
        selectBlog.put("userName", username);
        return collection.find(selectBlog).first()!=null;
	}

	/**Helper method to find printing order
	 * 
	 * @param entryIntID
	 * @param blogName
	 * @param userName
	 * @param collection
	 * @return
	 */
	public static int getCommentOrder (int entryIntID, String blogName, MongoCollection<Document> collection) {
	    BasicDBObject selectBlog = new BasicDBObject();
	    selectBlog.put("blogName", blogName); 
	    selectBlog.put("_id", entryIntID); 
	    Document doc = collection.find(selectBlog).first();
	    return (int)doc.get("commentOrder");
	}
	
	
	/** Method to get a specified blog, it returns does not exist message if the blog is not found
	 */
	public static void showBlogs (String name, MongoCollection<Document> postCollection, MongoCollection<Document> commentCollection) {
	    
		if (!isBlogPresent(name, postCollection)){ //if blog doesnt exist
	        System.err.println("Referenced Blog is not present");
	        return;
		}
	
        BasicDBObject selectBlog = new BasicDBObject();
        selectBlog.put("blogName", name);    
        System.out.println(name);
    	System.out.println("");

        
        for (Document doc : postCollection.find(selectBlog)) {  //iterate through matching documents
            System.out.println("	" + doc.get("date") + "	");
            System.out.println("	(" + doc.get("_id") + ") " + doc.get("title"));
            System.out.println("	" + doc.get("userName"));
            System.out.println("	" + doc.get("body"));
            System.out.println("	" + doc.get("tags"));
        	System.out.println("");
            int entryID = (Integer) doc.get("_id");
            findComment("		", entryID, commentCollection);
            }
        }

/**Method to find Comment , and linked comments (comments of comments)
 * 
 * @param entryID
 * @param commentCollection
 */
	
public static void findComment (String gap, int entryID, MongoCollection<Document> commentCollection){
    BasicDBObject selectComment= new BasicDBObject();
    selectComment.put("ref", entryID);    
    for (Document doc: commentCollection.find(selectComment)){
    	System.out.println(gap+doc.get("date"));
    	System.out.println(gap+ "(" +doc.get("_id") + ")" + doc.get("userName"));
    	System.out.println(gap+doc.get("body"));
    	System.out.println("");
        int newEntryID = (int) doc.get("_id");
    	findComment(gap + "		", newEntryID, commentCollection);
    }
}
        

		
	/**Method to show if a blog exists or not,
	 * @param name
	 * @param collection
	 * @return true if the specified blog exists, and false if it doesnt
	 */
	public static boolean isBlogPresent (String name, MongoCollection<Document> collection) {
        BasicDBObject selectBlog = new BasicDBObject();
        selectBlog.put("blogName", name); 
		return collection.find(selectBlog).first()!=null;
	}
}
