package com.elyashevich.library.marshaller;


import com.elyashevich.library.entity.paper.PaperEditionList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class PaperEditionUnMarshal  {
    private static final String FILE_NAME="XMLParser\\text\\papers.xml";
    private static final String FILE_NAME_GENRE="XMLParser\\text\\genres.xml";
    private static final String FILE_NAME_GENRE_PAPER="XMLParser\\text\\genre_papers.xml";
    public PaperEditionList buildPaperListEdition(String filename) {
        PaperEditionList paperEditions = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(PaperEditionList.class);
            Unmarshaller u = jc.createUnmarshaller();
            FileReader reader = new FileReader(filename);
            paperEditions =(PaperEditionList) u.unmarshal(reader);
        } catch (JAXBException | FileNotFoundException e) {
            System.err.println(e);
        }
        return paperEditions;
    }

    public static void main(String[] args) {
        PaperEditionUnMarshal unMarshal = new PaperEditionUnMarshal();
        System.out.println(unMarshal.buildPaperListEdition(FILE_NAME));
        System.out.println();
    }
}
