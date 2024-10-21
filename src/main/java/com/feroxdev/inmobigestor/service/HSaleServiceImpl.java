package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.repository.HSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HSaleServiceImpl implements HSaleService {

    @Autowired
    private HSaleRepository hSaleRepository;
}
