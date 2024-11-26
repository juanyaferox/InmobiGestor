package com.feroxdev.inmobigestor.controller;

import com.feroxdev.inmobigestor.enums.EnumClient;
import com.feroxdev.inmobigestor.enums.EnumEstate;
import com.feroxdev.inmobigestor.model.*;
import com.feroxdev.inmobigestor.navigation.LoginView;
import com.feroxdev.inmobigestor.navigation.UserView;
import com.feroxdev.inmobigestor.service.ClientService;
import com.feroxdev.inmobigestor.service.EstateService;
import com.feroxdev.inmobigestor.service.UserService;
import com.feroxdev.inmobigestor.service.UserSessionService;
import com.feroxdev.inmobigestor.validation.Validation;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

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
    //region Logout

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
//endregion

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

    //region Listado de inmuebles
    // Realizar modificación: solo se deben mostrar los inmuebles que pertenezcan a la sucursal del usuario logueado
    @FXML
    private void showEstateAll() {
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByBranch(user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateSale(){
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.ON_SALE, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateRent(){
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.FOR_RENT, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateSold(){
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.SOLD, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateRented(){
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.RENTED, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateAnother(){
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.ANOTHER, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }

    @FXML
    private void showEstateInactive(){
        reloadView();
        changeVisibility(optionListEstates);
        List<Estate> estateList = (List<Estate>) estateService.getEstatesByStateAndBranch(EnumEstate.INACTIVE, user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + estateList.toString());
        showEstateGrid(estateList);
    }
    //endregion
//region Listado de clientes
    @FXML
    private void showClientAll() {
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranch(user.getBranch());
        log.warn("LISTA DE INMUEBLES;---------------------- " + clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListHouseowner(){
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.HOUSE_OWNER);
        log.warn("LISTA DE INMUEBLES;---------------------- " + clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListRenter(){
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.RENTER);
        log.warn("LISTA DE INMUEBLES;---------------------- " + clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void showClientListAnother(){
        reloadView();
        changeVisibility(optionListClients);
        List<Client> clientList = (List<Client>) clientService.getAllClientsByBranchAndType(user.getBranch(), EnumClient.ANOTHER);
        log.warn("LISTA DE INMUEBLES;---------------------- " + clientList.toString());
        showClientGrid(clientList);
    }

    @FXML
    private void reloadView() {
        try {
            userView.showUserView((Stage) adminLogout.getScene().getWindow());

        } catch (IOException e) {
            log.error("Error al recargar la vista de usuario", e);
        }
    }

    private void showEstateGrid(List<Estate> estateList) {
        for (int i = 0; i < estateList.size(); i++) {
            var estate = estateList.get(i);

            ImageView imageView = null;
            try {
                Image image = new Image(estate.getImagePath());
                imageView = new ImageView(image);

                double fixedWidth = 200;
                double fixedHeight = 150;

                imageView.setFitWidth(fixedWidth);
                imageView.setFitHeight(fixedHeight);

                imageView.setPreserveRatio(false);
                imageView.setSmooth(true);

            } catch (Exception e) {
                log.error("Error al cargar la imagen del inmueble: " + estate.getReference());
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
            //tnDelete.setOnAction(e -> handleEstateDelete(estate));

            Button btnEdit = new Button();
            FontIcon editIcon = new FontIcon();
            editIcon.setIconColor(Color.LIGHTBLUE);
            editIcon.setIconLiteral("mdi2h-home-edit");
            editIcon.setIconSize(14);
            btnEdit.setGraphic(editIcon);
            buttonColumn2.getChildren().add(btnEdit);
            //btnEdit.setOnAction(e -> showModalEstateEdit(estate)); // Aquí tiene que abrirse

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
            //tnDelete.setOnAction(e -> handleEstateDelete(estate));

            Button btnEdit = new Button();
            FontIcon editIcon = new FontIcon();
            editIcon.setIconColor(Color.LIGHTBLUE);
            editIcon.setIconLiteral("mdi2h-human-edit");
            editIcon.setIconSize(14);
            btnEdit.setGraphic(editIcon);
            Tooltip tooltipEdit = new Tooltip("Editar cliente");
            btnEdit.setTooltip(tooltipEdit);
            buttonColumn1.getChildren().add(btnEdit);
            //btnEdit.setOnAction(e -> showModalEstateEdit(estate)); // Aquí tiene que abrirse

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
//endregion
    private void changeVisibility(AnchorPane anchorPane) {

        optionModifyUser.setVisible(false);
        optionListClients.setVisible(false);
        optionListEstates.setVisible(false);
        //optionListDashboard.setVisible(false);

        anchorPane.setVisible(true);
    }

    private void configureTownComboBox(ComboBox<Town> boxTown, ObservableList<Town> branchItems) {
        boxTown.setConverter(new StringConverter<>() {
            @Override
            public String toString(Town town) {
                return town != null ? town.getName() : "";
            }

            @Override
            public Town fromString(String string) {
                return branchItems.stream()
                        .filter(town -> town.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Configurar el filtrado del ComboBox mediante el editor de texto
        TextField editor = boxTown.getEditor();
        FilteredList<Town> filteredItems = new FilteredList<>(branchItems, p -> true);
        boxTown.setItems(filteredItems);

        editor.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                filteredItems.setPredicate(town -> true); // Muestra todos los elementos
            } else {
                String search = newValue.toLowerCase();
                filteredItems.setPredicate(town ->
                        town.getName().toLowerCase().contains(search)); // Filtro aplicado
            }
            boxTown.show(); // Mantiene el ComboBox desplegado mientras se escribe
        });
    }
}
