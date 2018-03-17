package com.elyashevich.library.marshaller;

import com.elyashevich.library.builder.PaperEditionStAXBuilder;
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
import java.util.Set;

public class PaperEditionMarshal {
    private static final String FILE_NAME="XMLParser\\text\\papers.xml";
    private static final String FILE_NAME_GENRE="XMLParser\\text\\genres.xml";
    private static final String FILE_NAME_GENRE_PAPER="XMLParser\\text\\genre_papers.xml";
    /*public List<Genre> marshalingGenres(String filename) {
        GenresType st = null;
        try {
            JAXBContext context = JAXBContext.newInstance(GenresType.class);
            Marshaller m = context.createMarshaller();
            st = new GenresType() {
                {
                    PaperEditionStAXBuilder paperEditionStAXBuilder = new PaperEditionStAXBuilder();
                    paperEditionStAXBuilder.buildSetGenres(FILE_NAME_GENRE);
                    Set<Genre> editionSet = paperEditionStAXBuilder.getGenres();
                    for(Genre paperEdition: editionSet) {
                        this.getGenre().add(paperEdition);
                    }
                }
            };
            System.out.println(st);
            m.marshal(st, new FileOutputStream(filename));
        } catch (FileNotFoundException | JAXBException e) {
            System.err.println(e);
        }
        return st;
    }*/

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
                m.marshal(st, new FileOutputStream("XMLParser\\text\\papers_2.xml"));
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
            m.marshal(st, new FileOutputStream("XMLParser\\text\\genres_2.xml"));
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
            m.marshal(st, new FileOutputStream("XMLParser\\text\\genre_papers_2.xml"));
        } catch (FileNotFoundException | JAXBException e) {
            System.err.println(e);
        }
    }

    /*public void marshalingPaperEdition(String filename) {
        PaperEditionList st = null;
        try {
            JAXBContext context = JAXBContext.newInstance(PaperEditionList.class);
            Marshaller m = context.createMarshaller();
            st = new PaperEditionList() {
                {
                    PaperEditionStAXBuilder paperEditionStAXBuilder = new PaperEditionStAXBuilder();
                    paperEditionStAXBuilder.buildSetPaperEdition(FILE_NAME);
                    Set<PaperEdition> editionSet = paperEditionStAXBuilder.getPapers();
                    for(PaperEdition paperEdition: editionSet) {
                        this.getPaperEdition().add(paperEdition);
                    }
                }
            };
            System.out.println(st);
            m.marshal(st, new FileOutputStream(filename));
        } catch (FileNotFoundException | JAXBException e) {
            System.err.println(e);
        }
      //  return st;
    }*/

    public GenrePapersType marshalingGenrePaper(String filename) {
        GenrePapersType st = null;
        try {
            JAXBContext context = JAXBContext.newInstance(GenrePapersType.class);
            Marshaller m = context.createMarshaller();
            st = new GenrePapersType() {
                {
                    PaperEditionStAXBuilder paperEditionStAXBuilder = new PaperEditionStAXBuilder();
                    paperEditionStAXBuilder.buildSetGenrePapers(FILE_NAME_GENRE_PAPER);
                    Set<GenrePaperType> editionSet = paperEditionStAXBuilder.getGenrePaperTypes();
                    for(GenrePaperType paperEdition: editionSet) {
                        this.getGenrePaper().add(paperEdition);
                    }
                }
            };
            System.out.println(st);
            m.marshal(st, new FileOutputStream(filename));
        } catch (FileNotFoundException | JAXBException e) {
            System.err.println(e);
        }
        return st;
    }

   /* public static void main(String[] args) {
        PaperEditionMarshal marshal = new PaperEditionMarshal();
        marshal.marshalingPaperEdition("XMLParser\\text\\marshalling_paperEdition.xml");
        PaperEditionUnMarshal unMarshal = new PaperEditionUnMarshal();
        System.out.println(unMarshal.buildPaperListEdition(FILE_NAME));
        System.out.println();
    }*/
}
