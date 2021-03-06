import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class register {
	private int nWork;
	private Connection con;
	private String usrName = "root";
	private String pasWs = "Fotball12";
	private Scanner sc;
	private boolean running = true;

	public register() throws Exception{
		System.out.println("Now running");
		sc = new Scanner(System.in);
		setupConnection();
		
		
		while(running){
			System.out.println("1: Registrere �velse");
			System.out.println("2: Registrere �kt");
			System.out.println("3: Registrere apparat");
			System.out.println("3: Lage ny gruppe");
			System.out.println("4: Hent ut �velser fra gruppe");
			System.out.println("5: Hent mest brukte apparat");
			System.out.println("10: Avslutt");
			int valg = Integer.parseInt(sc.nextLine());
		if(valg == 1){
			registerOvelse();
		}else if(valg == 2){
			registerOkt();
		}else if(valg == 3){
			registerApparat();
		}else if(valg == 4){
			showWorkouts();
		}else if(valg==5){
			
		}
		else if(valg == 5){
			System.err.println("Avslutter...");
			sc.close();
			con.close();
		}
		else{
			System.out.println("Prøv igen, smarten");
		}
		
		}
		
		
	}
	
	

	private void registerOvelse() throws Exception {
		System.out.println("Navn p� �velse");
		String name = sc.nextLine();
		System.out.println("Legg in prestasjon til �velse");
		int prestasjon = Integer.parseInt(sc.nextLine());
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from trenings�kt");
		while (rs.next()){
			System.out.println("ID: " + rs.getString(1) + "  " + rs.getString(4));
		}
		System.err.println("Dersom du �nsker � legge til en ny �kt m� du lage dette f�rst!");
		System.out.println("Skriv inn ID p� den �kten du �nsker � legge �velsen til");
		
		//Finner hva øvelse ID kommer til � v�re:
		Statement idQst = con.createStatement();
		ResultSet rst = idQst.executeQuery("SELECT �velseID from �velse ORDER BY �velseID DESC LIMIT 1");
		rst.next();
		int ovelseID = Integer.parseInt(rst.getString(1)) + 1;
		
		
		int oktID = Integer.parseInt(sc.nextLine());
		
		
		Statement stm = con.createStatement();
		stm.executeUpdate("INSERT INTO �velse VALUES(" + ovelseID + ", '" + oktID + "','" + name + "','" + prestasjon + "')");
		
		Statement sta = con.createStatement();
		ResultSet result = stmt.executeQuery("select * from trenings�kt where �ktID = " + oktID);
		result.next();
		
		System.out.println("�velsen " + name + " lagt til i " + result.getString(4));
		
	
		System.out.println("Er denne �velsen fastmontert? Svar: y/n");
		String ans = sc.nextLine();
		if(ans.equals("y")){
			System.out.println("Hvor mange kilo p� denne?");
			int kilo = Integer.valueOf(sc.nextLine());
			System.out.println("Hvor mange sett p� denne?");
			int sett = Integer.valueOf(sc.nextLine());
			
			Statement stmAp = con.createStatement();
			ResultSet rsAp = stmAp.executeQuery("select ApparatID, Navn from apparat");
			while (rsAp.next()){
				System.out.println("ID: " + rsAp.getString(1) + " " + rsAp.getString(2));
			}
			System.out.println("Hvilket apparat brukes. Skriv inn ID");
			int ApparatID = Integer.valueOf(sc.nextLine());
			
			Statement fastmontert = con.createStatement();
			fastmontert.executeUpdate("INSERT INTO fastmontert VALUES (DEFAULT,'" + ovelseID + "','" + kilo + "','" + sett + "','" + ApparatID + "')");
			
			
		}else if(ans.equals("n")){
			System.out.println("Dette er da en fri�velse");
			System.out.println("Hva er beskrivelsen av denne fri�velsen?");
			String besc = sc.nextLine();
			
			Statement friOvelse = con.createStatement();
			friOvelse.executeUpdate("INSERT INTO fri�velse VALUES (DEFAULT,'" + besc + "','" + ovelseID  + "')");

			System.out.println("Fri�velse satt inn");
				
		}else{
			System.out.println("�velsen m� enten var fri eller fastmontert");
		}
		
		
		
		
	
	}
	
	private void registerOkt() throws Exception {
		System.out.println("Tidspunkt p� formen ����-MM-DD TT:MM:SS");
		String DateTime = sc.nextLine();
		System.out.println("Skriv inn varighet");
		int length = Integer.parseInt(sc.nextLine());
		System.out.println("Skriv inn en notat p� treningen");
		String note = sc.nextLine();

		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO trenings�kt VALUES (DEFAULT, '" + DateTime + "','" + length + "','" + note + "')");

		System.out.println("Satt inn ");
		
	}
	public void showWorkouts() throws Exception {
			System.out.println("Hvor mange �kter vil du se?");
			int n = Integer.valueOf(sc.nextLine());
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from trenings�kt order by tidspunkt desc limit "+ n);
			while (rs.next())
				System.out.println(rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4));
			
}
	
	

	private void setupConnection() throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TDT4145?useSSL=false", usrName, pasWs);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void registerApparat() throws Exception {
		System.out.println("Skriv inn navn");
		String name = sc.nextLine();
		System.out.println("Skriv inn beskrivelse");
		String besc = sc.nextLine();

		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Apparat VALUES (DEFAULT, '" + name + "','" + besc + "')");

		System.out.println("Satt inn " + name + " inn i apparat");

	}
	
	public static void getExercisesThatAreInASpecificGroup() throws Exception{
		String gruppenavn=sc.nextLine();
		String query ="select distinct * from (Gruppe  INNER JOIN �velseGruppe ON Gruppe.GruppeID=�velseGruppe.GruppeID) INNER JOIN �velse ON �velseGruppe.�velseID=�velse.�velseID WHERE Gruppe.gruppenavn='"
				+gruppenavn+"';";
		
		
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
			System.out.println(rs.getString("navn"));
		}
		
		st.close();
		con.close();
	}
	
	public static void main(String[] args) {
		System.out.println("sdf");
	}

}
