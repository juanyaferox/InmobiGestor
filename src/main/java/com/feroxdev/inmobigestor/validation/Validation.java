package com.feroxdev.inmobigestor.validation;

import com.feroxdev.inmobigestor.model.*;
import com.feroxdev.inmobigestor.utilities.CsvUtils;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Validation {

    /**
     * @param user: Usuario a ser validado
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    public boolean validationUser(User user) {
        boolean isValid = true;

        if (user == null) {    // evitamos posible nullPointerException
            validationNotification("Usuario");
            return false;
        }
        if (user.getUser().length() > 50 || user.getUser().isEmpty()) {
            validationNotification("Usuario");
            isValid = false;
        }
        if (user.getPassword().length() > 255 || user.getPassword().isEmpty()) {
            validationNotification("Contraseña");
            isValid = false;
        }
        if (user.getEmail().length() > 255) {
            validationNotification("Correo");
            isValid = false;
        }
        if (user.getName().length() > 50 || user.getName().isEmpty()) {
            validationNotification("Nombre");
            isValid = false;
        }
        if (user.getLastname1().length() > 50 || user.getLastname1().isEmpty()) {
            validationNotification("Primer apellido");
            isValid = false;
        }
        if (user.getLastname2().length() > 50) {
            validationNotification("Segundo apellido");
            isValid = false;
        }
        if (user.getDni().length() > 9 || user.getDni().isEmpty()) {
            validationNotification("DNI");
            isValid = false;
        }
        return isValid;
    }

    public boolean validationBranch(Branch branch) {
        boolean isValid = true;

        if (branch == null) {   // evitamos posible nullPointerException
            validationNotification("Sucursal");
            return false;
        }
        if (branch.getTown() == null || branch.getTown().equals(new Town())) {
            validationNotification("Sucursal");
            isValid = false;
        }
        return isValid;
    }

    public boolean validationEstate(Estate estate) {
        boolean isValid = true;

        if (estate == null) {   // evitamos posible nullPointerException
            validationNotification("Inmueble");
            return false;
        }
        if (estate.getBranch() == null || estate.getBranch().equals(new Branch())) {
            validationNotification("Inmueble");
            isValid = false;
        }
        if (estate.getReference() == null || estate.getReference().isEmpty() || estate.getReference().length() > 255) {
            validationNotification("Referencia");
            isValid = false;
        }
        if (estate.getClient() == null || estate.getClient().equals(new Client())) {
            validationNotification("Propietario");
            isValid = false;
        }
        if (estate.getState() == null) {
            validationNotification("Estado");
            isValid = false;
        }
        if (estate.getFullAddress() == null || estate.getFullAddress().isEmpty() || estate.getFullAddress().length() > 500) {
            validationNotification("Dirección");
            isValid = false;
        }
        if (estate.getImagePath().length() > 500) {
            validationNotification("Imagen");
            isValid = false;
        }
        return isValid;
    }

    public boolean validationClient(Client client) {
        boolean isValid = true;

        if (client == null) {   // evitamos posible nullPointerException
            validationNotification("Cliente");
            return false;
        }
        if (client.getBranch() == null || client.getBranch().equals(new Branch())) {
            validationNotification("Cliente");
            isValid = false;
        }
        if (client.getUser() == null || client.getUser().equals(new User())) {
            validationNotification("Cliente");
            isValid = false;
        }
        if (client.getName() == null || client.getName().isEmpty() || client.getName().length() > 50) {
            validationNotification("Nombre");
            isValid = false;
        }
        if (client.getLastname1() == null || client.getLastname1().isEmpty() || client.getLastname1().length() > 50) {
            validationNotification("Primer apellido");
            isValid = false;
        }
        if (client.getLastname2().length() > 50) {
            validationNotification("Segundo apellido");
            isValid = false;
        }
        if (client.getEmail().length() > 255) {
            validationNotification("Correo");
            isValid = false;
        }
        if (client.getDni() == null || client.getDni().length() > 9 || client.getDni().isEmpty()) {
            validationNotification("DNI");
            isValid = false;
        }
        if (client.getPhone().length() > 20) {
            validationNotification("Teléfono");
            isValid = false;
        }
        if (client.getAddress().length() > 255) {
            validationNotification("Dirección");
            isValid = false;
        }
        return isValid;
    }

    public boolean validationHistoryRent(HistoryRent historyRent) {
        boolean isValid = true;

        if (historyRent == null) {   // evitamos posible nullPointerException
            validationNotification("Historial de alquiler");
            return false;
        }
        if (historyRent.getEstate() == null || historyRent.getEstate().equals(new Estate())) {
            validationNotification("Inmueble");
            isValid = false;
        }
        if (historyRent.getClient() == null || historyRent.getClient().equals(new Client())) {
            validationNotification("Arrendador");
            isValid = false;
        }
        if (historyRent.getClientRented() == null || historyRent.getClientRented().equals(new Client())) {
            validationNotification("Arrendatario");
            isValid = false;
        }
        if (historyRent.getStartDate() == null) {
            validationNotification("Fecha de inicio");
            isValid = false;
        }
        if (historyRent.getEndDate() == null) {
            validationNotification("Fecha de fin");
            isValid = false;
        }
        if (historyRent.getEndDate() != null && historyRent.getStartDate() != null && historyRent.getStartDate().isAfter(historyRent.getEndDate())) {
            validationNotification("Fecha de inicio y fecha de fin");
            isValid = false;
        }
        if (historyRent.getRentPrice() == null || historyRent.getRentPrice().compareTo(new BigDecimal(0)) < 0) {
            validationNotification("Precio de alquiler");
            isValid = false;
        }
        if (historyRent.getClient() == historyRent.getClientRented()) {
            validationNotification("Arrendador");
            isValid = false;
        }
        return isValid;
    }

    public boolean validationHistorySale(HistorySale historySale) {
        boolean isValid = true;

        if (historySale == null) {   // evitamos posible nullPointerException
            validationNotification("Historial de venta");
            return false;
        }
        if (historySale.getEstate() == null || historySale.getEstate().equals(new Estate())) {
            validationNotification("Inmueble");
            isValid = false;
        }
        if (historySale.getClientActual() == null || historySale.getClientActual().equals(new Client())) {
            validationNotification("Vendedor");
            isValid = false;
        }
        if (historySale.getClientPrevious() == null || historySale.getClientPrevious().equals(new Client())) {
            validationNotification("Comprador");
            isValid = false;
        }
        if (historySale.getSaleDate() == null) {
            validationNotification("Fecha de venta");
            isValid = false;
        }
        if (historySale.getSalePrice() == null || historySale.getSalePrice().compareTo(new BigDecimal(0)) < 0) {
            validationNotification("Precio de venta");
            isValid = false;
        }
        return isValid;
    }


    private void validationNotification(String text) {
        Notifications.create()
                .title("Campo " + text + " inválido")
                .text("Asegurese de que el campo " + text + " es correcto")
                .showError();
    }
}

