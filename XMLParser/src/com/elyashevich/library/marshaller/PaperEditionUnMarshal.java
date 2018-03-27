package com.elyashevich.library.marshaller;


import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.genre.GenresType;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.entity.paper.PaperEditionList;
import com.elyashevich.library.entity.union.GenrePaperType;
import com.elyashevich.library.entity.union.GenrePapersType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class PaperEditionUnMarshal  {
    private static final String FILE_NAME="C:\\Users\\Максим\\Desktop\\Programming\\Java\\Library\\text\\papers.xml";
    private static final String FILE_NAME_GENRE="C:\\Users\\Максим\\Desktop\\Programming\\Java\\Library\\text\\genres.xml";
    private static final String FILE_NAME_GENRE_PAPER="C:\\Users\\Максим\\Desktop\\Programming\\Java\\Library\\text\\genre_papers.xml";
    public List<PaperEdition> buildPaperListEdition(String filename) {
        PaperEditionList paperEditions = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(PaperEditionList.class);
            Unmarshaller u = jc.createUnmarshaller();
            FileReader reader = new FileReader(filename);
            paperEditions =(PaperEditionList) u.unmarshal(reader);
        } catch (JAXBException | FileNotFoundException e) {
            System.err.println(e);
        }
        return  paperEditions.getPaperEdition();
    }

    public List<Genre> buildGenreListEdition(String filename) {
        GenresType paperEditions = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(GenresType.class);
            Unmarshaller u = jc.createUnmarshaller();
            FileReader reader = new FileReader(filename);
            paperEditions =(GenresType) u.unmarshal(reader);
        } catch (JAXBException | FileNotFoundException e) {
            System.err.println(e);
        }
        return  paperEditions.getGenre();
    }

    public List<GenrePaperType> buildGenrePaperListEdition(String filename) {
        GenrePapersType paperEditions = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(GenrePapersType.class);
            Unmarshaller u = jc.createUnmarshaller();

            FileReader reader = new FileReader(filename);
            paperEditions =(GenrePapersType) u.unmarshal(reader);
        } catch (JAXBException | FileNotFoundException e) {
            System.err.println(e);
        }
        return  paperEditions.getGenrePaper();
    }

    public static void main(String[] args) {
        PaperEditionUnMarshal unMarshal = new PaperEditionUnMarshal();
        System.out.println(unMarshal.buildPaperListEdition(FILE_NAME));
        System.out.println(unMarshal.buildGenreListEdition(FILE_NAME_GENRE));
        System.out.println(unMarshal.buildGenrePaperListEdition(FILE_NAME_GENRE_PAPER));
    }
}
