package com.feroxdev.inmobigestor.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
public enum EnumEstate {

    ANOTHER("Otros"), //0
    ON_SALE("En venta"), //1
    SOLD("Vendido"), //2
    FOR_RENT("En alquiler"), //3
    RENTED("Alquilado"), //4
    INACTIVE("Inactivo"), //5
    FOR_RENT_AND_SALE("En venta y en alquiler"); //6

    private final String description;
}
