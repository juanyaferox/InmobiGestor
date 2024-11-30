package com.feroxdev.inmobigestor.controller;

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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
    BranchService branchService;

    @Autowired
    LoginView loginView;
    @Autowired
    UserView userView;

    @Autowired
    Validation validation;

    @FXML
    Button adminLogout;
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
    AnchorPane optionModifyUser;
    @FXML
    AnchorPane optionListClients;
    @FXML
    AnchorPane optionListEstates;

//    @FXML
//    AnchorPane optionListDashboard;

    //variable global del usuario actual
    User user;

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
            log.warn("CAMPOS A ENVIAR;------" + user.toString());
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

    //region Listado de clientes
    @FXML
    private void showClientAll() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranch(user.getBranch());
        log.warn("LISTA DE CLIENTES;---------------------- " + clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListHouseowner() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.HOUSE_OWNER);
        log.warn("LISTA DE CLIENTES;---------------------- " + clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListRenter() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.RENTER);
        log.warn("LISTA DE CLIENTES;---------------------- " + clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListAnother() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.ANOTHER);
        log.warn("LISTA DE CLIENTES;---------------------- " + clientList.toString());
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
            btnDelete.setOnAction(e -> handleClienteDelete(client));

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

            Button btnHouseHistory = new Button();
            FontIcon houseHistoryIcon = new FontIcon();
            houseHistoryIcon.setIconColor(Color.GREY);
            houseHistoryIcon.setIconLiteral("mdi2h-history");
            houseHistoryIcon.setIconSize(14);
            btnHouseHistory.setGraphic(houseHistoryIcon);
            Tooltip tooltipHistory = new Tooltip("Ver historial de inmuebles");
            btnHouseHistory.setTooltip(tooltipHistory);
            buttonColumn1.getChildren().add(btnHouseHistory);
            //houseHistoryIcon.setOnAction(e -> showModalEstateHistory(estate)); // Aquí tiene que abrirse

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
                //btnViewRented.setOnAction(e -> showModalEstateHistory(estate)); // Aquí tiene que abrirse
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
                //btnViewEstates.setOnAction(e -> showModalEstateHistory(estate)); // Aquí tiene que abrirse
            }

            if (client.getType() == EnumClient.ANOTHER) {
                Button btnRelatedHouses = new Button();
                FontIcon relatedHousesIcon = new FontIcon();
                relatedHousesIcon.setIconColor(Color.DARKRED);
                relatedHousesIcon.setIconLiteral("mdi2h-home-search");
                relatedHousesIcon.setIconSize(14);
                Tooltip tooltipRelatedHouses = new Tooltip("Ver inmuebles relacionados");
                btnRelatedHouses.setTooltip(tooltipRelatedHouses);
                btnRelatedHouses.setGraphic(relatedHousesIcon);
                buttonColumn2.getChildren().add(btnRelatedHouses);
                //btnViewRented.setOnAction(e -> showModalEstateHistory(estate)); // Aquí tiene que abrirse
            }

            // Ajustar márgenes
            HBox.setMargin(buttonColumn1, new Insets(10));
            HBox.setMargin(buttonColumn2, new Insets(10));

            buttonBox.getChildren().addAll(buttonColumn1, buttonColumn2);

            gridPaneClientList.add(buttonBox, 4, i + 1);
        }
    }

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
            showClientAll();

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

        if(validation.validationClient(client)){
            clientService.saveClient(client);
            Notifications.create()
                    .title("Éxito")
                    .text("Se ha guardado el cliente correctamente")
                    .showWarning();
            stage.close();
        }
    }

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



    //region Listado de inmuebles
    @FXML
    private void showEstateAll() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByBranch(user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateSale() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.ON_SALE, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateRent() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.FOR_RENT, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateSold() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.SOLD, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateRented() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.RENTED, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateAnother() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.ANOTHER, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateInactive() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.INACTIVE, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    private void showEstateGrid(List<Estate> estateList) {
        for (int i = 0; i < estateList.size(); i++) {
            var estate = estateList.get(i);

            ImageView imageView = null;
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

                log.error("CATCH1 : Error al cargar la imagen del inmueble: " + System.getProperty("user.dir") + estate.getImagePath() + " - " + e.getMessage());
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
                    log.error("CATCH2: Error al cargar la imagen del inmueble: " + estate.getImagePath());
                }

            }
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

            Button btnEdit = new Button();
            FontIcon editIcon = new FontIcon();
            editIcon.setIconColor(Color.LIGHTBLUE);
            editIcon.setIconLiteral("mdi2h-home-edit");
            editIcon.setIconSize(14);
            btnEdit.setGraphic(editIcon);
            buttonColumn2.getChildren().add(btnEdit);
            btnEdit.setOnAction(e -> showModalEstateEdit(estate)); // Aquí tiene que abrirse

            Button btnHouseHistory = new Button();
            FontIcon houseHistoryIcon = new FontIcon();
            houseHistoryIcon.setIconColor(Color.GREY);
            houseHistoryIcon.setIconLiteral("mdi2h-history");
            houseHistoryIcon.setIconSize(14);
            btnHouseHistory.setGraphic(houseHistoryIcon);
            buttonColumn1.getChildren().add(btnHouseHistory);
            //houseHistoryIcon.setOnAction(e -> showModalEstateHistory(estate)); // Aquí tiene que abrirse

            // Ajustar márgenes
            HBox.setMargin(buttonColumn1, new Insets(10));
            HBox.setMargin(buttonColumn2, new Insets(10));

            buttonBox.getChildren().addAll(buttonColumn1, buttonColumn2);

            gridPaneEstateList.add(buttonBox, 4, i + 1);
        }
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

            var listState = EnumEstate.values();
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

                        log.info("Imagen seleccionada y copiada temporalmente: " + destinationPath);
                    } catch (IOException ex) {
                        log.error("Error al copiar la imagen - " + ex.getMessage());
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

                log.info("Imagen seleccionada y copiada temporalmente: " + destinationPath.getFileName());
            }
        } catch (IOException ex) {
            log.error("Error al copiar la imagen" + ex.getMessage());
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
    @FXML
    private void showModalEstateEdit(Estate estate) {
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

                        log.info("Imagen seleccionada y copiada temporalmente: " + destinationPath);
                    } catch (IOException ex) {
                        log.error("Error al copiar la imagen - " + ex.getMessage());
                    }
                }
            });

            ComboBox<EnumEstate> boxState = (ComboBox<EnumEstate>) root.lookup("#boxState");
            boxState.setItems(FXCollections.observableArrayList(EnumEstate.values()));
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

            stage.showAndWait(); // Bloquea la interacción con la ventana principal hasta que cierre la emergente
            log.warn("Se ha cerrado la ventana emergente de edición de inmueble");
            reloadView(); // Recargo la ventana
            log.warn("Se ha recargado la ventana principal de inmuebles");
            showEstateAll(); // muestro la lista de inmuebles
            log.warn("Se ha mostrado la lista de inmuebles");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private void handleListEstateEdit(Estate estate, Parent root, File imageFile) {

        TextField textReference = (TextField) root.lookup("#textReference");
        TextField textFullAddress = (TextField) root.lookup("#textFullAddress");
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

                log.info("Imagen seleccionada y copiada temporalmente: " + destinationPath.getFileName());
            }
        } catch (IOException ex) {
            log.error("Error al copiar la imagen" + ex.getMessage());
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
        log.warn("Eliminar inmueble: " + estate.getReference());
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



    private void changeVisibility(AnchorPane anchorPane) {

        optionModifyUser.setVisible(false);
        optionListClients.setVisible(false);
        optionListEstates.setVisible(false);
        //optionListDashboard.setVisible(false);

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

    private void configureBranchComboBox(ComboBox<Branch> boxBranch, ObservableList<Branch> branchItems) {
        boxBranch.setConverter(new StringConverter<>() {
            @Override
            public String toString(Branch branch) {
                return branch != null ? branch.getReference() : "";
            }

            @Override
            public Branch fromString(String string) {
                return branchItems.stream()
                        .filter(branch -> branch.getReference().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Configurar el filtrado del ComboBox mediante el editor de texto
        TextField editor = boxBranch.getEditor();
        FilteredList<Branch> filteredItems = new FilteredList<>(branchItems, p -> true);
        boxBranch.setItems(filteredItems);

        editor.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                filteredItems.setPredicate(branch -> true); // Muestra todos los elementos
            } else {
                String search = newValue.toLowerCase();
                filteredItems.setPredicate(branch ->
                        branch.getReference().toLowerCase().contains(search)); // Filtro aplicado
            }
            boxBranch.show(); // Mantiene el ComboBox desplegado mientras se escribe
        });
    }
}
