package com.elyashevich.library.builder;

public enum GenreEnum {
    GENRES("genres"),
    NAME("name"),
    GENRE("genre"),
    ID("id");
    private String value;

    private GenreEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
