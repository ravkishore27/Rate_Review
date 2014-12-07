package com.mysql.dbproject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class UnRegisteredUserServices {

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

			do {
				System.out.println();
				System.out.println("Press Enter to view options ..... ");
				stdin.nextLine();
				System.out.println("UNRegistered User Services");
				System.out.println("----------------------------------");
				System.out.println("[0] Quit");
				System.out.println("[1] Search a category by type");
				System.out.println("[2] Search category with Name");
				System.out.println("[3] Search category by rating");
				System.out.println("[4] Search category by city");
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
					function1_SearchCategoryByType();
					break;
				case 2: 
					function2_SearchCategoryByName();
					break;
				case 3:
					function3_SearchCategoryByRating();
					break;
				case 4:
					function4_SearchCategoryByCity();
					break;
				default:
					System.out.println("Wrong input. Try again!");
				}
			} while (func != 0);

		} catch(java.util.NoSuchElementException e){
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

	private static void function1_SearchCategoryByType() throws SQLException{
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
						+ "Category_Id = "+ rs.getString("Category_Id") +" and "
						+ "R.Rate_Id > (SELECT MAX(RR.Rate_Id) - 5 FROM RATE AS RR);");
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
	private static void function2_SearchCategoryByName() throws SQLException{
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
							+ "Category_Id = "+ rs.getString("Category_Id") +" and "
							+ "R.Rate_Id > (SELECT MAX(RR.Rate_Id) - 5 FROM RATE AS RR);");
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

	private static void function3_SearchCategoryByRating() throws SQLException {
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
							+ "Category_Id = "+ rs.getString("Category_Id") +" and "
							+ "R.Rate_Id > (SELECT MAX(RR.Rate_Id) - 5 FROM RATE AS RR);");
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

	private static void function4_SearchCategoryByCity() throws SQLException {
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
								+ "Category_Id = "+ rs.getString("Category_Id") +" and "
								+ "R.Rate_Id > (SELECT MAX(RR.Rate_Id) - 5 FROM RATE AS RR);");
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



}
