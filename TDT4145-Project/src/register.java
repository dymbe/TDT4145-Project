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

		
		int oktID = Integer.parseInt(sc.nextLine());
		
		Statement stm = con.createStatement();
		stm.executeUpdate("INSERT INTO øvelse VALUES(DEFAULT, '" + oktID + "','" + name + "','" + prestasjon + "')");
		
		Statement sta = con.createStatement();
		ResultSet result = stmt.executeQuery("select * from treningsøkt where øktID = " + oktID);
		result.next();
		
		System.out.println("Øvelsen " + name + " lagt til i " + result.getString(4));
		
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
