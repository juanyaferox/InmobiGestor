package com.feroxdev.inmobigestor.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumClient {
    HOUSE_OWNER("Poseedor"),
    RENTER("Inquilino"),
    RENTER_AND_HOUSE_OWNER("Poseedor e inquilino"),
    ANOTHER("Otro"), // Sin utilizar en la aplicación
    INACTIVE("Inactivo");

    private final String type;
}
