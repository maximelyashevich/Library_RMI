package com.elyashevich.library.util;

public final class RegexComponent {
    public static final String TITLE_REGEX = "^.{1,150}$";
    public static final String MONEY_REGEX = "^-?[0-9]+(?:\\.[0-9]{1,5})?";
    public static final String PERIODICITY_REGEX = "[1-6]";
}
