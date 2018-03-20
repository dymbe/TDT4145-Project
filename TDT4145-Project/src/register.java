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
			System.out.println("1: Registrere øvelse");
			System.out.println("2: Registrere økt");
			System.out.println("3: Registrere apparat");
			System.out.println("4: Avslutt");
			int valg = Integer.parseInt(sc.nextLine());
		if(valg == 1){
			registerOvelse();
		}else if(valg == 2){
			registerOkt();
		}else if(valg == 3){
			registerApparat();
		}else if(valg == 4){
			showWorkouts();
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
		System.out.println("Navn på øvelse");
		String name = sc.nextLine();
		System.out.println("Legg in prestasjon til øvelse");
		int prestasjon = Integer.parseInt(sc.nextLine());
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from treningsøkt");
		while (rs.next()){
			System.out.println("ID: " + rs.getString(1) + "  " + rs.getString(4));
		}
		System.err.println("Dersom du ønsker å legge til en ny økt må du lage dette først!");
		System.out.println("Skriv inn ID på den økten du ønker å legge økten til");
		
		//Finner hva øvelse ID kommer til å være:
		Statement idQst = con.createStatement();
		ResultSet rst = idQst.executeQuery("SELECT ØvelseID from øvelse ORDER BY ØvelseID DESC LIMIT 1");
		rst.next();
		int ovelseID = Integer.parseInt(rst.getString(1)) + 1;
		
		
		int oktID = Integer.parseInt(sc.nextLine());
		
		
		Statement stm = con.createStatement();
		stm.executeUpdate("INSERT INTO øvelse VALUES(" + ovelseID + ", '" + oktID + "','" + name + "','" + prestasjon + "')");
		
		Statement sta = con.createStatement();
		ResultSet result = stmt.executeQuery("select * from treningsøkt where øktID = " + oktID);
		result.next();
		
		System.out.println("Øvelsen " + name + " lagt til i " + result.getString(4));
		
	
		System.out.println("Er denne øvelsen fastmontert? Svar: y/n");
		String ans = sc.nextLine();
		if(ans.equals("y")){
			System.out.println("Hvor mange kilo på denne?");
			int kilo = Integer.valueOf(sc.nextLine());
			System.out.println("Hvor mange sett på denne?");
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
			System.out.println("Dette er da er friøvelse");
			System.out.println("Hva er beskrivelsen av denne friøvelsen?");
			String besc = sc.nextLine();
			
			Statement friOvelse = con.createStatement();
			friOvelse.executeUpdate("INSERT INTO friøvelse VALUES (DEFAULT,'" + besc + "','" + ovelseID  + "')");

			System.out.println("Friøvelse satt inn");
				
		}else{
			System.out.println("Øvelsen må enten var fri eller fastmontert");
		}
		
		
		
		
	
	}
	
	private void registerOkt() throws Exception {
		System.out.println("Tidspunkt på formen ÅÅÅÅ-MM-DD TT:MM:SS");
		String DateTime = sc.nextLine();
		System.out.println("Skriv inn varighet");
		int length = Integer.parseInt(sc.nextLine());
		System.out.println("Skriv inn en notat på treningen");
		String note = sc.nextLine();

		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO treningsøkt VALUES (DEFAULT, '" + DateTime + "','" + length + "','" + note + "')");

		System.out.println("Satt inn ");
		
	}
	public void showWorkouts() throws Exception {
			System.out.println("Hvor mange økter vil du se?");
			int n = Integer.valueOf(sc.nextLine());
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from treningsøkt order by tidspunkt desc limit "+ n);
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

}
