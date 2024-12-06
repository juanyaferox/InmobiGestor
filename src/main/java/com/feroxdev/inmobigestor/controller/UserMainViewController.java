package com.feroxdev.inmobigestor.controller;

import com.feroxdev.inmobigestor.dto.HistoryDTO;
import com.feroxdev.inmobigestor.enums.EnumClient;
import com.feroxdev.inmobigestor.enums.EnumEstate;
import com.feroxdev.inmobigestor.model.*;
import com.feroxdev.inmobigestor.navigation.LoginView;
import com.feroxdev.inmobigestor.navigation.UserView;
import com.feroxdev.inmobigestor.service.*;
import com.feroxdev.inmobigestor.validation.Validation;
import jakarta.annotation.Nullable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@Slf4j
public class UserMainViewController {
    //region Inyeccion de dependencias

    // Servicios
    @Autowired
    UserSessionService userSessionService;
    @Autowired
    UserService userService;
    @Autowired
    EstateService estateService;
    @Autowired
    ClientService clientService;
    @Autowired
    HRentService hRentService;
    @Autowired
    HSaleService hSaleService;

    // Vistas
    @Autowired
    LoginView loginView;
    @Autowired
    UserView userView;

    // Validaciones
    @Autowired
    Validation validation;

    // Botón, se utilizara para obtener la vista actual
    @FXML
    Button adminLogout;

    // Búsqueda
    @FXML
    Button btnSearchRental;
    @FXML
    TextField textSearchRental;
    @FXML
    Button btnSearchSale;
    @FXML
    TextField textSearchSale;


    // TextFields
    @FXML
    TextField textUser;
    @FXML
    TextField textPassword;
    @FXML
    TextField text1Surname;
    @FXML
    TextField text2Surname;
    @FXML
    TextField textID;
    @FXML
    TextField textName;
    @FXML
    TextField textEmail;

    // GridPanes
    @FXML
    GridPane gridPaneEstateList;
    @FXML
    GridPane gridPaneClientList;
    @FXML
    GridPane gridPaneRentalList;
    @FXML
    GridPane gridPaneSaleList;

    // AnchorPanes
    @FXML
    AnchorPane optionModifyUser;
    @FXML
    AnchorPane optionListClients;
    @FXML
    AnchorPane optionListEstates;
    @FXML
    AnchorPane optionListRentals;
    @FXML
    AnchorPane optionListSales;

    // Canvas
    @FXML
    Canvas canvasUser;
    //endregion

    // Variable global del usuario actual
    User user;

    DecimalFormat formatter = new DecimalFormat("#,##0.00");

    ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

    /**
     * Inicializa los datos necesarios de la vista
     */
    public void initialize() {
        if (userSessionService != null)
            user = userSessionService.getLoggedInUser();

        if (canvasUser!= null)
            drawCanvasUser();
    }

    /**
     * Dibuja un triángulo en la mitad derecha de la pantalla que actuará de fondo
     */
    private void drawCanvasUser() {
        GraphicsContext gc = canvasUser.getGraphicsContext2D();
        double width = canvasUser.getWidth();
        double height = canvasUser.getHeight();

        // Mitad derecha diagonal
        gc.setFill(Color.web("#ECFADC"));
        gc.fillPolygon(new double[]{width, width, 0}, new double[]{0, height, height}, 3);
    }

    /**
     * Obtiene el Stage actual y lo sustituye por la pantalla de Login
     *
     * @throws IOException
     */
    @FXML
    private void handleLogout() throws IOException {
        Stage stage = (Stage) adminLogout.getScene().getWindow();
        loginView.showLogin(stage);
    }

    //region MODIFICACION de datos proprios del usuario

    /**
     * Muestra en pantalla los datos del usuario en text boxs modificables
     */
    @FXML
    private void showUserModify() {
        //Se obtiene el usuario al momento de modificar
        changeVisibility(optionModifyUser);
        textUser.setEditable(false);
        int idUser = user.getIdUser();
        textUser.setText(userService.GetUserById(idUser).getUser());
        textPassword.setText(userService.GetUserById(idUser).getPassword());
        text1Surname.setText(userService.GetUserById(idUser).getLastname1());
        text2Surname.setText(userService.GetUserById(idUser).getLastname2());
        textID.setText(userService.GetUserById(idUser).getDni());
        textName.setText(userService.GetUserById(idUser).getName());
        textEmail.setText(userService.GetUserById(idUser).getEmail());
    }

    /**
     * Recibe los datos del usuario y los modifica en la bbdd
     */
    @FXML
    private void handleUserModify() {
        log.warn(user.toString());
        user.setUser(textUser.getText());
        user.setPassword(textPassword.getText());
        user.setLastname1(text1Surname.getText());
        user.setLastname2(text2Surname.getText());
        user.setDni(textID.getText());
        user.setName(textName.getText());
        user.setEmail(textEmail.getText());
        //VALIDACIONES
        if (validation.validationUser(user)) {
            log.warn("CAMPOS A ENVIAR: {}", user.toString());
            userService.changeInfoUser(user);
            showUserModify();
            Notifications.create()
                    .title("Éxito")
                    .text("Se han realizado los cambios correctamente")
                    .showWarning();
            reloadView();
            showUserModify();
        }
    }
    //endregion

    //region CLIENTES

    //region Listado de clientes

