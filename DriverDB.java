
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class DriverDB {
	public static final String SERVER = "jdbc:mysql://sunapee.cs.dartmouth.edu/";
	public static final String USERNAME="F001743";
	public static final String PASSWORD = "b9gw7vLw";
	public static final String DATABASE ="F001743_db";
	public static String QUERY = "SELECT * FROM Author";
	public static Statement stmt = null;
	public static  ResultSet res = null;	
	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static LocalDateTime now = LocalDateTime.now();
	
	
	/**Function for logging in, Welcomes the user and gives him options to login or register
	 * More like a home pages in a more visual app. If the user logs in, he is directed to the function
	 * chooseLogin Mode, if he registers , he is directed to the function choose register mode.
	 * 
	 * The function also handles error cases when the user inputs the wrong input
	 */
	public static void startOption () {
		Scanner sc = new Scanner (System.in);
		System.out.println("\nWELCOME TO THE MANUSCRIPT PUBLISHING DATABASE");
		System.out.println("\nWant to Login or register? (TYPE IN RESPONSE AND HIT ENTER)");

		String ans=sc.nextLine();  //input entered
		ans = ans.toLowerCase();  
		if (ans.equals("login")) {
			chooseLoginMode(); //methods directs to login mode
		}
		else if (ans.equals("register"))  {
			chooseRegisterMode();  // method directs to register mode
		}
		else {
			System.out.println("\nPlease check your entry spelling, and type in  a valid response\n");
			startOption();
		}
		sc.close();
	}
	
	
	/** In this method, the user chooses the mode that he wants to login with. If he is an editor, he
	 * logs in with his editor ID, if an author he logs in with his unique author id and if he is a
	 * reviewer he logs in with his reviewer id.
	 * 
	 * The function handles error cases when the user inputs the wrong input
	 */
	public static void chooseLoginMode () {
		Scanner sc = new Scanner (System.in);   //scanner for author response
		Scanner m = new Scanner (System.in);   //scanner for ID input
		System.out.println("\n------------------------------------------------------------------------------------------");
		System.out.println("\nLogin as one of these options: Editor, Reviewer, Author");
		System.out.println("\nType in you choice and hit enter: \n");
		System.out.println("------------------------------------------------------------------------------------------");

		String ans=sc.nextLine();  //input entered
		ans = ans.toLowerCase();  
		if (ans.equals("editor")) {
		
			System.out.println("Please enter your Editor ID here, (if not an Editor press 0)! ");   //SHOULD GIVE OPTION TO EXIT, IF NOT AUTHOR
			System.out.println("\n------------------------------------------------------------------------------------------");
			try {
            	int numInput = m.nextInt();
    			if (numInput==0) {
    				startOption();
    				sc.close();
    				m.close();
    				return;
    			}
    			else { 
    				EditorLoginMode(numInput); //Login as author
    				sc.close();
    				m.close();
    				return;
    			}
            }catch (Exception e){
            	System.out.println(e.getMessage()+" error:  Please enter a valid number");
            }
            startOption();
			
		}
		else if (ans.equals("reviewer"))  {
			System.out.println("Please enter your Reviewer ID here, (if not an Reviewer press 0)! ");   //SHOULD GIVE OPTION TO EXIT, IF NOT AUTHOR
			System.out.println("\n------------------------------------------------------------------------------------------");
			try {
            	int numInput = m.nextInt();
    			if (numInput==0) {
    				startOption();
    				sc.close();
    				m.close();
    				return;
    			}
    			else { 
    				ReviewerLoginMode(numInput); //Login as author
    				sc.close();
    				m.close();
    				return;
    			}
            }catch (Exception e){
            	System.out.println(e.getMessage()+" error:  Please enter a valid number");
            }
            startOption();
		}
		
		else if (ans.equals("author")) {
			System.out.println("Please enter your Author ID here, (if not an Author press 0)! ");   //SHOULD GIVE OPTION TO EXIT, IF NOT AUTHOR
			System.out.println("\n------------------------------------------------------------------------------------------");
			try {
            	int numInput = m.nextInt();
    			if (numInput==0) {
    				startOption();
    				sc.close();
    				m.close();
    				return;
    			}
    			else { 
    				AuthorloginMode(numInput); //Login as author
    				sc.close();
    				m.close();
    				return;
    			}
            }catch (Exception e){
            	System.out.println(e.getMessage()+" error:  Please enter a valid number");
            }
            startOption();
		}
		
		else {
			System.out.println("\nPlease check your entry spelling, and type in  a valid response");
			System.out.println("DO YOU STILL WANT TO LOGIN ?\n");
			ans=sc.nextLine();
            ans=ans.toLowerCase();
            if (ans.equals("yes")){
            	chooseLoginMode();
            	sc.close();
            	m.close();
            	return;
            }
            else {
            	System.out.println("Thank you, Exiting App..");
            	sc.close();
            	m.close();
            	return;
            }
		}
	}
	
	
	/**Helper method for the chooseLogin function. This functions allows the user to login as an editor after 
	 * providing his/her unique ID.
	 * It also handles error cases pretty well
	 * @param editorID
	 */
	public static void EditorLoginMode( int editorID)  {
			
			Scanner sc = new Scanner (System.in);
			Scanner m = new Scanner (System.in);
			System.out.println("\nEditor ID Entered is: "+editorID);
			System.out.println("\n\nConfirm if your Editor ID is correctly entered (YES OR NO):\n");
			System.out.println("\n------------------------------------------------------------------------------------------");
			String resp = sc.nextLine();
			resp = resp.toLowerCase();
			if (resp.equals("yes")){
				try {
					Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
					QUERY = "SELECT * FROM Editor WHERE idEditor=?";
					PreparedStatement stmt = con.prepareStatement(QUERY);
				    stmt.setInt(1, editorID);
					ResultSet res = stmt.executeQuery();
					
					int check =0;   //to check if the user actually has a valid unique id
		
					while (res.next()){
						System.out.println("WELCOME, EDITOR LOGIN SUCCESSFUL! ");
						System.out.println("\nEDITOR NAME: "+ res.getObject(2)+" "+res.getObject(3));
						check=1;    //user id is valid
						}
					
					if (check==0) {  // user id is not valid
						System.out.println("Invalid ID, still want to login again (Yes/no)?");
						String ans = m.nextLine();
						ans=ans.toLowerCase();
						if (ans.equals("yes")) {
							chooseLoginMode();
							  res.close();
							  con.close();
							  stmt.close();
							  m.close();
							  return;
						}
						else {
							startOption();         //go to login/register page
							  res.close();
							  con.close();
							  stmt.close();
							  m.close();
							  return;
						}
					}
					 View_All_Manuscripts(editorID);
					  EditorOptions(editorID);
					  res.close();
					  con.close();
					  stmt.close();
					  m.close();
				}
				catch (SQLException e) {
					  System.err.format("SQL Error: %s", e.getMessage());
				}
			}
			
			else {
				System.out.println("-------------------------------------------------------------------------------------");
				System.out.println("Enter a valid ID:  ");
				chooseLoginMode();
				sc.close();
				m.close();
				return;
			}
		}

	
	/** Helper Method for function chooseLogin Mode for a reviewer to login with his unique reviewer ID
	 * Handles error cases when the user enters invalid or wrong information
	 * @param reviewerID
	 */
	public static void ReviewerLoginMode (int reviewerID) {
			
			Scanner sc = new Scanner (System.in);
			Scanner m = new Scanner (System.in);
			System.out.println("reviewer ID Entered is: "+reviewerID);
			System.out.println("\n\nConfirm if your reviewer ID is correctly entered (YES OR NO):\n");
			System.out.println("\n------------------------------------------------------------------------------------------");
			String resp = sc.nextLine();
			resp = resp.toLowerCase();
			if (resp.equals("yes")){
				
				try {

					Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
					QUERY = "SELECT * FROM Reviewer WHERE ReviewerID=?";
					PreparedStatement stmt = con.prepareStatement(QUERY);
				    stmt.setInt(1, reviewerID);
					ResultSet res = stmt.executeQuery();
					
					int check =0;   //to check if the user actually has a valid unique id
					while (res.next()){
						System.out.println("WELCOME, LOG IN SUCCESSFUL! ");
							System.out.print("REVIEWER NAME: "+res.getObject(4)+ "  "+res.getObject(5));

							check=1;    //user id is valid
						}
					
					if (check==0) {  // user id is not valid
						System.out.println("Invalid ID, still want to login again (Yes/no)?");
						String ans = m.nextLine();
						ans=ans.toLowerCase();
						if (ans.equals("yes")) {
							chooseLoginMode();
							  res.close();
							  con.close();
							  stmt.close();
							  m.close();
							  return;
							  }
						else {
							startOption();         //go to login/register page
							  res.close();
							  con.close();
							  stmt.close();
							  m.close();
							  return; 
							  }
					}
				     ArrayList<Integer> mscripts = View_Assigned_Manuscripts(reviewerID);	  //get list of all manuscripts for a reviewer
				   	Get_Manuscripts(mscripts);
					  ReviewerOptions(reviewerID);
					  res.close();
					  con.close();
					  stmt.close();
					  m.close();
					  }
				catch (SQLException e) {
					  System.err.format("SQL Error: %s", e.getMessage()); }
		} else {
				System.out.println("-------------------------------------------------------------------------------------");
				System.out.println("Enter a valid ID:  ");
				chooseLoginMode();
				sc.close();
				m.close();
				return;}
		}

	/**Helper method to the method choose login mode for a user who is an author. This method handles error cases 
	 * pretty well
	 * @param authorID
	 */
	public static void AuthorloginMode (int authorID) {
		
		Scanner sc = new Scanner (System.in);
		Scanner m = new Scanner (System.in);
		System.out.println("Author ID Entered is: "+authorID);
		System.out.println("\n\nConfirm if your Author ID is correctly entered (YES OR NO):\n");
		System.out.println("\n------------------------------------------------------------------------------------------");
		String resp = sc.nextLine();
		resp = resp.toLowerCase();
		if (resp.equals("yes")){
			
			try {

				Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
				QUERY = "SELECT * FROM Author WHERE AuthorID=?";
				PreparedStatement stmt = con.prepareStatement(QUERY);
			    stmt.setInt(1, authorID);
				ResultSet res = stmt.executeQuery();
				
				int check =0;   //to check if the user actually has a valid unique id
				while (res.next()){
					System.out.println("AUTHOR LOG IN SUCCESSFUL, WELCOME!\n\n");
						System.out.println("FULLNAME: "+ res.getObject(5)+" "+ res.getObject(6));
						System.out.println("\nMAILING ADDRESS: "+ res.getObject(2));
						check=1;    //user id is valid
					}
				if (check==1) {
			        ManuscriptStatus(authorID);			
					AuthorOptions(authorID);
					res.close();
					con.close();
					m.close();
					sc.close();
					stmt.close();
					return;
				}
				
				else {  // user id is not valid
					System.out.println("Invalid ID, still want to login again (Yes/no)?");
					String ans = m.nextLine();
					ans=ans.toLowerCase();
					if (ans.equals("yes")) {
						chooseLoginMode();
						  res.close();
						  con.close();
						  sc.close();
						  m.close();
						  stmt.close();
						  return;
					}
					else {
						startOption();         //go to login/register page
						  res.close();
						  con.close();
						  sc.close();
						  m.close();
						  stmt.close();
						  return;
					}
				}
			}
			catch (SQLException e) {
				  System.err.format("SQL Error: %s", e.getMessage());
			}
		}
		
		else {
			System.out.println("-------------------------------------------------------------------------------------");
			System.out.println("Enter a valid AuthorID:  ");
			chooseLoginMode();
			sc.close();
			m.close();
			return;
		}
	}
	
	/** Method to display and provide options for an editor once they login in so that he can
	 * 1)view status, 2) assign  3) reject, 4)accept, 5) typeset a manuscript, 6)schedule, 7)publish an issue
	 * Option 8 also allows the user to exit and return to the menu of choices
	 * @param editorID
	 * returns void
	 */
	public static void EditorOptions (int editorID) {
			Scanner sc = new Scanner (System.in);
			System.out.print("\n\n\nChoose what you want to do from this MENU: ");
			System.out.print("\n1. VIEW ALL MANUSCRIPT STATUS \n2. ASSIGN MANUSCRIPT TO A REVIEWER \n3. REJECT MANUSCRIPT "
					+ "\n4. ACCEPT MANUSCRIPT"+ "\n5. TYPESET MANUSCRIPT "+"\n6. SCHEDULE ISSUE"+
					"\n7. PUBLISH ISSUE" + "\n8. VIEWS"+ "\n9. EXIT\n\n");
			String ans = sc.nextLine();
			/*If menu option is 1, view all manuscripts*/
			if(ans.equals("1")) {
				View_All_Manuscripts(editorID);
				EditorOptions(editorID);
				
			/*if option is 2, assign manuscript to reviewer*/
			}else if (ans.equals("2")){
				try {
				Scanner m = new Scanner (System.in);
				System.out.println("ENTER THE ID OF THE MANUSCRIPT TO BE ASSIGNED: ");
				System.out.println("If you have forgotten the ID, press 0 to return to menu and check status: ");
				int idManuscript = m.nextInt();
				if (idManuscript!=0){     // if you didn't forget id
					Assign_To_Reviewers(idManuscript, editorID);   //assign to reviewers
				}
				EditorOptions(editorID);

				}
				catch (Exception ec){
					System.out.println("ERROR: Please Enter valid integer for ID");
				}finally {
					EditorOptions(editorID);
				}
			//if option is 3, reject the manuscript (should be submitted or under review status)
			}else if (ans.equals("3")){
				//First, see the reviews of the manuscript, then decide
				try{
				Scanner m1 = new Scanner (System.in);
				System.out.println("ENTER THE ID OF THE MANUSCRIPT TO REJECT: ");
				System.out.println("If you have forgotten the ID, press 0 to return to menu and CHECK STATUS: ");
				int idManuscript = m1.nextInt();
				
				//check to see status of manuscript, if under review, then it can be accepted
				String status = get_Manuscript_Status(idManuscript, editorID);
				
				if (status.equals("Under Review") || status.equals("submitted")){
					if (idManuscript!=0){     // if you didn't forget id
						get_Manuscript_Feedback(idManuscript, editorID);
							Scanner m2 = new Scanner(System.in);
							System.out.println("\nAre you sure you want to reject (yes/no) ?");
							String rep = m2.nextLine();
							rep=rep.toLowerCase();
							if (rep.equals("yes")){
								Update_Status(idManuscript, "Rejected");
								Update_Timestamp(idManuscript);
						}
					}
					EditorOptions(editorID);
					}
			     else {
						System.out.println("CAN ONLY REJECT IF STATUS IS SUBMITTED OR UNDER REVIEW:  ");
					}
				}
				catch (Exception ec){
					System.out.println("ERROR: Please Enter valid integer for ID");
				}finally{
					EditorOptions(editorID);
				}
			//If option 4, you can accept the manuscript for publication if it has at least 3 reviews
			}else if (ans.equals("4")){
				//First, see the reviews of the manuscript, and get count
				try{
				Scanner m1 = new Scanner (System.in);
				System.out.println("ENTER THE ID OF THE MANUSCRIPT TO REVIEW FOR ACCEPTANCE: ");
				System.out.println("If you have forgotten the ID, press 0 to return to menu and CHECK STATUS: ");
				int idManuscript = m1.nextInt();
				
				//check to see status of manuscript, if under review, then it can be accepted
				String status = get_Manuscript_Status(idManuscript, editorID);
				
				if (status.equals("Under Review")){
					if (idManuscript!=0){     // if you didn't forget id
						int val=get_Manuscript_Feedback(idManuscript, editorID);
						if (val>=3){
							Scanner m2 = new Scanner(System.in);
							System.out.println("\nAre you sure you want to accept (yes/no) ?");
							String rep = m2.nextLine();
							rep=rep.toLowerCase();
							if (rep.equals("yes")){
								Update_Status(idManuscript, "Accepted");
								Update_Timestamp(idManuscript);  //update timestamp
							}else {
								System.out.println("DECIDED NOT TO ACCEPT AT THE MOMENT");
							}
						}
					}
					EditorOptions(editorID);
					}
			     else {
						System.out.println("CAN ONLY ACCEPT IF IS UNDER REVIEW FOR ACCEPTANCE:  ");
					}
				}
				catch (Exception ec){
					System.out.println("ERROR: Please Enter valid integer for ID");
				}finally{
					EditorOptions(editorID);
				}
			
			//Set status of manuscript to typeset. Should be an accepted manuscript
			}else if (ans.equals("5")){
				try{
					Scanner m1 = new Scanner (System.in);
					System.out.println("ENTER THE ID OF THE MANUSCRIPT TO TYPESET: ");
					System.out.println("If you have forgotten the ID, press 0 to return to menu and CHECK MANUSCRIPT STATUS: ");
					int idManuscript = m1.nextInt();
					if (idManuscript!=0){
						Type_Set(idManuscript, editorID);
					}
			       	}
					catch (Exception ec){
						System.out.println("ERROR: Please Enter valid integer for ID");
					}finally{
						EditorOptions(editorID);
					}
				
			//Schedule issue for publication
			}else if (ans.equals("6")){
				try{
					Scanner m1 = new Scanner (System.in);
					
					System.out.println("ENTER THE ID OF THE MANUSCRIPT TO SCHEDULED: ");
					System.out.println("If you have forgotten the ID, press 0 to return to menu and CHECK MANUSCRIPT STATUS: ");
					int idManuscript = m1.nextInt();
					String status = get_Manuscript_Status(idManuscript, editorID);
					if (status.equals("TypeSet")){  
						if (idManuscript!=0){
							schedule_Manuscript(idManuscript);
						}
					}
				}catch (Exception ec){
						System.out.println("ERROR: Please Enter valid integer for ID");
				}finally{
						EditorOptions(editorID);
				}
		    }else if (ans.equals("7")){
				try{
					Scanner m1 = new Scanner (System.in);
					System.out.println("ENTER THE ID MANUSCRIPT THAT IS LINKED TO SCHEDUELED ISSUE: ");
					System.out.println("If you have forgotten the ID, press 0 to return to menu and CHECK MANUSCRIPT STATUS: ");
					int idManuscript = m1.nextInt();
					String status = get_Manuscript_Status(idManuscript, editorID);
					if (status.equals("Scheduled")){  
						if (idManuscript!=0){
							publish_Issue(idManuscript);
							Update_Status(idManuscript, "published");
						}
					}
				}catch (Exception ec){
						System.out.println("ERROR: Please Enter valid integer for ID");
				}finally{
						EditorOptions(editorID);
				}
		    }else if (ans.equals("8")){
		    	Editor_Views();
		    	EditorOptions(editorID);
		    }else if (ans.equals("9")){
			    startOption();   //return to the starting page
		    }else {
				EditorOptions(editorID);   //repeat call if entry is incorrect
		    }
			sc.close();
	} 
	
	public static void Author_Views () {
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			
			QUERY = "Select * From PublishedIssues";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.execute();
		    ResultSet res = stmt.executeQuery();
			
			System.out.println("PUBLISHED ISSUE VIEW\n");
			System.out.println("----------------------------------------------------------------------------------------");
		    System.out.printf("%1$-15s %2$-15s %3$-15s\n", "Publ. Year", "Publ. #", "Title");
			System.out.println("----------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-15s %2$-15s %3$-15s\n", res.getObject(1), res.getObject(2), res.getObject(3)); 
				}
			con.close();
			res.close();
			stmt.close();
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}
	}
	
	
	public static void Editor_Views () {
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "Select * From LeadAuthorManuscripts";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.execute();
			ResultSet res = stmt.executeQuery();
			System.out.println("LEAD AUTHOR MANUSCRIPT VIEW\n");
			System.out.println("----------------------------------------------------------------------------------------");
		    System.out.printf("%1$-15s %2$-15s %3$-15s %4$-15s\n", "LastName", "AuthorID", "idManuscript", "Man_Status");
			System.out.println("----------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-15s %2$-15s %3$-15s %4$-15s\n", res.getObject(1), res.getObject(2), res.getObject(3),res.getObject(4)); 
				}
			
			QUERY = "Select * From PublishedIssues";
			stmt = con.prepareStatement(QUERY);
			stmt.execute();
		    res = stmt.executeQuery();
			
			System.out.println("/nPUBLISHED ISSUE VIEW\n");
			System.out.println("----------------------------------------------------------------------------------------");
		    System.out.printf("%1$-15s %2$-15s %3$-15s\n", "Publ. Year", "Publ. #", "Title");
			System.out.println("----------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-15s %2$-15s %3$-15s\n", res.getObject(1), res.getObject(2), res.getObject(3)); 
				}
			
			QUERY = "Select * From ReviewQueue";
			stmt = con.prepareStatement(QUERY);
			stmt.execute();
			res = stmt.executeQuery();
			System.out.println("\nREVIEW QUEUE VIEW\n");
			System.out.println("----------------------------------------------------------------------------------------");
		    System.out.printf("%1$-15s %2$-15s %3$-15s %4$-15s\n", "LastName", "Primary AuthorID", "idManuscript", "Reviewer_LastName");
			System.out.println("----------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-15s %2$-15s %3$-15s %4$-15s\n", res.getObject(1), res.getObject(2), res.getObject(3),res.getObject(5)); 
				}
			
			QUERY = "Select * From WhatsLeft";
			stmt = con.prepareStatement(QUERY);
			stmt.execute();
			res = stmt.executeQuery();
			System.out.println("\nWHATS LEFT VIEW\n");
			System.out.println("----------------------------------------------------------------------------------------");
		    System.out.printf("%1$-15s %2$-15s %3$-20s\n", "idManuscript", "Man_Status", "DateReceived");
			System.out.println("----------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-15s %2$-15s %3$-15s\n", res.getObject(1), res.getObject(2), res.getObject(3)); 
				}
			
			QUERY = "Select * From ReviewStatus";
			stmt = con.prepareStatement(QUERY);
			stmt.execute();
			res = stmt.executeQuery();
			System.out.println("\nREVIEW STATUS VIEW\n");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
		    System.out.printf("%1$-23s %2$-15s %3$-25s %4$-15s %5$-15s %6$-15s %7$-15s %8$-15s\n", "Timestamp","idManuscript",
		    		"Title", "Appropriateness", "Clarity", "Methodology", "Contribution","Recommendation");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-23s %2$-15s %3$-25s %4$-15s %5$-15s %6$-15s %7$-15s %8$-15s\n",
						res.getObject(1), res.getObject(2), res.getObject(3),res.getObject(4),
						res.getObject(5), res.getObject(6), res.getObject(7),res.getObject(8)); 
				}
			
			con.close();
			res.close();
			stmt.close();
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}
	}
	
	
	/** Method to publish an issue, and insert the date, correct number of pages etc
	 * 
	 * @param idManuscript
	 */
	public static void publish_Issue (int idManuscript) {
		//first check if there is a  published issue
		
		Random rand = new Random();
		int od = rand.nextInt(10);
		int manIssue = get_Largest_Issue_Manuscript(); //largest issue in the manuscript table
		int unpubID = get_Next_UnPublishedIssue();   //largest possible next value of issue ID
		int numPages = find_Num_Of_Pages(manIssue);  //find total number of pages in largest unpublished issue
		int pages = find_Total_Pages(idManuscript);  //find total number of pages in the to be scheduled manuscript

		if (manIssue>unpubID-1) {  //implying that there might still be space in an unpublished issue
		    if (pages+numPages<100) {
				Insert_Issue(manIssue, pages+numPages, idManuscript);}
		    else {
		    	Insert_Issue(unpubID, pages, idManuscript);}
		}else {
			Insert_Issue(unpubID+od, pages, idManuscript);}
	}
	
	/** Helper method to insert an issue, and gather the dates,year etc
	 * 
	 * @param issueID
	 * @param numPages
	 */
	public static void Insert_Issue (int issueID, int numPages,int idManuscript) {
				
		//to get publication date, year and month
		String date = dtf.format(now);
		Integer year = Integer.parseInt(date.substring(0, 4));
		Integer month= Integer.parseInt(date.substring(5, 7));
		Integer period = 0;
		
		if (month<=3) {  period=1;}     //fall } 
		else if (month>3 && month<=6) { period=2 ;}
		else if (month>6 && month <=9) { period = 3; }
		else {period = 4; }

		try {
				Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
				QUERY = "INSERT INTO Issue (Publication_Year,Number_Of_Pages,Publication_Number, PrintDate, IssueID)"+"VALUES(?,?,?,?,?)"; 
				PreparedStatement preparedStmt = con.prepareStatement(QUERY);
				preparedStmt.setInt(1, year);
				preparedStmt.setInt(2, numPages);
				preparedStmt.setInt(3,period);
				preparedStmt.setString(4, date);
				preparedStmt.setInt(5, issueID);
				preparedStmt.execute();
				System.out.println("--ISSUE PUBLICATION SUCCESSFUL--");
				
			    QUERY = "UPDATE Manuscript SET IssueID=? where idManuscript=?";
				PreparedStatement stmt = con.prepareStatement(QUERY);
			    stmt.setInt(1, issueID);
				stmt.setInt(2, idManuscript);
				stmt.execute();
			    stmt.close();
			    con.close();
		} catch  (SQLException e) {
			  System.err.format("SQL Error: %s", e.getMessage());
		} 
	}
	
	/** Helper method for the editor login mode option number 6 (Schedule).  The function updates the 
	 * Manuscript status to schedule and inputs its issueID, page number and updates its shhedule to 
	 * "scheduled". It also checks to see that each Issue is less than 100 paged
	 * @param idManuscript
	 */
	public static void schedule_Manuscript (int idManuscript) {
		//first check if there is a  published issue
		int manIssue = get_Largest_Issue_Manuscript();
		int unpub = get_Next_UnPublishedIssue();

		if (manIssue>unpub-1) {  //implying that there might still be space in an unpublished issue
			int numPages = find_Num_Of_Pages(manIssue);
			int pages = find_Total_Pages(idManuscript);
		    if (pages+numPages>100) {
				Update_Man_IssueID(unpub, idManuscript, 1, "Scheduled", 1);
		    }
		    else {
		    	Update_Man_IssueID(manIssue, idManuscript, 2, "Scheduled", pages);
		    }
		}else {
			Update_Man_IssueID(unpub, idManuscript, 1, "Scheduled", 1);
		}
	}
	
	
	/** Helper function for the above function. The function perfomms the update functions described above
	 * 
	 * @param issueID
	 * @param idManuscript
	 * @param order
	 * @param status
	 * @param pageNum
	 */
	public static void Update_Man_IssueID (int issueID, int idManuscript, int order, String status,int pageNum) {   	
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "UPDATE Manuscript SET Man_Status=?,Order_In_Issue=?, Page_Number_In_Issue=? WHERE idManuscript=?";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setString(1, status);
			stmt.setInt(2, order);
			stmt.setInt(3, pageNum);
			stmt.setInt(4, idManuscript);
			stmt.execute();
			System.out.println("--Manuscript UPDATED --");
			stmt.close();
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
     	}
    }
	
	/**Algorithm to get the nextID for an unpublished issue in the Database. The algorithm creates the ID by incrementing the 
	 * maximum ID from the published issue. This ID is entered in the Manuscript table for a scheduled issue
	 * @return
	 */
	public static int get_Next_UnPublishedIssue() {
        int issue = 0;
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "Select MAX(IssueID) From Issue";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.execute();
			ResultSet res = stmt.executeQuery();
			while (res.next()){
				issue = (int) res.getObject(1); //get the reviewer ID
				}
			con.close();
			stmt.close();
			res.close();
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}	
		return issue+1;	
	}
	
	/** Helper method to get largest issueID in manuscripts 
	 * 
	 * @return id of largest manuscript
	 */
	public static int get_Largest_Issue_Manuscript() {
        int issue = 0;
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "Select MAX(IssueID) From Manuscript";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.execute();
			ResultSet res = stmt.executeQuery();
			while (res.next()){
				issue = (int) res.getObject(1); //get the reviewer ID
				}
			
			con.close();
			stmt.close();
			res.close();
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}
		return issue;
	}
	
	
	/**Helper method to find number of pages in a manuscript using IssueID
	 * 
	 * @param issueID
	 * @return number of pages
	 */
	 
	public static int find_Num_Of_Pages (int issueID){
		int sum=0;
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "SELECT Number_of_Pages FROM Manuscript WHERE IssueID = ?";
		    PreparedStatement stmt = con.prepareStatement(QUERY);
		    stmt.setInt(1, issueID);
			ResultSet res = stmt.executeQuery();
			while (res.next()){
			   sum= sum+(int)(res.getObject(1)); 
			}
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
	    }
		return sum;
	}
	
	/** Helper Method to find total number of pages in a manuscript
	 * 
	 * @param manID
	 * @return number of pages
	 */
	public static int find_Total_Pages (int manID){
		int sum=0;
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "SELECT Number_of_Pages FROM Manuscript WHERE idManuscript = ?";
		    PreparedStatement stmt = con.prepareStatement(QUERY);
		    stmt.setInt(1, manID);
			ResultSet res = stmt.executeQuery();
			while (res.next()){
			   res.getObject(1); 
			}
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
	    }
		return sum;
	}
	
	
	/** Helper method for option 5 of editor mode. This method sets the manuscript status to Typeset and the manuscript is 
	 * ready for scheduling. It inputs the page numbers as well.
	 * @param idManuscript
	 * @param idEditor
	 */
	public static void Type_Set(int idManuscript, int idEditor) {
		
		/*To Typeset, the status of the Manuscript should be accepted*/
		String status = get_Manuscript_Status(idManuscript, idEditor);
		
		if (status.equals("Accepted")){  //ready for Typesetting
			try{
				Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
				QUERY = "UPDATE Manuscript SET Man_Status=? WHERE idManuscript=?";
				PreparedStatement stmt = con.prepareStatement(QUERY);
				stmt.setString(1, "TypeSet" );
				stmt.setInt(2, idManuscript);
				stmt.execute();
				
				Scanner sc = new Scanner (System.in);
				System.out.println("ENTER FINAL NUMBER OF MANUSCRIPT PAGES: ");
				int numPages = 200;
				while (numPages>100){
					System.out.println("Number of Pages Should be less thanor equal to 100, Enter page number less than 100!");
					numPages=sc.nextInt();
				}
				
             	QUERY = "UPDATE Manuscript SET Number_Of_Pages=? WHERE idManuscript=?";
                stmt = con.prepareStatement(QUERY);   
                stmt.setInt(1, numPages );
				stmt.setInt(2, idManuscript);
				stmt.execute();
				System.out.println("MANUSCRIPT TYPESET AND PAGES NUMBER INSERTED, READY FOR SCHEDULING");
				stmt.close();             
			} catch (SQLException e) {
			    System.err.format("SQL Error: %s", e.getMessage());
	     	}
		}
    }
	
	/** Helper method to update dates on manuscript
	 * 
	 * @param Manuscript
	 */
	public static void Update_Timestamp (int idManuscript) {   
		
		System.out.println("Date Received: "+dtf.format(now)); //e.g 2016-11-16 12:08:43	
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "UPDATE Manuscript SET DateReceived=? WHERE idManuscript=?";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setString(1,dtf.format(now) );
			stmt.setInt(2, idManuscript);
			stmt.execute();
			System.out.println("--TIMESTAMP UPDATED --");
			stmt.close();
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
     	}
    }
	
	
	/** Helper Method to get the feedback of a manuscript
	 *  
	 * @param idManuscript
	 * @param editorID
	 * @returns the number of reviews that a manuscript has.
	 */
	public static int get_Manuscript_Feedback (int idManuscript, int editorID) {
		    int check =0;
			
			try{
				Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
				QUERY = "SELECT * FROM Feedback WHERE Manuscript_idManuscript=?";
			    PreparedStatement stmt = con.prepareStatement(QUERY);
			    stmt.setInt(1, idManuscript);
				ResultSet res = stmt.executeQuery();
				System.out.println("HERE IS THE FEEDBACK OF THE MANUSCRIPT FROM ALL REVIEWERS\n");
				System.out.println("-------------------------------------------------------------------------------------------------------");
			    System.out.printf("%1$-15s %2$-15s %3$-15s %4$-20s %5$-15s \n", "Approp.", "Clarity", "Methodology","Contribution", "Recommendation");
				System.out.println("-------------------------------------------------------------------------------------------------------");
				while (res.next()){
					System.out.format("%1$-15s %2$-15s %3$-15s %4$-20s %5$-15s \n", res.getObject(1), res.getObject(2), res.getObject(3), res.getObject(4), res.getObject(5)); 
					check=check+1; //manuscripts present in system
					}
				
				if (check==0) { //no manuscripts in system
					System.out.println("Manuscript not yet reviewed!");	}
			} catch (SQLException e) {
			    System.err.format("SQL Error: %s", e.getMessage()); }
		return check;
	}
	
	/**Helper Method to assign a manuscript to several reviewers under the control of the editor.
	 * @param idManuscript
	 * @param idEditor
	 * returns void
	 */
	public static void Assign_To_Reviewers(int idManuscript, int idEditor) {
		
		/*Get status of manuscript, if the manuscript does not have status 'submitted or under review' then the 
		 * manuscript cannot be assigned to a reviewer */
		String status = get_Manuscript_Status(idManuscript,idEditor );
		if (status.equals("submitted")) {  //need to fix case for under review
			
			//Get RICode of the manuscript, and use it to identify reviewers to review the manuscript 
			int RICode = get_Manuscript_RICode (idManuscript);
			
			//get all reviewers with corresponding RICode
			ArrayList<Integer> reviewers = get_Reviewers_For_RICode(RICode);
			System.out.println("\nAvailable ReviewersID List: "+reviewers+"\n");
			if (reviewers.size()<3) {
				System.out.println("Not enough reviewers to review manuscript, need at least 3 reviewers: ");
				EditorOptions(idEditor);
				return; }
			Scanner sc = new Scanner(System.in);
			int count=0;
			int peak =reviewers.size();
			while (count<peak){
				    Assign_Manuscript_To_Reviewer(reviewers.get(count), idManuscript);
				    Update_Manuscript_Editor (idManuscript, idEditor);
                count=count+1;}
		} else {
			System.out.println("Manuscript Already Assigned to Reviewers! ");}
		EditorOptions(idEditor);  //return to main menu
	}


	/**Helper method to update the Editor who assigned reviewers to the manuscript
	 * @param idManuscript
	 * @param idEditor
	 * return void
	 */
	public static void Update_Manuscript_Editor (int idManuscript, int idEditor){
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "UPDATE Manuscript SET idEditor=? WHERE idManuscript=?";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setInt(1, idEditor);
			stmt.setInt(2, idManuscript);
			stmt.execute();
			System.out.println("--MANUSCRIPT EDITOR FIELD UPDATED ---");
			stmt.close();
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
     	}
    }
		
	
	/**Helper method to assign a specific manuscript to a reviewer
	 * Inserts the reviewer to Reviewer Group which is directly linked with manuscript
	 * @param reviewerID
	 * @param idManuscript
	 */
	public static void Assign_Manuscript_To_Reviewer (int reviewerID, int idManuscript) {
		
		System.out.println("Date Received: "+dtf.format(now)); //e.g 2016-11-16 12:08:43	
		try {
				Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
				QUERY = "INSERT INTO Reviewer_Group (ReviewerID,DateReceived,idManuscript)"+"VALUES(?,?,?)"; 
				PreparedStatement preparedStmt = con.prepareStatement(QUERY);
				preparedStmt.setInt(1, reviewerID);
				preparedStmt.setString(2, dtf.format(now));
				preparedStmt.setInt(3, idManuscript);
				preparedStmt.execute();
				System.out.println("--REVIEWER ASSIGNMENT SUCCESSFUL--");

				/*Get manuscript status, and check to see if it should be updated to received*/
				String status =get_Manuscript_Status(idManuscript, reviewerID);
				if (status.equals("submitted")) {
					status = "Under Review";   
					Update_Status(idManuscript, status);   //update manuscript to under review
				}
				con.close();
				preparedStmt.close();
		} catch  (SQLException e) {
			  System.err.format("SQL Error: %s", e.getMessage());} 
	}
	
	/** Helper method to update manuscript status,
	 * @param idManuscript
	 * @param status
	 * returns void
	 */
	public static void Update_Status (int idManuscript, String status) {
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "UPDATE Manuscript SET Man_Status=? WHERE idManuscript=?";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setString(1, status);
			stmt.setInt(2, idManuscript);
			stmt.execute();
			System.out.println("--STATUS UPDATED TO: "+status+"--");
			stmt.close();
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());}
    }
	
	/**Helper  method to view all the Manuscripts in the system ordered by status, and the manuscript id
	 * @param editorID
	 * returns void
	 */
	public static void View_All_Manuscripts(int editorID) {
		
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			Scanner out = new Scanner (System.in);
			QUERY = "SELECT * FROM Manuscript ORDER BY Man_Status, idManuscript";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.execute();
			ResultSet res = stmt.executeQuery();
			int check = 0;   //to check is there any manuscripts in the system
			System.out.println("HERE ARE ALL THE MANUSCRIPTS IN THE SYSTEM\n\n");
			System.out.println("------------------------------------------------------------");
		    System.out.printf("%1$-15s %2$-20s %3$-15s \n", "ManuscriptID", "TITLE", "STATUS");
			System.out.println("------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-15s %2$-20s %3$-15s\n", res.getObject(1), res.getObject(4), res.getObject(3)); 
				check=1; //manuscripts present in system
				}
			if (check==0) { //no manuscripts in system
				System.out.println("No manuscripts in System! (Press any key to go back)");
			}
			
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());}
	}
	
	/** Method to display and provide options to an author so that he can
	 * 1)view status, 2) submit manuscript 3) retract manuscript
	 * Option 4 also allows the author to exit and return to the menu of choices
	 * @param editorID
	 * returns void
	 */
	public static void AuthorOptions (int authorID) {
			Scanner sc = new Scanner (System.in);
			Scanner out = new Scanner (System.in);
			Scanner out1 = new Scanner (System.in);
            //need to put output from status window
			System.out.print("\n\n\nChoose what you want to do from this MENU: ");
			System.out.print("\n1. VIEW MANUSCRIPTS' STATUS \n2. SUBMIT MANUSCRIPT \n3. RETRACT MANUSCRIPT \n4. VIEWS \n5. EXIT \n\n");
			String ans = sc.nextLine();
			if(ans.equals("1")) {
			        ManuscriptStatus(authorID);		
			        AuthorOptions(authorID);
			}else if (ans.equals("2")){
				Scanner ss = new Scanner(System.in);   
				System.out.println("Enter manuscript title below: ");
				String title = out.nextLine();
				System.out.println("Enter your current affiliation below: ");
				String aff = out.nextLine();
				System.out.println("Enter the RICode for the manuscript:  ");
				int RICode = out1.nextInt();
				System.out.println("Enter the name of co-Author 1: If none input 0");
				String auth1 = out.nextLine();
				System.out.println("Enter the name of co-Author 2: If none input 0");
				String auth2 = out.nextLine();
				System.out.println("Enter the name of co-Author 3: If none input 0");
				String auth3 = out.nextLine();
				System.out.println("Enter the file text: ");
				String filename = out.nextLine();
                System.out.println("The data entered is shown below: ");
                System.out.print("\nMANUSCRIPT TITLE: "+title+ "\nAFFILIATION: "+aff+"\nRICode: "+RICode+
                		"\nAuthor2: "+auth1+ "\nAuthor3: "+auth2+ "\nAuthor4: "+auth3+ "\nFILENAME\n\n"+filename);
                System.out.println("\nIs everything entered correctly");
                	String reply = ss.nextLine();
                	reply = reply.toLowerCase();
                	if (reply.equals("yes")){
                		int idManuscript= Manuscript_Insert(authorID,title, filename,RICode);  //insert manuscript, and retain id
                		Author_Group_Insert(authorID, idManuscript, 1);  //insert primary author into group
                		System.out.println("\nYour submitted manuscript ID is : "+idManuscript);
        				System.out.println("\nSUBMISSION SUCCESSFUL!");
                	}
                	AuthorOptions(authorID);
                	ss.close();
                
			}else if (ans.equals("3")){
				Scanner s = new Scanner(System.in);
				System.out.println("\nEnter ID of Manuscript you want to delete?");
				int idManuscript = s.nextInt();	
				String status = get_Manuscript_Status(idManuscript, authorID);
                if (status==null) {
                	System.out.println("\nMANUSCRIPT DOES NOT EXIST IN SYSTEM");
                }
                else if (status.equals("typeset")){   //dont delete if in typesetting mode
					System.out.println("\nIN TYPESETTED STATUS, CAN'T BE DELETED!");
				}
                else {
                	Manuscript_Delete(idManuscript,authorID);
                }
                AuthorOptions(authorID);
              s.close();
			}else if (ans.equals("4")){
				Author_Views();  
				AuthorOptions(authorID);
			}else if (ans.equals("5")){
				startOption();  //go to main page
			}else {
				AuthorOptions(authorID);   //repeat call if entry is incorrect
			}
			
			sc.close();
			out.close();
			out1.close();
    }
	
	/** Helper method to delete a manuscript. Since author group uses a foreign key, before deleting a 
	 * manuscript, the Author_Group should be deleted as well.
	 * 
	 * @param idManuscript
	 * @param authorID
	 */
	public static void Manuscript_Delete (int idManuscript, int authorID) {
			try{
				Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
				/*manuscript is foreign key to Author_Group hence to delete
				 * it we first have to delete in Author_Group
				 */
				QUERY = "DELETE FROM Author_Group where idManuscript=? and AuthorID=?";  
				PreparedStatement stmt = con.prepareStatement(QUERY);
				stmt.setInt(1, idManuscript);
				stmt.setInt(2, authorID);
				stmt.execute();
				System.out.println("--AUTHOR_GROUP DELETED--");
				stmt.close();
				
				QUERY = "DELETE FROM Manuscript where idManuscript=? and PrimaryAuthorID=?";
				stmt = con.prepareStatement(QUERY);
				stmt.setInt(1, idManuscript);
				stmt.setInt(2, authorID);
				stmt.execute();
				System.out.println("--MANUSCRIPT SUCCESSFULLY DELETED--");
				stmt.close();		
				con.close();
				return;
				
				
			} catch (SQLException e) {
			    System.err.format("SQL Error: %s", e.getMessage());
	     	}catch (Exception ec){
	     		System.out.println("Error: You can only delete your own manuscripts");
	     	}
	}
	
	
	/** Helper method to insert a manuscript, and return its ID. USed in authorOptions
	 * 
	 * @param pAuthorID
	 * @param title
	 * @param filename
	 * @param RICode
	 * @return
	 */
			
	public static int Manuscript_Insert (int pAuthorID, String title, String filename,int RICode) {
		int ans = 0;
		try {
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
				QUERY = "INSERT INTO Manuscript (PrimaryAuthorID,Man_Status,Title,Man_Text,RICode)"+"VALUES(?,?,?,?,?)"; 
				PreparedStatement preparedStmt = con.prepareStatement(QUERY);
				preparedStmt.setInt(1, pAuthorID);
				preparedStmt.setString(2, "submitted");
				preparedStmt.setString(3, title);
				preparedStmt.setString(4,filename);
				preparedStmt.setInt(5, RICode);
				preparedStmt.execute();
				System.out.println("--MANUSCRIPT SUBMISSION SUCCESSFUL--");

				//retrieve manuscript id
				QUERY= "SELECT idManuscript FROM Manuscript where PrimaryAuthorID=? and Title=? and RICode=?";
			    PreparedStatement stmt = con.prepareStatement(QUERY);
			    stmt.setInt(1, pAuthorID);
			    stmt.setString(2, title);
			    stmt.setInt(3, RICode);
				ResultSet res = stmt.executeQuery();
				
				while (res.next()){
					ans = (Integer)res.getObject(1);  //get the id
				}
				con.close();
			    return ans;
		} catch  (SQLException e) {
			  System.err.format("SQL Error: %s", e.getMessage());} 
		return ans;
	}
	
	
	/** Helper method to Insert an author into an author group, which links author ID to Manuscrpt ID and 
	 * shows the order of the author
	 * @param authorID
	 * @param idManuscript
	 * @param authorOrder
	 */
	public static void Author_Group_Insert (int authorID, int idManuscript, int authorOrder) {
		
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "INSERT INTO Author_Group (AuthorID, idManuscript,AuthorOrder)"+"VALUES(?,?,?)";  
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setInt(1,authorID);
			stmt.setInt (2, idManuscript);
            stmt.setInt(3,authorOrder);
			stmt.execute();
			System.out.println("--AUTHOR INSERTION INTO GROUP SUCCESSFUL--");
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());}
	}
	
	/** Helper method to get all the reviewers whose interest area is the RICode specified. 
	 * Input -> RICode
	 * Output -> List with IDs of all reviewers with the specified interest field
	 */
	public static ArrayList <Integer> get_Reviewers_For_RICode (int RICode) {

		ArrayList<Integer> reviewList = new ArrayList<Integer>();   //list of reviewers
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "Select * From Area_of_Interest_Group where RICode=?";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setInt(1, RICode);
			stmt.execute();
			ResultSet res = stmt.executeQuery();
			while (res.next()){
				Integer reviewerID = (Integer) res.getObject(1); //get the reviewer ID
				reviewList.add(reviewerID);  //add to list
				}
			con.close();
			stmt.close();
			return reviewList;	
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}	
		return reviewList;	
	}
	
	
	/**Helper method to get RICode for a manuscript.
	 * Input -> Manuscript iD
	 * Output -> RICode  (if 0, then manuscript was not found)
	 */
	public static int get_Manuscript_RICode (int idManuscript) {
		int RICode =0;

		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "Select * From Manuscript where idManuscript=?";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setInt(1, idManuscript);
			stmt.execute();
			ResultSet res = stmt.executeQuery();
			int check =0;   //to check if manuscript is actually in:
			while (res.next()){
				RICode = (int) res.getObject(12); 
				check=1;    //manuscript in
				}
			if (check==0) {
				System.out.println("Manuscript not in System");
			    stmt.close();
			    con.close();
			    return 0;
			}
			con.close();
			stmt.close();
			return RICode;		
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}
		return 0;
	}
	
	
	/** Helper method to get the status of a Manuscript
	 * 
	 */
	public static String get_Manuscript_Status (int idManuscript, int authorID) {
		String status ="";

		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "Select * From Manuscript where idManuscript=?";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setInt(1, idManuscript);
			stmt.execute();
			ResultSet res = stmt.executeQuery();
			int check =0;   //to check if manuscript is actually in:
			
			while (res.next()){
				status = (String) res.getObject(3); 
				check=1;    //manuscript in
				}
			
			if (check==0) {
				System.out.println("Manuscript already not in System");
			    stmt.close();
			    con.close();
			    return null;
			}
			
			con.close();
			stmt.close();
			return status;
						
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}
		
		return null;
	}
	
	
	/** Helper method to get the status of a manuscript after inserting the authorID
	 * 
	 * @param authorID
	 */
	public static void ManuscriptStatus(int authorID) {
		
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			Scanner out = new Scanner (System.in);
			QUERY = "Select * From Manuscript where PrimaryAuthorID=?";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setInt(1, authorID);
			stmt.execute();
			ResultSet res = stmt.executeQuery();
			int check =0;   //to check if the user actually has a manuscript
			
			System.out.println("HERE ARE YOUR MANUSCRIPTS IN THE SYSTEM\n\n");
			System.out.println("----------------------------------------------------------------------------------------");
		    System.out.printf("%1$-15s %2$-20s %3$-15s %4$-20s\n", "ManuscriptID", "TITLE", "STATUS", "NUMBER OF PAGES");
			System.out.println("----------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-15s %2$-20s %3$-15s %4$-20s\n", res.getObject(1), res.getObject(4), res.getObject(3), res.getObject(6)); 
				check=1;    //user id is valid
				}
			
			if (check==0) {
				System.out.println("No manuscripts in System! (Press any key to go back)");
			}
			
			else {
				System.out.println("\n\nPress any key to go back to options");

			}
			
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}
	}
	
	
	/** Method to register into the application. Gives options to register as an author, editor or a reviewer
	 * 
	 */
	public static void chooseRegisterMode () {
	
		Scanner sc = new Scanner (System.in);
		System.out.println("\n------------------------------------------------------------------------------------------");
		System.out.println("\nRegister as on of these 3 options: Editor, Reviewer, Author");
		System.out.println("\nType your in your choice and hit enter: \n");

		String ans=sc.nextLine();  //input entered
		ans = ans.toLowerCase();  
		if (ans.equals("editor")) {
			System.out.println("Welcome to Editor mode");
			registerEditor();
			sc.close();
			return;
		}
		else if (ans.equals("reviewer"))  {
			System.out.println("Welcome to Reviewer mode");
			registerReviewer();
			sc.close();
			return;
		}
		else if (ans.equals("author")) {
			System.out.println("Welcome to author mode");
			registerAuthor();
			sc.close();
			return;
		}
		else {
			System.out.println("Please check your entry spelling, and type in  a valid response\n");
			System.out.println("\nDo you still want to register?");
			ans=sc.nextLine();
            ans=ans.toLowerCase();
            if (ans.equals("yes")){
            	chooseRegisterMode();
            	sc.close();
            	return;
            }
            else {
            	System.out.println("Thank you, Exiting App..");
            	sc.close();
            	return;
            }
		}
	}

	
	/** Method to register an editor
	 * 
	 */
	public static void registerEditor () {
		
		try {
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
         
			Scanner sc = new Scanner (System.in); 
			System.out.println("\nEnter ALL your personal details as requested by the prompts below:");
			System.out.println("\nEnter your first name: ");
			String fname = sc.nextLine();
			System.out.println("Enter your last name: ");
			String lname =sc.nextLine();
			System.out.println("\nDetails entered: ");
			System.out.print("fname: "+fname+ "\nlname: "+lname);
			System.out.println("\n\nConfirm all details have been entered correctly\n");
			System.out.println("Type Yes, or No to confirm\n");
			String resp = sc.nextLine();
			resp = resp.toLowerCase();
			if (resp.equals("yes")){	
				QUERY = "INSERT INTO Editor (FirstName,LastName)"+"VALUES(?,?)";  //insert user into editor table
				//NOTE: I NEED TO CHECK IF ENTERED USER EXITS ALREADY IS IN THE DATABASE, AND FOR NULL CASES
				PreparedStatement preparedStmt = con.prepareStatement(QUERY);
				preparedStmt.setString(1, fname);
				preparedStmt.setString(2, lname);
				//execute the prepared statement
				preparedStmt.execute();
				System.out.println("--EDITOR REGISTRATION SUCCESSFUL--");

				//get id of this editor
				QUERY= "SELECT idEditor FROM Editor where FirstName=? and LastName=?";
			    PreparedStatement stmt = con.prepareStatement(QUERY);
			    stmt.setString(1, fname);
			    stmt.setString(2, lname);
				ResultSet res = stmt.executeQuery();
				while (res.next()){
					System.out.println("\nYour unique editor id is: ");
					System.out.format("%d", res.getObject(1));
				}
				
				System.out.println("\nYou can login with your EditorID now: ");
				chooseLoginMode();  //go to login page
				
				sc.close();
				con.close();
				preparedStmt.close();
				stmt.close();
				res.close();
				return;
			}
			else {
				System.out.println("-------------------------------------------------------------------------------------");
				System.out.println("Enter correct details again");
				registerEditor();
				sc.close();
				con.close();
				stmt.close();
				res.close();
				return;
			}
		} catch  (SQLException e) {
			  System.err.format("SQL Error: %s", e.getMessage());
		} 
	}
	
	
	/** Method to register an author
	 * 
	 */
	public static void registerAuthor () {
	
	 try {
		Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
         
		Scanner sc = new Scanner (System.in); 
		System.out.println("\nEnter ALL your personal details as requested by the prompts below:");
		System.out.println("\nEnter your first name: ");
		String fname = sc.nextLine();
		System.out.println("Enter your last name: ");
		String lname =sc.nextLine();
		System.out.println("Enter your email: ");
		String email = sc.nextLine();
		System.out.println("Enter your address: ");
		String address = sc.nextLine();
		System.out.println("Enter your current affiliation");
		String affiliation = sc.nextLine();	
		System.out.println("\nDetails entered: ");
		System.out.print("fname: "+fname+ "\nlname: "+lname+"\nemail: "+email+ "\naddress: "+address+ "\naffiliation: "+affiliation);
		System.out.println("\n\nConfirm all details have been entered correctly\n");
		System.out.println("Type Yes, or No to confirm\n");
		String resp = sc.nextLine();
		resp = resp.toLowerCase();
		if (resp.equals("yes")){	
			QUERY = "INSERT INTO Author (MailingAddress, EmailAddress, Affiliation, FirstName,LastName)"+"VALUES(?,?,?,?,?)";  //insert user into editor table
			//NOTE: I NEED TO CHECK IF ENTERED USER EXITS ALREADY IS IN THE DATABASE, AND FOR NULL CASES
			PreparedStatement preparedStmt = con.prepareStatement(QUERY);
			preparedStmt.setString(1, address);
			preparedStmt.setString(2, email);
			preparedStmt.setString(3, affiliation);
			preparedStmt.setString(4, fname);
			preparedStmt.setString(5, lname);
			//execute the prepared statement
			preparedStmt.execute();
			System.out.println("--AUTHOR REGISTRATION SUCCESSFUL--");

			//get id of this author
			QUERY= "SELECT AuthorID FROM Author where EmailAddress=? and FirstName=? and LastName=?";
		    PreparedStatement stmt = con.prepareStatement(QUERY);
		    stmt.setString(1, email);
		    stmt.setString(2, fname);
		    stmt.setString(3, lname);
			ResultSet res = stmt.executeQuery();
			
			while (res.next()){
				System.out.println("\nYour unique Author id is: ");
				System.out.format("%d", res.getObject(1));
			}
			
			System.out.println("\nYou can login with your AuthorID now: ");
			chooseLoginMode();  //go to login page
			
			sc.close();
			con.close();
			preparedStmt.close();
			stmt.close();
			res.close();
			return;
		}
		else {
			System.out.println("-------------------------------------------------------------------------------------");
			System.out.println("Enter correct details again");
			registerEditor();
			sc.close();
			con.close();
			stmt.close();
			res.close();
			return;
		}
	} catch  (SQLException e) {
		  System.err.format("SQL Error: %s", e.getMessage());
	} 
}
	
	/** Method to register a reviewer
	 * 
	 */
	public static void registerReviewer() {
		
	 try {
		Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
		Scanner sc = new Scanner (System.in); 
		Scanner s = new Scanner (System.in);
		System.out.println("\nEnter ALL your personal details as requested by the prompts below:");
		System.out.println("\nEnter your first name: ");
		String fname = sc.nextLine();
		System.out.println("Enter your last name: ");
		String lname =sc.nextLine();
		System.out.println("Enter your first RICode: ");
		Integer RICode1 = s.nextInt();
		System.out.println("Enter your 2nd RICode, if none input 0: ");
		Integer RICode2 = s.nextInt();
		System.out.println("Enter your 3rd RICode, if none input 0: ");
		Integer RICode3 = s.nextInt();	
		System.out.println("Enter your email: ");
		String email = sc.nextLine();
		System.out.println("Enter your Affiliation: ");
		String affiliation = sc.nextLine();
		System.out.println("\nDetails entered: ");
		System.out.print("fname: "+fname+ "\nlname: "+lname+"\nemail: "+email+"\naffiliation: "+affiliation
				+"\nRICode1: "+RICode1+ "\nRICode2: "+RICode2+ "\nRICode3: "+RICode3);
		System.out.println("\n\nConfirm all details have been entered correctly\n");
		System.out.println("Type Yes, or No to confirm\n");
		String resp = sc.nextLine();
		resp = resp.toLowerCase();
		if (resp.equals("yes")){	
			QUERY = "INSERT INTO Reviewer (Email, Affiliation, FirstName,LastName)"+"VALUES(?,?,?,?)";  //insert user into editor table
			//NOTE: I NEED TO CHECK IF ENTERED USER EXITS ALREADY IS IN THE DATABASE, AND FOR NULL CASES
			PreparedStatement preparedStmt = con.prepareStatement(QUERY);
			preparedStmt.setString(1, email);
			preparedStmt.setString(2, affiliation);
			preparedStmt.setString(3, fname);
			preparedStmt.setString(4, lname);
			//execute the prepared statement
			preparedStmt.execute();

			//get id of this reviewer
			QUERY= "SELECT ReviewerID FROM Reviewer where FirstName=? and LastName=?";
		    PreparedStatement stmt = con.prepareStatement(QUERY);
		    stmt.setString(1, fname);
		    stmt.setString(2, lname);
			ResultSet res = stmt.executeQuery();
			int id = 0;
			while (res.next()){
				System.out.println("\nYour unique Reviewer id is: ");
				id =  (int) res.getObject(1);
				System.out.format("%d", res.getObject(1));
			}
			
			QUERY = "INSERT INTO Area_of_Interest_Group (ReviewerID, RICode) VALUES(?,?)";
			PreparedStatement ps = con.prepareStatement(QUERY);
			ps.setInt(1, id);
			ps.setInt(2, RICode1);
			ps.execute();
			if (RICode2 != 0){
				QUERY= "INSERT INTO Area_of_Interest_Group (ReviewerID, RICode) VALUES(?,?)";
				ps = con.prepareStatement(QUERY);
				ps.setInt(1, id);
				ps.setInt(2, RICode2);
				ps.execute();
			}
			if (RICode3 != 0){
				QUERY= "INSERT INTO Area_of_Interest_Group (ReviewerID, RICode) VALUES(?,?)";
				ps = con.prepareStatement(QUERY);
				ps.setInt(1, id);
				ps.setInt(2, RICode3);
				ps.execute();
			}

			System.out.println("\n--REGISTRATION SUCCESSFUL!!--");
			System.out.println("\nYou can login with your Reviewer ID now: ");
			chooseLoginMode();  //go to login page
			
			sc.close();
			s.close();
			con.close();
			preparedStmt.close();
			ps.close();
			stmt.close();
			res.close();
			return;
		}
		else {
			System.out.println("-------------------------------------------------------------------------------------");
			System.out.println("Enter correct details again");
			registerEditor();
			sc.close();
			s.close();
			con.close();
			stmt.close();
			res.close();
			return;
		}
		
	} catch  (SQLException e) {
		  System.err.format("SQL Error: %s", e.getMessage());
	}
    }
	
	/** Options for the reviewer, once he logs in. He can check status of manuscripts, resign  and review manuscripts
	 * 
	 * @param reviewerID
	 */
	public static void ReviewerOptions (int reviewerID) {
		Scanner sc = new Scanner (System.in);
	    ArrayList<Integer> mscripts = View_Assigned_Manuscripts(reviewerID);	  //get list of all manuscripts for a reviewer
		System.out.print("\n\nChoose what you want to do from this MENU: ");
		System.out.print("\n1. VIEW YOUR ASSIGNED MANUSCRIPT STATUS \n2. REVIEW MANUSCRIPT (THEN ACCEPT OR REJECT IT)"
                     + "\n3. RESIGN "+"\n4. VIEWS"+"\n5. EXIT\n");	
		System.out.println("\n------------------------------------------------------------------------------------------");

		
		String ans = sc.nextLine();
		/*If menu option is 1, view all manuscripts*/
		if(ans.equals("1")) {
	       Get_Manuscripts(mscripts);
	       ReviewerOptions(reviewerID);
	       
		//if option is 2, review
		}else if (ans.equals("2")){
			Review_Manuscript(reviewerID);  //review manuscript
		    ReviewerOptions(reviewerID);

		//if option is 3, resign
		}else if (ans.equals("3")){
			try {
			Scanner s = new Scanner (System.in);
			System.out.println("Enter you ID again inorder to resign?");
			int Id = s.nextInt();
				if (Id==reviewerID){
					Resign_Reviewer(Id);
				}
				else {
					System.out.println("Please enter your actual ID to resign");
				}
			} 
			catch (Exception e) {
				System.out.println("Error: "+e.getMessage());
			}finally {
				ReviewerOptions(reviewerID);
			}
				
		//if option 4, views
		}else if (ans.equals("4")){
			Reviewer_Views();
			ReviewerOptions(reviewerID);
		}else if (ans.equals("5")){
	    	startOption();   //return to the starting page
	    }else {
			ReviewerOptions(reviewerID);   //repeat call if entry is incorrect
	    }
		sc.close();
    } 

	public static void Reviewer_Views (){
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "Select * From PublishedIssues";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.execute();
			ResultSet res = stmt.executeQuery();
			
			System.out.println("PUBLISHED ISSUE VIEW\n\n");
			System.out.println("----------------------------------------------------------------------------------------");
		    System.out.printf("%1$-15s %2$-15s %3$-15s\n", "Publ. Year", "Publ. #", "Title");
			System.out.println("----------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-15s %2$-15s %3$-15s\n", res.getObject(1), res.getObject(2), res.getObject(3)); 
				}
			
			QUERY = "Select * From ReviewQueue";
			stmt = con.prepareStatement(QUERY);
			stmt.execute();
			res = stmt.executeQuery();
			System.out.println("\nREVIEW QUEUE VIEW\n");
			System.out.println("----------------------------------------------------------------------------------------");
		    System.out.printf("%1$-15s %2$-15s %3$-15s %4$-15s\n", "LastName", "Primary AuthorID", "idManuscript", "Reviewer_LastName");
			System.out.println("----------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-15s %2$-15s %3$-15s %4$-15s\n", res.getObject(1), res.getObject(2), res.getObject(3),res.getObject(5)); 
				}
			
			QUERY = "Select * From ReviewStatus";
			stmt = con.prepareStatement(QUERY);
			stmt.execute();
			res = stmt.executeQuery();
			System.out.println("\nREVIEW STATUS VIEW\n");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
		    System.out.printf("%1$-23s %2$-15s %3$-25s %4$-15s %5$-15s %6$-15s %7$-15s %8$-15s\n", "Timestamp","idManuscript",
		    		"Title", "Appropriateness", "Clarity", "Methodology", "Contribution","Recommendation");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
			while (res.next()){
				System.out.format("%1$-23s %2$-15s %3$-25s %4$-15s %5$-15s %6$-15s %7$-15s %8$-15s\n",
						res.getObject(1), res.getObject(2), res.getObject(3),res.getObject(4),
						res.getObject(5), res.getObject(6), res.getObject(7),res.getObject(8)); 
				}
			
			
			
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}
	}
	
	
	/** Method to delete reviewer when he resigns from the database, deletes instances where it's the foreign key as well
	 * 
	 * @param reviewerID
	 */
	public static void Resign_Reviewer (int reviewerID) {		 

		try{ //insert feedback
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "DELETE FROM Feedback WHERE ReviewerId = ?";
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setInt(1,reviewerID);
			stmt.execute();		
			QUERY = "DELETE FROM Reviewer_Group WHERE ReviewerID= ?";
			stmt = con.prepareStatement(QUERY);
			stmt.setInt(1,reviewerID);
			stmt.execute();
			QUERY = "DELETE FROM Area_of_Interest_Group WHERE ReviewerID = ?";
			stmt = con.prepareStatement(QUERY);
			stmt.setInt(1,reviewerID);
			stmt.execute();	
			QUERY = "DELETE FROM Reviewer WHERE ReviewerID = ?";
			stmt = con.prepareStatement(QUERY);
			stmt.setInt(1,reviewerID);
			stmt.execute();	
			System.out.println("THANK YOU FOR YOUR SERVICE!");
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());}
	}
	
	/** Method to get list of all manuscripts belonging to a reviewer with a certain ID
	 * 
	 * @param reviewerID
	 * @return list of the manuscript IDs
	 */
	public static ArrayList<Integer> View_Assigned_Manuscripts(int reviewerID) {
		
		ArrayList<Integer> manuscriptIDs = new ArrayList<Integer> ();
			try{
				Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
				QUERY = "Select * From Reviewer_Group where ReviewerID=?";
				PreparedStatement stmt = con.prepareStatement(QUERY);
				stmt.setInt(1, reviewerID);
				stmt.execute();
				ResultSet res = stmt.executeQuery();
				
				while (res.next()){
					manuscriptIDs.add((Integer)res.getObject(3)); 
					}
				con.close();
				stmt.close();
				res.close();
			} catch (SQLException e) {
			    System.err.format("SQL Error: %s", e.getMessage());
			}
			
			return manuscriptIDs;
		}
	
	/** Helper method to get all the manuscripts assigned to a reviewer. The list of manuscripts contain
	 * The ID, title, status and number of pages
	 * @param mscripts list, reviewerID
	 */
	public static void Get_Manuscripts(ArrayList<Integer> mscripts) {
		
		if (mscripts.isEmpty()) {
			System.out.println("\n\nNo manuscripts assigned to you at the moment!");
			return;
		}
		System.out.println("\nHERE ARE MANUSCRIPTS ASSIGNED TO YOU IN THE SYSTEM\n\n");
		System.out.println("----------------------------------------------------------------------------------------");
	    System.out.printf("%1$-15s %2$-20s %3$-15s %4$-20s\n", "ManuscriptID", "TITLE", "STATUS", "NUMBER OF PAGES");
		System.out.println("----------------------------------------------------------------------------------------");
		try{
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			for (int i=0; i<mscripts.size();i++){
				int idManuscript = mscripts.get(i);
				QUERY = "Select * From Manuscript where idManuscript=?";
				PreparedStatement stmt = con.prepareStatement(QUERY);
				stmt.setInt(1, idManuscript);
				stmt.execute();
				ResultSet res = stmt.executeQuery();
				
				while (res.next()){
					System.out.format("%1$-15s %2$-20s %3$-15s %4$-20s\n", res.getObject(1), res.getObject(4), res.getObject(3), res.getObject(6)); 
					}
			}
			con.close();	
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());
		}
	}
	
	/** Method for the reviewer to insert feedback of an unrated manuscript. The method checks to see if the manuscript has already been
	 * rated before.
	 * @param reviewerID
	 */
	public static void Review_Manuscript (int reviewerID) {
		try{
			Scanner m1 = new Scanner (System.in);
			System.out.println("ENTER THE ID OF THE MANUSCRIPT YOU WANT TO REVIEW : ");
			System.out.println("If you have forgotten the ID, press 0 to return to main menu and CHECK STATUS OF YOUR ASSIGNED MANUSCRIPTS: ");
			int idManuscript = m1.nextInt();
			
			//check to see if this manuscript is part of the reviewer's list of manuscripts
			ArrayList<Integer> mlist = View_Assigned_Manuscripts(reviewerID);
			if (!mlist.contains(idManuscript)) {  //If there is no manuscripts in list
				System.out.println("\nMANUSCRIPT NOT IN THE LIST OF YOUR ASSIGNED MANUSCRIPTS");
			}
			else {
				//check to see if feedback already exists for the manuscript and print it if it does
				get_Manuscript_Feedback(idManuscript, reviewerID);
					Scanner s = new Scanner(System.in);
					System.out.println("\nRATE APPROPRIATENESS: Input number(1=low, 10=high)");
			        int appr = s.nextInt();
					System.out.println("\nRATE CLARITY: Input number (1=low, 10=high)");
					int clar = s.nextInt();
					System.out.println("\nRATE METHODOLOGY: Input number (1=low, 10=high)");
					int met = s.nextInt();
					System.out.println("\nRATE CONTRIBUTION TO FIELD: Input number (1=low, 10=high)");
					int cont = s.nextInt();
					
					System.out.println("HERE IS YOUR SUMMARY OF YOUR RATINGS\n");
					System.out.println("Appropriateness:      "+appr);
					System.out.println("Clarity:              "+clar);
					System.out.println("Methodology:          "+met);
					System.out.println("ContributionToField:  "+cont);
					Insert_Feedback(appr, clar, met, cont, reviewerID, idManuscript);
				}
		}
		catch (Exception ec){
			System.out.println("Error: "+ec.getMessage());
		}finally{
			ReviewerOptions(reviewerID);
		}
	}
	
	/** Method to insert feedback about manuscript
	 * 
	 * @param appr
	 * @param clar
	 * @param met
	 * @param cont
	 * @param reviewerId
	 * @param idManuscript
	 */
	public static void Insert_Feedback (int appr, int clar, int met, int cont, int reviewerId, int idManuscript) {
		
		String recomm ="accept";  //default decision
		String date = dtf.format(now);    // gets the when feedback is made
		Scanner sc = new Scanner (System.in);
		int check =0;  //checks to see if input is correct
		
		while (check==0){
			System.out.println("\nDo you recommend to ACCEPT OR REJECT the manuscript(Please Type 'accept' or 'reject' as an answer)");
			String ans=sc.nextLine();
			ans=ans.toLowerCase();
			if (ans.equals("accept") || ans.equals("reject")){
				recomm=ans;
				check=1;
			}
		}
		try{ //insert feedback
			Connection con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			QUERY = "INSERT INTO Feedback (Appropriateness,Clarity, Methodology,ContributionToField,Recommendation, Date, ReviewerId,Manuscript_idManuscript)"
			+"VALUES(?,?,?,?,?,?,?,?)";  
			PreparedStatement stmt = con.prepareStatement(QUERY);
			stmt.setInt(1,appr);
			stmt.setInt(2, clar);
            stmt.setInt(3,met);
            stmt.setInt(4, cont);
            stmt.setString(5, recomm);
            stmt.setString(6, date);
            stmt.setInt(7, reviewerId);
            stmt.setInt(8, idManuscript);
			stmt.execute();		
			System.out.println("--FEEDBACK SUBMISION SUCCESSFUL--");
		} catch (SQLException e) {
		    System.err.format("SQL Error: %s", e.getMessage());}
	}
	
	/**Program main**/
	public static void main(String[] args) {
		Connection con = null;

		// attempt to connect to db
		try {
			
			// load mysql driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//establish connection
			con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);
			System.out.println("Connection established.\n");
			
			// Start  application 
			startOption();     

		}catch (SQLException e ) {          // catch SQL errors
			System.err.format("SQL Error: %s", e.getMessage());
		}catch (Exception e) {              // anything else
			e.printStackTrace();
		}finally {
	    // cleanup
	    try {
		    con.close();
			System.out.print("\nConnection terminated.\n");
	    } catch (Exception e) { /* ignore cleanup errors */ }
	   }
     }
   }
