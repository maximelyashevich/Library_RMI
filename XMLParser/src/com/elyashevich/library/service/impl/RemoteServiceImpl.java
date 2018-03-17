package com.elyashevich.library.service.impl;

import com.elyashevich.library.builder.PaperEditionStAXBuilder;
import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.entity.union.GenrePaperType;
import com.elyashevich.library.marshaller.PaperEditionMarshal;
import com.elyashevich.library.service.RemoteService;
import com.elyashevich.library.util.IdGenerator;

import java.rmi.RemoteException;
import java.util.*;

public class RemoteServiceImpl implements RemoteService {
    private static final String FILE_NAME="XMLParser\\text\\papers.xml";
    private static final String FILE_NAME_GENRE="XMLParser\\text\\genres.xml";
    private static final String FILE_NAME_GENRE_PAPER="XMLParser\\text\\genre_papers.xml";

    //private PaperEditionMarshal marshal = new PaperEditionMarshal();
    private PaperEditionStAXBuilder paperEditionStAXBuilder = new PaperEditionStAXBuilder();

    @Override
    public void insertGenreToPaper(Genre genre, PaperEdition paperEdition) throws RemoteException {
        ArrayList<GenrePaperType> list = (ArrayList<GenrePaperType>) findAllGenrePaperTypes();
        GenrePaperType genrePaperType = new GenrePaperType(genre.getId(), paperEdition.getId());
        genrePaperType.setId(IdGenerator.generateId(list));
        list.add(genrePaperType);
        //GenrePapersType genrePapersType = new GenrePapersType();
        //genrePapersType.getGenrePaper().addAll(list);
        System.out.println(list);
        PaperEditionMarshal.marshalingGenrePapers(list);
    }

    private ArrayList<GenrePaperType> findAllGenrePaperTypes(){
        paperEditionStAXBuilder.buildSetGenrePapers(FILE_NAME_GENRE_PAPER);
        return new ArrayList<>(paperEditionStAXBuilder.getGenrePaperTypes());
    }
    @Override
    public void deleteGenrePaper(long paperEditionId) throws RemoteException {
        ArrayList<GenrePaperType> list = (ArrayList<GenrePaperType>) findAllGenrePaperTypes();
        for (GenrePaperType genrePaperType: list){
            if (IdGenerator.defineID(genrePaperType.getPaperEditionID())==paperEditionId){
                list.remove(genrePaperType);
                break;
            }
        }
        System.out.println(list);
        PaperEditionMarshal.marshalingGenrePapers(list);
    }

    @Override
    public boolean updateGenrePaper(long genreIdNew, long genreIdOld, long paperId) throws RemoteException {
        ArrayList<GenrePaperType> list = (ArrayList<GenrePaperType>) findAllGenrePaperTypes();
        for (GenrePaperType genrePaperType: list){
            System.out.println("updateGenreID");
            if (IdGenerator.defineID(genrePaperType.getPaperEditionID())==paperId &&
                    IdGenerator.defineID(genrePaperType.getGenreID())==genreIdOld){
                int index = list.indexOf(genrePaperType);
                genrePaperType.setGenreID("ID-"+genreIdNew);
                list.set(index, genrePaperType);
                PaperEditionMarshal.marshalingGenrePapers(list);
                return true;
            }
        }
        System.out.println(list);
        PaperEditionMarshal.marshalingGenrePapers(list);
        return false;
    }

    @Override
    public void createGenrePaper(long genreId, long paperId) throws RemoteException {
        ArrayList<GenrePaperType> list = (ArrayList<GenrePaperType>) findAllGenrePaperTypes();
        GenrePaperType genrePaperType = new GenrePaperType("ID-"+genreId, "ID-"+paperId);
        System.out.println();
        genrePaperType.setId(IdGenerator.generateId(list));
        list.add(genrePaperType);
        System.out.println(list);
        PaperEditionMarshal.marshalingGenrePapers(list);
    }

    @Override
    public void updateGenrePaper(PaperEdition paperEdition, HashSet<String> data) throws RemoteException {
        Genre genre;
        System.out.println("updateGENRE_PAPER: "+paperEdition.getId());
        deleteGenrePaper(IdGenerator.defineID(paperEdition.getId()));
        for (Object dataComponent : data) {
            genre = findByPaperGenreNameGenres(dataComponent.toString());
            createGenrePaper(IdGenerator.defineID(genre.getId()), IdGenerator.defineID(paperEdition.getId()));
        }
        System.out.println("123");
        PaperEditionMarshal.marshalingGenrePapers((ArrayList<GenrePaperType>) findAllGenrePaperTypes());
    }

