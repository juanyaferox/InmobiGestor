package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Users;
import com.feroxdev.inmobigestor.repository.BranchRepository;
import com.feroxdev.inmobigestor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Users GetUserById(int id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public Users GetUserByUsername(String user) {
        return userRepository.findByUser(user);
    }

    @Override
    public Users changeInfoUser(Users user) {
        Users userToChange = userRepository.getReferenceById(user.getIdUser());

        if (!userToChange.getUser().equals(user.getUser()))
            userToChange.setUser(user.getUser());

        if (!userToChange.getPassword().equals(user.getPassword()))
            userToChange.setPassword(user.getPassword());

        if (!userToChange.getLastname1().equals(user.getLastname1()))
            userToChange.setLastname1(user.getLastname1());

        if (!userToChange.getLastname2().equals(user.getLastname2()))
            userToChange.setLastname2(user.getLastname2());

        if (!userToChange.getDni().equals(user.getDni()))
            userToChange.setDni(user.getDni());

        if (!userToChange.getName().equals(user.getName()))
            userToChange.setName(user.getName());

        if (!userToChange.getEmail().equals(user.getEmail()))
            userToChange.setEmail(user.getEmail());

        userRepository.save(userToChange);

        return userToChange;
    }
}
