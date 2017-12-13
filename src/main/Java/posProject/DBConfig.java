package posProject;


import java.sql.*;

public class DBConfig {

    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/"; //Database location
    private static final String DB_NAME = "POS"; //Database name
    private static final String USER = System.getenv("MYSQL_USER"); //Environment variable for username
    private static final String PASSWORD = System.getenv("MYSQL_PASSWORD"); //Environment variable for password

    private static Statement st = null;
    private static Connection con = null;
    private static ResultSet rs = null;
    private static PreparedStatement ps = null;

    //Table names
    final static String DRINK_TABLE_NAME = "Drinks";
    final static String FOOD_TABLE_NAME = "Food";
    final static String MERCHANDISE_TABLE_NAME = "Merchandise";
    final static String DEFAULT_TABLE_NAME = "DefaultTable";

    //Table columns
    final static String PRODUCT_NAME_COLUMN = "Product_Name";
    final static String TYPE_COLUMN = "Subtype";
    final static String PRICE_COLUMN = "Price";

     static ResultSet setup(String tableName) throws SQLException {
         try { //Loads Java driver
             String Driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(Driver);
        } catch (ClassNotFoundException cnfe) {
            System.exit(-1);  // Crashed
        }

        con = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASSWORD );
        st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); //Makes the result set updatable

        String getStartUpData = "SELECT ID, Product_Name, Price, Subtype FROM " + tableName;
        rs = st.executeQuery(getStartUpData);

        return rs;
    }

    static void startUpData() throws SQLException {
        con = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD );

        //Creates database if it doesn't already exist
        String createDatabase = "CREATE DATABASE IF NOT EXISTS POS";
        ps = con.prepareStatement(createDatabase);
        ps.executeUpdate();

        //Gets connection to the database just created
        con = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASSWORD );

       //Creates tables if they don't already exist
        String createDrinksTable = "CREATE TABLE IF NOT EXISTS Drinks (ID int NOT NULL AUTO_INCREMENT UNIQUE, " +
                "Product_Name varchar(50) UNIQUE, Price DECIMAL(5,2), Subtype varchar(11), CHECK (Price > 0), PRIMARY KEY(ID))";
        ps = con.prepareStatement(createDrinksTable);
        ps.executeUpdate();

        String createFoodTable = "CREATE TABLE IF NOT EXISTS Food (ID int NOT NULL AUTO_INCREMENT UNIQUE, " +
                "Product_Name varchar(50) UNIQUE, Price DECIMAL (5,2), Subtype varchar(11), CHECK (Price > 0), PRIMARY KEY(ID))";
        ps = con.prepareStatement(createFoodTable);
        ps.executeUpdate();

        String createMerchTable = "CREATE TABLE IF NOT EXISTS Merchandise (ID int NOT NULL AUTO_INCREMENT UNIQUE, " +
                "Product_Name varchar(50) UNIQUE, Price DECIMAL (5,2), Subtype varchar(11), CHECK (Price > 0), PRIMARY KEY(ID))";
        ps = con.prepareStatement(createMerchTable);
        ps.executeUpdate();

        String createDefaultTable = "CREATE TABLE IF NOT EXISTS DefaultTable (ID int NOT NULL AUTO_INCREMENT UNIQUE, " +
                "Product_Name varchar(50) UNIQUE, Price DECIMAL (5,2), Subtype varchar(11), CHECK (Price > 0), PRIMARY KEY(ID))";
        ps = con.prepareStatement(createDefaultTable);
        ps.executeUpdate();
    }

    //Shutdown method for when the program is terminated. Closes all open connections used.
    static void shutdown() {
        try {
            if (rs != null) { //Closes result set
                rs.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            if (st != null) {// Closes statement
                st.close();
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }

        try {
            if (con != null) { //Closes connection
                con.close();
            }
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
