package sk.tuke.kpi.kp.nonogram;

import java.sql.*;

public class DBTest {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/gamestudio";
        String user = "vira";
        String password = "";

        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("✅ Connected successfully!");
        conn.close();
    }
}
