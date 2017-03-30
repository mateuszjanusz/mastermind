// JDBC program createdy by Mateusz Janusz (mjanu001@gold.ac.uk)
// March 2017
// using local server and database due to access declined at igor

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
	public static void main(String[] args){
			
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://127.0.0.1:8889"; //"jdbc:mysql://igor.gold.ac.uk";
	        String database = "mjanu001_jdbc";
	        String user = "root";//"mjanu001";
	        String password = "root";//"mysqlmjanu001";
	        String table = "marks";
	        
	        Connection db = null;
	        Statement stmt = null;
	        BufferedReader in;
	        String sql;
	        java.sql.PreparedStatement preparedStmt;
	        
	        try {
	        System.out.println("Connecting to database...");
	        Class.forName(driver); //Load the database driver
	        //db = DriverManager.getConnection("jdbc:mysql://igor.gold.ac.uk/mjanu001_jdbc", "mjanu001", "mysqlmjanu001");  
	        db = DriverManager.getConnection(url + "/" + database, user, password);  
	        
	        System.out.println("Creating statement...");
	        stmt = db.createStatement();
	        
	        // INSERT QUERY

	        try{
	        	System.out.println("Reading from file...");
	            String read=null;
	            in = new BufferedReader(new FileReader("marks-input.txt")); 
	            while ((read = in.readLine()) != null) {
	                String[] splited = read.split("\\s+");
	                String id = splited[0]; 
	                String name = splited[1];
	                String last_name = splited[2];
	                String year = splited[3];
	                String mark = splited[4];
	                
	                System.out.println("inserting values for: " + name);
	                sql = "INSERT INTO " + table + " VALUES (" +
	                		id + 
	                		"  , '" + name + "'" +
	                		"  , '" + last_name + "'" +
	                		"  , " + year + 
	                		"  , " + mark + ")";
	                stmt.executeUpdate(sql);               
	            }
	        }
	        catch (IOException e) {
	        	System.out.println("There was a problem: " + e);
	        	} 

	        //SELECT QUERY
        	System.out.println("Query all data from database");
	        ResultSet rs = stmt.executeQuery("SELECT * FROM marks");

	        while (rs.next()) {
	        	int userid = rs.getInt("id");
	        	String firstname = rs.getString("name");
	        	String lastname = rs.getString("last_name");
	        	int year = rs.getInt("year_of_study");
	        	int mark = rs.getInt("mark");

	        	System.out.println(userid + " " + firstname + " " + lastname + "	" + year + " 	" + mark);
	        }
	        
	        
	        //UPDATE QUERY
        	System.out.println("Update query: update mark for Sofia");
	        sql = "UPDATE " + table + " SET mark = ? WHERE name = ?";
	        preparedStmt = db.prepareStatement(sql);
	        preparedStmt.setInt   (1, 100); //set mark to 100
	        preparedStmt.setString(2, "Sofia"); //set name to Sofia
	        preparedStmt.executeUpdate();
        	System.out.println("Marks for Sofia updated.");
        	
        	//THE LOWEST AND THE HIGHEST MARK
        	System.out.println("Searching for average, the lowest and the highest mark");
        	System.out.println("--Year one--");
        	//average
        	ResultSet a1 = stmt.executeQuery("SELECT avg(mark) AS mark FROM marks WHERE year_of_study=1");
        	a1.last();
        	int av = a1.getInt("mark");
        	System.out.println("Average mark: " + av);
        	//highest
        	ResultSet h1 = stmt.executeQuery("SELECT name, mark FROM marks WHERE mark = (SELECT max(mark) FROM marks WHERE year_of_study=1)");
        	h1.last();
        	int m1 = h1.getInt("mark");
        	String n1 = h1.getString("name");
        	System.out.println("Highest mark: " + n1 + " " + m1);
        	//lowest
        	ResultSet l1 = stmt.executeQuery("SELECT name, mark FROM marks WHERE mark = (SELECT min(mark) FROM marks WHERE year_of_study=1)");
        	l1.last();
        	int lm1 = l1.getInt("mark");
        	String ln1 = l1.getString("name");
        	System.out.println("Lowest mark: " + ln1 + " " + lm1);
        	
        	System.out.println("--Year two--");
        	ResultSet a2 = stmt.executeQuery("SELECT AVG(mark) AS mark FROM marks WHERE year_of_study=2");
        	a2.last();
        	int av2 = a2.getInt("mark");
        	System.out.println("Average mark: " + av2);
        	ResultSet h2 = stmt.executeQuery("SELECT name, mark FROM marks WHERE mark = (SELECT max(mark) FROM marks WHERE year_of_study=2)");
        	h2.last();
        	int m2 = h2.getInt("mark");
        	String n2 = h2.getString("name");
        	System.out.println("Highest mark: " + n2 + " " + m2);
        	ResultSet l2 = stmt.executeQuery("SELECT name, mark FROM marks WHERE mark = (SELECT min(mark) FROM marks WHERE year_of_study=2)");
        	l2.last();
        	int lm2 = l2.getInt("mark");
        	String ln2 = l2.getString("name");
        	System.out.println("Lowest mark: " + ln2 + " " + lm2);
        	
        	System.out.println("--Year three--");
        	ResultSet a3 = stmt.executeQuery("SELECT AVG(mark) AS mark FROM marks WHERE year_of_study=3");
        	a3.last();
        	int av3 = a3.getInt("mark");
        	System.out.println("Average mark: " + av3);
        	ResultSet h3 = stmt.executeQuery("SELECT name, mark FROM marks WHERE mark = (SELECT max(mark) FROM marks WHERE year_of_study=3)");
        	h3.last();
        	int m3 = h3.getInt("mark");
        	String n3 = h3.getString("name");
        	System.out.println("Highest mark: " + n3 + " " + m3);
        	ResultSet l3 = stmt.executeQuery("SELECT name, mark FROM marks WHERE mark = (SELECT min(mark) FROM marks WHERE year_of_study=3)");
        	l3.last();
        	int lm3 = l3.getInt("mark");
        	String ln3 = l3.getString("name");
        	System.out.println("Lowest mark: " + ln3 + " " + lm3);
        	
        	//WRITE DATA TO FILE
        	System.out.println("Writing data from database to file output.txt");
            List<String> data = new ArrayList<>();
            ResultSet read = stmt.executeQuery("SELECT * FROM marks");
  
            while (read.next()) {
	        	int userid = read.getInt("id");
	        	String firstname = read.getString("name");
	        	String lastname = read.getString("last_name");
	        	int year = read.getInt("year_of_study");
	        	int mark = read.getInt("mark");
	        	data.add(userid + " " + firstname + " " + lastname + " " + year + " " + mark);
            }
            BufferedWriter out = null;
            try {
                    File file = new File("output.txt");
                    out = new BufferedWriter(new FileWriter(file, true));
                    for (Object s : data) {
                            out.write((String) s);
                            out.newLine();

                    }
                    out.close();
                	System.out.println("Output ready.");

            } catch (IOException e) {
            	System.out.println("Something went wrong: " + e);
            }
        	
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
}
