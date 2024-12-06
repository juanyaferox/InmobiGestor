package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Branch;
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

    @Override
    public User changeInfoUser(User user) {
        return changeInfoUser(user, null);
    }

    /**
     * Se cambia la información del usuario en la bbdd
     * @param user usuario a cambiar la informacion
     * @return usuario con la información alterada
     */
    @Override
    public User changeInfoUser(User user, String oldDni) {

        if (user.getBranch() == null && user.getIdUser() == 0) { // Caso no sea el admin no entra aquí
            var listUsersAdmin = userRepository.findByDni(oldDni);
            for (User userAdminBranch : listUsersAdmin) {
                if (userAdminBranch.getIdUser() != 0) { // Evitamos volver a modificar el admin general
                    userAdminBranch.setPassword(user.getPassword());
                    userAdminBranch.setLastname1(user.getLastname1());
                    userAdminBranch.setLastname2(user.getLastname2());
                    userAdminBranch.setDni(user.getDni());
                    userAdminBranch.setName(user.getName());
                    userAdminBranch.setEmail(user.getEmail());
                    userRepository.save(userAdminBranch);
                }
            }
        }

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

    /**
     * Añade un usuario al administrador a una sucursal concreta
     * @param branch sucursal a la que se le añade el usuario
     * @param user usuario a añadir
     * @return usuario añadido
     */
    @Override
    public User addAdminUserBranch(Branch branch, User user) {
        var userToSave = new User();
        userToSave = user.toBuilder().build(); // se copia el usuario para no setear los campos 1x1
        userToSave.setUser(branch.getTown().getIdTown().toString());
        userToSave.setIdUser(null); // para que no se sobreescriba el usuario
        userToSave.setBranch(branch);
        return userRepository.save(userToSave);
    }


}
