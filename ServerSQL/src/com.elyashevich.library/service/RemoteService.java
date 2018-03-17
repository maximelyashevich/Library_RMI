package com.elyashevich.library.service;

import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.exception.DAOTechnicalException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface RemoteService extends Remote {
    void insertGenreToPaper(Genre genre, PaperEdition paperEdition) throws DAOTechnicalException, RemoteException;

    void deleteGenrePaper(long paperEditionId) throws DAOTechnicalException, RemoteException;

    boolean updateGenrePaper(long genreIdNew, long genreIdOld, long paperId) throws DAOTechnicalException, RemoteException;

    void createGenrePaper(long genreId, long paperId) throws DAOTechnicalException, RemoteException;

    void updateGenrePaper(PaperEdition paperEdition, HashSet<String> data) throws DAOTechnicalException, RemoteException;

    List<Genre> findAllGenres() throws DAOTechnicalException, RemoteException;

    Set<Genre> findByPaperIDGenres(long id) throws DAOTechnicalException, RemoteException;

    Genre findByPaperGenreNameGenres(String data) throws DAOTechnicalException, RemoteException;

    List<PaperEdition> findByDescriptionPapers(String data) throws DAOTechnicalException, RemoteException;

    PaperEdition findPaperByIdPapers(long id) throws DAOTechnicalException, RemoteException;

    boolean deleteByIdPapers(long paperId) throws DAOTechnicalException, RemoteException;

    List<PaperEdition> findAllPapers() throws DAOTechnicalException, RemoteException;

    boolean updatePapers(PaperEdition paperEdition) throws DAOTechnicalException, RemoteException;

    PaperEdition createPapers(PaperEdition paperEdition) throws DAOTechnicalException, RemoteException;

    boolean createPapers(PaperEdition paperEdition, String[] genreNames) throws DAOTechnicalException, RemoteException;

    default void close(Statement statement) throws RemoteException{
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            System.err.println();
        }
    }

    default void close(Connection connection) throws DAOTechnicalException, RemoteException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage(), e.getCause());
        }
    }
}
