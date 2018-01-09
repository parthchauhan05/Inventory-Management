
package inventory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author parth
 */
public class SignUpController implements Initializable{
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/INVENTORY";

   //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";
    Connection conn = null;
    Statement stmt = null;
   
    @FXML
    private AnchorPane signuppane;
    
    @FXML
    private JFXTextField fname;

    @FXML
    private JFXTextField lname;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXTextField phonenumber;

    @FXML
    private JFXRadioButton female;

    @FXML
    private JFXButton signupbtn;
    
    @FXML
    private JFXButton signupbackbtn;
    
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXRadioButton male;
    
    @FXML
    void gotoSignin(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        signuppane.getChildren().setAll(pane);
    }
    boolean flag = true;
    boolean flag2 = true;
    @FXML
    void signUp(ActionEvent event) throws IOException {
        System.out.println("IN SIGN UP");
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Sign Up"));
        
        if(fname.getText().isEmpty()){
            content.setBody(new Text("Please enter First Name"));
            flag = false;
        }
        else if(lname.getText().isEmpty()){
            content.setBody(new Text("Please enter Last Name"));
            flag = false;
        }
        else if(email.getText().isEmpty()){
            content.setBody(new Text("Please enter Email"));
            flag = false;
        }
        else if(phonenumber.getText().isEmpty()){
            content.setBody(new Text("Please enter Phone Number"));
            flag = false;
        }
        else if(password.getText().isEmpty()){
            content.setBody(new Text("Please enter Password"));
            flag = false;
        }
        else if(!(male.isSelected() || female.isSelected())){
            content.setBody(new Text("Please select Gender"));
            flag = false;
        }
        else{
            try{
                //STEP 2: Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver");

                //STEP 3: Open a connection
                System.out.println("Connecting to a selected database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Connected database successfully...");
      
                //STEP 4: Execute a query
                stmt = conn.createStatement();
                String sql = "INSERT INTO Users "+
                        "Values (?, ?, ?, ?, ?, ? )";
                //      stmt.executeUpdate(sql);
                PreparedStatement ps=conn.prepareStatement(sql);
                ps.setString(1,fname.getText());
                ps.setString(2,lname.getText());
                ps.setString(3,email.getText());
                System.out.println(phonenumber.getText());
                try{
                    ps.setInt(4,Integer.parseInt(phonenumber.getText()));
                }catch(NumberFormatException e){
                    content.setBody(new Text("Enter NUMBERS in Phone Number not anything else"));
                    flag = false;
                }
                ps.setString(6,password.getText());
                if(male.isSelected()){
                    ps.setString(5,male.getText());
                }
                else{
                    ps.setString(5,female.getText());
                }
                System.out.println(ps);
                System.out.println(flag);
                if (flag){
                    flag2 =false;
                    ps.execute();
                }
                System.out.println(flag2);
                if(!flag2){
                    content.setBody(new Text("Sign Up successful, "+fname.getText()));
                }
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
                System.out.println(dialog.getContent().getAccessibleText());
                dialog.close();
                AnchorPane pane = null;
                
                try {
                    if(!flag2){
                        pane = FXMLLoader.load(getClass().getResource("Main.fxml"));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try{
                    signuppane.getChildren().setAll(pane);
                }catch(NullPointerException e){
                     e.printStackTrace();
                }
                
            }  
        });
        content.setActions(dialogCloseBtn);
      
        dialog.show();
        
        
    }
    
    @FXML
    void gotoHome(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Main.fxml"));
        signuppane.getChildren().setAll(pane);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }  
}
