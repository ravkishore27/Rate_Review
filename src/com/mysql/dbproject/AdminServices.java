package com.mysql.dbproject;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Statement;

public class AdminServices {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/yelp";

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
				
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO USER VALUES(1,'ADMIN');");
				pstmt.executeUpdate();
				
				pstmt = con.prepareStatement("INSERT INTO ADMIN VALUES(1,'admin','password','Admin','admin@gmail.com');");
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				System.out.println(e);
			}

			do {
				System.out.println();
				System.out.println("Press Enter to view options ..... ");
				stdin.nextLine();
				System.out.println();
				System.out.println("ADMIN SERVICES");
				System.out.println("----------------------------------");
				System.out.println("[0] Quit");
				System.out.println("[1] Add Category");
				System.out.println("[2] Delete a Category");
				System.out.println("[3] Update a Category");
				System.out.println("[4] Add Promotion");
				System.out.println("[5] Update Promotion");
				System.out.println("[6] List all Promotions");
				System.out.println("[7] Remove Promotion");
				System.out.println("[8] Admin Login");
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
					System.out.println("See You soon!!");
					break;
				case 1:
					function1_add_category();
					break;
				case 2:
					function2_delete_category();
					break;
				case 3:
					function3_update_category();
					break;
				case 4:
					function4_add_promotion();
					break;
				case 5:
					function5_update_promotion();
					break;
				case 6:
					function6_list_promotions();
					break;
				case 7:
					function7_remove_promotion();
					break;
				case 8:
					function8_admin_login();
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


	private static void function1_add_category() throws SQLException {
		String contactNo;
		int category_id = 0;
		String category_name, address, special_attribute;
		int category_type;
		Statement stmt =  con.createStatement();
		ResultSet rs = null;
		String operation="";
		try {
			con.setAutoCommit(false);
			System.out.println("Add a new Category");

			System.out.println("Enter the Category Name :");
			category_name = stdin.nextLine();

			System.out.println("Select Category Type : ");
			System.out.println("1. RESTAURANT");
			System.out.println("2. SHOPPING");
			System.out.println("3. HOTEL_TRAVEL");
			category_type = Integer.parseInt(stdin.nextLine());

			System.out.print("Address : ");
			address = stdin.nextLine();

			System.out.print("Contact Number : ");
			contactNo = stdin.nextLine();

			String proc = "{call categories_insert(?, ?, ?, ?)}";

			cs = con.prepareCall(proc);
			cs.setString(1, category_name);	
			cs.setInt(2, category_type);
			cs.setString(3, address);
			cs.setString(4, contactNo);
			cs.executeUpdate();

			rs = stmt.executeQuery("SELECT MAX(Category_Id) AS M_ID FROM CATEGORIES");
			while(rs.next()){
				category_id = rs.getInt("M_ID");
			}

			switch(category_type){
			case 1:

				System.out.println("Enter the Cuisine ");
				special_attribute = stdin.nextLine();
				operation = "restaurant_insert";


				break;
			case 2:

				System.out.println("Enter the working Hours");
				special_attribute = stdin.nextLine();
				operation = "shopping_insert";


				break;
			case 3:
				System.out.println("Enter the Star");
				special_attribute = stdin.nextLine();
				operation = "hotel_travel_insert";


				break;
			default:
				System.out.println("Sorry wrong input");
				return;
			}			
			proc = "{call subcategories_insert(?, ?, ?)}";
			cs = con.prepareCall(proc);
			cs.setString(1, operation );
			cs.setInt(2, category_id);
			cs.setString(3, special_attribute);
			cs.executeUpdate();
			con.commit();


		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
				stmt.close();
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

	private static void function2_delete_category() {
		int Category_Id=0;
		System.out.println("Delete Category");
		System.out.print("Category ID : ");
		Category_Id = stdin.nextInt();
		stdin.nextLine();


		try {
			con.setAutoCommit(false);

			String proc = "{call categories_delete(?)}";

			cs = con.prepareCall(proc);
			cs.setInt(1, Category_Id);
			//cs.registerOutParameter(6, java.sql.Types.INTEGER);
			cs.executeUpdate();

			con.commit();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
				} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}
	}


	private static void function3_update_category() throws SQLException {
		long contactNo;
		int category_id = 0;
		String category_name, address, special_attribute;
		int category_type= 0;
		Statement stmt =  con.createStatement();
		ResultSet rs = null;
		String operation="";

		System.out.println("Update a new Category");

		System.out.println("Enter the Category Id :");
		category_id=Integer.parseInt(stdin.nextLine());

		System.out.println("Enter the Category Name :");
		category_name = stdin.nextLine();

		System.out.print("Address : ");
		address = stdin.nextLine();

		System.out.print("Contact Number : ");
		contactNo = Integer.parseInt(stdin.nextLine());



		try {
			con.setAutoCommit(false);

			rs = stmt.executeQuery("SELECT Category_Type AS C_T FROM CATEGORIES WHERE Category_Id = " + category_id);
			while(rs.next()){
				String str =  rs.getString("C_T");
				if (str.equals("RESTAURANT"))
				{
					category_type = 1;
				}else if(str.equals("SHOPPING")){
					category_type = 2;
				}else if(str.equals("HOTEL_TRAVEL")){
					category_type = 3;					
				}
			}
			String proc;

			switch(category_type){
			case 1:

				System.out.println("Enter the Cuisine ");
				special_attribute = stdin.nextLine();
				operation = "restaurant_insert";


				break;
			case 2:

				System.out.println("Enter the working Hours");
				special_attribute = stdin.nextLine();
				operation = "shopping_insert";


				break;
			case 3:
				System.out.println("Enter the Star");
				special_attribute = stdin.nextLine();
				operation = "hotel_travel_insert";


				break;
			default:
				System.out.println("Sorry wrong input");
				return;
			}			
			proc = "{call categories_update(?, ?, ?, ?, ?, ?)}";
			cs = con.prepareCall(proc);
			cs.setInt(1, category_id);
			cs.setString(2, category_name);
			cs.setString(3, address);
			cs.setLong(4, contactNo);
			cs.setString(5, operation );
			cs.setString(6, special_attribute);

			cs.executeUpdate();
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


	private static void function4_add_promotion() {
		int categoryid;
		String promo_msg,expiration_date;
		PreparedStatement pstmt = null;

		System.out.println("enter the promotion message:");
		promo_msg=stdin.nextLine();
		System.out.println("enter expiration date:");
		expiration_date=stdin.nextLine();        
		System.out.println("Enter the category id:");
		categoryid=stdin.nextInt();
		System.out.println("enter the admin id : ");
		int admin_id = stdin.nextInt();

		try {
			con.setAutoCommit(false);

			System.out.println();
			System.out.println("[PROMOTION TABLE]");


			pstmt = con.prepareStatement("INSERT INTO PROMOTION VALUES(null,?,?,?,?)");
			pstmt.setString(1, promo_msg);
			pstmt.setString(2,expiration_date);

			pstmt.setInt(3,admin_id);
			pstmt.setInt(4,categoryid);
			pstmt.executeUpdate();

			con.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		} 
	}

	public static void function5_update_promotion(){
		int promotion_id;
		int categoryid;
		String promo_msg,expiration_date;
		PreparedStatement pstmt = null;

		System.out.println("Enter the promotion id");
		promotion_id=Integer.parseInt(stdin.nextLine());
		System.out.println("enter new promotion message:");
		promo_msg=stdin.nextLine();
		System.out.println("enter new expiration date:");
		expiration_date=stdin.nextLine();        
		System.out.println("Enter new category id:");
		categoryid=Integer.parseInt(stdin.nextLine());
		System.out.println("enter the admin id : ");
		int admin_id = Integer.parseInt(stdin.nextLine());

		try {
			con.setAutoCommit(false);

			System.out.println();
			System.out.println("[PROMOTION TABLE]");


			pstmt = con.prepareStatement("UPDATE PROMOTION SET Promo_Message=?,Expiration_Date=?,Admin_Id=?,Category_Id=? where Promo_Id = ?");
			pstmt.setString(1,promo_msg);
			pstmt.setString(2,expiration_date);
			pstmt.setInt(3,admin_id);
			pstmt.setInt(4,categoryid);
			pstmt.setInt(5,promotion_id);
			pstmt.executeUpdate();

			con.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}	
	}
	public static void function6_list_promotions(){
		int promo_id;
		String category_name,admin_name;
		String promo_msg,expiration_date;
		try {
			java.sql.Statement stmt = con.createStatement();		
			ResultSet rs = null;
			con.setAutoCommit(false);

			System.out.println();
			System.out.println("[PROMOTION TABLE]");
			rs=stmt.executeQuery("SELECT P.*, AD.Name, C.Category_Name FROM PROMOTION AS P "
					+ "INNER JOIN ADMIN AS AD on P.Admin_Id = AD.Admin_Id "
					+ "INNER JOIN CATEGORIES C ON P.Category_Id = C.Category_Id  "
					+ "ORDER BY Admin_Id ;");
			while(rs.next()){
				promo_id =rs.getInt("Promo_Id");	
				promo_msg=rs.getString("Promo_Message");
				expiration_date=rs.getString("Expiration_Date");

				admin_name=rs.getString("Name");
				category_name=rs.getString("Category_Name");
				System.out.printf("%-4d   %-20s   %-5s   %-10s  %-10s %n",promo_id,promo_msg,expiration_date,admin_name,category_name);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}
	} 

	public static void function7_remove_promotion(){
		int promotion_id;

		PreparedStatement pstmt = null;

		System.out.println("Enter the promotion id");
		promotion_id=Integer.parseInt(stdin.nextLine());


		try {
			con.setAutoCommit(false);

			System.out.println();
			System.out.println("[DELETING FROM PROMOTION TABLE]");


			pstmt = con.prepareStatement("DELETE FROM PROMOTION WHERE Promo_Id = ?");
			pstmt.setInt(1,promotion_id);;

			pstmt.executeUpdate();

			con.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				con.rollback();
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
		}	
	}

	public static void function8_admin_login() throws SQLException{
		Statement stmt = con.createStatement();
		String sql;
		ResultSet rs = null;

		String username,password;

		System.out.println("Enter your username");
		username = stdin.nextLine();

		System.out.println("Enter your password");
		password = stdin.nextLine();

		sql = "Select COUNT(*) AS result from ADMIN where Username = '"+ username + "' and Password = '" + password + "'";
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
}
