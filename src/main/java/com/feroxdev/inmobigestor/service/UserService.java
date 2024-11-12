package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.User;

import java.util.List;

public interface UserService {
    User GetUserById(int id);
    User GetUserByUsername(String user);
    User changeInfoUser(User user);
    List<User> allUsersList();
    User deleteUser(User user);
}
