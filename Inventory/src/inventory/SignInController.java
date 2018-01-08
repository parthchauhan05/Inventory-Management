/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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
    private StackPane stackPane;

    @FXML
    void makeSignin(ActionEvent event) {
      System.out.println("IN SIGN IN");
      JFXDialogLayout content = new JFXDialogLayout();
      content.setHeading(new Text("Sign In"));
      
      
        try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully...");
      
      //STEP 4: Execute a query
      stmt = conn.createStatement();
        String sql = "SELECT fname, password  FROM Users WHERE email = ?";
//      stmt.executeUpdate(sql);
        String fname = null;
        if(!username.getText().isEmpty()){
            
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,username.getText());
    //       ps.setString(2,password.getText());
            
            ResultSet rs = ps.executeQuery();
            System.out.println(ps);
            String lname = "SomeText";
            
            while(rs.next()){
                fname = rs.getString("fname");
                lname = rs.getString("password");

                System.out.println(fname+' '+lname);
            }
            if(fname == null){
                content.setBody(new Text(username.getText()+", not found"));
            }
            else{
                if(lname.equals(password.getText())){
                    content.setBody(new Text("Welcome, "+fname));
                    try (Writer out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("./src/inventory/user.txt"), "utf-8"))) {
//                    writer.write("something");

                            System.out.println("Writting");
                             out.write(fname);
                             System.out.println("Written");
                             }
                }
                else{
                    content.setBody(new Text("Wrong Password, "+fname));
                }
            }
        }
        else{
            content.setBody(new Text("Enter Details"));
        }

      
        JFXDialog dialog = new JFXDialog(stackPane,content,JFXDialog.DialogTransition.CENTER);
        JFXButton dialogCloseBtn = new JFXButton("Close");
        dialogCloseBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                
                AnchorPane pane = null;
                try {
                    pane = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                } catch (IOException ex) {
                    Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
                }
                rootpane.getChildren().setAll(pane);
            }  
        });
      content.setActions(dialogCloseBtn);
      
      dialog.show();
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
    
    @FXML
    void gotoHome(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Main.fxml"));
        rootpane.getChildren().setAll(pane);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    private AnchorPane FXMLLoader(URL resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
