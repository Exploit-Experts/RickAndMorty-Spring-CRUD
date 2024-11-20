package com.rickmorty.enums;

import com.rickmorty.exceptions.InvalidParameterException;

public enum SortFavoriteField {
    ID("id"),
    API_ID("apiId"),
    ITEM_TYPE("itemType");

    private final String field;

    SortFavoriteField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static boolean isValid(String value) {
        for (SortFavoriteField sortField : values()) {
            if (sortField.getField().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public static SortFavoriteField fromValue(String value) {
        for (SortFavoriteField sortField : values()) {
            if (sortField.getField().equalsIgnoreCase(value)) {
                return sortField;
            }
        }
        throw new InvalidParameterException("Campo sort inválido: " + value);
    }
}
