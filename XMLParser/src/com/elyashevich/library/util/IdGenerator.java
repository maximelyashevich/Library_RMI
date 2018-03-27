package com.elyashevich.library.util;

import com.elyashevich.library.entity.paper.PaperEdition;
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
    public static String generatePaperId(ArrayList<PaperEdition>list){
        list.sort((s1, s2) -> {
            String firstId = s1.getId().replace("ID-", "");
            String secondId = s2.getId().replace("ID-", "");
            return Integer.parseInt(firstId)-Integer.parseInt(secondId);
        });
        System.out.println();
        System.out.println(list);
        System.out.println();
        String lastId = list.isEmpty()?"0":(list.get(list.size()-1).getId());
        if (lastId.contains("ID-")) lastId = lastId.replace("ID-", "");
        System.out.println("-------start generation---------");
        System.out.println(lastId);
        for (PaperEdition paperEdition: list){
            if (("ID-"+(Long.parseLong(lastId)+3)).equals(paperEdition.getId())){
                return "ID-"+(Long.parseLong(lastId)+4);
            }
        }
        return "ID-"+(Long.parseLong(lastId)+1);
    }
}
