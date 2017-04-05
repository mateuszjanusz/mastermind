/* JDBC program created by Mateusz Janusz (mjanu001@gold.ac.uk) 
* 	March 2017
* 	using local server and database due to access declined at igor
*/


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class question1_jdbc {
	static String driver = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://127.0.0.1:8889"; //"jdbc:mysql://igor.gold.ac.uk";
	static String database = "mjanu001_jdbc"; //database name
	static String user = "root";//"mjanu001"
	static String password = "root";//igor mysql password;
	static String table = "marks_table"; //table name
	static Connection db = null;
	static Statement stmt = null;
	static String sql;
	static java.sql.PreparedStatement preparedStmt;
    static BufferedReader in;

	public static void main(String[] args){		
		try {
			// CONNECT TO DATABASE
	        System.out.println("Connecting to database...");
	        Class.forName(driver); //Load the database driver
	        db = DriverManager.getConnection(url + "/" + database, user, password); 
	        // CREATE TABLE
	        createTable(table);
	        // INSERT QUERY 
        	insertFromFile("marks-input.txt", table);
    	 	// WRITE DATA TO FILE
        	writeDataToFile(table, "marks-out.txt");
        	
        	// close statements and connection
	     	stmt.close();
	     	db.close();
	        } catch(SQLException se){
	            //Handle errors for JDBC
	            se.printStackTrace();
	        } catch(Exception e){
	            //Handle errors for Class.forName
	            e.printStackTrace();
	        }
	} 
	
	private static void createTable(String tablename){
		System.out.println("Creating table " + tablename);
		 try {
			stmt = db.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS " + tablename + //use of 'if not exists' to prevent overwrite error
	                   "(id INTEGER not NULL, " +
	                   " first_name VARCHAR(255), " + 
	                   " last_name VARCHAR(255), " + 
	                   " year_of_study INTEGER, " + 
	                   " mark INTEGER, " + 
	                   " PRIMARY KEY ( id ))"; 

			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}     
	}
	
	private static void insertFromFile(String filename, String tablename){
		try{
			System.out.println("Reading from file "+filename);
	        String read=null;
	        in = new BufferedReader(new FileReader(filename));
	        in.readLine(); // this will skip the first line with column titles
	        in.readLine(); // this will skip the second line with dashes
	        while ((read = in.readLine()) != null) {
	            String[] splited = read.split("\\s+"); //use of split to skip all blank spaces between characters
	            String id = splited[0]; 
	            String name = splited[1];
	            String last_name = splited[2];
	            String year = splited[3];
	            String mark = splited[4];	        
	            System.out.println("inserting values for: " + name);
	            sql = "INSERT INTO " + tablename + " VALUES (" +
	            		id + 
	            		"  , '" + name + "'" +
	            		"  , '" + last_name + "'" +
	            		"  , " + year + 
	            		"  , " + mark + ")";
	            stmt.executeUpdate(sql);               
	        }
        }
        catch (IOException e) {
        	e.printStackTrace();
        } catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeDataToFile(String tablename, String filename){
     	System.out.println("Writing data from table "+tablename+" to file "+filename);
     	try { 
     		List<String> data = new ArrayList<>();
     		ResultSet read = stmt.executeQuery("SELECT * FROM "+tablename);
        	while (read.next()) {
	        	int userid = read.getInt("id");
	        	String firstname = read.getString("first_name");
	        	String lastname = read.getString("last_name");
	        	int year = read.getInt("year_of_study");
	        	int mark = read.getInt("mark");
	        	data.add(userid + " " + firstname + " " + lastname + " " + year + " " + mark);
        	}
        	BufferedWriter out = null;
            File file = new File(filename);
            out = new BufferedWriter(new FileWriter(file, true));
            for (Object s : data) {
                 out.write((String) s);
                 out.newLine();
            }
            out.close();
            System.out.println("Done. Data has been written to "+filename);
         } catch (IOException e) {
        	e.printStackTrace();
         } catch (SQLException e) {
 			e.printStackTrace();
 		}
	}
}
