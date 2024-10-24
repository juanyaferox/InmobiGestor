package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Users;

public interface UserService {
    Users GetUserById(int id);
    Users GetUserByUsername(String user);
    Users changeInfoUser(Users user);
}
