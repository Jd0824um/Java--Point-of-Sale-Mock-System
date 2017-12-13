package posProject;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

class POSModel extends AbstractTableModel {

    private int rowCount = 0;
    private int columnCount = 0;
    private ResultSet resultSet;

    public POSModel(ResultSet rs) {
        this.resultSet = rs;
        setup();
    }

    //Setup the table model with data from the database
    private void setup() {
        countRows();

        try {
            columnCount = resultSet.getMetaData().getColumnCount();

        }catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public String getColumnName(int index) {
        String[] columnNames = {"ID", "Product Name", "Price", "Type"};
        return columnNames[index];
    }

    private void countRows() {
        rowCount = 0;

        try {
            resultSet.beforeFirst();

            while (resultSet.next()) {
                rowCount++;
            }
        }catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            resultSet.absolute(rowIndex + 1);
            Object getValue = resultSet.getObject(columnIndex + 1);
            return getValue.toString();

        } catch (SQLException sqle) {
            return sqle.getErrorCode();
        }
    }

    //Method to delete by ID
    public boolean deleteByID(int ID) {
        try {
            resultSet.beforeFirst();
            while (resultSet.next()) { //Searches for ID from the "ID" column
                Object getID = resultSet.getObject("ID");
                if (String.valueOf(getID).equals(String.valueOf(ID))) { //if it equals the argument, it's then deleted
                    resultSet.deleteRow();
                    fireTableDataChanged();
                    return true;
                }
            }
            return false;
        } catch (SQLException sqle) {
            sqle.getErrorCode();
            return false;
        }
    }

    //Method to delete by searching for a name. Deletes name from all tables.
    public boolean deleteSearch(String name){
        try{
            resultSet.beforeFirst();

            while (resultSet.next()) { // If name matches, it is then deleted
                Object getName = resultSet.getObject("Product_Name");
                if (String.valueOf(getName).equals(name)){
                    resultSet.deleteRow();
                    fireTableDataChanged();
                    return true;
                }
            }
            return false;
        }catch (SQLException sqlee) {
            sqlee.getErrorCode();
            return false;
        }
    }

    //Method to delete by selection
    public boolean deleteProduct(int row) {
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            fireTableDataChanged();
            return true;

        } catch (SQLException sqle) {
            sqle.getErrorCode();
            return false;
        }
    }

    //Method to add a product to the database
    public boolean addProduct(String name, double price, String type) {
        try {
            resultSet.moveToInsertRow();
            resultSet.updateString(DBConfig.PRODUCT_NAME_COLUMN, name);
            resultSet.updateDouble(DBConfig.PRICE_COLUMN, price);
            resultSet.updateString(DBConfig.TYPE_COLUMN, type);
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
            fireTableDataChanged();
            return true;

        } catch (SQLException sqle) {
            sqle.getErrorCode();
            return false;
        }
    }

    //Method to write the result sets data to a file
    public void writeToFile() throws IOException {
        try {
            IO io = new IO();
            io.writeToFile(resultSet);

        } catch (SQLException sqlee) {
            sqlee.printStackTrace();
        }
    }


    //Method to append the result set to the file
    public void appendToFile() throws IOException {
        try {
            IO io = new IO();
            io.appendToFile(resultSet);
        } catch (SQLException sqlee) {
            sqlee.getErrorCode();
        }
    }
}