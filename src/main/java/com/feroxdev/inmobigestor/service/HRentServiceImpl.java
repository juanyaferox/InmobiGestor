package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.repository.BranchRepository;
import com.feroxdev.inmobigestor.repository.HRentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HRentServiceImpl implements HRentService{

    @Autowired
    private HRentRepository hRentRepository;
}
