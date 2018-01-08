/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import static inventory.SignUpController.DB_URL;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author parth
 */
public class AddInventoryController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/INVENTORY";

   //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";
    Connection conn = null;
    Statement stmt = null;
    

    @FXML
    private StackPane stackPane; 
   
    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField addCompany;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXTextField addOwns;

    @FXML
    private JFXTextField addQuantity;

    @FXML
    private JFXTextField addPrice;

    @FXML
    private Text text;

    @FXML
    private JFXTextField addName;

    @FXML
    private JFXButton addbtn;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    void addItem(ActionEvent event) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Add Item"));
        if(addName.getText().isEmpty()){
            content.setBody(new Text("Please Enter Name"));
        }
        else if(addPrice.getText().isEmpty()){
            content.setBody(new Text("Please Enter Price"));
        }
        else if(addQuantity.getText().isEmpty()){
            content.setBody(new Text("Please Enter Quantity"));
        }
        else if(addCompany.getText().isEmpty()){
            content.setBody(new Text("Please Enter Company"));
        }
        else if(addOwns.getText().isEmpty()){
            content.setBody(new Text("Please Enter who owns this"));
        }
        else{
            content.setBody(new Text(addQuantity.getText()+" "+ addName.getText()+" added successfully, "+text.getText()));
            
            try{
                //STEP 2: Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver");

                //STEP 3: Open a connection
                System.out.println("Connecting to a selected database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Connected database successfully...");
      
                //STEP 4: Execute a query
                stmt = conn.createStatement();
                String sql = "INSERT INTO items "+
                        "Values (?, ?, ?, ?, ?, ? )";
                //      stmt.executeUpdate(sql);
                PreparedStatement ps=conn.prepareStatement(sql);
                ps.setString(1,addName.getText());
                ps.setString(2,addCompany.getText());
                try{
                    ps.setInt(3,Integer.parseInt(addQuantity.getText()));
                }catch(NumberFormatException e){
                    content.setBody(new Text("Enter NUMBERS in Quantity not anything else"));
                }
                
                try{
                    ps.setInt(4,Integer.parseInt(addPrice.getText()));
                }catch(NumberFormatException e){
                    content.setBody(new Text("Enter NUMBERS in Price not anything else"));
                }
                ps.setString(5, text.getText());
                ps.setString(6,addOwns.getText());
                
                System.out.println(ps);
                
                boolean x = ps.execute();
                System.out.println(x);
            }catch(SQLException se){
                //Handle errors for JDBC
                se.printStackTrace();
            }catch(Exception e){
                //Handle errors for Class.forName
                e.printStackTrace();
            }
        }
        JFXDialog dialog = new JFXDialog(stackPane,content,JFXDialog.DialogTransition.CENTER);
        JFXButton dialogCloseBtn = new JFXButton("Close");
        dialogCloseBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }  
        });
        content.setActions(dialogCloseBtn);
      
        dialog.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            InputStream is = new FileInputStream("./src/inventory/user.txt");
            BufferedReader file = new BufferedReader(new InputStreamReader(is));

            String s = file.readLine().trim();
            System.out.println("From file "+s);
            text.setText(s);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {
            VBox box = FXMLLoader.load(getClass().getResource("DrawerContent.fxml"));
            
            drawer.setSidePane(box);
            
            for(Node node: box.getChildren()){
                if(node.getAccessibleText() != null){
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
                        switch (node.getAccessibleText()){
                            case "SignOut":
                            {
                            try {
                                gotoHome();
                            } catch (IOException ex) {
                                Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            }
                                break;
                            case "Add":
                                System.out.println("This is add");
                                AnchorPane pane = null;
                                try {
                                    pane = FXMLLoader.load(getClass().getResource("AddInventory.fxml"));
                                } catch (IOException ex) {
                                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                rootPane.getChildren().setAll(pane);
                                break;
                            case "Dashboard":
                                System.out.println("This is Dashboard");
                                AnchorPane Upane = null;
                                try {
                                    Upane = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                                } catch (IOException ex) {
                                    Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                rootPane.getChildren().setAll(Upane);
                                break;
                        }
                    });
                }
            }
            
            HamburgerBackArrowBasicTransition burgerTask = new HamburgerBackArrowBasicTransition(hamburger);
            burgerTask.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) ->{
                burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();
            
            
                if(drawer.isShown()){
                    drawer.close();
                } else{
                    drawer.open();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    void gotoHome() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Main.fxml"));
        rootPane.getChildren().setAll(pane);
    }
    
}
