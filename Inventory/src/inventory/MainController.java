package inventory;

import com.jfoenix.controls.JFXButton;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author parth
 */
public class MainController implements Initializable{
    
    @FXML
    private AnchorPane rootpane;

    @FXML
    private JFXButton mainSignUp;

    @FXML
    private JFXButton mainSignIn;

    @FXML
    void gotoSignUp(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        rootpane.getChildren().setAll(pane);
    }

    @FXML
    void gotoSignIn(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        rootpane.getChildren().setAll(pane);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("./src/inventory/user.txt"), "utf-8"))) {
//                    writer.write("something");

                            System.out.println("Writting");
                             out.flush();
                             System.out.println("Written");
                    } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}
