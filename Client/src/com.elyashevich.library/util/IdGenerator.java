package com.elyashevich.library.util;

import com.elyashevich.library.entity.union.GenrePaperType;

import java.util.ArrayList;

public class IdGenerator {
    public static long defineID(String str){
        return str.contains("ID-")?Long.parseLong(str.replace("ID-", "")):Long.parseLong(str);
    }
    public static String generateId(ArrayList<GenrePaperType> list){
        String lastId = list.isEmpty()?"0":(list.get(list.size()-1).getId());
        if (lastId.contains("ID-")) lastId = lastId.replace("ID-", "");
        return "ID-"+(Long.parseLong(lastId)+1);
    }
}
