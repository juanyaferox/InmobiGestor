package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Users;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class UserSessionService {
    private Users loggedInUser;
}