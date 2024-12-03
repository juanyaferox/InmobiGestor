package com.feroxdev.inmobigestor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class HistoryDTO {
    String operation;
    String client;
    LocalDate date;
    String precio;
}
