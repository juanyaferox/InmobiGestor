package com.feroxdev.inmobigestor.validation;

import com.feroxdev.inmobigestor.model.*;
import com.feroxdev.inmobigestor.utilities.CsvUtils;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;

@Component
public class Validation {

    /**
     * @param user: Usuario a ser validado
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    public boolean validationUser(User user) {
        boolean isValid = true;

        if (user==null){    // evitamos posible nullPointerException
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

    public boolean validationBranch(Branch branch){
        boolean isValid = true;

        if (branch==null){   // evitamos posible nullPointerException
            validationNotification("Sucursal");
            return false;
        }
        if (branch.getTown()==null || branch.getTown().equals(new Town())){
            validationNotification("Sucursal");
            isValid = false;
        }
        return isValid;
    }

    public boolean validationEstate(Estate estate){
        boolean isValid = true;

        if (estate==null){   // evitamos posible nullPointerException
            validationNotification("Inmueble");
            return false;
        }
        if (estate.getBranch()==null || estate.getBranch().equals(new Branch())){
            validationNotification("Inmueble");
            isValid = false;
        }
        if (estate.getReference().isEmpty() || estate.getReference().length() > 255){
            validationNotification("Referencia");
            isValid = false;
        }
        if (estate.getClient() == null || estate.getClient().equals(new Client())){
            validationNotification("Propietario");
            isValid = false;
        }
        if (estate.getState() == null){
            validationNotification("Estado");
            isValid = false;
        }
        if (estate.getFullAddress() == null || estate.getFullAddress().isEmpty() || estate.getFullAddress().length() > 500){
            validationNotification("Dirección");
            isValid = false;
        }
        if (estate.getImagePath().length() > 500){
            validationNotification("Imagen");
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
