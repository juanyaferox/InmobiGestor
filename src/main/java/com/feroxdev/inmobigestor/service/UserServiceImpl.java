package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.repository.BranchRepository;
import com.feroxdev.inmobigestor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
}