    /**
     * Muestra todos los clientes de la sucursal
     */
    @FXML
    private void showClientAll() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranch(user.getBranch());
        log.warn("LISTA DE CLIENTES;---------------------- {}", clientList.toString());
        showClientGrid(clientList);
    }

    /**
     * Muestra los clientes propietarios de la sucursal
     */
    @FXML
    private void showClientListHouseowner() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.HOUSE_OWNER);
        log.warn("LISTA DE CLIENTES;---------------------- {}", clientList.toString());
        showClientGrid(clientList);
    }

    /**
     * Muestra los clientes arrendatarios de la sucursal
     */
    @FXML
    private void showClientListRenter() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.RENTER);
        log.warn("LISTA DE CLIENTES;---------------------- {}", clientList.toString());
        showClientGrid(clientList);
    }

    /**
     * Muestra los clientes inactivos de la sucursal
     */
    @FXML
    private void showClientListInactive() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.INACTIVE);
        log.warn("LISTA DE CLIENTES: {}", clientList.toString());
        showClientGrid(clientList);
    }

    /**
     * Muestra los clientes propietarios y arrendatarios de la sucursal
     */
    @FXML
    private void showClientListRenterAndHouseowner() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.RENTER_AND_HOUSE_OWNER);
        log.warn("LISTA DE CLIENTES;---------------------- {}", clientList.toString());
        showClientGrid(clientList);
    }

    /**
     * Muestra los clientes por tipo
     * @param type
     */
    private void showClientListByType(EnumClient type) {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), type);
        log.warn("LISTA DE CLIENTES: {}", clientList.toString());
        showClientGrid(clientList);
    }

    /**
     * Muestra por pantalla los clientes
     * @param clientList Lista de clientes
     */
    private void showClientGrid(List<Client> clientList) {

        for (int i = 0; i < clientList.size(); i++) {
            var client = clientList.get(i);

            String fullName = client.getName() + " " + client.getLastname1() + " " + (client.getLastname2() != null ? client.getLastname2() : "");

            // Añadir las celdas correspondientes en cada columna de la fila actual
            gridPaneClientList.add(new Label(fullName), 0, i + 1);
            gridPaneClientList.add(new Label(client.getPhone()), 1, i + 1);
            int numEstates = client.getEstates().size();
            gridPaneClientList.add(new Label(String.valueOf(numEstates)), 2, i + 1);
            boolean isRented = client.getEstateRented() != null;
            gridPaneClientList.add(new Label(isRented ? "Sí" : "No"), 3, i + 1);

            // Crear un HBox para contener los botones
            HBox buttonBox = new HBox();
            buttonBox.setAlignment(Pos.CENTER);

            // Crear un VBox para organizar los botones en dos filas
            VBox buttonColumn1 = new VBox();
            VBox buttonColumn2 = new VBox();

            // Espaciado entre los botones
            buttonColumn1.setSpacing(10);
            buttonColumn2.setSpacing(10);

            // Crear los botones

            Button btnDelete = new Button();
            FontIcon deleteIcon = new FontIcon();
            deleteIcon.setIconColor(Color.RED);
            deleteIcon.setIconLiteral("mdi2d-delete");
            deleteIcon.setIconSize(14);
            btnDelete.setGraphic(deleteIcon);
            Tooltip tooltipDelete = new Tooltip("Eliminar cliente");
            btnDelete.setTooltip(tooltipDelete);
            buttonColumn2.getChildren().add(btnDelete);
            btnDelete.setOnAction(e -> {
                handleClienteDelete(client);
                reloadView();
                showClientListByType(client.getType());
            });

            Button btnEdit = new Button();
            FontIcon editIcon = new FontIcon();
            editIcon.setIconColor(Color.LIGHTBLUE);
            editIcon.setIconLiteral("mdi2h-human-edit");
            editIcon.setIconSize(14);
            btnEdit.setGraphic(editIcon);
            Tooltip tooltipEdit = new Tooltip("Editar cliente");
            btnEdit.setTooltip(tooltipEdit);
            buttonColumn1.getChildren().add(btnEdit);
            btnEdit.setOnAction(e -> showModalUserEdit(client)); // Aquí tiene que abrirse

            // Botón para ????
            Button btnHouseHistory = new Button();
            FontIcon houseHistoryIcon = new FontIcon();
            houseHistoryIcon.setIconColor(Color.GREY);
            houseHistoryIcon.setIconLiteral("mdi2h-history");
            houseHistoryIcon.setIconSize(14);
            btnHouseHistory.setGraphic(houseHistoryIcon);
            Tooltip tooltipHistory = new Tooltip("Ver historial de inmuebles");
            btnHouseHistory.setTooltip(tooltipHistory);
            buttonColumn1.getChildren().add(btnHouseHistory);
            var history = new ArrayList<HistoryDTO>();
            client.getEstates().forEach(estate -> {
                history.addAll(getHistoryDTOS(new ArrayList<>(), estate.getHistorySales()));
            });
            if (history.isEmpty())
                btnHouseHistory.setDisable(true);
            btnHouseHistory.setOnAction(e -> showModalEstateHistory(history)); // Aquí tiene que abrirse

            if (isRented) {
                Button btnViewRented = new Button();
                FontIcon viewRentedIcon = new FontIcon();
                viewRentedIcon.setIconColor(Color.GREEN);
                viewRentedIcon.setIconLiteral("mdi2h-home-heart");
                viewRentedIcon.setIconSize(14);
                btnViewRented.setGraphic(viewRentedIcon);
                Tooltip tooltipRented = new Tooltip("Ver inmueble alquilado");
                btnViewRented.setTooltip(tooltipRented);
                buttonColumn2.getChildren().add(btnViewRented);
                btnViewRented.setOnAction(e -> showModalEstateEdit(client.getEstateRented(), true)); // Aquí tiene que abrirse
            }

            // Hacer que caso ya esté boton de isRented crear un nuevo button column
            if (numEstates > 0) {
                Button btnViewEstates = new Button();
                FontIcon viewEstatesIcon = new FontIcon();
                viewEstatesIcon.setIconColor(Color.BLUE);
                viewEstatesIcon.setIconLiteral("mdi2h-home-city");
                viewEstatesIcon.setIconSize(14);
                Tooltip tooltipEstates = new Tooltip("Ver inmuebles");
                btnViewEstates.setTooltip(tooltipEstates);
                btnViewEstates.setGraphic(viewEstatesIcon);
                if (isRented)
                    buttonColumn1.getChildren().add(btnViewEstates);
                else
                    buttonColumn2.getChildren().add(btnViewEstates);
                // Función para ver los inmuebles que le pertenecen en un grid
                btnViewEstates.setOnAction(e -> showModalEstatesOwner(client)); // Aquí tiene que abrirse
            }

            // Ajustar márgenes
            HBox.setMargin(buttonColumn1, new Insets(10));
            HBox.setMargin(buttonColumn2, new Insets(10));

            buttonBox.getChildren().addAll(buttonColumn1, buttonColumn2);

            buttonBox.setAlignment(Pos.CENTER);
            gridPaneClientList.add(buttonBox, 4, i + 1);
            GridPane.setHalignment(buttonBox, HPos.CENTER);

            System.out.println("Añadiendo fila para cliente: " + fullName + " en fila " + (i + 1));
        }
    }

    /**
     * Muestra en una ventana emergen la lista de inmuebles adquiridos de un cliente
     * @param client Cliente a obtener los inmuebles
     */
    private void showModalEstatesOwner(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/User_EstatesList_ModalWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inmuebles adquiridos por " + client.getFullName());
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); // Hace la ventana emergente bloqueante
            Stage primaryStage = (Stage) adminLogout.getScene().getWindow(); // Hace que la ventana principal sea dueña de la emergente
            stage.initOwner(primaryStage);

            var estateList = client.getEstates();

            for (int i = 0; i < estateList.size(); i++) {
                var estate = estateList.get(i);

                ImageView imageView = getImageView(estate, null);
                var stringState = estate.getState() != null ? estate.getState().getDescription() : "Sin estado";
                ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPaneEstatesList");
                GridPane gridPaneEstatesList = (GridPane) scrollPane.getContent().lookup("#gridPaneEstatesList");
                // Añadir las celdas correspondientes en cada columna de la fila actual
                gridPaneEstatesList.add(imageView, 0, i + 1);
                GridPane.setHalignment(imageView, HPos.CENTER);
                GridPane.setMargin(imageView, new Insets(20, 0, 20, 0));
                gridPaneEstatesList.add(new Label(" "+estate.getReference()), 1, i + 1);
                gridPaneEstatesList.add(new Label(" "+stringState), 2, i + 1);
                gridPaneEstatesList.add(new Label(" "+String.valueOf(estate.getBranch().getTown().getName())), 3, i + 1);
            }
            stage.showAndWait();
            } catch (IOException e) {
                log.error("Error: {}", e.getMessage());
            }
    }
    //endregion

    //region Añadir y edición cliente

    /**
     * Muestra la ventana emergente para añadir un nuevo cliente
     */
    @FXML
    public void showModalUserAdd() {
        showModalUser(new Client());
    }

    /**
     * Muestra la ventana emergente para editar un cliente
     * @param client Cliente a editar
     */
    @FXML
    public void showModalUserEdit(Client client) {
        showModalUser(client);
    }

    /**
     * Muestra la ventana emergente para crear o editar un cliente
     * @param client
     */
    private void showModalUser(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/User_Client_ModalWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nuevo Cliente");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); // Hace la ventana emergente bloqueante
            Stage primaryStage = (Stage) adminLogout.getScene().getWindow(); // Hace que la ventana principal sea dueña de la emergente
            stage.initOwner(primaryStage);

            //Caso de edición
            if (!Objects.equals(client, new Client())){
                stage.setTitle("Editar Cliente");

                TextField textClientName = (TextField) root.lookup("#textClientName");
                textClientName.setText(client.getName());

                TextField textClientSurname1 = (TextField) root.lookup("#textClientSurname1");
                textClientSurname1.setText(client.getLastname1());

                TextField textClientSurname2 = (TextField) root.lookup("#textClientSurname2");
                textClientSurname2.setText(client.getLastname2());

                TextField textClientEmail = (TextField) root.lookup("#textClientEmail");
                textClientEmail.setText(client.getEmail());

                TextField textClientDNI = (TextField) root.lookup("#textClientDNI");
                textClientDNI.setText(client.getDni());

                TextField textClientPhone = (TextField) root.lookup("#textClientPhone");
                textClientPhone.setText(client.getPhone());

                TextField textClientAddress = (TextField) root.lookup("#textClientAddress");
                textClientAddress.setText(client.getAddress());
            }

            Button btnConfirmClientModal = (Button) root.lookup("#btnConfirmClientModal");
            btnConfirmClientModal.setOnAction(e -> {
                handleClientSave(root, client, stage);
            });

            stage.showAndWait();
            reloadView();
            showClientListByType(client.getType());

        } catch (IOException e) {
            log.error("Error", e);
        }

    }

    /**
     * Guarda los datos del cliente en la bbdd
     * @param root Ventana emergente
     * @param client Cliente a guardar
     * @param stage Stage de la ventana emergente
     */
    private void handleClientSave(Parent root, Client client, Stage stage){

        //Obtener los datos del cliente
        TextField textClientName = (TextField) root.lookup("#textClientName");
        client.setName(textClientName.getText());
        TextField textClientSurname1 = (TextField) root.lookup("#textClientSurname1");
        client.setLastname1(textClientSurname1.getText());
        TextField textClientSurname2 = (TextField) root.lookup("#textClientSurname2");
        client.setLastname2(textClientSurname2.getText());
        TextField textClientEmail = (TextField) root.lookup("#textClientEmail");
        client.setEmail(textClientEmail.getText());
        TextField textClientDNI = (TextField) root.lookup("#textClientDNI");
        client.setDni(textClientDNI.getText());
        TextField textClientPhone = (TextField) root.lookup("#textClientPhone");
        client.setPhone(textClientPhone.getText());
        TextField textClientAddress = (TextField) root.lookup("#textClientAddress");
        client.setAddress(textClientAddress.getText());
        client.setUser(user);
        client.setBranch(user.getBranch());

        if(validation.validationClient(client)){
            clientService.saveClient(client);
            Notifications.create()
                    .title("Éxito")
                    .text("Se ha guardado el cliente correctamente")
                    .showWarning();
        }
    }
    //endregion

    //region Eliminar cliente

    /**
     * Elimina un cliente de la bbdd
     * @param client Cliente a eliminar
     */
    private void handleClienteDelete(Client client) {
        log.warn("Eliminar cliente: {}", client.getDni());
        if (clientService.deleteClient(client)==null) {
            Notifications.create()
                    .title("No se puede borrar el cliente: " + client.getName())
                    .text("Verifique que no tenga un inmueble alquilado o en propiedad")
                    .showError();
        }
    }
    //endregion

    //endregion

    //region INMUEBLES

    //region Listado de inmuebles

    private void showEstateByState(EnumEstate state) {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(state, user.getBranch());
        estateList = estateList != null ? estateList : (List<Estate>) estateService.getAllEstates();
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    /**
     * Muestra todos los inmuebles de la sucursal
     */
    @FXML
    private void showEstateAll() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByBranch(user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    /**
     * Muestra los inmuebles en venta de la sucursal
     */
    @FXML
    private void showEstateSale() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.ON_SALE, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    /**
     * Muestra los inmuebles en alquiler de la sucursal
     */
    @FXML
    private void showEstateRent() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.FOR_RENT, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    /**
     * Muestra los inmuebles vendidos de la sucursal
     */
    @FXML
    private void showEstateSold() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.SOLD, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    /**
     * Muestra los inmuebles alquilados de la sucursal
     */
    @FXML
    private void showEstateRented() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.RENTED, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    /**
     * Muestra los inmuebles en estado otro de la sucursal
     */
    @FXML
    private void showEstateAnother() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.ANOTHER, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    /**
     * Muestra los inmuebles inactivos de la sucursal
     */
    @FXML
    private void showEstateInactive() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.INACTIVE, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    /**
     * Muestra los inmuebles en venta y alquiler de la sucursal
     */
    @FXML
    private void showEstateRentSale() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.FOR_RENT_AND_SALE, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    /**
     * Muestra los inmuebles segun la lista de inmuebles
     * @param estateList Lista de inmuebles
     */
    private void showEstateGrid(List<Estate> estateList) {
        for (int i = 0; i < estateList.size(); i++) {
            var estate = estateList.get(i);

            ImageView imageView = getImageView(estate, null);
            var stringState = estate.getState() != null ? estate.getState().getDescription() : "Sin estado";

            // Añadir las celdas correspondientes en cada columna de la fila actual
            gridPaneEstateList.add(imageView, 0, i + 1);
            GridPane.setHalignment(imageView, HPos.CENTER);
            GridPane.setMargin(imageView, new Insets(20, 0, 20, 0));
            gridPaneEstateList.add(new Label(estate.getReference()), 1, i + 1);
            gridPaneEstateList.add(new Label(stringState), 2, i + 1);
            gridPaneEstateList.add(new Label(estate.getBranch().getTown().getName()), 3, i + 1);

            // Crear un HBox para contener los botones
            HBox buttonBox = new HBox();
            buttonBox.setAlignment(Pos.CENTER);

            // Crear un VBox para organizar los botones en dos filas
            VBox buttonColumn1 = new VBox();
            VBox buttonColumn2 = new VBox();

            // Espaciado entre los botones
            buttonColumn1.setSpacing(10);
            buttonColumn2.setSpacing(10);

            // Crear los botones

            Button btnDelete = new Button();
            FontIcon deleteIcon = new FontIcon();
            deleteIcon.setIconColor(Color.RED);
            deleteIcon.setIconLiteral("mdi2d-delete");
            deleteIcon.setIconSize(14);
            btnDelete.setGraphic(deleteIcon);
            buttonColumn2.getChildren().add(btnDelete);
            btnDelete.setOnAction(e -> {
                handleEstateDelete(estate);
                reloadView();
                showEstateAll();
            });

            if (estate.getState() != EnumEstate.RENTED) {
                Button btnEdit = new Button();
                FontIcon editIcon = new FontIcon();
                editIcon.setIconColor(Color.LIGHTBLUE);
                editIcon.setIconLiteral("mdi2h-home-edit");
                editIcon.setIconSize(14);
                btnEdit.setGraphic(editIcon);
                buttonColumn1.getChildren().add(btnEdit);
                btnEdit.setOnAction(e -> showModalEstateEdit(estate)); // Aquí tiene que abrirse
            } else {
                Button btnViewRental = new Button();
                FontIcon viewRentalIcon = new FontIcon();
                viewRentalIcon.setIconColor(Color.GREEN);
                viewRentalIcon.setIconLiteral("mdi2n-newspaper-variant-outline");
                viewRentalIcon.setIconSize(14);
                btnViewRental.setGraphic(viewRentalIcon);
                buttonColumn1.getChildren().add(btnViewRental);
                btnViewRental.setOnAction(e -> showModalRentalEdit(
                        estate.getHistoryRents().stream()
                        .max(Comparator.comparing(HistoryRent::getStartDate))
                        .orElse(null))); // Aquí tiene que abrirse
            }


            Button btnHouseHistory = new Button();
            FontIcon houseHistoryIcon = new FontIcon();
            houseHistoryIcon.setIconColor(Color.GREY);
            houseHistoryIcon.setIconLiteral("mdi2h-history");
            houseHistoryIcon.setIconSize(14);
            btnHouseHistory.setGraphic(houseHistoryIcon);
            buttonColumn1.getChildren().add(btnHouseHistory);
            btnHouseHistory.setOnAction(e -> showModalEstateHistory(estate)); // Aquí tiene que abrirse

            // Ajustar márgenes
            HBox.setMargin(buttonColumn1, new Insets(10));
            HBox.setMargin(buttonColumn2, new Insets(10));

            // Se intentó centrar
            buttonBox.getChildren().addAll(buttonColumn1, buttonColumn2);
            buttonBox.setAlignment(Pos.CENTER);

            gridPaneEstateList.add(buttonBox, 4, i + 1);
            GridPane.setValignment(buttonBox, VPos.CENTER);
            GridPane.setHalignment(buttonBox, HPos.CENTER);
        }
    }

    /**
     * Obtiene la imagen del inmueble, caso no haya imagen se carga una por defecto
     * @param estate Inmueble
     * @param imageView ImageView a devolver
     * @return ImageView con la imagen del inmueble
     */
    private static ImageView getImageView(Estate estate, ImageView imageView) {
        try {
            String imagePath = System.getProperty("user.dir") + estate.getImagePath();
            File file = new File(imagePath);
            if (!file.exists()) {
                throw new Exception("No se ha encontrado la imagen");
            }
            Image image = new Image(file.toURI().toString());
            imageView = new ImageView(image);

            double fixedWidth = 200;
            double fixedHeight = 150;

            imageView.setFitWidth(fixedWidth);
            imageView.setFitHeight(fixedHeight);

            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);

        } catch (Exception e) {

            log.error("CATCH1: Error al cargar la imagen del inmueble: {}{} - {}", System.getProperty("user.dir"), estate.getImagePath(), e.getMessage());
            try {
                Image image = new Image("/images/no_found_house.jpg");
                imageView = new ImageView(image);

                double fixedWidth = 200;
                double fixedHeight = 150;

                imageView.setFitWidth(fixedWidth);
                imageView.setFitHeight(fixedHeight);

                imageView.setPreserveRatio(false);
                imageView.setSmooth(true);
            } catch (Exception ex) {
                log.error("CATCH2: Error al cargar la imagen del inmueble: {}", estate.getImagePath());
            }

        }
        return imageView;
    }

    /**
     * Sobrecarga de metodo: Muestra en una ventana emergente el historial de un inmueble
     * @param estate Inmueble a obtener el historial
     */
    private void showModalEstateHistory(Estate estate) {
        var rents = estate.getHistoryRents();
        var sales = estate.getHistorySales();

        // Juntar las listas y ordenarlas por fecha
        List<HistoryDTO> historyList = getHistoryDTOS(rents, sales);
        showModalEstateHistory(historyList);
    }

    /**
     * Muestra en una ventana emergente el historial de operaciones de un inmueble
     * @param historyList Lista de historial de operaciones
     */
    private void showModalEstateHistory(List<HistoryDTO> historyList) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/User_HistoryEstate_ModalWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Historial de inmueble");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); // Hace la ventana emergente bloqueante
            Stage primaryStage = (Stage) adminLogout.getScene().getWindow(); // Hace que la ventana principal sea dueña de la emergente
            stage.initOwner(primaryStage);

            historyList.sort(Comparator.comparing(HistoryDTO::getDate, Comparator.nullsLast(Comparator.naturalOrder())));
            ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPaneHistoryEstateList");
            GridPane gridPaneHistoryEstateList = (GridPane) scrollPane.getContent().lookup("#gridPaneHistoryEstateList");
            for (int i = 0; i < historyList.size(); i++) {
                var historyDTO = historyList.get(i);
                log.warn("Historial: {}", historyDTO.toString());
                gridPaneHistoryEstateList.add(new Label(historyDTO.getOperation()), 0, i + 1);
                gridPaneHistoryEstateList.add(new Label(historyDTO.getClient()), 1, i + 1);
                gridPaneHistoryEstateList.add(new Label(" "+historyDTO.getPrecio()), 2, i + 1);
                gridPaneHistoryEstateList.add(new Label(" "+(historyDTO.getDate() != null ? historyDTO.getDate().toString() : "N/A")), 3, i + 1);
            }
            stage.showAndWait();
//            reloadView();
//            showEstateAll();
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }

    }

    /**
     * Contruye la lista de historial de operaciones de un inmueble
     * @param rents Historial de alquileres
     * @param sales Historial de ventas
     * @return Lista de historial de operaciones
     */
    private static List<HistoryDTO> getHistoryDTOS(List<HistoryRent> rents, List<HistorySale> sales) {
        List<HistoryDTO> historyList = new ArrayList<>();
        for (HistoryRent rent : rents) {
            historyList.add(
                    new HistoryDTO(
                            " Alquiler: " + (rent.getExitDate() == null ? "En curso" + (rent.getEndDate() != null ? " indefinido" : "") : "Finalizado"),
                            " "+ rent.getClient().getFullName()+" ,"+rent.getClient().getDni() + " | " + rent.getClientRented().getFullName() + ", " + rent.getClientRented().getDni(),
                            (rent.getExitDate() == null ? rent.getStartDate() : rent.getExitDate()),
                            rent.getRentPrice() + " €/mes"
                    )
            );
        }
        for (HistorySale sale : sales) {
            historyList.add(
                    new HistoryDTO(
                            " Venta",
                            " "+sale.getClientPrevious().getFullName() + ", "+ sale.getClientPrevious().getDni() + " | " + sale.getClientActual().getFullName() + ", " + sale.getClientActual().getDni(),
                            sale.getSaleDate(),
                            sale.getSalePrice() + " €"
                    )
            );
        }
        return historyList;
    }

    //endregion

    //region Añadir inmueble

    /**
     * Muestra la ventana emergente para añadir un nuevo inmueble
     */
    @FXML
    private void showModalEstateAdd(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/User_Estate_ModalWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nuevo Inmueble");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); // Hace la ventana emergente bloqueante
            Stage primaryStage = (Stage) adminLogout.getScene().getWindow(); // Hace que la ventana principal sea dueña de la emergente
            stage.initOwner(primaryStage);

            TextField textBranch = (TextField) root.lookup("#textBranch"); // Inmodificable
            textBranch.setEditable(false);
            textBranch.setText(user.getBranch().getReference());

            var listClient = (List<Client>) clientService.getAllClientsByBranch(user.getBranch());
            var fxListClient = FXCollections.observableArrayList(listClient);

            @SuppressWarnings ("unchecked")
            ComboBox<Client> boxClient = (ComboBox<Client>) root.lookup("#boxClient");
            boxClient.setPromptText("Elija un cliente");

            constructClientComboBox(boxClient, fxListClient);

            var listState = Arrays.stream(EnumEstate.values())
                      .filter(state -> state != EnumEstate.SOLD && state != EnumEstate.RENTED)
                      .toArray(EnumEstate[]::new);
            var fxListState = FXCollections.observableArrayList(listState);

            @SuppressWarnings ("unchecked")
            ComboBox<EnumEstate> boxState = (ComboBox<EnumEstate>) root.lookup("#boxState");
            boxState.setPromptText("Elija un estado");
            boxState.setItems(fxListState);
            boxState.setConverter(new StringConverter<>() {
                @Override
                public String toString(EnumEstate state) {
                    return state != null ? state.getDescription() : "";
                }

                @Override
                public EnumEstate fromString(String string) {
                    for (EnumEstate state : EnumEstate.values()) {
                        if (state.getDescription().equals(string)) {
                            return state;
                        }
                    }
                    return null;
                }
            });

            AtomicReference<File> imageFile = new AtomicReference<>(null);

            Button btnSelectNewImage = (Button) root.lookup("#btnSelectNewImage");
            btnSelectNewImage.setText("Seleccione una imagen (opcional)");

            btnSelectNewImage.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Seleccionar imagen (opcional)");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
                File file = fileChooser.showOpenDialog(stage);

                if (file != null) {
                    try {
                        // Validar si es una imagen
                        String mimeType = Files.probeContentType(file.toPath());
                        if (!mimeType.startsWith("image/")) {
                            log.error("El archivo seleccionado no es una imagen válida.");
                            return; // Meter notificación
                        }

                        String imagesDir = "/images/houses";
                        Files.createDirectories(Paths.get(imagesDir)); // Crear directorio si no existe
                        String extension = file.getName().substring(file.getName().lastIndexOf("."));
                        String uniqueName = UUID.randomUUID() + extension;
                        Path destinationPath = Paths.get(imagesDir, uniqueName);

                        // Copiar la imagen al archivo destino
                        Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                        // Asignar el archivo destino a selectedImageFile (variable de instancia)
                        imageFile.set(destinationPath.toFile());

                        btnSelectNewImage.setText("Imagen seleccionada: " + imageFile.get().getName().replace("\\", "/"));

                        log.info(" Imagen seleccionada y copiada temporalmente: {}", destinationPath);
                    } catch (IOException ex) {
                        log.error(" Error al copiar la imagen - {}", ex.getMessage());
                    }
                }
            });

            Button btnConfirmEditEstateModal = (Button) root.lookup("#btnConfirmEditEstateModal");

            btnConfirmEditEstateModal.setOnAction(e -> {
                handleListEstateAdd(root, imageFile.get() != null ? imageFile.get() : null, stage);

            });
            stage.showAndWait();


        } catch (IOException e) {
            log.error("Error", e);
        }
    }

    private static void constructClientComboBox(ComboBox<Client> boxClient, ObservableList<Client> fxListClient) {

        boxClient.setItems(fxListClient);
        boxClient.setConverter(new StringConverter<>() {
            @Override
            public String toString(Client client) {
                return client != null ? (client.getFullName()+", "+client.getDni()) : "";
            }

            @Override
            public Client fromString(String string) {
               return fxListClient.stream()
                       .filter(client -> (client.getFullName()+", "+client.getDni()).equals(string))
                       .findFirst()
                       .orElse(null);
            }
        });

        // Al hacer el .jar del proyecto el buscador solo trae problemas

//        TextField editor = boxClient.getEditor();
//        FilteredList<Client> filteredItems = new FilteredList<>(fxListClient, p -> true);
//        boxClient.setItems(filteredItems);
//
//        editor.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.SPACE) {
//                event.consume(); // Ignore the whitespace key press
//            }
//        });
//
//        boxClient.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.SPACE) {
//                event.consume(); // Ignore the whitespace key press
//            }
//        });
//
//        editor.textProperty().addListener((obs, oldValue, newValue) -> {
//            if (newValue == null || newValue.trim().isEmpty()) {
//                filteredItems.setPredicate(client -> true); // Muestra todos los elementos
//            } else {
//                String search = newValue.trim().toLowerCase();
//                if (!search.isBlank()) {
//                    filteredItems.setPredicate(client -> {
//                        String fullNameDni = (client.getFullName() + ", " + client.getDni()).toLowerCase();
//                        return fullNameDni.contains(search);
//                    });// Filtro aplicado
//                }
//            }
//            if (!newValue.endsWith(" ")) {
//                boxClient.show(); // Keep ComboBox open while typing
//            }
//        });
    }

    /**
     * Guarda los datos del inmueble en la bbdd
     * @param root Ventana emergente
     * @param imageFile Archivo de la imagen
     */
    private void handleListEstateAdd(Parent root, @Nullable File imageFile, Stage stage) {
        TextField textReference = (TextField) root.lookup("#textReference");
        TextField textFullAddress = (TextField) root.lookup("#textFullAddress");
        @SuppressWarnings ("unchecked")
        ComboBox<Client> boxClient = (ComboBox<Client>) root.lookup("#boxClient");
        @SuppressWarnings ("unchecked")
        ComboBox<EnumEstate> boxState = (ComboBox<EnumEstate>) root.lookup("#boxState");
        String imagePath = imageFile != null ? imageFile.getPath().replace("\\", "/") : null;
        log.warn(imagePath);

        Estate estate = Estate.builder()
                .branch(user.getBranch())
                .client(boxClient.getValue())
                .reference(textReference.getText())
                .fullAddress(textFullAddress.getText())
                .state(boxState.getValue())
                .imagePath(imagePath)
                .build();
        try {
            if (imageFile != null) {
                String imagesDir = System.getProperty("user.dir") + "/images/houses";
                Files.createDirectories(Paths.get(imagesDir)); // Crear directorio si no existe

                Path destinationPath = Paths.get(imagesDir, imageFile.getName());

                // Copiar la imagen al archivo destino
                Files.move(imageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                log.info("Imagen seleccionada y copiada temporalmente: {}", destinationPath.getFileName());
            }
        } catch (IOException ex) {
            log.error("Error al copiar la imagen{}", ex.getMessage());
        }

        if (validation.validationEstate(estate)) {

            estateService.saveEstate(estate);
            clientService.saveClientAsOwner(estate.getClient());

            Notifications.create()
                    .title("Éxito")
                    .text("Se ha añadido el inmueble correctamente")
                    .showWarning();
            reloadView();
            showEstateByState(estate.getState());
        }
    }

    //endregion

    //region Editar inmueble

    /**
     * Sobrecarga de metodo:
     * Muestra la ventana emergente para editar un inmueble que no esté alquilado
     * @param estate Inmueble a editar
     */
    private void showModalEstateEdit(Estate estate) {
        showModalEstateEdit(estate, false);
    }

    /**
     * Muestra la ventana emergente para editar un inmueble
     * @param estate
     * @param isRentedCase
     */
    private void showModalEstateEdit(Estate estate, boolean isRentedCase) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/User_Estate_ModalWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Editar Inmueble");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); // Hace la ventana emergente bloqueante
            Stage primaryStage = (Stage) adminLogout.getScene().getWindow(); // Hace que la ventana principal sea dueña de la emergente
            stage.initOwner(primaryStage);

            TextField textBranch = (TextField) root.lookup("#textBranch"); // Inmodificable
            textBranch.setEditable(false);
            textBranch.setText(estate.getBranch().getReference());

            @SuppressWarnings ("unchecked")
            ComboBox<String> boxClient = (ComboBox<String>) root.lookup("#boxClient");
            boxClient.setValue(estate.getClient().getFullName());
            boxClient.setDisable(true);
            boxClient.setEditable(false);

            TextField textReference = (TextField) root.lookup("#textReference");
            textReference.setText(estate.getReference());

            TextField textFullAddress = (TextField) root.lookup("#textFullAddress");
            textFullAddress.setText(estate.getFullAddress());

            Button btnSelectNewImage = (Button) root.lookup("#btnSelectNewImage");
            btnSelectNewImage.setText("Seleccionar nueva imagen");

            Button btnConfirmEditEstateModal = (Button) root.lookup("#btnConfirmEditEstateModal");

            AtomicReference<File> imageFile = new AtomicReference<>(null);

            btnSelectNewImage.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Seleccionar imagen");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg"));
                File file = fileChooser.showOpenDialog(stage);

                if (file != null) {
                    try {
                        // Validar si es una imagen
                        String mimeType = Files.probeContentType(file.toPath());
                        if (!mimeType.startsWith("image/")) {
                            log.error("El archivo seleccionado no es una imagen valida.");
                            return; // Meter notificación
                        }

                        String imagesDir = "/images/houses";
                        Files.createDirectories(Paths.get(imagesDir)); // Crear directorio si no existe
                        String extension = file.getName().substring(file.getName().lastIndexOf("."));
                        String uniqueName = UUID.randomUUID() + extension;
                        Path destinationPath = Paths.get(imagesDir, uniqueName);

                        // Copiar la imagen al archivo destino
                        Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                        // Asignar el archivo destino a selectedImageFile (variable de instancia)
                        imageFile.set(destinationPath.toFile());

                        btnSelectNewImage.setText("Imagen seleccionada: " + imageFile.get().getName().replace("\\", "/"));

                        log.info("Imagen seleccionada y copiada temporalmente : {}", destinationPath);
                    } catch (IOException ex) {
                        log.error("Error al copiar la  imagen - {}", ex.getMessage());
                    }
                }
            });

            @SuppressWarnings("unchecked")
            ComboBox<EnumEstate> boxState = (ComboBox<EnumEstate>) root.lookup("#boxState");

            if (estate.getState() == EnumEstate.RENTED)
                boxState.setDisable(true);

            if (estate.getState() == null) {
                boxState.setPromptText("Elija un estado");
            } else {
                boxState.setValue(estate.getState());
            }

            boxState.setItems(FXCollections.observableArrayList(
                    Arrays.stream(EnumEstate.values())
                    .filter(state -> state != EnumEstate.SOLD && state != EnumEstate.RENTED)
                    .toArray(EnumEstate[]::new)));
            boxState.setConverter(new StringConverter<>() {
                @Override
                public String toString(EnumEstate state) {
                    return state != null ? state.getDescription() : "";
                }

                @Override
                public EnumEstate fromString(String string) {
                    for (EnumEstate state : EnumEstate.values()) {
                        if (state.getDescription().equals(string)) {
                            return state;
                        }
                    }
                    throw new IllegalArgumentException("Estado no encontrado: " + string);
                }
            });

            btnConfirmEditEstateModal.setOnAction(e -> handleListEstateEdit(estate, root, imageFile.get() != null ? imageFile.get() : null));

            if (isRentedCase) {
                textReference.setEditable(false);
                textFullAddress.setEditable(false);
                boxState.setDisable(true);
                btnSelectNewImage.setDisable(true);
                btnConfirmEditEstateModal.setVisible(false);
            }
            stage.showAndWait(); // Bloquea la interacción con la ventana principal hasta que cierre la emergente

        } catch (IOException e) {
            log.error("Error al abrir la ventana emergente de edición de inmueble{}", e.getMessage());
        }


    }

    /**
     * Guarda los datos del inmueble editado en la bbdd
     * @param estate Inmueble a editar
     * @param root Ventana emergente
     * @param imageFile Archivo de la imagen
     */
    private void handleListEstateEdit(Estate estate, Parent root, File imageFile) {

        TextField textReference = (TextField) root.lookup("#textReference");
        TextField textFullAddress = (TextField) root.lookup("#textFullAddress");
        @SuppressWarnings("unchecked")
        ComboBox<Client> boxClient = (ComboBox<Client>) root.lookup("#boxClient");
        @SuppressWarnings ("unchecked")
        ComboBox<EnumEstate> boxState = (ComboBox<EnumEstate>) root.lookup("#boxState");
        String imagePath = imageFile != null ? imageFile.getPath().replace("\\", "/") : null;
        log.warn(imagePath);

        estate.setReference(textReference.getText());
        estate.setFullAddress(textFullAddress.getText());
        estate.setState(boxState.getValue());

        if (imageFile != null) {
            estate.setImagePath(imagePath);
        }
        try {
            if (imageFile != null) {
                String imagesDir = System.getProperty("user.dir") + "/images/houses";
                Files.createDirectories(Paths.get(imagesDir)); // Crear directorio si no existe

                Path destinationPath = Paths.get(imagesDir, imageFile.getName());

                // Copiar la imagen al archivo destino
                Files.move(imageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                log.info("Imagen  seleccionada y copiada temporalmente: {}", destinationPath.getFileName());
            }
        } catch (IOException ex) {
            log.error(" Error al copiar la  imagen{}", ex.getMessage());
        }

        if (validation.validationEstate(estate)) {
            estateService.saveEstate(estate);
            Notifications.create()
                    .title("Éxito")
                    .text("Se ha editado el inmueble correctamente")
                    .showWarning();
            reloadView();
            showEstateByState(estate.getState());
        }



    }

    //endregion

    //region Borrar inmueble

    /**
     * Elimina un inmueble de la bbdd
     * @param estate Inmueble a eliminar
     */
    private void handleEstateDelete(Estate estate) {
        log.warn("Eliminar inmueble: {}", estate.getReference());
        if (estate.getClientRenter()!=null){
            Notifications.create()
                    .title("No se puede borrar el inmueble: "+estate.getReference())
                    .text("El inmueble está alquilado")
                    .showError();
            return;
        }

        estateService.deleteEstate(estate);
        reloadView();
    }
    //endregion

    //endregion

    //region ALQUILERES

    //region Listado de alquileres

    /**
     * Apartir de un texto de busqueda muestra los alquileres que coincidan con la referencia
     */
    @FXML
    private void handleSearchRental() {
        String search = textSearchRental.getText();
        if (search.isEmpty()) {
            showRental();
        } else {
            List<HistoryRent> historyRentList = hRentService.getHistoryRentByBranchAndReference(user.getBranch(), search);
            log.warn("LISTA DE ALQUILERES;---------------------- {}", historyRentList.toString());
            gridPaneRentalList.getChildren().clear();
            showRental(historyRentList);
        }
    }

    /**
     * Sobrecarga de método:
     * Muestra el historial de todos los alquileres de la sucursal
     */
    @FXML
    private void showRental(){
        List<HistoryRent> historyRentList = hRentService.getHistoryRentByBranch(user.getBranch());
        log.warn("LISTA DE ALQUILERES;---------------------- {}", historyRentList.toString());
        showRental(historyRentList);
    }

    /**
     * Muestra el historial de alquileres apartir de una lista
     * @param historyRentList Lista de historial de alquileres
     */
    private void showRental(List<HistoryRent> historyRentList) {
        reloadView();
        changeVisibility(optionListRentals);

        FontIcon searchIcon = new FontIcon();
        searchIcon.setIconColor(Color.GREY);
        searchIcon.setIconLiteral("mdi2t-text-box-search");
        searchIcon.setIconSize(14);
        btnSearchRental.setGraphic(searchIcon);

        log.warn("LISTA DE ALQUILERES;---------------------- {}", historyRentList.toString());
        // En caso de que tenga exit-date no se permitira la edicion
        for (int i = 0; i < historyRentList.size(); i++) {
            var historyRent = historyRentList.get(i);
            gridPaneRentalList.add(new Label(historyRent.getEstate().getReference()), 0, i + 1);
            gridPaneRentalList.add(new Label(historyRent.getClient().getFullName()), 1, i + 1);
            gridPaneRentalList.add(new Label(historyRent.getClientRented().getFullName()), 2, i + 1);
            gridPaneRentalList.add(new Label(formatter.format(historyRent.getRentPrice()) + " €"), 3, i + 1);
            gridPaneRentalList.add(new Label(historyRent.getStartDate().toString()), 4, i + 1);
            gridPaneRentalList.add(new Label(historyRent.getEndDate() != null ? historyRent.getEndDate().toString() : "Indefinido"), 5, i + 1);
            gridPaneRentalList.add(new Label(historyRent.getExitDate() != null ? historyRent.getExitDate().toString() : "Sin salida"), 6, i + 1);

        }
    }
    //endregion

    //region Añadir y editar alquiler
    /**
     * Muestra la ventana emergente para añadir un nuevo alquiler
     */
    @FXML
    private void showModalRentalAdd(){
        showModalRental(new HistoryRent());
    }

    /**
     * Muestra la ventana emergente para editar un alquiler
     * @param historyRent Alquiler a editar
     */
    @FXML
    private void showModalRentalEdit(HistoryRent historyRent){
        showModalRental(historyRent);
    }

    /**
     * Muestra la ventana emergente para añadir o editar un alquiler
     * En el caso de editar, solo se puede modificar el campo fecha de salida y el precio de alquiler
     * En el caso de añadir, se puede modificar todos los campos excepto fecha de salida
     * @param historyRent
     */
    private void showModalRental(HistoryRent historyRent){
        try {
            log.warn("HISTORY RENT: {}", historyRent);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/User_Rental_ModalWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nuevo Alquiler");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); // Hace la ventana emergente bloqueante
            Stage primaryStage = (Stage) adminLogout.getScene().getWindow(); // Hace que la ventana principal sea dueña de la emergente
            stage.initOwner(primaryStage);

            var listEstate = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.FOR_RENT, user.getBranch());
            listEstate.addAll((List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.FOR_RENT_AND_SALE, user.getBranch()));
            var fxListEstate = FXCollections.observableArrayList(listEstate);

            @SuppressWarnings ("unchecked")
            ComboBox<Estate> boxEstate = (ComboBox<Estate>) root.lookup("#boxEstate");
            boxEstate.setPromptText("Elija un inmueble");
            boxEstate.setItems(fxListEstate);
            boxEstate.setConverter(new StringConverter<>() {
                @Override
                public String toString(Estate estate) {
                    return estate != null ? (estate.getReference() + " | " + estate.getFullAddress()): "";
                }

                @Override
                public Estate fromString(String string) {
                    return fxListEstate.stream()
                            .filter(estate -> (estate.getReference() + " | " + estate.getFullAddress()).equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

            TextField textClientOwner = (TextField) root.lookup("#textClientOwner");
            textClientOwner.setEditable(false);

            boxEstate.valueProperty().addListener((obs, oldEstate, newEstate) -> {
                if (newEstate != null) {
                    textClientOwner.setText(newEstate.getClient().getFullName() + ", " + newEstate.getClient().getDni());
                } else {
                    textClientOwner.setText("");
                }
            });

            var listClient = (List<Client>) clientService.getAllClientsByBranch(user.getBranch());
            var fxListClient = FXCollections.observableArrayList(listClient);

            @SuppressWarnings ("unchecked")
            ComboBox<Client> boxRenter = (ComboBox<Client>) root.lookup("#boxRenter");
            constructClientComboBox(boxRenter, fxListClient);

            DatePicker exitDate = (DatePicker) root.lookup("#exitDate");
            exitDate.setVisible(false);
            Label lblExit = (Label) root.lookup("#lblExit");
            lblExit.setVisible(false);

            TextField textPrice = (TextField) root.lookup("#textPrice");
            textPrice.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                    textPrice.setText(oldValue);
                }
            });

            // Caso de edición
            if (!Objects.equals(historyRent, new HistoryRent())){

                stage.setTitle("Editar Alquiler");

                boxEstate.setValue(historyRent.getEstate());
                boxEstate.setDisable(true);

                boxRenter.setValue(historyRent.getClientRented());
                boxRenter.setDisable(true);

                textPrice.setText(historyRent.getRentPrice().toPlainString());
                textPrice.setEditable(true);

                DatePicker dateStart = (DatePicker) root.lookup("#dateStart");
                dateStart.setValue(historyRent.getStartDate());
                dateStart.setDisable(true);

                DatePicker dateEnd = (DatePicker) root.lookup("#dateEnd");
                dateEnd.setValue(historyRent.getEndDate());
                dateEnd.setDisable(true);

                exitDate.setVisible(true);
                lblExit.setVisible(true);
            }

            Button btnConfirmRentalModal = (Button) root.lookup("#btnConfirmRentalModal");
            btnConfirmRentalModal.setOnAction(e -> {
                handleRentalSave(root, stage, historyRent);
            });

            stage.showAndWait();
            reloadView();
            showRental();

        } catch (IOException e) {
            log.error("Error", e);
        }
    }

    /**
     * Guarda los datos del alquiler en la bbdd
     * @param root Ventana emergente
     * @param stage Ventana emergente
     * @param historyRent Alquiler a guardar
     */
    private void handleRentalSave(Parent root, Stage stage, HistoryRent historyRent) {
        @SuppressWarnings("unchecked")
        ComboBox<Estate> boxEstate = (ComboBox<Estate>) root.lookup("#boxEstate");
        @SuppressWarnings("unchecked")
        ComboBox<Client> boxRenter = (ComboBox<Client>) root.lookup("#boxRenter");
        DatePicker dateStart = (DatePicker) root.lookup("#dateStart");
        DatePicker dateEnd = (DatePicker) root.lookup("#dateEnd");
        TextField textPrice = (TextField) root.lookup("#textPrice");
        DatePicker exitDate = (DatePicker) root.lookup("#exitDate");

        if (historyRent.equals(new HistoryRent())) {

            BigDecimal price = new BigDecimal(-1);
            try {
                price = new BigDecimal(textPrice.getText()).setScale(2, BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                log.error("Error al convertir el precio a BigDecimal: {}", e.getMessage());
            }

            historyRent = HistoryRent.builder()
                    .estate(boxEstate.getValue())
                    .client(boxEstate.getValue() != null ? boxEstate.getValue().getClient() : null)
                    .clientRented(boxRenter.getValue())
                    .startDate(dateStart.getValue())
                    .endDate(dateEnd.getValue())
                    .rentPrice(price)
                    .build();
            log.warn("HISTORY RENT: {}", historyRent);
        } else {
            historyRent.setExitDate(exitDate.getValue());
        }

        if (validation.validationHistoryRent(historyRent)) {
            // En caso de que el contrato ya haya finalizado
            if (historyRent.getExitDate() != null){
                hRentService.saveHistoryRent(historyRent);
                var client = historyRent.getClientRented();
                client.setEstateRented(null);
                clientService.saveClientAsNoRenter(client);
                var estate = historyRent.getEstate();
                estate.setState(EnumEstate.INACTIVE);
                estate.setClientRenter(null);
                estateService.saveEstate(estate);
                return;
            }
            else if (historyRent.getClientRented().getType() == EnumClient.RENTER
                || historyRent.getClientRented().getType() == EnumClient.RENTER_AND_HOUSE_OWNER) {
                Notifications.create()
                        .title("Error")
                        .text("El cliente ya es arrendatario")
                        .showError();
                return;
            }
            //Caso de que el contrato no haya finalizado (nuevo contrato)
            log.warn(historyRent.toString());
            hRentService.saveHistoryRent(historyRent);
            var client = historyRent.getClientRented();
            client.setEstateRented(historyRent.getEstate());
            log.warn(client.toString());
            client = clientService.saveClientAsRenter(client);
            var estate = historyRent.getEstate();
            estate.setState(EnumEstate.RENTED);
            log.warn(estate.toString());
            estateService.saveEstate(estate);
            Notifications.create()
                    .title("Éxito")
                    .text("Se ha guardado el alquiler correctamente")
                    .showWarning();
        }
    }
    //endregion

    //endregion

    //region VENTAS

    /**
     * Apartir de un texto de busqueda muestra las ventas que coincidan con la referencia
     */
    @FXML
    private void handleSearchSale() {
        String search = textSearchSale.getText();
        if (search.isEmpty()) {
            showSale();
        } else {
            List<HistorySale> historySaleList = hSaleService.getHistorySaleByBranchAndReference(user.getBranch(), search);
            log.warn("LISTA DE ALQUILERES;---------------------- {}", historySaleList.toString());
            gridPaneRentalList.getChildren().clear();
            showSale(historySaleList);
        }
    }

    /**
     * Sobrecarga de método:
     * Muestra el historial de todas las ventas de la sucursal
     */
    @FXML
    private void showSale(){
        List<HistorySale> historySaleList = hSaleService.getHistorySaleByBranch(user.getBranch());
        log.warn("LISTA DE VENTAS;---------------------- {}", historySaleList.toString());
        showSale(historySaleList);
    }

    /**
     * Muestra el historial de ventas apartir de una lista
     * @param historySaleList Lista de historial de ventas
     */
    @FXML
    private void showSale(List<HistorySale> historySaleList) {
        reloadView();
        changeVisibility(optionListSales);

        FontIcon searchIcon = new FontIcon();
        searchIcon.setIconColor(Color.GREY);
        searchIcon.setIconLiteral("mdi2t-text-box-search");
        searchIcon.setIconSize(14);
        btnSearchSale.setGraphic(searchIcon);

        // En caso de que tenga exit-date no se permitira la edicion
        for (int i = 0; i < historySaleList.size(); i++) {
            var historySale = historySaleList.get(i);
            gridPaneSaleList.add(new Label(" "+historySale.getEstate().getReference()), 0, i + 1);
            gridPaneSaleList.add(new Label(" "+historySale.getClientPrevious().getFullName()), 1, i + 1);
            gridPaneSaleList.add(new Label(" "+historySale.getClientActual().getFullName()), 2, i + 1);
            gridPaneSaleList.add(new Label(" "+formatter.format(historySale.getSalePrice())+" €"), 3, i + 1);
            gridPaneSaleList.add(new Label(" "+historySale.getSaleDate().toString()), 4, i + 1);
        }
    }

    /**
     * Muestra la ventana emergente para añadir una nueva venta
     */
    @FXML
    private void showModalSaleAdd(){
        showModalSale(new HistorySale());
    }

    /**
     * Muestra la ventana emergente para añadir una venta
     * @param historySale Venta a añadir
     */
    private void showModalSale(HistorySale historySale){
        try {
            log.warn("HISTORY RENT: {}", historySale);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/User_Sale_ModalWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();

            if (historySale.equals(new HistorySale())){
                stage.setTitle("Nueva Venta");
            } else {
                stage.setTitle("Editar Venta");
            }
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL); // Hace la ventana emergente bloqueante
            Stage primaryStage = (Stage) adminLogout.getScene().getWindow(); // Hace que la ventana principal sea dueña de la emergente
            stage.initOwner(primaryStage);

            var listEstate = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.ON_SALE, user.getBranch());
            listEstate.addAll((List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.FOR_RENT_AND_SALE, user.getBranch()));
            var fxListEstate = FXCollections.observableArrayList(listEstate);

            @SuppressWarnings ("unchecked")
            ComboBox<Estate> boxEstate = (ComboBox<Estate>) root.lookup("#boxEstate");
            boxEstate.setPromptText("Elija un inmueble");
            boxEstate.setItems(fxListEstate);
            boxEstate.setConverter(new StringConverter<>() {
                @Override
                public String toString(Estate estate) {
                    return estate != null ? (estate.getReference() + " | " + estate.getFullAddress()): "";
                }

                @Override
                public Estate fromString(String string) {
                    return fxListEstate.stream()
                            .filter(estate -> (estate.getReference() + " | " + estate.getFullAddress()).equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

            var listClient = (List<Client>) clientService.getAllClientsByBranch(user.getBranch());
            var fxListClient = FXCollections.observableArrayList(listClient);

            TextField txtSeller = (TextField) root.lookup("#txtSeller");
            txtSeller.setEditable(false);

            boxEstate.valueProperty().addListener((obs, oldEstate, newEstate) -> {
                if (newEstate != null) {
                    txtSeller.setText(newEstate.getClient().getFullName() + ", " + newEstate.getClient().getDni());
                } else {
                    txtSeller.setText("");
                }
            });

            @SuppressWarnings ("unchecked")
            ComboBox<Client> boxBuyer = (ComboBox<Client>) root.lookup("#boxBuyer");
            boxBuyer.setPromptText("Elija un comprador");
            boxBuyer.setItems(fxListClient);

            constructClientComboBox(boxBuyer, fxListClient);

            TextField txtSellPrice = (TextField) root.lookup("#txtSellPrice");
            txtSellPrice.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                    txtSellPrice.setText(oldValue);
                }
            });

            Button btnConfirmSaleModal = (Button) root.lookup("#btnConfirmSaleModal");
            btnConfirmSaleModal.setOnAction(e -> {
                handleSaleSave(root, stage, historySale);
            });

            stage.showAndWait();
            reloadView();
            showSale();

        } catch (IOException e) {
            log.error("Error", e);
        }
    }

    /**
     * Guarda los datos de la venta en la bbdd
     * @param root Ventana emergente
     * @param stage Ventana emergente
     * @param historySale Historial de venta a guardar
     */
    private void handleSaleSave(Parent root, Stage stage, HistorySale historySale) {
        @SuppressWarnings("unchecked")
        ComboBox<Estate> boxEstate = (ComboBox<Estate>) root.lookup("#boxEstate");
        @SuppressWarnings("unchecked")
        ComboBox<Client> boxBuyer = (ComboBox<Client>) root.lookup("#boxBuyer");
        TextField txtSellPrice = (TextField) root.lookup("#txtSellPrice");
        DatePicker dateSale = (DatePicker) root.lookup("#dateSale");

        if (historySale.equals(new HistorySale())) {
            BigDecimal price = new BigDecimal(-1);
            try {
                price = new BigDecimal(txtSellPrice.getText()).setScale(2, BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                log.error("Error al convertir el precio a BigDecimal: {}", e.getMessage());
            }

            historySale = HistorySale.builder()
                .estate(boxEstate.getValue())
                .clientPrevious(boxEstate.getValue().getClient())
                .clientActual(boxBuyer.getValue())
                .salePrice(price)
                .saleDate(dateSale.getValue())
                .build();
        }

        if (validation.validationHistorySale(historySale)) {
            hSaleService.saveHistorySale(historySale);
            var clientSeller = historySale.getClientPrevious();
            var clientBuyer = historySale.getClientActual();
            var estate = historySale.getEstate();
            estate.setClient(clientBuyer);
            estate.setState(EnumEstate.SOLD);
            estateService.saveEstate(estate);
            clientService.saveClientAsOwner(clientBuyer);
            clientService.saveClientAsNoOwner(clientSeller);
            Notifications.create()
                    .title("Éxito")
                    .text("Se ha guardado la venta correctamente")
                    .showWarning();
        }
    }

    //endregion

    /**
     * Método para cambiar entre las opciones del menu, comodificando la visilibidad de los paneles
     * @param anchorPane Panel a mostrar
     */
    private void changeVisibility(AnchorPane anchorPane) {

        optionModifyUser.setVisible(false);
        optionListClients.setVisible(false);
        optionListEstates.setVisible(false);
        optionListRentals.setVisible(false);
        optionListSales.setVisible(false);

        anchorPane.setVisible(true);
    }

    /**
     * Recarga la vista de usuario
     */
    @FXML
    private void reloadView() {
        try {
            userView.showUserView((Stage) adminLogout.getScene().getWindow());

        } catch (IOException e) {
            log.error("Error al recargar la vista de usuario", e);
        }
    }
}
