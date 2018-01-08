
package inventory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author parth
 */
public class SignUpController implements Initializable{
   
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
    private JFXTextField email;

    @FXML
    private JFXRadioButton male;
    
    @FXML
    void gotoSignin(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        signuppane.getChildren().setAll(pane);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }  
}
