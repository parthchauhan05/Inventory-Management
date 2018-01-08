package inventory;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import static inventory.SignInController.DB_URL;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author parth
 */
public class DashboardController implements Initializable{
    
    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXHamburger hamburger;
    
    
    @FXML
    private Text text;
    
//    @FXML
//    private TreeTableView<Item> tableView;
//    
//    
//    @FXML
//    private TreeTableColumn<Item, String> col1;
//    
//    @FXML
//    private TreeTableColumn<Item, String> col2;
//    
//    @FXML
//    private TreeTableColumn<Item, Number> col3;
//    
//    @FXML
//    private TreeTableColumn<Item, Number> col4;
//
//    @FXML
//    private TreeTableColumn<Item, String> col5;
//    
//    @FXML
//    private TreeTableColumn<Item, String> col6;
    
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/INVENTORY";

   //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";
    Connection conn = null;
    Statement stmt = null;
    
    @FXML
    private JFXTreeTableView<Item> treeView;
    
    @FXML
    private JFXTextField input;

    
    @FXML
    void gotoHome(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Main.fxml"));
        rootPane.getChildren().setAll(pane);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Just trying");
        
        JFXTreeTableColumn<Item, String> col1 = new JFXTreeTableColumn<>("Name");
        col1.setPrefWidth(120);
        JFXTreeTableColumn<Item, String> col2 = new JFXTreeTableColumn<>("Company");
        col2.setPrefWidth(104);
        JFXTreeTableColumn<Item, Number> col3 = new JFXTreeTableColumn<>("Quantity");
        col3.setPrefWidth(60);
        JFXTreeTableColumn<Item, Number> col4 = new JFXTreeTableColumn<>("Price");
        col4.setPrefWidth(50);
        JFXTreeTableColumn<Item, String> col5 = new JFXTreeTableColumn<>("Purchase");
        col5.setPrefWidth(100);
        JFXTreeTableColumn<Item, String> col6 = new JFXTreeTableColumn<>("Owner");
        col6.setPrefWidth(100);
        
        col1.setCellValueFactory((TreeTableColumn.CellDataFeatures<Item,String> param) -> param.getValue().getValue().getName());
        col2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Item,String> param) -> param.getValue().getValue().getCompany());
        col3.setCellValueFactory((TreeTableColumn.CellDataFeatures<Item,Number> param) -> param.getValue().getValue().getQuantity());
        col4.setCellValueFactory((TreeTableColumn.CellDataFeatures<Item,Number> param) -> param.getValue().getValue().getPrice());
        col5.setCellValueFactory((TreeTableColumn.CellDataFeatures<Item,String> param) -> param.getValue().getValue().getPurchased_by());
        col6.setCellValueFactory((TreeTableColumn.CellDataFeatures<Item,String> param) -> param.getValue().getValue().getOwns());
        
        ObservableList<Item> items = FXCollections.observableArrayList();
        items.add(new Item("Mouse", "HP", 2, 150, "Parth", ""));
          try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
      
            //STEP 4: Execute a query
            stmt = conn.createStatement();
            String sql = "SELECT name, company, quantity, price, purchased_by, owns FROM Items";
//          stmt.executeUpdate(sql);
            
        
            
            PreparedStatement ps=conn.prepareStatement(sql);
           
            
            ResultSet rs = ps.executeQuery();
            System.out.println(ps);
           
            
            while(rs.next()){
                items.add(new Item(rs.getString("name"), rs.getString("company"), rs.getInt("quantity"), rs.getInt("price"), rs.getString("purchased_by"), rs.getString("owns")));
            }
        
          }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }
        
        final TreeItem<Item> root = new RecursiveTreeItem<Item>(items, RecursiveTreeObject::getChildren);
        
        treeView.getColumns().setAll(col1, col2, col3, col4, col5, col6);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        
        input.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                treeView.setPredicate(new Predicate<TreeItem<Item>>(){
                    @Override
                    public boolean test(TreeItem<Item> item){
                        Boolean flag = item.getValue().name.getValue().contains(newValue);
                        return flag;
                    }
                });
            }
        });
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
                                    pane = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
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

class Item extends RecursiveTreeObject<Item>{
    SimpleStringProperty name;
    SimpleStringProperty company;
    SimpleIntegerProperty quantity;
    SimpleIntegerProperty price;
    SimpleStringProperty purchased_by;
    SimpleStringProperty owns;
    
    public Item(String name, String company, Integer quantity, Integer price, String purchased_by, String owns){
        this.name = new SimpleStringProperty(name);
        this.company = new SimpleStringProperty(company);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleIntegerProperty(price);
        this.purchased_by = new SimpleStringProperty(purchased_by);
        this.owns = new SimpleStringProperty(owns);
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    public SimpleStringProperty getCompany() {
        return company;
    }

    public void setCompany(SimpleStringProperty company) {
        this.company = company;
    }

    public SimpleIntegerProperty getQuantity() {
        return quantity;
    }

    public void setQuantity(SimpleIntegerProperty quantity) {
        this.quantity = quantity;
    }

    public SimpleIntegerProperty getPrice() {
        return price;
    }

    public void setPrice(SimpleIntegerProperty price) {
        this.price = price;
    }

    public SimpleStringProperty getPurchased_by() {
        return purchased_by;
    }

    public void setPurchased_by(SimpleStringProperty purchased_by) {
        this.purchased_by = purchased_by;
    }

    public SimpleStringProperty getOwns() {
        return owns;
    }

    public void setOwns(SimpleStringProperty owns) {
        this.owns = owns;
    }
    
}