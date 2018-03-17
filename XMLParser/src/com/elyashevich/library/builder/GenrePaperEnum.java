package com.elyashevich.library.builder;

public enum GenrePaperEnum {
    GENREPAPERS("genrePapers"),
    PAPEREDITIONID("paperEditionID"),
    GENREID("genreID"),
    GENREPAPER("genrePaper"),
    ID("id");
    private String value;

    private GenrePaperEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
