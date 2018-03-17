package com.elyashevich.library.service;


import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.paper.PaperEdition;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface RemoteService extends Remote {
        void insertGenreToPaper(Genre genre, PaperEdition paperEdition) throws RemoteException;

        void deleteGenrePaper(long paperEditionId) throws RemoteException;

        boolean updateGenrePaper(long genreIdNew, long genreIdOld, long paperId) throws RemoteException;

        void createGenrePaper(long genreId, long paperId) throws RemoteException;

        void updateGenrePaper(PaperEdition paperEdition, HashSet<String> data) throws RemoteException;

        List<Genre> findAllGenres() throws RemoteException;

        Set<Genre> findByPaperIDGenres(long id) throws RemoteException;

        Genre findByPaperGenreNameGenres(String data) throws RemoteException;

        List<PaperEdition> findByDescriptionPapers(String data) throws RemoteException;

        PaperEdition findPaperByIdPapers(long id) throws RemoteException;

        boolean deleteByIdPapers(long paperId) throws RemoteException;

        List<PaperEdition> findAllPapers() throws RemoteException;

        boolean updatePapers(PaperEdition paperEdition) throws RemoteException;

        PaperEdition createPapers(PaperEdition paperEdition) throws RemoteException;

        boolean createPapers(PaperEdition paperEdition, String[] genreNames) throws RemoteException;

}
