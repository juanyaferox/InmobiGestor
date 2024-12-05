package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class UserSessionService {
    /**
     * Usuario logueado
     */
    private User loggedInUser;
}