    @Override
    public ArrayList<Genre> findAllGenres() throws RemoteException {
                paperEditionStAXBuilder.buildSetGenres(FILE_NAME_GENRE);
                return new ArrayList<>(paperEditionStAXBuilder.getGenres());
    }

    @Override
    public Set<Genre> findByPaperIDGenres(long id) throws RemoteException {
        ArrayList<GenrePaperType>genrePaperTypes = (ArrayList<GenrePaperType>) findAllGenrePaperTypes();
        Set<Genre> genreSet = new HashSet<>();
        for (GenrePaperType genrePaperType: genrePaperTypes){
            if (genrePaperType.getPaperEditionID().contains("ID-") && genrePaperType.getPaperEditionID().equals("ID-" + id)) {
                genreSet.add(defineGenreById(genrePaperType.getGenreID()));
            } else{
                if (genrePaperType.getPaperEditionID().equals(String.valueOf(id))){
                    genreSet.add(defineGenreById(genrePaperType.getGenreID()));
                }
            }
        }
        return genreSet;
    }

    private Genre defineGenreById(String id) throws RemoteException {
        ArrayList<Genre> list = (ArrayList<Genre>) findAllGenres();
        for (Genre genre:list){
            if (genre.getId().contains("ID-") && genre.getId().equals("ID-"+id)){
                return genre;
            } else{
                if (genre.getId().equals(String.valueOf(id))) {
                    return genre;
                }
            }
        }
        return null;
    }

    @Override
    public Genre findByPaperGenreNameGenres(String data) throws RemoteException {
        ArrayList<Genre> list = findAllGenres();
        for (Genre genre:list){
            if (genre.getName().contains(data)){
                return genre;
            }
        }
        return null;
    }

    @Override
    public ArrayList<PaperEdition> findByDescriptionPapers(String data) throws RemoteException {
        ArrayList<PaperEdition> list = findAllPapers();
        ArrayList<PaperEdition> arrayList = new ArrayList<>();
        for (PaperEdition paperEdition: list){
            if ((paperEdition.getTitle()+paperEdition.getDescription()).contains(data)){
                arrayList.add(paperEdition);
            }
        }
        return arrayList;
    }

    @Override
    public PaperEdition findPaperByIdPapers(long id) throws RemoteException {
        for (PaperEdition paperEdition1: findAllPapers()){
            if (paperEdition1.getId().equals(String.valueOf(id)) || paperEdition1.getId().equals("ID-"+id)){
                return paperEdition1;
            }
        }
        return null;
    }

    @Override
    public boolean deleteByIdPapers(long paperId) throws RemoteException {
        ArrayList<PaperEdition> list = findAllPapers();
        Iterator<PaperEdition> iterator = list.iterator();
        PaperEdition paperEdition;
        while (iterator.hasNext()){
            paperEdition = iterator.next();
            if (paperEdition.getId().equals(String.valueOf(paperId)) || paperEdition.getId().equals("ID-"+paperId)){
                iterator.remove();
            }
        }
        System.out.println(list);
        PaperEditionMarshal.marshalingPaper(list);
        return true;
    }

    @Override
    public ArrayList<PaperEdition> findAllPapers() throws RemoteException {
                paperEditionStAXBuilder.buildSetPaperEdition(FILE_NAME);
               return new ArrayList<>(paperEditionStAXBuilder.getPapers());
    }

    @Override
    public boolean updatePapers(PaperEdition paperEdition) throws RemoteException {
        ArrayList<PaperEdition> list = findAllPapers();
        for (PaperEdition paperEdition1: list){
            if (paperEdition1.getId().equals(paperEdition.getId()) || paperEdition1.getId().equals("ID-"+paperEdition.getId())){
                int index = list.indexOf(paperEdition1);
                list.set(index, paperEdition);
            }
        }
        System.out.println(list);
        PaperEditionMarshal.marshalingPaper(list);
        return true;
    }

    @Override
    public PaperEdition createPapers(PaperEdition paperEdition) throws RemoteException {
        ArrayList<PaperEdition> list = (ArrayList<PaperEdition>) findAllPapers();
        list.add(paperEdition);
        paperEdition.setId(IdGenerator.generatePaperId(list));
        System.out.println(list);
        PaperEditionMarshal.marshalingPaper(list);
        return null;
    }

    @Override
    public boolean createPapers(PaperEdition paperEdition, String[] genreNames) throws RemoteException {
        ArrayList<PaperEdition> list = (ArrayList<PaperEdition>) findAllPapers();
        paperEdition.setId(IdGenerator.generatePaperId(list));
        list.add(paperEdition);
        System.out.println("Start creating papers on server...");
        PaperEditionMarshal.marshalingPaper(list);
        return true;
    }
}
