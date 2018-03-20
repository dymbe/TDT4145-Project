
import java.sql.*;

public class Nummer4 {
	public static void insertGroup(int id, String navn) throws Exception{
		String url ="jdbc:mysql://localhost:3306/tdt4145?useSSL=false";
		String uname ="root";
		String pass="root";
		String update ="INSERT INTO Gruppe VALUES ("+id+", '"+navn+"')";
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,uname,pass);
		System.out.println("komplett");
		
		Statement st = con.createStatement();
		st.executeUpdate(update);
		System.out.println("komplett");

		st.close();
		con.close();
	}
	
	public static void getExerciesThatAreInASpecificGroup(String gruppenavn) throws Exception{
		String url ="jdbc:mysql://localhost:3306/tdt4145?useSSL=false";
		String uname ="root";
		String pass="root";
		String query ="select distinct * from (Gruppe  INNER JOIN �velseGruppe ON Gruppe.GruppeID=�velseGruppe.GruppeID) INNER JOIN �velse ON �velseGruppe.�velseID=�velse.�velseID WHERE Gruppe.gruppenavn='"
				+gruppenavn+"';";
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,uname,pass);
		
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
			System.out.println(rs.getString("navn"));
		}
		
		st.close();
		con.close();
	}
	
	public static void main(String[] args) throws Exception {
		//insertGroup(4,"Styrke");
		//getExerciesThatAreInASpecificGroup("Overkropp");
	}
}
