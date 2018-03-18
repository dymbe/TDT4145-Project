
import java.sql.*;

public class Nummer5 {


	
	public static void getMostUsedApparat() throws Exception{
		String url ="jdbc:mysql://localhost:3306/tdt4145?useSSL=false";
		String uname ="root";
		String pass="root";
		String query ="SELECT apparat.navn, count(apparat.apparatid) as A FROM Apparat INNER JOIN fastmontert on fastmontert.ApparatID=apparat.ApparatID group by apparat.navn order by a desc limit 1";
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,uname,pass);
		
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		if(rs.next()){
			System.out.println("Apparat: "+rs.getString("navn"));
			System.out.println("Brukt "+rs.getInt("A")+" ganger");
		}
		
		st.close();
		con.close();
	}
	
	public static void main(String[] args) throws Exception {
		getMostUsedApparat();
	}
}
