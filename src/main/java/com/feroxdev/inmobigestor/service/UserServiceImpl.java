package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Users;
import com.feroxdev.inmobigestor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene el usuario apartir de su id
     * @param id
     * @return
     */
    @Override
    public Users GetUserById(int id) {
        return userRepository.getReferenceById(id);
    }

    /**
     * Obtiene el Usuario segun su nombre de usuario
     * @param user: nombre del usuario
     * @return objeto Users
     */
    @Override
    public Users GetUserByUsername(String user) {
        return userRepository.findByUser(user);
    }

    /**
     * Se cambia la información del usuario en la bbdd
     * @param user usuario a cambiar la informacion
     * @return usuario con la información alterada
     */
    @Override
    public Users changeInfoUser(Users user) {

        //copia del usuario de la bbdd
        Users userToChange = userRepository.getReferenceById(user.getIdUser());

        //se cambia campo por campo
            userToChange.setUser(user.getUser());
            userToChange.setPassword(user.getPassword());
            userToChange.setLastname1(user.getLastname1());
            userToChange.setLastname2(user.getLastname2());
            userToChange.setDni(user.getDni());
            userToChange.setName(user.getName());
            userToChange.setEmail(user.getEmail());

        return userRepository.save(userToChange);
    }
}
