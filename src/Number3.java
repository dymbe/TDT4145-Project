import java.sql.*;
import java.util.Scanner;

public class Number3 {

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

    static void run(String dbName, String user, String password, Scanner scanner) {
        String start = getDate(scanner, "start-tid");
        String end = getDate(scanner, "slutt-tid");
        printSessionsBetween(dbName, user, password, start, end);
    }

    private static String getDate(Scanner scanner, String type) {
        System.out.print("Skriv inn " + type + " på yyyy-mm-dd hh:mm:ss format:\n~$ ");
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        Number3.run("tdt4145", "root", "root", new Scanner(System.in));
    }
}

//Number3.printSessionsBetween("tdt4145", "root", "root", "2018-03-13 12:12:12", "2018-03-13 17:00:21");
/*
StringBuilder string = new StringBuilder();
        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i < columnCount; i++) {
                string.append(resultSet.getString(i)).append(", ");
            }
            string.append(resultSet.getString(columnCount)).append("\n");
        }
 */