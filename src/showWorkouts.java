import java.sql.*;

public class showWorkouts {
	private int nWork;
	private Connection con;
	private String usrName = "root";
	private String pasWs = "**";
	
	public showWorkouts(int last){
		System.out.println("Now running");
		Connect(last);
		
	}
	public void Connect(int n) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TDT4145?useSSL=false", usrName, pasWs);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select * from treningsøkt order by tidspunkt desc limit "+ n);
				while (rs.next())
					System.out.println(rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4));
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		
	}

}
