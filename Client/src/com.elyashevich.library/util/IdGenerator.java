package com.elyashevich.library.util;

import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.entity.union.GenrePaperType;

import java.util.ArrayList;

public class IdGenerator {
    public static long defineID(String str){
        System.out.println("define ID");
        return str.contains("ID-")?Long.parseLong(str.replace("ID-", "")):Long.parseLong(str);
    }
    public static String generateId(ArrayList<GenrePaperType> list){
        String lastId = list.get(list.size()-1).getId();
        System.out.println("Generate ID into genreId");
        if (lastId.contains("ID-")) lastId=lastId.replaceAll("ID-", "");
        lastId = String.valueOf(Integer.parseInt(lastId)+1);
        return lastId;
    }
    public static String generatePaperId(ArrayList<PaperEdition>list){
        String lastId = list.get(list.size()-1).getId();
        System.out.println("Generate ID into paperId");
        if (lastId.contains("ID-")) lastId = lastId.replaceAll("ID-", "");
        lastId = String.valueOf(Integer.parseInt(lastId)+1);
        return lastId;
    }
}
