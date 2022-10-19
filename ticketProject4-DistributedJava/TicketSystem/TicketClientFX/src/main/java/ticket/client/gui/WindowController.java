package ticket.client.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import services.ServicesException;
import services.TicketSystemObserver;
import services.TicketSystemServices;
import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;
import ticket.model.validators.ValidationException;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class WindowController implements Initializable, TicketSystemObserver {

    Seller user = null;

    public PasswordField password;
    public TextField username;
    public AnchorPane loginAnchor;
    public AnchorPane loggedinAnchor;
    public TextField firstName;
    public TextField noTickets;
    public TextField lastName;
    @FXML
    public TableView<ArtisticRepresentation> searchTableView;
    @FXML
    public TableColumn<ArtisticRepresentation, String> artistColumn;
    @FXML
    public TableColumn<ArtisticRepresentation, LocalDateTime> dataColumn;
    @FXML
    public TableColumn<ArtisticRepresentation, String> locationColumn;
    @FXML
    public TableColumn<ArtisticRepresentation, Long> availableColumn;
    @FXML
    public TableColumn<ArtisticRepresentation, Long> soldColumn;
    @FXML
    public TableColumn<ArtisticRepresentation, Long> idColumn;


    @FXML
    public TableView<ArtisticRepresentation> tableAll;
    @FXML
    public TableColumn<ArtisticRepresentation, String> artistColumn2;
    @FXML
    public TableColumn<ArtisticRepresentation, LocalDateTime> dataColumn2;
    @FXML
    public TableColumn<ArtisticRepresentation, String> locationColumn2;
    @FXML
    public TableColumn<ArtisticRepresentation, Long> availableColumn2;
    @FXML
    public TableColumn<ArtisticRepresentation, Long> soldColumn2;
    @FXML
    public TableColumn<ArtisticRepresentation, Long> idColumn2;
    public DatePicker datePicker;

    //connection to services
    private TicketSystemServices server;

    ObservableList<ArtisticRepresentation> modelAll = FXCollections.observableArrayList();
    ObservableList<ArtisticRepresentation> modelAllSearch = FXCollections.observableArrayList();


    public WindowController(){

    }

    public Seller getUser() {
        return user;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources){
        this.loginAnchor.setVisible(true);
        this.loggedinAnchor.setVisible(false);

        //------------------------------------------------------
        idColumn.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, Long>("id"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, String>("artistName"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, LocalDateTime>("data"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, String>("location"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, Long>("availableSeats"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, Long>("soldSeats"));

        searchTableView.getSelectionModel().setSelectionMode(
                SelectionMode.SINGLE
        );
        idColumn.setVisible(false);

        searchTableView.setItems(modelAllSearch);
        //------------------------------------------------------
        idColumn2.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, Long>("id"));
        artistColumn2.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, String>("artistName"));
        dataColumn2.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, LocalDateTime>("data"));
        locationColumn2.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, String>("location"));
        availableColumn2.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, Long>("availableSeats"));
        soldColumn2.setCellValueFactory(new PropertyValueFactory<ArtisticRepresentation, Long>("soldSeats"));

        tableAll.getSelectionModel().setSelectionMode(
                SelectionMode.SINGLE
        );
        idColumn.setVisible(false);

        tableAll.setItems(modelAll);

        //--------------------
        tableAll.setRowFactory(tv-> new TableRow<ArtisticRepresentation>(){
            @Override
            protected void updateItem(ArtisticRepresentation item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null)
                    setStyle("");
                else if(item.getAvailableSeats()==0)
                    setStyle("-fx-background-color: #FF3232;");
                else
                    setStyle("");
            }
        });
        searchTableView.setRowFactory(tv-> new TableRow<ArtisticRepresentation>(){
            @Override
            protected void updateItem(ArtisticRepresentation item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null)
                    setStyle("");
                else if(item.getAvailableSeats()==0)
                    setStyle("-fx-background-color: #FF3232;");
                else
                    setStyle("");
            }
        });

    }
    private void initModel() throws SQLException, ServicesException {
        Iterable<ArtisticRepresentation> ars = server.getAllAR();
        List<ArtisticRepresentation> arrs = StreamSupport.stream(ars.spliterator(), false)
                .collect(Collectors.toList());

//        tableAll.getItems().clear();
//        tableAll.getItems().addAll(arrs);
        System.out.println("sunt in initmodel");
        this.modelAll.setAll(arrs);
    }

    public void setService(TicketSystemServices server){
        this.server = server;
    }

    public void handleLogin(ActionEvent actionEvent) {
        try {
            String username = this.username.getText();
            String psswd = this.password.getText();
            Seller s = new Seller("firstName", "lastName", username, psswd);
            s.setId(1L);

            Seller loggedSeller = this.server.login(s, this);
            //Seller seller = this.server.findSellerByUsername(s);

            if (loggedSeller != null) {
                //Seller loggedSeller = this.server.login(seller, this);
                if (loggedSeller.getPassword().equals(psswd)) {
                    this.user = loggedSeller;

                    this.loginAnchor.setVisible(false);
                    this.loggedinAnchor.setVisible(true);

                    initModel();
                } else
                    MessageAlert.showErrorMessage(null, "Invalid password");
            } else
                MessageAlert.showErrorMessage(null, "Invalid username");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        catch (ServicesException s){
            MessageAlert.showErrorMessage(null, "Already logged in!");
        }
    }

    public void handleSearch(ActionEvent actionEvent) throws SQLException, ServicesException {
        if (datePicker.getValue()!=null) {
            LocalDateTime data = LocalDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth(), datePicker.getValue().getDayOfMonth(), 00, 00, 00);

            Iterable<ArtisticRepresentation> ars = server.getAllARFromDate(data);
            List<ArtisticRepresentation> arsList= StreamSupport.stream(ars.spliterator(), false)
                    .collect(Collectors.toList());

//            searchTableView.getItems().clear();
//            searchTableView.getItems().addAll(searchFilter);
            modelAllSearch.setAll(arsList);
        } else {
            searchTableView.getItems().clear();
        }
    }

    public void handleBuy(ActionEvent actionEvent) {
        if(firstName.getText().equals("") || lastName.getText().equals("") || noTickets.getText().equals(""))
            MessageAlert.showErrorMessage(null, "Complete all fields");
        else {
            try {
                String fName= this.firstName.getText();
                String lName = this.lastName.getText();
                Long no = Long.valueOf(this.noTickets.getText());
                if (no <= 0)
                    MessageAlert.showErrorMessage(null, "Invalid number of tickets!");
                else{
                    ArtisticRepresentation ar = searchTableView.getSelectionModel().getSelectedItem();
                    if (ar == null) {
                        MessageAlert.showErrorMessage(null, "No concert selected!");
                    } else {
                        if(ar.getAvailableSeats()<no)
                            MessageAlert.showMessage(null, Alert.AlertType.WARNING,"Unavailable","Select a smaller number of tickets!");
                        else {
                            ar.setAvailableSeats(ar.getAvailableSeats() - no);
                            ar.setSoldSeats(ar.getSoldSeats() + no);
                            if (this.server.updateAR(ar, this) == null) {
                                System.out.println("aici1");
                                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Succes", "Successfully bought!");
                                this.noTickets.clear();
                                this.firstName.clear();
                                this.lastName.clear();

                                Buyer buyer = new Buyer(ar.getId(), fName, lName, no);
                                buyer.setId(1L);
                                server.addBuyer(buyer);

                                //
                                modelAllSearch.clear();
                                searchTableView.setItems(modelAllSearch);

                            } else
                                MessageAlert.showErrorMessage(null, "Something went wrong!");
                        }
                    }
                }
            }
            catch (Exception e) {
            }
        }
    }

    public void clearField(){
        this.firstName.clear();
        this.lastName.clear();
        this.noTickets.clear();
        this.username.clear();
        this.password.clear();

        modelAll.clear();
        modelAllSearch.clear();
        tableAll.getItems().clear();
        searchTableView.getItems().clear();
    }

    public void handleLogout(ActionEvent actionEvent) throws ServicesException {
        this.loginAnchor.setVisible(true);
        this.loggedinAnchor.setVisible(false);
        clearField();

        this.server.logout(this.user, this);
    }

    public void boughtTicket(ArtisticRepresentation ar) throws ServicesException, SQLException {
        modelAll.removeIf(artisticRepresentation -> artisticRepresentation.getId().equals(ar.getId()));
        modelAll.add(ar);

        //-----------
        modelAllSearch.removeIf(artisticRepresentation -> artisticRepresentation.getId().equals(ar.getId()));
        modelAllSearch.add(ar);
    }

    public void logout() {
        try {
            server.logout(this.user, this);
        } catch (ServicesException e) {
            System.out.println("Logout error " + e);
        }

    }
}
