package fhwedel.JDBC;

import java.sql.*;

public class JDBC {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/firma";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Connected to the database successfully!");

            createHendrikKrause(conn);
            readAllPersonal(conn);
            updateGehalt(conn);
            findMitarbeiterImVerkauf(conn);

        } catch (SQLException e) {
            e.printStackTrace();        
        }
    }

    public static void createHendrikKrause(Connection conn) {
        String statement = "INSERT INTO personal (pnr, name, vorname, geh_stufe, abt_nr, krankenkasse) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(statement)) {
            pstmt.setInt(1, 417);
            pstmt.setString(2, "Krause");
            pstmt.setString(3, "Hendrik");
            pstmt.setString(4, "it1");
            pstmt.setString(5, "d13");
            pstmt.setString(6, "tkk");

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readAllPersonal(Connection conn) {
        String query = "SELECT pnr, name, vorname, geh_stufe, abt_nr, krankenkasse FROM personal";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("PNR | Name | Vorname | Gehaltsstufe | Abteilungsnummer | Krankenkasse");
            while (rs.next()) {
                System.out.println(rs.getInt("pnr") + " | " + rs.getString("name") + " | " + rs.getString("vorname") + " | " + rs.getString("geh_stufe") + " | " + rs.getString("abt_nr") + " | " + rs.getString("krankenkasse"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateGehalt(Connection conn) {
        String query = "UPDATE gehalt SET betrag = betrag * 1.10 WHERE geh_stufe = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "it1");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteLutzTietze(Connection conn) {
        String query = "DELETE FROM personal WHERE vorname = ? AND name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "Lutz");
            pstmt.setString(2, "Tietze");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findMitarbeiterImVerkauf(Connection conn) {
        String query = "SELECT * FROM personal as p JOIN abteilung as a ON p.abt_nr = a.abt_nr WHERE a.Name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString (1, "Verkauf");
            ResultSet rs = pstmt.executeQuery();
            System.out.println("PNR | Name | Vorname | Abteilungsnummer");
            while (rs.next()) {
                System.out.println(rs.getInt("pnr") + " | " + rs.getString("name") + " | " + rs.getString("vorname") + " | " + rs.getString("abt_nr"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void JDBCMigrate(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS krankenversicherung (kkid INT PRIMARY KEY, kuerzel VARCHAR(3) UNIQUE, name VARCHAR(100))");
        } catch (SQLException e) {
            e.printStackTrace();
        }        
    }
}
