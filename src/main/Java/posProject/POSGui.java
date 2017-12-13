package posProject;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.io.IOException;
import java.text.DecimalFormat;


public class POSGui extends JFrame implements WindowListener {

    //Main panel setup
    private JPanel mainPanel;
    private JTable productDataTable;
    private JButton addItem;
    private JButton deleteButton;
    private JPanel tablePanel;
    private JComboBox viewComboBox;
    private JLabel viewLabel;
    private JScrollPane productScroll;

    //Add panel setup
    private JPanel addPanel;
    private JTextField getPriceTextField;
    private JTextField getNameTextField;
    private JButton addButton;
    private JComboBox typeComboBox;
    private JComboBox subtypeComboBox;
    private JLabel nameLabel;
    private JLabel priceLabel;
    private JLabel typeLabel;
    private JLabel subtypeLabel;
    private JButton cancelButton;

    //Delete panel setup
    private JComboBox deleteIDTypeComboBox;
    private JTextField deleteIDTextField;
    private JButton deleteIDButton;
    private JPanel deleteIDPanel;
    private JLabel deleteIDTypeComboBoxLabel;
    private JLabel deleteIDlabel;
    private JButton deleteByIDCancel;
    private JTabbedPane POSTabbedPane;
    private JTextField searchNameTextField;
    private JButton searchDeleteButon;
    private JButton searchCancelButton;
    private JLabel searchNameLabel;
    private JPanel searchDeletePanel;
    private JButton saveButton;
    private JButton exitButton;
    private JButton Cancel;

    //Model setup
    private final DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(new String[]{"Select Type", "Food", "Drink", "Merchandise"});
    private final POSModel newDefaultModel;
    private final POSModel newFoodModel;
    private final POSModel newDrinkModel;
    private final POSModel newMerchandiseModel;

