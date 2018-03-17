package com.elyashevich.library.builder;

public enum PaperEditionEnum {
    PAPERS("papers"),
    TITLE("title"),
    DESCRIPTION("description"),
    PERIODICITY("periodicity"),
    PRICE("price"),
    PAPEREDITION("paperEdition"),
    NEWSPAPER("newspaper"),
    MAGAZINE("magazine"),
    BOOK("book"),
    ID("id"),
    CATEGORY("category");
    private String value;

    private PaperEditionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
