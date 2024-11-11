package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Users;

import java.util.List;

public interface UserService {
    Users GetUserById(int id);
    Users GetUserByUsername(String user);
    Users changeInfoUser(Users user);
    List<Users> allUsersList();

    Users deleteUser(Users user);
}
