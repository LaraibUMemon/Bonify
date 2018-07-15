import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class PGCSVLoader {

    public static Properties prop = new Properties();

    public boolean copyBankData() {

        try {
            Connection conn = getConnection();
            long records = insertData((BaseConnection) conn);
            System.out.println(records + " Records inserted");
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long insertData(BaseConnection conn) throws SQLException, IOException {
        CopyManager cm = new CopyManager(conn);
        Reader reader = new FileReader(new File("src\\main\\resources\\bankData.csv"));
        long inserted = cm.copyIn("copy bank from STDIN DELIMITER ';' CSV header", reader);
        return inserted;
    }

    public String findData(int bank_identifier) {
        Connection conn = null;
        try {
            conn = getConnection();
            String sql = "SELECT name FROM bank where bank_identifier = " + bank_identifier;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String bankName = rs.getString(1);
            System.out.println("Bank with ID " + bank_identifier + " found. Bank name = " + bankName);
            return bankName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() throws SQLException {

        StringBuilder connectionString = new StringBuilder();
        connectionString.append("jdbc:postgresql://").append(prop.getProperty("host"))
                .append(":").append(prop.getProperty("port")).append("/").append(prop.getProperty("server"));
        return DriverManager.getConnection(connectionString.toString(), prop.getProperty("user"), prop.getProperty("password"));

    }

    public static void loadApplicationProperties() {
        InputStream input = null;

        try {
            String filename = "application.properties";
            input = PGCSVLoader.class.getClassLoader().getResourceAsStream(filename);

            // load a properties file
            prop.load(input);


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        PGCSVLoader pgcsvLoader = new PGCSVLoader();
        loadApplicationProperties();
        pgcsvLoader.copyBankData();
        pgcsvLoader.findData(10040000);
    }

}
