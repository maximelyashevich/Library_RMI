package com.elyashevich.library.validator;

import com.elyashevich.library.util.RegexComponent;
import com.elyashevich.library.util.TextConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryValidator {
    public static boolean isTitleCorrect(String title){
        boolean result = false;
        if (isLibraryDataCorrect(title)) {
            Pattern patternLogic = Pattern.compile(RegexComponent.TITLE_REGEX);
            Matcher matcher = patternLogic.matcher(title);
            result = matcher.matches();
        }
        return result;
    }

    public static boolean isLibraryDataCorrect(String data){
        return data!=null && !data.isEmpty();
    }

    public static boolean isMoneyCorrect(String money){
        boolean result = false;
        if (isLibraryDataCorrect(money)) {
            Pattern patternLogic = Pattern.compile(RegexComponent.MONEY_REGEX);
            Matcher matcher = patternLogic.matcher(money);
            result = matcher.matches();
        }
        return result && !money.contains(TextConstant.MINUS);
    }

    public static boolean isPeriodicityCorrect(String periodicity){
        boolean result = false;
        if (isLibraryDataCorrect(periodicity)) {
            Pattern patternLogic = Pattern.compile(RegexComponent.PERIODICITY_REGEX);
            Matcher matcher = patternLogic.matcher(periodicity);
            result = matcher.matches();
        }
        return result;
    }
}
