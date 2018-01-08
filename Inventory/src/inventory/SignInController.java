/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import static inventory.Inventory.DB_URL;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.*;

/**
 *
 * @author parth
 */
public class SignInController implements Initializable {
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/INVENTORY";

   //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";
    Connection conn = null;
    Statement stmt = null;
    
    @FXML
    private AnchorPane rootpane;


    @FXML
    private ImageView img1;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXButton signin;

    @FXML
    private JFXButton signup;

    @FXML
    private JFXPasswordField password;

    @FXML
    void makeSignin(ActionEvent event) {
        try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully...");
      
      //STEP 4: Execute a query
      stmt = conn.createStatement();
      String sql = "SELECT fname, lname FROM Users WHERE email = ?";
//      stmt.executeUpdate(sql);
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1,username.getText());
//       ps.setString(2,password.getText());
        System.out.println(ps);
      ResultSet rs = ps.executeQuery();
        System.out.println("Created table in given database...");
        while(rs.next()){
            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            
            System.out.println(fname+' '+lname);
        }
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }
    }

    @FXML
    void makeSignup(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        rootpane.getChildren().setAll(pane);
//        try {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
//                Parent root1 = (Parent) fxmlLoader.load();
//                Stage stage = new Stage();
//                stage.setScene(new Scene(root1));  
//                stage.show();
//        } catch(Exception e) {
//           e.printStackTrace();
//          }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File file = new File("src/images/logo_name.png");
        Image image = new Image(file.toURI().toString());
        img1.setImage(image);
    }    

    private AnchorPane FXMLLoader(URL resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
