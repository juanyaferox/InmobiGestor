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
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @Autowired
    LoginView loginView;
    @Autowired
    UserView userView;

    @Autowired
    Validation validation;

    @FXML
    Button adminLogout;

    //busqueda
    @FXML
    Button btnSearchRental;
    @FXML
    TextField textSearchRental;
    @FXML
    Button btnSearchSale;
    @FXML
    TextField textSearchSale;


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

    @FXML
    GridPane gridPaneEstateList;
    @FXML
    GridPane gridPaneClientList;
    @FXML
    GridPane gridPaneRentalList;
    @FXML
    GridPane gridPaneSaleList;

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

//    @FXML
//    AnchorPane optionListDashboard;

    //variable global del usuario actual
    User user;

    DecimalFormat formatter = new DecimalFormat("#,##0.00");

    ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

    public void initialize() {
        if (userSessionService != null)
            user = userSessionService.getLoggedInUser();
    }

    /**
     * Obtiene el Stage actual y lo sustituye por la pantalla de Login
     *
     * @throws IOException :
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
    @FXML
    private void showClientAll() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranch(user.getBranch());
        log.warn("LISTA DE CLIENTES;---------------------- {}", clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListHouseowner() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.HOUSE_OWNER);
        log.warn("LISTA DE CLIENTES;---------------------- {}", clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListRenter() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.RENTER);
        log.warn("LISTA DE CLIENTES;---------------------- {}", clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListAnother() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.ANOTHER);
        log.warn("LISTA DE CLIENTES: {}", clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListInactive() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.INACTIVE);
        log.warn("LISTA DE CLIENTES: {}", clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListRenterAndHouseowner() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.RENTER_AND_HOUSE_OWNER);
        log.warn("LISTA DE CLIENTES;---------------------- {}", clientList.toString());
        showClientGrid(clientList);
    }

    private void showClientListByType(EnumClient type) {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), type);
        log.warn("LISTA DE CLIENTES: {}", clientList.toString());
        showClientGrid(clientList);
    }

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
            gridPaneClientList.add(new Label(isRented ? "Si" : "No"), 3, i + 1);

            // Crear un HBox para contener los botones
            HBox buttonBox = new HBox();

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

            gridPaneClientList.add(buttonBox, 4, i + 1);
        }
    }

    private void showModalEstatesOwner(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/User_EstatesList_ModalWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Editar Inmueble");
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
                gridPaneEstatesList.add(new Label(estate.getReference()), 1, i + 1);
                gridPaneEstatesList.add(new Label(stringState), 2, i + 1);
                gridPaneEstatesList.add(new Label(String.valueOf(estate.getBranch().getTown().getName())), 3, i + 1);
            }
            stage.showAndWait();
            } catch (IOException e) {
                log.error("Error: {}", e.getMessage());
            }
    }
    //endregion

    //region Añadir y edición cliente
    @FXML
    public void showModalUserAdd() {
        showModalUser(new Client());
    }

    @FXML
    public void showModalUserEdit(Client client) {
        showModalUser(client);
    }

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
            stage.close();
        }
    }
    //endregion

    //region Eliminar cliente
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
    @FXML
    private void showEstateAll() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByBranch(user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateSale() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.ON_SALE, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateRent() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.FOR_RENT, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateSold() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.SOLD, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateRented() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.RENTED, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateAnother() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.ANOTHER, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateInactive() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.INACTIVE, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- {}", estateList.toString());
        showEstateGrid(estateList);
    }

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
            gridPaneEstateList.add(new Label(String.valueOf(estate.getBranch().getTown().getName())), 3, i + 1);

            // Crear un HBox para contener los botones
            HBox buttonBox = new HBox();

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
            buttonColumn1.getChildren().add(btnDelete);
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
                buttonColumn2.getChildren().add(btnEdit);
                btnEdit.setOnAction(e -> showModalEstateEdit(estate)); // Aquí tiene que abrirse
            } else {
                Button btnViewRental = new Button();
                FontIcon viewRentalIcon = new FontIcon();
                viewRentalIcon.setIconColor(Color.GREEN);
                viewRentalIcon.setIconLiteral("mdi2n-newspaper-variant-outline");
                viewRentalIcon.setIconSize(14);
                btnViewRental.setGraphic(viewRentalIcon);
                buttonColumn2.getChildren().add(btnViewRental);
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

            buttonBox.getChildren().addAll(buttonColumn1, buttonColumn2);

            gridPaneEstateList.add(buttonBox, 4, i + 1);
        }
    }

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

    private void showModalEstateHistory(Estate estate) {
        var rents = estate.getHistoryRents();
        var sales = estate.getHistorySales();

        // Juntar las listas y ordenarlas por fecha
        List<HistoryDTO> historyList = getHistoryDTOS(rents, sales);
        showModalEstateHistory(historyList);
    }

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

            historyList.sort(Comparator.comparing(HistoryDTO::getDate));
            ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPaneHistoryEstateList");
            GridPane gridPaneHistoryEstateList = (GridPane) scrollPane.getContent().lookup("#gridPaneHistoryEstateList");
            for (int i = 0; i < historyList.size(); i++) {
                var historyDTO = historyList.get(i);
                log.warn("Historial: {}", historyDTO.toString());
                gridPaneHistoryEstateList.add(new Label(historyDTO.getOperation()), 0, i + 1);
                gridPaneHistoryEstateList.add(new Label(historyDTO.getClient()), 1, i + 1);
                gridPaneHistoryEstateList.add(new Label(historyDTO.getPrecio()), 2, i + 1);
                gridPaneHistoryEstateList.add(new Label(historyDTO.getDate().toString()), 3, i + 1);
            }
            stage.showAndWait();
//            reloadView();
//            showEstateAll();
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }

    }

    private static List<HistoryDTO> getHistoryDTOS(List<HistoryRent> rents, List<HistorySale> sales) {
        List<HistoryDTO> historyList = new ArrayList<>();
        for (HistoryRent rent : rents) {
            historyList.add(
                    new HistoryDTO(
                            "Alquiler: " + (rent.getEndDate() == null ? "En curso" : "Finalizado"),
                            rent.getClientRented().getFullName() + ", " + rent.getClientRented().getDni(),
                            rent.getEndDate() == null ? rent.getStartDate() : rent.getExitDate(),
                            rent.getRentPrice() + "€/mes"
                    )
            );
        }
        for (HistorySale sale : sales) {
            historyList.add(
                    new HistoryDTO(
                            "Venta",
                            sale.getClientActual().getFullName() + ", " + sale.getClientActual().getDni(),
                            sale.getSaleDate(),
                            sale.getSalePrice() + "€"
                    )
            );
        }
        return historyList;
    }

    //endregion

    //region Añadir inmueble
    @FXML
    private void showModalEstateAdd(){
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
            textBranch.setText(user.getBranch().getReference());

            var listClient = (List<Client>) clientService.getAllClientsByBranch(user.getBranch());
            var fxListClient = FXCollections.observableArrayList(listClient);

            @SuppressWarnings ("unchecked")
            ComboBox<Client> boxClient = (ComboBox<Client>) root.lookup("#boxClient");
            boxClient.setPromptText("Elija un cliente");
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

            TextField editor = boxClient.getEditor();
            FilteredList<Client> filteredItems = new FilteredList<>(fxListClient, p -> true);
            boxClient.setItems(filteredItems);

            editor.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    filteredItems.setPredicate(client -> true); // Muestra todos los elementos
                } else {
                    String search = newValue.toLowerCase();
                    filteredItems.setPredicate(client ->
                            (client.getFullName()+", "+client.getDni()).toLowerCase().contains(search)); // Filtro aplicado
                }
                boxClient.show(); // Mantiene el ComboBox desplegado mientras se escribe
            });

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
                handleListEstateAdd(root, imageFile.get() != null ? imageFile.get() : null);
                stage.close();
            });
            stage.showAndWait();
            reloadView();
            showEstateAll();

        } catch (IOException e) {
            log.error("Error", e);
        }
    }

    private void handleListEstateAdd(Parent root, @Nullable File imageFile) {
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
            Notifications.create()
                    .title("Éxito")
                    .text("Se ha añadido el inmueble correctamente")
                    .showWarning();
        }


    }

    //endregion

    //region Editar inmueble

    private void showModalEstateEdit(Estate estate) {
        showModalEstateEdit(estate, false);
    }

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
            log.warn("Se ha cerrado la ventana emergente de edición de inmueble");
            reloadView(); // Recargo la ventana
            log.warn("Se ha recargado la ventana principal de inmuebles");
            showEstateAll(); // muestro la lista de inmuebles
            log.warn("Se ha mostrado la lista de inmuebles");

        } catch (IOException e) {
            log.error("Error al abrir la ventana emergente de edición de inmueble{}", e.getMessage());
        }


    }



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
        }



    }

    //endregion

    //region Borrar inmueble
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

    @FXML
    private void showRental(){
        List<HistoryRent> historyRentList = hRentService.getHistoryRentByBranch(user.getBranch());
        log.warn("LISTA DE ALQUILERES;---------------------- {}", historyRentList.toString());
        showRental(historyRentList);
    }



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
            gridPaneRentalList.add(new Label(historyRent.getEndDate().toString()), 5, i + 1);
            gridPaneRentalList.add(new Label(historyRent.getExitDate() != null ? historyRent.getExitDate().toString() : "Sin salida"), 6, i + 1);

        }
    }

    @FXML
    private void showModalRentalAdd(){
        showModalRental(new HistoryRent());
    }

    @FXML
    private void showModalRentalEdit(HistoryRent historyRent){
        showModalRental(historyRent);
    }

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
            boxRenter.setPromptText("Elija un arrendatario");
            boxRenter.setItems(fxListClient);
            boxRenter.setConverter(new StringConverter<>() {
                @Override
                public String toString(Client client) {
                    return client != null ? (client.getFullName() + ", " + client.getDni()) : "";
                }

                @Override
                public Client fromString(String string) {
                    return fxListClient.stream()
                            .filter(client -> (client.getFullName() + ", " + client.getDni()).equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

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
            hRentService.saveHistoryRent(historyRent);
            var client = historyRent.getClientRented();
            client.setEstateRented(historyRent.getEstate());
            client = clientService.saveClientAsRenter(client);
            var estate = historyRent.getEstate();
            estate.setState(EnumEstate.RENTED);
            estateService.saveEstate(estate);
            Notifications.create()
                    .title("Éxito")
                    .text("Se ha guardado el alquiler correctamente")
                    .showWarning();
            stage.close();
        }
    }

    //endregion

    //region VENTAS

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

    @FXML
    private void showSale(){
        List<HistorySale> historySaleList = hSaleService.getHistorySaleByBranch(user.getBranch());
        log.warn("LISTA DE VENTAS;---------------------- {}", historySaleList.toString());
        showSale(historySaleList);
    }

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
            gridPaneSaleList.add(new Label(historySale.getEstate().getReference()), 0, i + 1);
            gridPaneSaleList.add(new Label(historySale.getClientPrevious().getFullName()), 1, i + 1);
            gridPaneSaleList.add(new Label(historySale.getClientActual().getFullName()), 2, i + 1);
            gridPaneSaleList.add(new Label(formatter.format(historySale.getSalePrice())+" €"), 3, i + 1);
            gridPaneSaleList.add(new Label(historySale.getSaleDate().toString()), 4, i + 1);
        }
    }

    @FXML
    private void showModalSaleAdd(){
        showModalSale(new HistorySale());
    }

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
            boxBuyer.setConverter(new StringConverter<>() {
                @Override
                public String toString(Client client) {
                    return client != null ? (client.getFullName() + ", " + client.getDni()) : "";
                }

                @Override
                public Client fromString(String string) {
                    return fxListClient.stream()
                            .filter(client -> (client.getFullName() + ", " + client.getDni()).equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });

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
            showRental();

        } catch (IOException e) {
            log.error("Error", e);
        }
    }

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
            stage.close();
            return;
        }
    }

    //endregion

    private void changeVisibility(AnchorPane anchorPane) {

        optionModifyUser.setVisible(false);
        optionListClients.setVisible(false);
        optionListEstates.setVisible(false);
        optionListRentals.setVisible(false);
        optionListSales.setVisible(false);

        anchorPane.setVisible(true);
    }

    @FXML
    private void reloadView() {
        try {
            userView.showUserView((Stage) adminLogout.getScene().getWindow());

        } catch (IOException e) {
            log.error("Error al recargar la vista de usuario", e);
        }
    }
}
