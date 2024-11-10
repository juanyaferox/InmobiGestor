package com.feroxdev.inmobigestor.validation;

import com.feroxdev.inmobigestor.model.Users;
import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;

@Component
public class ModifyUserValidation {

    /**
     * @param user: Usuario a ser validado
     * @return true si está todo correcto, false si no paso alguna validacion
     */
    public boolean validationUser(Users user) {
        boolean isValid = true;

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

    private void validationNotification(String text) {
        Notifications.create()
                .title("Campo " + text + " inválido")
                .text("Asegurese de que el campo " + text + " es correcto")
                .showError();
    }


    //función para calcular el dni (no se usar para evitar molestias)

}