    public POSGui(POSModel defaultModel, POSModel foodModel, POSModel drinkModel, POSModel merchandiseModel) {

        //GUI setup
        POSTabbedPane.removeAll();
        POSTabbedPane.addTab("Point of Sales System", mainPanel);
        setContentPane(POSTabbedPane);
        pack();
        addWindowListener(this);
        setVisible(true);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        viewLabel.setSize(150, 50);

        //Sets Models
        newDefaultModel = defaultModel;
        newFoodModel = foodModel;
        newDrinkModel = drinkModel;
        newMerchandiseModel = merchandiseModel;

        //JTable setup
        productDataTable.setModel(defaultModel);
        productScroll.setViewportView(productDataTable);
        productDataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = productDataTable.getTableHeader();
        TableColumnModel columnModel = header.getColumnModel();
        TableColumn tableColumn = columnModel.getColumn(0);
        tableColumn.setHeaderValue("ID");
        tableColumn = columnModel.getColumn(1);
        tableColumn.setHeaderValue("Product Name");
        tableColumn = columnModel.getColumn(2);
        tableColumn.setHeaderValue("Price");
        tableColumn = columnModel.getColumn(3);
        tableColumn.setHeaderValue("Subtype");

        //Switches database result sets
        viewComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (String.valueOf(viewComboBox.getSelectedItem()).trim()) {
                    case "Food":
                        productDataTable.setModel(newFoodModel);
                        newFoodModel.fireTableDataChanged();
                        viewLabel.setText("Viewing");
                        break;
                    case "Drinks":
                        productDataTable.setModel(newDrinkModel);
                        newDrinkModel.fireTableDataChanged();
                        viewLabel.setText("Viewing");
                        break;
                    case "Merchandise":
                        productDataTable.setModel(newMerchandiseModel);
                        newMerchandiseModel.fireTableDataChanged();
                        viewLabel.setText("Viewing");
                        break;
                    default:
                        productDataTable.setModel(newDefaultModel);
                        newDefaultModel.fireTableDataChanged();
                        viewLabel.setText("View Group");
                        break;
                }
            }
        });

        //Changes the subtype combo box values based on the selection in the type combobox
        typeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String type = String.valueOf(typeComboBox.getSelectedItem()).trim();
                DefaultComboBoxModel drinkModel = new DefaultComboBoxModel(new String[]{"Select Subtype", "Cocktail", "Bottle Beer", "Draft Beer", "Wine", "N/A"});
                DefaultComboBoxModel foodModel = new DefaultComboBoxModel(new String[]{"Select Subtype", "Entree", "Appetizer", "Dessert", "Soup", "Side", "Misc", "Special", "Salad"});
                DefaultComboBoxModel merchModel = new DefaultComboBoxModel(new String[]{"Select Subtype", "Clothing", "Misc", "Gift Card", "Glassware"});

                switch (type) {
                    case "Food":
                        viewLabel.setText("Viewing");
                        subtypeComboBox.setModel(foodModel);
                        break;
                    case "Drink":
                        viewLabel.setText("Viewing");
                        subtypeComboBox.setModel(drinkModel);
                        break;
                    case "Merchandise":
                        viewLabel.setText("Viewing");
                        subtypeComboBox.setModel(merchModel);
                        break;
                    default:
                        viewLabel.setText("View Group");
                        subtypeComboBox.setModel(new DefaultComboBoxModel());
                        break;
                }
            }
        });

        //Event handler to add a product and configuration for its associated buttons/event listeners
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                POSTabbedPane.addTab("Add Product", addPanel);
                POSTabbedPane.setSelectedIndex(1);
                POSTabbedPane.setEnabledAt(0, false);
                typeComboBox.setModel(defaultComboBoxModel);
                typeComboBox.setSelectedIndex(0);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = getNameTextField.getText();
                String price = getPriceTextField.getText();
                String type = String.valueOf(typeComboBox.getSelectedItem()).trim();
                String subType = String.valueOf(subtypeComboBox.getSelectedItem()).trim();
                boolean validation = addInputValidation(name, price, type, subType);

                if (validation) {
                    try {
                        double convertPrice = Double.parseDouble(price);
                        String formatedPrice = new DecimalFormat("####.##").format(convertPrice);
                        convertPrice = Double.parseDouble(formatedPrice);

                        switch (type) {
                            case "Food":
                                newFoodModel.addProduct(name, convertPrice, subType);
                                break;
                            case "Drink":
                                newDrinkModel.addProduct(name, convertPrice, subType);
                                break;
                            case "Merchandise":
                                newMerchandiseModel.addProduct(name, convertPrice, subType);
                                break;
                            default:
                                break;
                        }

                        showMessageDialog("Item was added!");
                        getPriceTextField.setText("");
                        getNameTextField.setText("");
                        typeComboBox.setSelectedIndex(0);
                        POSTabbedPane.setEnabledAt(0, true);
                        POSTabbedPane.setSelectedIndex(0);
                        POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement());

                    } catch (NumberFormatException nfee) {
                        showMessageDialog("Please enter in a valid number for the products price");
                    }
                }
            }
        });

        //Action Listener for the main delete button. Has a switch for the method the user wishes to use to delete
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] deleteOptions = {"ID", "Name", "Selection", "Cancel"}; //String list to hold the names of the buttons used by the JOptionPane
                int response = JOptionPane.showOptionDialog(null, "Choose a method to delete by",
                        "Delete Product", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, deleteOptions,
                        deleteOptions[0]); //JOptionPane that shows the user different options to delete

                switch (response) { //Switch method to get the preferred method of deletion
                    case 0: //Deletes by ID
                        POSTabbedPane.addTab("Delete By ID", deleteIDPanel); //Creates new tab
                        POSTabbedPane.setSelectedIndex(1); //Sets selected tab to the newly created tab
                        POSTabbedPane.setEnabledAt(0, false); //Disables the main tab
                        deleteIDTypeComboBox.setModel(defaultComboBoxModel); //Sets the combo box to default
                        break;
                    case 1: //Deletes by search
                        POSTabbedPane.addTab("Delete By Name", searchDeletePanel);
                        POSTabbedPane.setSelectedIndex(1);
                        POSTabbedPane.setEnabledAt(0, false);
                        break;
                    case 2: //Deletes by selection
                        deleteBySelection();
                        break;
                    default:
                        break;
                }
            }
        });

        //Action listener for the searchDeleteButton to delete an item by name
        searchDeleteButon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = searchNameTextField.getText(); //Gets the name from the textfield
                boolean inFood = newFoodModel.deleteSearch(name); //Checks to see if the name is in the food result set
                boolean inDrink = newDrinkModel.deleteSearch(name); //Checks to see if the name is the drink result set
                boolean inMerch = newMerchandiseModel.deleteSearch(name); //Checks to see if the name is in the merchendise result set

                if (inFood) { //If-elseif-else statement that deletes based off the the boolean values
                    showMessageDialog("Item was DELETED from Food!");
                } else if (inDrink){
                    showMessageDialog("Item was DELETED from Drinks!");
                } else if (inMerch) {
                    showMessageDialog("Item was DELETED from Merchandise");
                } else {
                    showMessageDialog("Couldn't find item to delete"); //Validation to show that item wasn't found
                }

                searchNameTextField.setText(""); //Resets textfield to blank
                POSTabbedPane.setEnabledAt(0, true); //Re-enables the main screen
                POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement()); //Removes current tab
                POSTabbedPane.setSelectedIndex(0); //Sets selection to main screen
            }
        });

        //Action Listener for the deleteSearchButton to cancel and return to the main screen
        searchCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                POSTabbedPane.setEnabledAt(0, true); //Re-enables the main screen
                POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement()); //Removes current tab
                POSTabbedPane.setSelectedIndex(0); //Sets selection to main screen
            }
        });

        //Action listener for the deleteIDButton to delete an item by the primary key
        deleteIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ID = deleteIDTextField.getText(); //Gets the ID

                try {
                    int convertID = Integer.parseInt(ID); //Converts it to an integer

                    switch (String.valueOf(deleteIDTypeComboBox.getSelectedItem()).trim()) { //Gets type from the combo box
                        case "Food": //Switch statement to update the proper model
                            boolean inFood = newFoodModel.deleteByID(convertID);

                            if (inFood){ //Validation if an item was in the result set
                                showMessageDialog("Item was DELETED from Food");
                                break;
                            }

                            showMessageDialog("Please enter in a valid ID to delete"); //Shows message if ID was not found
                            break;
                        case "Drink":
                            boolean inDrink = newDrinkModel.deleteByID(convertID);

                            if (inDrink){
                                showMessageDialog("Item was DELETED from Drinks");
                                break;
                            }

                            showMessageDialog("Please enter in a valid ID to delete");
                            break;
                        case "Merchandise":
                            boolean inMerch = newMerchandiseModel.deleteByID(convertID);

                            if (inMerch){
                                showMessageDialog("Item was DELETED from Merchandise");
                                break;
                            }

                            showMessageDialog("Please enter in a valid ID to delete");
                            break;
                        default:
                            break;
                    }

                    POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement()); //Removes current tab
                    POSTabbedPane.setEnabledAt(0, true); //Re-enables main screen
                    POSTabbedPane.setSelectedIndex(0); //Sets selection back to main screen

                } catch (NumberFormatException nfee) {
                    showMessageDialog("ID was not found or ID was invalid"); // Validation if an non-number was entered in
                }
            }
        });

        //Action listener for the delete ID cancel button to return to the main screen
        deleteByIDCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement()); //Removes the current panel
                POSTabbedPane.setEnabledAt(0, true); //Re-enables the main panel
                POSTabbedPane.setSelectedIndex(0); //Selects the main panel
                deleteIDTextField.setText(""); //Resets the text field of the deleteByID text field
                deleteIDTypeComboBox.setSelectedIndex(0); //Resets the deleteByID combo box to default
            }
        });

        //Action listener for the add product cancel button to return to the main screen
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement());
            }
        });

        //Saves all current products to a file
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    newFoodModel.writeToFile();
                    newDrinkModel.appendToFile();
                    newMerchandiseModel.appendToFile();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });

        //Action listener to shutdown program from the exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)  {
                POS.shutdown();
            }
        });
    }

    //Method to delete the selection on JTable
    private void deleteBySelection() {
        switch (String.valueOf(viewComboBox.getSelectedItem()).trim()) { //Switch statement to get the database selected
            case "Food":
                boolean inFood = newFoodModel.deleteProduct(productDataTable.getSelectedRow()); //Calls the model method to delete the product selected.

                if (inFood){
                    showMessageDialog("Item was DELETED from Food");
                    break;
                }

                showMessageDialog("Please select an item to delete");//If returned false, there wasn't and item selected
                break;
            case "Drinks":
                boolean inDrink = newDrinkModel.deleteProduct(productDataTable.getSelectedRow());

                if (inDrink){
                    showMessageDialog("Item was DELETED from Drinks");
                    break;
                }

                showMessageDialog("Please select an item to delete");
                break;
            case "Merchandise":
                boolean inMerch = newMerchandiseModel.deleteProduct(productDataTable.getSelectedRow());

                if (inMerch){
                    showMessageDialog("Item was DELETED from Merchandise");
                    break;
                }

                showMessageDialog("Please select an item to delete");
                break;
            default:
                showMessageDialog("Please select an item to delete");
                newDefaultModel.deleteProduct(productDataTable.getSelectedRow());
                break;
        }
    }

    //Input validation for adding a product to the system
    private boolean addInputValidation(String name, String price, String type, String subtype) {
        if (name == null || name.isEmpty()) { //Checks to see if the name is null or empty and returns false if true
            showMessageDialog("Please enter in a name for the product you wish to add");
            return false;
        } else if (type.equals("") || type.isEmpty()) { //Checks to see if a type was selected and returns false if true
            showMessageDialog("Please select the type for the product you wish to add");
            return false;
        } else if (price == null || price.isEmpty()) { //Checks to see if a price is null or empty and returns false if true
            showMessageDialog("Please enter in a price for the product you wish to add");
            return false;
        } else if (subtype.equals("") || subtype.isEmpty()) { //Checks to see if a subtype was selected and returns false if true
            showMessageDialog("Please select a subtype for the product you wish to add");
            return false;
        } else {
            return true; //Returns true if passes all validation checks
        }
    }

    //Default show message dialog
    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        DBConfig.shutdown();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}