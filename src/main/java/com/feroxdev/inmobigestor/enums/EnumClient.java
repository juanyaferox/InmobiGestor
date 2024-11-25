package com.feroxdev.inmobigestor.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumClient {
    HOUSE_OWNER("Poseedor"), //1
    RENTER("Inquilino"),//2
    RENTER_AND_HOUSE_OWNER("Poseedor e inquilino"),//3
    ANOTHER("Otro");//0

    private final String type;
}
