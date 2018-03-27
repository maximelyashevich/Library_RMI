package com.elyashevich.library.marshaller;

import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.genre.GenresType;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.entity.paper.PaperEditionList;
import com.elyashevich.library.entity.union.GenrePaperType;
import com.elyashevich.library.entity.union.GenrePapersType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PaperEditionMarshal {
    private static final String FILE_NAME="C:\\Users\\Максим\\Desktop\\Programming\\Java\\Library\\text\\papers.xml";
    private static final String FILE_NAME_GENRE="C:\\Users\\Максим\\Desktop\\Programming\\Java\\Library\\text\\genres.xml";
    private static final String FILE_NAME_GENRE_PAPER="C:\\Users\\Максим\\Desktop\\Programming\\Java\\Library\\text\\genre_papers.xml";

    public static void marshalingPaper(ArrayList<PaperEdition> list){
            PaperEditionList st = null;
            try {
                JAXBContext context = JAXBContext.newInstance(PaperEditionList.class);
                Marshaller m = context.createMarshaller();
                st = new PaperEditionList() {
                    {
                        for(PaperEdition paperEdition: list) {
                            this.getPaperEdition().add(paperEdition);
                        }
                    }
                };
                m.marshal(st, new FileOutputStream(FILE_NAME));
        } catch (FileNotFoundException | JAXBException e) {
            System.err.println(e);
        }
    }

    public static void marshalingGenre(ArrayList<Genre> list){
        GenresType st = null;
        try {
            JAXBContext context = JAXBContext.newInstance(GenresType.class);
            Marshaller m = context.createMarshaller();
            st = new GenresType() {
                {
                    for(Genre paperEdition: list) {
                        this.getGenre().add(paperEdition);
                    }
                }
            };
            m.marshal(st, new FileOutputStream(FILE_NAME_GENRE));
        } catch (FileNotFoundException | JAXBException e) {
            System.err.println(e);
        }
    }

    public static void marshalingGenrePapers(ArrayList<GenrePaperType> list){
        GenrePapersType st = null;
        try {
            JAXBContext context = JAXBContext.newInstance(GenrePapersType.class);
            Marshaller m = context.createMarshaller();
            st = new GenrePapersType() {
                {
                    for(GenrePaperType paperEdition: list) {
                        this.getGenrePaper().add(paperEdition);
                    }
                }
            };
            m.marshal(st, new FileOutputStream(FILE_NAME_GENRE_PAPER));
        } catch (FileNotFoundException | JAXBException e) {
            System.err.println(e);
        }
    }

   /* public static void main(String[] args) {
        PaperEditionMarshal marshal = new PaperEditionMarshal();
        marshal.marshalingPaperEdition("XMLParser\\text\\marshalling_paperEdition.xml");
        PaperEditionUnMarshal unMarshal = new PaperEditionUnMarshal();
        System.out.println(unMarshal.buildPaperListEdition(FILE_NAME));
        System.out.println();
    }*/
}
