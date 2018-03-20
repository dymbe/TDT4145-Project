import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class register {
	private int nWork;
	private static Connection con;
	private static String usrName = "root";
	private static String pasWs = "root";
	private static Scanner sc;
	private static boolean running = true;

	public static void run() throws Exception{
		System.out.println("Now running");
		sc = new Scanner(System.in);
		setupConnection();
		
		
		while(running){
			System.out.println("1: Registrere øvelse");
			System.out.println("2: Registrere økt");
			System.out.println("3: Registrere apparat");
			System.out.println("4: Vis siste økter");
			System.out.println("5: Hent ut øvelser fra gruppe");
			System.out.println("6: Lag ny gruppe");
			System.out.println("7: Hent ut mest brukte apparat");
			System.out.println("8: Hent ut økter mellom et tidsintervall");
			System.out.println("9: Avslutt");
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
			getExercisesThatAreInASpecificGroup();
		}else if(valg==6){
			insertGroup();
		}else if(valg==7){
			getMostUsedApparat();
		}else if(valg==8){
			runNummer3("tdt4145","root","root",sc);
		}
		else if(valg == 9){
			System.err.println("Avslutter...");
			sc.close();
			con.close();
		}
		else{
			System.out.println("PrÃ¸v igen, smarten");
		}
		
		}
		
		
	}
	
	

	private static void registerOvelse() throws Exception {
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
		System.out.println("Skriv inn ID på den Økten du ønsker å legge øvelsen til");
		
		//Finner hva Øvelse ID kommer til å være:
		Statement idQst = con.createStatement();
		ResultSet rst = idQst.executeQuery("SELECT ØvelseID from Øvelse ORDER BY ØvelseID DESC LIMIT 1");
		rst.next();
		int ovelseID = Integer.parseInt(rst.getString(1)) + 1;
		
		
		int oktID = Integer.parseInt(sc.nextLine());
		
		
		Statement stm = con.createStatement();
		stm.executeUpdate("INSERT INTO øvelse VALUES(" + ovelseID + ", '" + oktID + "','" + name + "','" + prestasjon + "')");
		
		Statement sta = con.createStatement();
		ResultSet result = stmt.executeQuery("select * from treningsøkt where ØktID = " + oktID);
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
			System.out.println("Dette er da en friøvelse");
			System.out.println("Hva er beskrivelsen av denne friøvelsen?");
			String besc = sc.nextLine();
			
			Statement friOvelse = con.createStatement();
			friOvelse.executeUpdate("INSERT INTO friøvelse VALUES (DEFAULT,'" + besc + "','" + ovelseID  + "')");

			System.out.println("Friøvelse satt inn");
				
		}else{
			System.out.println("øvelsen må enten var fri eller fastmontert");
		}
		
		
		
		
	
	}
	
	private static void registerOkt() throws Exception {
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
	public static void showWorkouts() throws Exception {
			System.out.println("Hvor mange økter vil du se?");
			int n = Integer.valueOf(sc.nextLine());
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from treningsøkt order by tidspunkt desc limit "+ n);
			while (rs.next())
				System.out.println(rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4));
			
}
	
	

	private static void setupConnection() throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TDT4145?useSSL=false", usrName, pasWs);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void registerApparat() throws Exception {
		System.out.println("Skriv inn navn");
		String name = sc.nextLine();
		System.out.println("Skriv inn beskrivelse");
		String besc = sc.nextLine();

		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO Apparat VALUES (DEFAULT, '" + name + "','" + besc + "')");

		System.out.println("Satt inn " + name + " inn i apparat");

	}
	
	public static void getExercisesThatAreInASpecificGroup() throws Exception{
		System.out.println("Skriv inn gruppenavn:");
		String gruppenavn=sc.nextLine();
		String query ="select distinct * from (Gruppe  INNER JOIN øvelseGruppe ON Gruppe.GruppeID=øvelseGruppe.GruppeID) INNER JOIN øvelse ON øvelseGruppe.øvelseID=øvelse.øvelseID WHERE Gruppe.gruppenavn='"
				+gruppenavn+"';";
		
		
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
			System.out.println(rs.getString("navn"));
		}
		System.out.println('\n');
		
	}
	
	public static void insertGroup() throws Exception{
		System.out.println("Skriv inn navn på gruppen: ");
		String navn=sc.nextLine();
		String update ="INSERT INTO Gruppe VALUES (DEFAULT, '"+navn+"')";
		
		Class.forName("com.mysql.jdbc.Driver");
		
		Statement st = con.createStatement();
		st.executeUpdate(update);
		System.out.println("Gruppen ble opprettet"+'\n');

	}
	
	public static void getMostUsedApparat() throws Exception{

		String query ="SELECT apparat.navn, count(apparat.apparatid) as A FROM Apparat INNER JOIN fastmontert on fastmontert.ApparatID=apparat.ApparatID group by apparat.navn order by a desc limit 1";
				
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		if(rs.next()){
			System.out.println("Apparat: "+rs.getString("navn"));
			System.out.println("Brukt "+rs.getInt("A")+" ganger"+'\n');
		}
		
	}
	
	
	
    static void printSessionsBetween(String dbName, String user, String password, String start, String end) {
        MySqlConnection mySql = new MySqlConnection(dbName, user, password);
        try {
            String query = "SELECT Navn, Prestasjon, Tidspunkt FROM Øvelse JOIN Treningsøkt ON Treningsøkt.ØktID = Øvelse.ØktID " +
                    String.format("WHERE Tidspunkt >= '%s' AND Tidspunkt <= '%s';", start, end);
            ResultSet resultSet = mySql.executeQuery(query);
            StringBuilder string = new StringBuilder();
            string.append(String.format("\nDine treningsøkter mellom %s og %s\n\n", start, end));
            boolean fail = true;
            while (resultSet.next()) {
                fail = false;
                string.append("Navn: ").append(resultSet.getString(1)).append(", ");
                string.append("Prestasjon: ").append(resultSet.getString(2)).append(", ");
                string.append("Tidspunkt: ").append(resultSet.getString(3)).append("\n");
            }
            if (fail) {
                System.out.println("\nIngen treningsøkter i det tidsrommet\n");
            } else {
                System.out.println(string);
            }
        } catch (SQLException e) {
            System.out.println("\nNoe gikk galt...\n");
        }
        mySql.close();
    }

    static void runNummer3(String dbName, String user, String password, Scanner scanner) {
        String start = getDate(scanner, "start-tid");
        String end = getDate(scanner, "slutt-tid");
        printSessionsBetween(dbName, user, password, start, end);
    }

    private static String getDate(Scanner scanner, String type) {
        System.out.print("Skriv inn " + type + " på yyyy-mm-dd hh:mm:ss format:\n");
        return scanner.nextLine();
    }
    
    
    
	
	public static void main(String[] args) throws Exception {
		run();
	}

}
