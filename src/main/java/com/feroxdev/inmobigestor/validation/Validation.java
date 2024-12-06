package com.feroxdev.inmobigestor.validation;

import com.feroxdev.inmobigestor.model.*;
import com.feroxdev.inmobigestor.utilities.CsvUtils;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
@Slf4j
public class Validation {

    ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

    /**
     * @param user: Usuario a ser validado
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    public boolean validationUser(User user) {
        boolean isValid = true;

        if (user == null) {    // evitamos posible nullPointerException
            validationNotification(resourceBundle.getString("user"));
            return false;
        }
        if (user.getUser().length() > 50 || user.getUser().isEmpty()) {
            validationNotification(resourceBundle.getString("user"));
            isValid = false;
        }
        if (user.getPassword().length() > 255 || user.getPassword().isEmpty()) {
            validationNotification(resourceBundle.getString("password"));
            isValid = false;
        }
        if (user.getEmail().length() > 255) {
            validationNotification(resourceBundle.getString("email"));
            isValid = false;
        }
        if (user.getName().length() > 50 || user.getName().isEmpty()) {
            validationNotification(resourceBundle.getString("name"));
            isValid = false;
        }
        if (user.getLastname1().length() > 50 || user.getLastname1().isEmpty()) {
            validationNotification(resourceBundle.getString("lastname1"));
            isValid = false;
        }
        if (user.getLastname2().length() > 50) {
            validationNotification(resourceBundle.getString("lastname2"));
            isValid = false;
        }
        if (user.getDni().length() > 9 || user.getDni().isEmpty()) {
            validationNotification(resourceBundle.getString("dni"));
            isValid = false;
        }
        return isValid;
    }

    /**
     * @param branch Sucursal a ser validada
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    public boolean validationBranch(Branch branch) {
        boolean isValid = true;

        if (branch == null) {   // evitamos posible nullPointerException
            validationNotification(resourceBundle.getString("branch"));
            return false;
        }
        if (branch.getTown() == null || branch.getTown().equals(new Town())) {
            validationNotification(resourceBundle.getString("town"));
            isValid = false;
        }
        if (branch.getReference() == null || branch.getReference().isEmpty() || branch.getReference().length() > 255) { // Validar para no repetir referencia
            validationNotification(resourceBundle.getString("reference"));
            isValid = false;
        }
        return isValid;
    }

    /**
     *
     * @param estate Propiedad a ser validada
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    public boolean validationEstate(Estate estate) {
        boolean isValid = true;

        if (estate == null) {   // evitamos posible nullPointerException
            validationNotification(resourceBundle.getString("estate"));
            return false;
        }
        if (estate.getBranch() == null || estate.getBranch().equals(new Branch())) {
            validationNotification(resourceBundle.getString("branch"));
            isValid = false;
        }
        if (estate.getReference() == null || estate.getReference().isEmpty() || estate.getReference().length() > 255) {
            validationNotification(resourceBundle.getString("reference"));
            isValid = false;
        }
        if (estate.getClient() == null || estate.getClient().equals(new Client())) {
            validationNotification(resourceBundle.getString("owner"));
            isValid = false;
        }
        if (estate.getState() == null) {
            validationNotification(resourceBundle.getString("state"));
            isValid = false;
        }
        if (estate.getFullAddress() == null || estate.getFullAddress().isEmpty() || estate.getFullAddress().length() > 500) {
            validationNotification(resourceBundle.getString("address"));
            isValid = false;
        }
        if (estate.getImagePath() != null && estate.getImagePath().length() > 500) {
            validationNotification(resourceBundle.getString("image"));
            isValid = false;
        }
        return isValid;
    }

    /**
     *
     * @param client Cliente a ser validado
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    public boolean validationClient(Client client) {
        boolean isValid = true;

        if (client == null) {   // evitamos posible nullPointerException
            validationNotification(resourceBundle.getString("client"));
            return false;
        }
        if (client.getName() == null || client.getName().isEmpty() || client.getName().length() > 50) {
            validationNotification(resourceBundle.getString("name"));
            isValid = false;
        }
        if (client.getLastname1() == null || client.getLastname1().isEmpty() || client.getLastname1().length() > 50) {
            validationNotification(resourceBundle.getString("lastname1"));
            isValid = false;
        }
        if (client.getLastname2().length() > 50) {
            validationNotification(resourceBundle.getString("lastname2"));
            isValid = false;
        }
        if (client.getEmail().length() > 255) {
            validationNotification(resourceBundle.getString("email"));
            isValid = false;
        }
        if (client.getDni() == null || client.getDni().length() > 9 || client.getDni().isEmpty()) {
            validationNotification(resourceBundle.getString("dni"));
            isValid = false;
        }
        if (client.getPhone().length() > 20) {
            validationNotification(resourceBundle.getString("phone"));
            isValid = false;
        }
        if (client.getAddress().length() > 255) {
            validationNotification(resourceBundle.getString("address"));
            isValid = false;
        }
        return isValid;
    }

    /**
     *
     * @param historyRent Historial de alquiler a ser validado
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    public boolean validationHistoryRent(HistoryRent historyRent) {
        boolean isValid = true;

        if (historyRent == null) {   // evitamos posible nullPointerException
            validationNotification(resourceBundle.getString("historyRent"));
            return false;
        }
        if (historyRent.getEstate() == null || historyRent.getEstate().equals(new Estate())) {
            validationNotification(resourceBundle.getString("estate"));
            isValid = false;
        }
        if (historyRent.getClient() == null || historyRent.getClient().equals(new Client())) {
            validationNotification(resourceBundle.getString("lessor"));
            isValid = false;
        }
        if (historyRent.getClientRented() == null || historyRent.getClientRented().equals(new Client())) {
            validationNotification(resourceBundle.getString("renter"));
            isValid = false;
        }
        if (historyRent.getStartDate() == null) {
            validationNotification(resourceBundle.getString("start.date"));
            isValid = false;
        }
        if (historyRent.getEndDate() != null && historyRent.getStartDate() != null && historyRent.getStartDate().isAfter(historyRent.getEndDate())) {
            validationNotification(
                    resourceBundle.getString("start.date") +
                    " " + resourceBundle.getString("and") + " " +
                    resourceBundle.getString("end.date"));
            isValid = false;
        }
        if (historyRent.getRentPrice() == null || historyRent.getRentPrice().compareTo(new BigDecimal(0)) < 0) {
            validationNotification(resourceBundle.getString("rent.price"));
            isValid = false;
        }
        //log.info("historyRent.getClient() == historyRent.getClientRented() = " + (historyRent.getClient() == historyRent.getClientRented()));
        if (Objects.equals(historyRent.getClient().getIdClient(), historyRent.getClientRented().getIdClient())) {
            validationNotification(resourceBundle.getString("lessor"));
            isValid = false;
        }
        return isValid;
    }

    /**
     *
     * @param historySale Historial de venta a ser validado
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    public boolean validationHistorySale(HistorySale historySale) {
        boolean isValid = true;

        if (historySale == null) {   // evitamos posible nullPointerException
            validationNotification(resourceBundle.getString("historySale"));
            return false;
        }
        if (historySale.getEstate() == null || historySale.getEstate().equals(new Estate())) {
            validationNotification(resourceBundle.getString("estate"));
            isValid = false;
        }
        if (historySale.getClientActual() == null || historySale.getClientActual().equals(new Client())) {
            validationNotification(resourceBundle.getString("seller"));
            isValid = false;
        }
        if (historySale.getClientPrevious() == null || historySale.getClientPrevious().equals(new Client())) {
            validationNotification(resourceBundle.getString("buyer"));
            isValid = false;
        }
        if (Objects.equals(historySale.getClientActual().getIdClient(), historySale.getClientPrevious().getIdClient())) {
            validationNotification(resourceBundle.getString("buyer"));
            isValid = false;
        }
        if (historySale.getSaleDate() == null) {
            validationNotification(resourceBundle.getString("sale.date"));
            isValid = false;
        }
        if (historySale.getSalePrice() == null || historySale.getSalePrice().compareTo(new BigDecimal(0)) < 0) {
            validationNotification(resourceBundle.getString("sale.price"));
            isValid = false;
        }
        return isValid;
    }

    /**
     * Crea una notificación de error de validación
     * @param text Propiedad no cumplida a mostrar en la notificación
     */
    private void validationNotification(String text) {
        Notifications.create()
                .title(resourceBundle.getString("field") +" "+text+" "+resourceBundle.getString("invalid"))
                .text(resourceBundle.getString("make")+" "+ text +" "+resourceBundle.getString("sure"))
                .showError();
    }
}

