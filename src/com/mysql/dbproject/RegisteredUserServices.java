package com.mysql.dbproject;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;


public class RegisteredUserServices {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/yelp";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "ravikishore";


	static Scanner stdin = new Scanner(System.in);

	static Connection con = null;

	static CallableStatement cs = null;
	

	public static void main(String[] args) {
		int func = -1;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			con = DriverManager.getConnection(DB_URL, USER, PASS);

			try {
				cs = con.prepareCall("{call DropTables()}");
				cs.executeUpdate();
				cs = con.prepareCall("{call CreateTables()}");
				cs.executeUpdate();
				cs = con.prepareCall("{call InsertData()}");
				cs.executeUpdate();
			} catch (SQLException e) {
				System.out.println(e);
			}
			
			do {
				System.out.println();
				System.out.println("Press Enter to view options ..... ");
				stdin.nextLine();
				System.out.println();
				System.out.println("User Services");
				System.out.println("----------------------------------");
				System.out.println("[0] Quit");
				System.out.println("[1] SignUp");
				System.out.println("[2] LogIn");
				System.out.println("[3] Update Personal Info");
				System.out.println("[4] Search a category by type");
				System.out.println("[5] Search category with Name");
				System.out.println("[6] Search category by rating");
				System.out.println("[7] Search category by city");
				System.out.println("[8] Check-In");
				System.out.println("[9] Do Rate_Review");
				System.out.println("[10] Add to Favourites");
				System.out.println("[11] Upload Pictures");
				System.out.println("[12] Search Friend");
				System.out.println("[13] Add Friend");
				System.out.println("[14] See Friendship");
				System.out.println("[15] Compliment");
				System.out.println("----------------------------------");
				System.out.print("Command >> ");
				try{
					func = Integer.parseInt(stdin.nextLine());
				}
				catch(java.lang.NumberFormatException e){
					System.out.println("No Strings Allowed!");
				}
				catch(java.util.InputMismatchException e){
					System.out.println("Input Mismatch!");
					System.out.println("Please try again!");
				}				
				switch (func) {
				case 0:
					System.out.println("Bye~!");
					break;
				case 1:
					function1_signUp();
					break;
				case 2:
					function2_LogIn();
					break;
				case 3:
					function3_UpdatePersonalInfo();
					break;
				case 4:
					function4_SearchCategoryByType();
					break;
				case 5: 
					function5_SearchCategoryByName();
					break;
				case 6:
					function6_SearchCategoryByRating();
					break;
				case 7:
					function7_SearchCategoryByCity();
					break;
				case 8:
					function8_CheckIn();
					break;
				case 9:
					function9_doRate_Review();
					break;
				case 10:
					function10_add_favourites();
					break;
				case 11:
					function11_uploadPicture();
					break;
				case 12:
					function12_searchFriend();
					break;
				case 13:
					function13_addFriend();
					break;
				case 14:
					function14_seeFriendship();
					break;
				case 15:
					function15_compliment();
					break;
				default:
					System.out.println("Wrong input. Try again!");
				}
			} while (func != 0);

		}
		catch(java.util.NoSuchElementException e){
			System.out.println("Bye~!");
		} 
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}
	}

	private static void function1_signUp() throws SQLException {
		String userName,password;
		Statement stmt = con.createStatement();
		String sql;
		PreparedStatement pstmt = null;

		System.out.println("SignUp");
		System.out.print("Enter Username : ");
		userName = stdin.nextLine();
		System.out.print("Password : ");
		password = stdin.nextLine();
		try {
			con.setAutoCommit(false);
			sql = "INSERT INTO USER VALUES(null,'REGISTERED_USER');";
			stmt.execute(sql);

			sql= "Select MAX(User_Id) as U_Id from USER where User_Type = 'REGISTERED_USER' ;";
			ResultSet rs = stmt.executeQuery(sql);

			if(rs.next()){
				int reg_id = rs.getInt("U_Id");

				pstmt = con.prepareStatement("INSERT INTO REGISTERED_USER VALUES(" + reg_id + ",?,?,null,null,null);");
				pstmt.setString(1, userName);
				pstmt.setString(2,password);
				pstmt.executeUpdate();

				System.out.println("Congrats You have been Signed Up!");
			}
			con.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
	}

	private static void function2_LogIn() throws SQLException {
		Statement stmt = con.createStatement();
		String sql;

		ResultSet rs = null;

		String username,password;

		System.out.println("Enter your username");
		username = stdin.nextLine();

		System.out.println("Enter your password");
		password = stdin.nextLine();

		sql = "Select COUNT(*) AS result from REGISTERED_USER where Username = '"+ username + "' and Password = '" + password + "'";
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			if(rs.getInt("result") == 1){
				System.out.println("You have successfully Logged In!");
			}
			else{
				System.out.println("Logged In Failed!");
			}
		}

	}

	private static void function3_UpdatePersonalInfo() throws SQLException {
		int user_id;
		String name, email, address;
		PreparedStatement pstmt = null;
		Statement stmt = con.createStatement();

		System.out.println("Update Personal Info");
		System.out.print("Enter your User Id");
		user_id = Integer.parseInt(stdin.nextLine());
		System.out.print("Enter your name");
		name = stdin.nextLine();
		System.out.print("Enter your email");
		email = stdin.nextLine();
		System.out.println("Enter your address");
		address = stdin.nextLine();

		try {
			con.setAutoCommit(false);

			pstmt = con.prepareStatement("UPDATE REGISTERED_USER SET  name = ? , email = ?, address = ? where User_Id = ? ;" );
			//+ "'" + name + "', email = '" + email + "', address = '"+ address +"'" +
			//" where User_Id = "+ user_id +", " + name + ", " + email + ", " +  address +,NameType(?,?),?,?)");
			pstmt.setString(1,name);
			pstmt.setString(2,email);
			pstmt.setString(3,address);
			pstmt.setInt(4,user_id);
			pstmt.executeUpdate();
			System.out.println("Updated Successfully");
			con.commit();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
	}

	private static void function4_SearchCategoryByType() throws SQLException{
		Statement stmt = null,stmt1 = null ,stmt2 = null;
		ResultSet rs = null;

		try{
			System.out.println("Search by Category Type");

			System.out.println("Enter the type");
			System.out.println("1. RESTAURANT");
			System.out.println("2. SHOPPING");
			System.out.println("3. HOTEL_TRAVEL");
			int category_type = Integer.parseInt(stdin.nextLine());
			stmt = con.createStatement();
			switch(category_type){
			case 1:
				rs = stmt.executeQuery("SELECT * FROM Restaurant_Search");
				break;
			case 2:
				rs = stmt.executeQuery("SELECT * FROM Shopping_Search");
				break;
			case 3:
				rs = stmt.executeQuery("SELECT * FROM Hotel_Travel_Search");
				break;
			default:
				System.out.println("Sorry Wrong Input");
				return;				
			}
			ResultSet rsInner;
			while(rs.next()){
				System.out.println(rs.getString("Category_Name") + "\t" + rs.getString("Address") + "\t" + rs.getString("Contact_Number") +
						"\t" +	rs.getString("Special") + "\t" + rs.getString("Promo_Message")+ "\t" + rs.getString("Expiration_Date")) ;
				stmt1 = con.createStatement();
				rsInner = stmt1.executeQuery("SELECT * FROM PICTURE WHERE Category_Id = " + rs.getString("Category_Id"));
				System.out.println("IMAGES at " + rs.getString("Category_Name"));
				while(rsInner.next()){					
					System.out.println(rsInner.getString("Image"));
				}

				stmt2 = con.createStatement();
				rsInner = stmt2.executeQuery("SELECT RU.Username, R.Rate, RE.Review, R.Date_Time FROM RATE AS R"
						+ " INNER JOIN REVIEW AS RE ON R.Review_Id = RE.Review_Id"
						+ " INNER JOIN REGISTERED_USER AS RU ON RU.User_Id = R.Reg_Id and "
						+ "Category_Id = "+ rs.getString("Category_Id") +";");
				System.out.println("Rate and Reviews");
				while(rsInner.next()){
					System.out.println(rsInner.getString("Username") + "\t" + rsInner.getString("Rate") + "\t" + rsInner.getString("Review") + "\t" +
							rsInner.getString("Date_Time"));
				}
				System.out.println();
				System.out.println();


			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt1 != null)
					stmt1.close();
				if (stmt2 != null)
					stmt2.close();
			} catch (Exception e) {
			}
		}
	}

	@SuppressWarnings("resource")
	private static void function5_SearchCategoryByName() throws SQLException{
		Statement stmt = con.createStatement();
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet rs = null;

		try{
			System.out.println("Search by Category Type");

			System.out.println("Enter the Name");
			String name = stdin.nextLine();

			rs = stmt.executeQuery("SELECT * FROM CATEGORIES WHERE Category_Name = '" + name + "';");
			String category_type = "";
			if(rs.next()){
				category_type = rs.getString("Category_Type");
			}
			else{
				System.out.println("Category Name not found!");
				return;
			}

			if(category_type.equals("RESTAURANT")){
				rs = stmt.executeQuery("SELECT * FROM Restaurant_Search");
			}else if(category_type.equals("SHOPPING")){
				rs = stmt.executeQuery("SELECT * FROM Shopping_Search");
			}else if(category_type.equals("HOTEL_TRAVEL")){
				rs = stmt.executeQuery("SELECT * FROM Hotel_Travel_Search");
			}
			ResultSet rsInner;
			while(rs.next())
			{

				if(rs.getString("Category_Name").equals(name)){
					System.out.println(rs.getString("Category_Name") + "\t" + rs.getString("Address") + "\t" + rs.getString("Contact_Number") +
							rs.getString("Special") + "\t" + rs.getString("Promo_Message")+ "\t" + rs.getString("Expiration_Date")) ;
					stmt1 = con.createStatement();
					rsInner = stmt1.executeQuery("SELECT * FROM PICTURE WHERE Category_Id = " + rs.getString("Category_Id"));
					System.out.println("IMAGES at " + rs.getString("Category_Name"));
					while(rsInner.next()){
						System.out.println(rsInner.getString("Image"));
					}

					stmt2 = con.createStatement();
					rsInner = stmt2.executeQuery("SELECT RU.Username, R.Rate, RE.Review, R.Date_Time FROM RATE AS R"
							+ " INNER JOIN REVIEW AS RE ON R.Review_Id = RE.Review_Id"
							+ " INNER JOIN REGISTERED_USER AS RU ON RU.User_Id = R.Reg_Id and "
							+ "Category_Id = "+ rs.getString("Category_Id") +";");
					while(rsInner.next()){
						System.out.println(rsInner.getString("Username") + "\t" + rsInner.getString("Rate") + "\t" + rsInner.getString("Review") + "\t" +
								rsInner.getString("Date_Time"));
					}
				}
				System.out.println();
				System.out.println();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt1 != null)
					stmt1.close();
				if (stmt2 != null)
					stmt2.close();
			} catch (Exception e) {
			}
		}
	}

	private static void function6_SearchCategoryByRating() throws SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		Statement stmt1 = null;
		Statement stmt2 = null;

		try{
			System.out.println("Search by Rating");

			System.out.println("Enter the Rating desired");
			int rating = Integer.parseInt(stdin.nextLine());

			int i = 1;
			while(i <= 3){
				if(i==1){
					rs = stmt.executeQuery("SELECT * FROM Restaurant_Search");
				}else if(i == 2){
					rs = stmt.executeQuery("SELECT * FROM Shopping_Search");
				}else if (i ==3){
					rs = stmt.executeQuery("SELECT * FROM Hotel_Travel_Search");
				}
				ResultSet rsInner;
				while(rs.next()){

					System.out.println(rs.getString("Category_Name") + "\t" + rs.getString("Address") + "\t" + rs.getString("Contact_Number") +
							rs.getString("Special") + "\t" + rs.getString("Promo_Message")+ "\t" + rs.getString("Expiration_Date")) ;
					stmt1 = con.createStatement();
					rsInner = stmt1.executeQuery("SELECT * FROM PICTURE WHERE Category_Id = " + rs.getString("Category_Id"));
					System.out.println("IMAGES at " + rs.getString("Category_Name"));
					while(rsInner.next()){
						System.out.println(rsInner.getString("Image"));
					}
					stmt2 = con.createStatement();
					rsInner = stmt2.executeQuery("SELECT RU.Username, R.Rate, RE.Review, R.Date_Time FROM RATE AS R"
							+ " INNER JOIN REVIEW AS RE ON R.Review_Id = RE.Review_Id"
							+ " INNER JOIN REGISTERED_USER AS RU ON RU.User_Id = R.Reg_Id and R.Rate >= "+ rating +" and "
							+ "Category_Id = "+ rs.getString("Category_Id") +";");
					while(rsInner.next()){
						System.out.println(rsInner.getString("Username") + "\t" + rsInner.getString("Rate") + "\t" + rsInner.getString("Review") + "\t" +
								rsInner.getString("Date_Time"));
					}
				}
				i++;
				System.out.println();
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt1 != null)
					stmt1.close();
				if (stmt2 != null)
					stmt2.close();
			} catch (Exception e) {
			}
		}
	}

	private static void function7_SearchCategoryByCity() throws SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		Statement stmt1 = null;
		Statement stmt2 = null;

		try{
			System.out.println("Search by City");

			System.out.println("Enter the City desired");
			String city = stdin.nextLine();

			int i = 1;
			while(i <= 3){
				if(i==1){
					rs = stmt.executeQuery("SELECT * FROM Restaurant_Search");
				}else if(i == 2){
					rs = stmt.executeQuery("SELECT * FROM Shopping_Search");
				}else if (i ==3){
					rs = stmt.executeQuery("SELECT * FROM Hotel_Travel_Search");
				}
				ResultSet rsInner;
				while(rs.next()){
					if(rs.getString("Address").equals(city)){
						System.out.println(rs.getString("Category_Name") + "\t" + rs.getString("Address") + "\t" + rs.getString("Contact_Number") +
								rs.getString("Special") + "\t" + rs.getString("Promo_Message")+ "\t" + rs.getString("Expiration_Date")) ;
						stmt1 = con.createStatement();
						rsInner = stmt1.executeQuery("SELECT * FROM PICTURE WHERE Category_Id = " + rs.getString("Category_Id"));
						System.out.println("IMAGES at " + rs.getString("Category_Name"));
						while(rsInner.next()){
							System.out.println(rsInner.getString("Image"));
						}
						stmt2 = con.createStatement();
						rsInner = stmt2.executeQuery("SELECT RU.Username, R.Rate, RE.Review, R.Date_Time FROM RATE AS R"
								+ " INNER JOIN REVIEW AS RE ON R.Review_Id = RE.Review_Id"
								+ " INNER JOIN REGISTERED_USER AS RU ON RU.User_Id = R.Reg_Id and "
								+ "Category_Id = "+ rs.getString("Category_Id") +";");
						while(rsInner.next()){
							System.out.println(rsInner.getString("Username") + "\t" + rsInner.getString("Rate") + "\t" + rsInner.getString("Review") + "\t" +
									rsInner.getString("Date_Time"));
						}
					}
				}
				i++;
				System.out.println();
				System.out.println();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt1 != null)
					stmt1.close();
				if (stmt2 != null)
					stmt2.close();
			} catch (Exception e) {
			}
		}	}




	private static void function8_CheckIn() throws SQLException{
		int user_id, category_id;
		PreparedStatement pstmt = null;

		try{
			con.setAutoCommit(false);

			System.out.println("Enter your User_Id");
			user_id = Integer.parseInt(stdin.nextLine());
			System.out.println("Enter your Category_Id");
			category_id = Integer.parseInt(stdin.nextLine());
			Date d = (new Date());


			pstmt = con.prepareStatement("Insert into CHECKIN VALUES(?,?,?);");
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, category_id);
			pstmt.setDate(3, new java.sql.Date(d.getTime()));
			pstmt.executeUpdate();
			con.commit();
			System.out.println("Successfully checked In!");

		}catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}

	}

	private static void function9_doRate_Review() throws SQLException{
		int user_id, category_id, rate;
		String review;
		PreparedStatement pstmt = null;
		try{

			Statement stmt = con.createStatement();
			ResultSet rs = null;

			con.setAutoCommit(false);

			System.out.println("Rate and Review");
			System.out.println("Enter your user_id");
			user_id = Integer.parseInt(stdin.nextLine());
			System.out.println("Enter the category_id");
			category_id = Integer.parseInt(stdin.nextLine());
			System.out.println("Enter your rating");
			rate = Integer.parseInt(stdin.nextLine());
			System.out.println("Enter your review");
			review = (stdin.nextLine());

			pstmt = con.prepareStatement("INSERT INTO REVIEW VALUES (null,?);");
			pstmt.setString(1, review);
			pstmt.executeUpdate();

			rs = stmt.executeQuery("SELECT MAX(Review_Id) AS Max_Id FROM REVIEW;");
			while(rs.next())
			{
				int review_id = rs.getInt("Max_Id");

				pstmt = con.prepareStatement("INSERT INTO RATE VALUES (null, ? , ? , ? , ? , ?);");
				pstmt.setInt(1, rate);
				pstmt.setInt(2,review_id);
				pstmt.setDate(3, new java.sql.Date((new java.util.Date()).getTime()));
				pstmt.setInt(4, user_id);
				pstmt.setInt(5, category_id);
				pstmt.executeUpdate();

			}
			con.commit();

			System.out.println("You have successfully rate and reviewed the category");
		}catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}
	}

	private static void function10_add_favourites() throws SQLException, IOException, Exception{
		int user_id, category_id;

		PreparedStatement pstmt = null;
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		try{
			con.setAutoCommit(false);

			System.out.println("Add Favourites");
			System.out.println("Enter your user_id");
			user_id = Integer.parseInt(stdin.nextLine());

			System.out.println("Select your Favourite place");
			rs = stmt.executeQuery("SELECT Category_Id,Category_Name FROM CATEGORIES");

			while(rs.next()){
				System.out.println(rs.getInt("Category_Id") + ". " + rs.getString("Category_Name"));
			}

			category_id = Integer.parseInt(stdin.nextLine());

			pstmt = con.prepareStatement("INSERt into FAVOURITES values(?,?)");
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, category_id);
			pstmt.executeUpdate();

			con.commit();
			System.out.println("Favourite successfully added!");
		}catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}

	}

	private static void function11_uploadPicture() throws SQLException, IOException, Exception{
		int user_id, category_id;
		String image_name;

		PreparedStatement pstmt = null;

		try{

			con.setAutoCommit(false);
			System.out.println(" Upload Picture");
			System.out.println("Enter your user_id");
			user_id = Integer.parseInt(stdin.nextLine());
			System.out.println("Enter your Category_id");
			category_id = Integer.parseInt(stdin.nextLine());
			System.out.println("Enter your Image name");
			image_name = stdin.nextLine();

			pstmt = con.prepareStatement("INSERT INTO PICTURE VALUES(null,?,?,?);");
			pstmt.setString(1, image_name);
			pstmt.setInt(2, user_id);
			pstmt.setInt(3, category_id);
			pstmt.executeUpdate();
			con.commit();
			System.out.println("Image inserted successfully!");
		}catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}

	}



	private static void function12_searchFriend() throws SQLException{
		String email;

		PreparedStatement pstmt = null;

		ResultSet rs = null;

		System.out.println("Search Friend");
		System.out.println("Enter your friend email address");
		email = (stdin.nextLine());

		pstmt = con.prepareStatement("SELECT * from REGISTERED_USER WHERE email = ?;");
		pstmt.setString(1, email);
		rs = pstmt.executeQuery();
		if(rs.next())
		{
			System.out.println("User Found!");
			System.out.println("UserName : " + rs.getString("Username"));
			System.out.println("Name : " + rs.getString("Name"));
		} 
		else{
			System.out.println("No User found with this email Address");
		}
	}


	private static void function13_addFriend() throws SQLException {

		int urID, friendID;


		Statement stmt= con.createStatement();

		System.out.println("Glad you are adding friends.");
		System.out.println("We need your ID and your friend's ID \n");
		System.out.println("So please enter \n");

		System.out.println("Your ID: ");
		urID = stdin.nextInt();

		System.out.println("Your Friend's ID: ");
		friendID = stdin.nextInt();

		try{

			con.setAutoCommit(false);

			String insertSQL = "INSERT INTO FRIENDSHIP VALUES (?, ?)";
			PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
			preparedStatement.setInt(1, urID);
			preparedStatement.setInt(2, friendID);
			preparedStatement.executeUpdate();

			con.commit();
			System.out.println("Congratulations your now friends with user id " + friendID);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
	}



	private static void function14_seeFriendship() throws SQLException{
		int user_id, friend_id;

		Statement stmt = con.createStatement();
		Statement stmt1 = con.createStatement();
		Statement stmt2 = con.createStatement();
		ResultSet rsSelf = null;
		ResultSet rsFriend = null;
		ResultSet rsCat = null;

		System.out.println("SEE FRIENDSHIP");
		System.out.println("Enter your user_id");
		user_id = Integer.parseInt(stdin.nextLine());
		System.out.println("Enter your friend_id");
		friend_id = Integer.parseInt(stdin.nextLine());

		rsSelf = stmt.executeQuery("SELECT * FROM FAVOURITES WHERE Reg_User_Id = '" + user_id + "';");
		rsFriend = stmt1.executeQuery("SELECT * FROM FAVOURITES WHERE Reg_User_Id = '" + friend_id + "';");
		System.out.println("The the common favourties both friends share are : ");
		String fav;
		while(rsSelf.next()){
			fav = rsSelf.getString("Favourites");
			while(rsFriend.next()){
				if(fav.equals(rsFriend.getString("Favourites"))){
					rsCat = stmt2.executeQuery("SELECT * FROM CATEGORIES WHERE Category_Id = " + fav +";");
					while(rsCat.next()){
						System.out.println(rsCat.getString("Category_Name"));
					}
				}
			}
		}




	}


	private static void function15_compliment() throws SQLException {

		int urID, friendID;
		String message;

		Statement stmt= con.createStatement();

		System.out.println("Compliments .");
		System.out.println("We need your ID and your friend's ID ");
		System.out.println("So please enter ");

		System.out.println("Your ID: ");
		urID = Integer.parseInt(stdin.nextLine());

		System.out.println("Your Friend's ID: ");
		friendID = Integer.parseInt(stdin.nextLine());
		
		System.out.println("Enter your compliment");
		message = stdin.nextLine();

		try{

			con.setAutoCommit(false);

			String insertSQL = "INSERT INTO COMPLIMENT VALUES (?, ?, ?)";
			PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
			preparedStatement.setInt(1, urID);
			preparedStatement.setInt(2, friendID);
			preparedStatement.setString(3, message);
			preparedStatement.executeUpdate();
			

			con.commit();
			System.out.println("Your friend with " + friendID + " has been complimented");
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
		}
	}
}