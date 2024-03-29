import java.sql.*;  
import oracle.jdbc.driver.*;

class TestCases {
    public static void main(final String[] args) {
		int testCaseResult = 0; // 0 = Passed -1 = Failed
        try {
            // load Oracle JDBC-driver
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
            // open connect to DB
            Connection con=DriverManager.getConnection(  
                                  "jdbc:oracle:thin:@91.219.60.189:1521/XEPDB1",
				  args[0], // get login as 1th command line parameter
				  args[1]); // get password as 2nd command line parameter 
            // create template string with PL/SQL-function "add_user"
	    CallableStatement cstmt1 = con.prepareCall("{? = call add_user(?,?)}");
	    cstmt1.registerOutParameter(1,Types.NUMERIC);		
	    System.out.print("TC1 ( user 'user1' is not exists; add_user('user1','a12A345678#') = 1 ) := ");
            Statement cstmt2 = con.createStatement();
			cstmt2.executeUpdate("DELETE FROM Users WHERE uname = upper('user1')");
            // init template variables
	    cstmt1.setString(2, "user1");
	    cstmt1.setString(3, "a12A345678#");
	    // execute query
	    cstmt1.executeUpdate();
	    // analize TestCase-result
            if (cstmt1.getInt(1) == 1) System.out.println("Passed");
	    else { 
	        System.out.println("Failed");
	   	testCaseResult = -1;
	    }
    	    System.out.print("TC2 ( user 'user1' is exists; add_user('user1','a12A345678#') = -1 ) := ");
            // init template variables
	    cstmt1.setString(2, "user1");
	    cstmt1.setString(3, "a12A345678#");
	    cstmt1.executeUpdate();
            if (cstmt1.getInt(1) == -1) System.out.println("Passed");
	    else { 
	        System.out.println("Failed");
		testCaseResult = -1;
	    }
   	    // close connect 
            con.close();  
        }
	catch(Exception e) { System.out.println(e); }  
	System.exit(testCaseResult);
    }    
}  
