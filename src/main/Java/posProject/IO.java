package posProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IO {

    static final String INVOICE_DIRECTORY = "C:\\Users\\JohnZ\\IdeaProjects\\Final_Project\\target\\Inventory\\Product_Inventory.txt";


    //Method used to create or get a file
    public File createFile() throws IOException {
        File saveFile = new File(INVOICE_DIRECTORY);//Creates file with designated path

        if (!saveFile.exists()) { // If the file doesn't exist, creates one and adds the corresponding directories if needed
            saveFile.getParentFile().mkdirs();
            saveFile.createNewFile();
            return saveFile;
        }
        return saveFile;
    }


    //Method that writes the initial result set to the file and creates a table for formating
    public void writeToFile(ResultSet rs) throws SQLException, IOException {
        File saveFile = createFile();
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(saveFile));

        try {  //Formats the result set row and writes it to the existing file
            buffWrite.write(String.format("%60s", "Product List"));
            buffWrite.newLine();
            buffWrite.write("--------------------------------------------------------------------------------------" +
                    "----------------------------------");
            buffWrite.newLine();
            buffWrite.newLine();
            buffWrite.write(String.format("%20s %20s %20s %20s", "ID", "Product Name", "Price", "Type"));
            buffWrite.newLine();
            buffWrite.write(String.format("%22s %20s %20s %20s", "------", "----------------", "---------", "--------"));
            buffWrite.newLine();

            rs.beforeFirst();

            while (rs.next()) {
                buffWrite.write(String.format("%20s %20s %20s %20s", Integer.toString(rs.getInt("ID")),
                        rs.getString("Product_Name"), Double.toString(rs.getDouble("Price")),
                        rs.getString("Subtype")));

                buffWrite.newLine();
            }

            buffWrite.close(); //Closes the file
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    //Method used to append the other 2 result sets
    public void appendToFile(ResultSet rs) throws SQLException, IOException {
        File saveFile = createFile();
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(saveFile, true));

        try {
            buffWrite.write("--------------------------------------------------------------------------------------" +
                    "----------------------------------");
            buffWrite.newLine();

            rs.beforeFirst();

            while (rs.next()) { //Formats the result set row and writes it to the existing file
                buffWrite.write(String.format("%20s %20s %20s %20s", Integer.toString(rs.getInt("ID")),
                        rs.getString("Product_Name"), Double.toString(rs.getDouble("Price")),
                        rs.getString("Subtype")));
                buffWrite.newLine();
            }

            buffWrite.close(); //Closes the file
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}