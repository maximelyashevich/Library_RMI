package com.elyashevich.library.service.impl;

import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.exception.DAOTechnicalException;
import com.elyashevich.library.pool.ConnectionPool;
import com.elyashevich.library.pool.ProxyConnection;
import com.elyashevich.library.service.RemoteService;
import com.elyashevich.library.util.IdGenerator;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoteServiceImpl implements RemoteService {
    private static final String LIKE_COMPONENT_SQL = "%";
    private static final String SQL_INSERT_PAPER_WITH_GENRE = "INSERT INTO genres_papers(genre_id, papers_id) VALUES(?, ?)";
    private static final String SQL_UPDATE_GENRE_PAPER = "UPDATE genres_papers SET genre_id=? WHERE papers_id =? AND genre_id=?";
    private static final String SQL_DELETE_GENRES = "DELETE FROM genres_papers WHERE papers_id = ?";
    private static final String EXCEPTION_MESSAGE_GENRE_PAPER = "exception in GenreRemoteServiceImpl";
    private static final String SQL_SELECT_ALL_GENRES = "SELECT name, id FROM GENRES";
    private static final String SQL_SELECT_GENRES_BY_PAPERS_ID = "SELECT genre_id, papers_id, genres.name " +
            "FROM genres_papers " +
            "LEFT JOIN genres ON genres_papers.genre_id = genres.id " +
            "WHERE papers_id = ?";
    private static final String SQL_SELECT_GENRE_BY_NAME = "SELECT id, name FROM genres WHERE name LIKE ?";
    private static final String EXCEPTION_MESSAGE = "exception in GenreRemoteServiceImpl";
    private static final String SQL_SELECT_PAPERS = "SELECT id FROM PAPERS";
    private static final String SQL_SELECT_PAPERS_BY_ID = "SELECT papers.id, name, title, price, publishing_periodicity, description  " +
            " FROM papers" +
            " LEFT JOIN paper_type ON paper_type.id = papers.paper_type_id " +
            " WHERE papers.id=?";
    private static final String SQL_SELECT_BY_DESCRIPTION = "SELECT papers.id, description, title FROM papers where CONCAT(title,' ',description) like ?";
    private static final String SQL_DELETE_PAPERS_BY_ID = "DELETE FROM PAPERS WHERE id=?";
    private static final String SQL_INSERT_PAPER = "INSERT INTO papers(paper_type_id, title, price, publishing_periodicity, description) " +
            " VALUES ((SELECT paper_type.id FROM paper_type  WHERE " +
            "    name LIKE ?), ?, ?, ?, ?)";
    private static final String SQL_UPDATE_PAPER = "UPDATE papers SET paper_type_id = (SELECT paper_type.id FROM paper_type  WHERE " +
            "    name LIKE ?), title = ?, price=?, publishing_periodicity=?, description=? WHERE id=?";

    @Override
    public void insertGenreToPaper(Genre genre, PaperEdition paperEdition) throws DAOTechnicalException, RemoteException {

        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionPool.getInstance().defineConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_PAPER_WITH_GENRE);
            preparedStatement.setLong(1, IdGenerator.defineID(genre.getId()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE_GENRE_PAPER, e.getCause());
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @Override
    public void deleteGenrePaper(long paperEditionId) throws DAOTechnicalException, RemoteException {
        ProxyConnection connection = null;
        PreparedStatement statement = null;
        boolean result = false;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            statement = connection.prepareStatement(SQL_DELETE_GENRES);
            statement.setLong(1, paperEditionId);
            if (statement.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE_GENRE_PAPER, e.getCause());
        } finally {
            close(statement);
            close(connection);
        }
    }

    @Override
    public boolean updateGenrePaper(long genreIdNew, long genreIdOld, long paperId) throws DAOTechnicalException, RemoteException {
        ProxyConnection connection = null;
        PreparedStatement statement = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            statement = connection.prepareStatement(SQL_UPDATE_GENRE_PAPER);
            statement.setLong(1, genreIdNew);
            statement.setLong(2, genreIdOld);
            statement.setLong(3, paperId);
            result = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE_GENRE_PAPER, e.getCause());
        } finally {
            close(statement);
            close(connection);
        }
        return result;
    }

    @Override
    public void createGenrePaper(long genreId, long paperId) throws DAOTechnicalException, RemoteException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_PAPER_WITH_GENRE);
            preparedStatement.setLong(1, genreId);
            preparedStatement.setLong(2, paperId);

            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE_GENRE_PAPER, e.getCause());
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @Override
    public void updateGenrePaper(PaperEdition paperEdition, HashSet data) throws DAOTechnicalException, RemoteException {
        Genre genre;

        //deleteGenrePaper(IdGenerator.defineID(paperEdition.getId()));
        deleteGenrePaper(Long.parseLong(paperEdition.getId()));
        for (Object dataComponent : data) {
            genre = findByPaperGenreNameGenres(dataComponent.toString());
            createGenrePaper(IdGenerator.defineID(genre.getId()), IdGenerator.defineID(paperEdition.getId()));
        }
    }

    @Override
    public List<Genre> findAllGenres() throws DAOTechnicalException, RemoteException {
        Set<Genre> genres = new HashSet<>();
        ProxyConnection connection = null;
        Statement statement = null;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL_GENRES);
            Genre genre;
            while (resultSet.next()) {
                genre = new Genre(resultSet.getString(1));
                genre.setId(String.valueOf(resultSet.getLong(2)));
                genres.add(genre);
            }
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(statement);
            close(connection);
        }
        return new ArrayList<>(genres);
    }

    @Override
    public HashSet<Genre> findByPaperIDGenres(long id) throws DAOTechnicalException, RemoteException {
        HashSet<Genre> list = new HashSet<>();
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            preparedStatement = connection.prepareStatement(SQL_SELECT_GENRES_BY_PAPERS_ID);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Genre genre;
            while (resultSet.next()) {
                genre = new Genre(resultSet.getString(3));
                genre.setId(String.valueOf(resultSet.getLong(1)));
                list.add(genre);
            }
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return list;
    }

    @Override
    public Genre findByPaperGenreNameGenres(String data) throws DAOTechnicalException, RemoteException {
        Genre genre = new Genre();
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            preparedStatement = connection.prepareStatement(SQL_SELECT_GENRE_BY_NAME);
            preparedStatement.setString(1, data);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                genre.setId(String.valueOf(resultSet.getLong(1)));
                genre.setName(resultSet.getString(2));
            }
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return genre;
    }

    @Override
    public List<PaperEdition> findAllPapers() throws DAOTechnicalException, RemoteException {
        List<PaperEdition> paperEditions = new ArrayList<>();
        ProxyConnection connection = null;
        Statement statement = null;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_PAPERS);
            while (resultSet.next()) {
                paperEditions.add(findPaperByIdPapers(resultSet.getLong(1)));
            }
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(statement);
            close(connection);
        }
        return paperEditions;
    }


    @Override
    public ArrayList<PaperEdition> findByDescriptionPapers(String data) throws DAOTechnicalException, RemoteException {
        ArrayList<PaperEdition> list = new ArrayList<>();
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            preparedStatement = connection.prepareStatement(SQL_SELECT_BY_DESCRIPTION);
            preparedStatement.setString(1, LIKE_COMPONENT_SQL + data + LIKE_COMPONENT_SQL);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(findPaperByIdPapers(resultSet.getLong(1)));
            }
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return list;
    }

    @Override
    public PaperEdition createPapers(PaperEdition paperEdition) throws DAOTechnicalException, RemoteException {

        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_PAPER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, paperEdition.getCategory());
            preparedStatement.setString(2, paperEdition.getTitle());
            preparedStatement.setDouble(3, paperEdition.getPrice());
            preparedStatement.setInt(4, paperEdition.getPeriodicity());
            preparedStatement.setString(5, paperEdition.getDescription());

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                paperEdition.setId(String.valueOf(rs.getInt(1)));
                System.out.println("!!!!!!!!!");
            }
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return paperEdition;
    }

    @Override
    public boolean createPapers(PaperEdition paperEdition, String[] genreNames) throws DAOTechnicalException, RemoteException {
        boolean flag;

        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_PAPER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, paperEdition.getCategory());
            preparedStatement.setString(2, paperEdition.getTitle());

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                paperEdition.setId(String.valueOf(rs.getInt(1)));
            }
            flag = true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return flag;
    }

    @Override
    public boolean updatePapers(PaperEdition paperEdition) throws DAOTechnicalException, RemoteException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_PAPER);
            preparedStatement.setString(1, paperEdition.getCategory());
            preparedStatement.setString(2, paperEdition.getTitle());
            preparedStatement.setDouble(3, paperEdition.getPrice());
            preparedStatement.setInt(4, paperEdition.getPeriodicity());
            preparedStatement.setString(5, paperEdition.getDescription());
            preparedStatement.setLong(6, IdGenerator.defineID(paperEdition.getId()));

            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return result;
    }

    @Override
    public PaperEdition findPaperByIdPapers(long id) throws DAOTechnicalException, RemoteException {
        PaperEdition paperEdition = null;
        ProxyConnection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            statement = connection.prepareStatement(SQL_SELECT_PAPERS_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                paperEdition = new PaperEdition(
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5),
                        resultSet.getString(2),
                        resultSet.getString(6)
                );
                paperEdition.setId(String.valueOf(resultSet.getLong(1)));
            }
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(statement);
            close(connection);
        }
        return paperEdition;
    }

    @Override
    public boolean deleteByIdPapers(long paperId) throws DAOTechnicalException, RemoteException {
        ProxyConnection connection = null;
        PreparedStatement statement = null;
        boolean result;
        try {
            connection = ConnectionPool.getInstance().defineConnection();
            statement = connection.prepareStatement(SQL_DELETE_PAPERS_BY_ID);
            statement.setLong(1, paperId);
            result = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOTechnicalException(EXCEPTION_MESSAGE, e.getCause());
        } finally {
            close(statement);
            close(connection);
        }
        return result;
    }

}
