package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.User;
import com.feroxdev.inmobigestor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public User GetUserById(int id) {
        return userRepository.getReferenceById(id);
    }

    /**
     * Obtiene el Usuario segun su nombre de usuario
     * @param user: nombre del usuario
     * @return objeto User
     */
    @Override
    public User GetUserByUsername(String user) {
        var optUser = userRepository.findByUser(user);
        return optUser.orElse(null);
    }

    /**
     * Se cambia la información del usuario en la bbdd
     * @param user usuario a cambiar la informacion
     * @return usuario con la información alterada
     */
    @Override
    public User changeInfoUser(User user) {

        //copia del usuario de la bbdd
        User userToChange = userRepository.getReferenceById(user.getIdUser());

        //se cambia campo por campo
            userToChange.setUser(user.getUser());
            userToChange.setPassword(user.getPassword());
            userToChange.setLastname1(user.getLastname1());
            userToChange.setLastname2(user.getLastname2());
            userToChange.setDni(user.getDni());
            userToChange.setName(user.getName());
            userToChange.setEmail(user.getEmail());
            userToChange.setBranch(user.getBranch());

        return userRepository.save(userToChange);
    }

    /**
     * Obtiene todos los usuarios
     * @return lista de usuarios
     */
    @Override
    public List<User> allUsersList() {
        return userRepository.findAll();
    }

    /**
     * Elimina un usuario
     * @param user usuario a eliminar
     * @return usuario eliminado
     */
    @Override
    public User deleteUser(User user) {
        Integer id= user.getIdUser();
        Optional<User> optionalUsers = userRepository.findById(id);
        if (optionalUsers.isPresent()){
            var userGet = optionalUsers.get();
            if (userGet.getIdUser() != 0){//nos aseguramos de que no sea el admin general
                userRepository.delete(user);
                return user;
            }
            return null;
        }
        return null;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    
}
