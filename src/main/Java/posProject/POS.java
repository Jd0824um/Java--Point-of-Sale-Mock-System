package posProject;

import java.sql.ResultSet;
import java.sql.SQLException;

class POS {

    //Creates the result sets needed for each type within the database
    private static ResultSet resultSetDefault;
    private static ResultSet resultSetFood;
    private static ResultSet resultSetDrinks;
    private static ResultSet resultSetMerchandise;

    //Creates the corresponding models for each type within the database
    private static POSModel posModelDefault;
    private static POSModel posModelFood;
    private static POSModel posModelDrinks;
    private static POSModel posModelMerchandise;
    private static POSGui posGui;

    //Starts the program
    public static void main(String[] args) {
        start();
    }

    //Starts the database, creates any tables missing, and creates the models.
    public static void start() {
        try {
            DBConfig.startUpData();
            resultSetDefault = DBConfig.setup(DBConfig.DEFAULT_TABLE_NAME);
            resultSetFood = DBConfig.setup(DBConfig.FOOD_TABLE_NAME);
            resultSetDrinks = DBConfig.setup(DBConfig.DRINK_TABLE_NAME);
            resultSetMerchandise = DBConfig.setup(DBConfig.MERCHANDISE_TABLE_NAME);

            posModelDefault = new POSModel(resultSetDefault);
            posModelFood = new POSModel(resultSetFood);
            posModelDrinks = new POSModel(resultSetDrinks);
            posModelMerchandise = new POSModel(resultSetMerchandise);

            posGui = new POSGui(posModelDefault, posModelFood, posModelDrinks, posModelMerchandise); //Inits GUI

        } catch (SQLException sqlee) {
            sqlee.getErrorCode();
        }
    }

    public static void shutdown() {
        DBConfig.shutdown();
        System.exit(0);
    }
}