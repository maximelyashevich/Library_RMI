package com.elyashevich.library.builder;


import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.entity.union.GenrePaperType;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PaperEditionStAXBuilder extends AbstractPaperBuilder{
    private XMLInputFactory inputFactory;

    public PaperEditionStAXBuilder() {
        super();
        inputFactory = XMLInputFactory.newInstance();
    }

    @Override
    public void buildSetPaperEdition(String fileName) {
        FileInputStream inputStream = null;
        XMLStreamReader reader;
        String name;
        try {
            inputStream = new FileInputStream(new File(fileName));
            reader = inputFactory.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                int type = reader.next();
                if (type == XMLStreamConstants.START_ELEMENT) {
                    name = reader.getLocalName();
                    if (PaperEditionEnum.valueOf(name.replace("-","_").toUpperCase())==PaperEditionEnum.PAPEREDITION) {
                        PaperEdition paperEdition = buildEdition(reader);
                        paperEditions.add(paperEdition);
                    }
                }
            }
        } catch (XMLStreamException | FileNotFoundException ex) {
            System.err.println(ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    @Override
    public void buildSetGenres(String fileName) {
        FileInputStream inputStream = null;
        XMLStreamReader reader;
        String name;
        try {
            inputStream = new FileInputStream(new File(fileName));
            reader = inputFactory.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                int type = reader.next();
                if (type == XMLStreamConstants.START_ELEMENT) {
                    name = reader.getLocalName();
                    if (GenreEnum.valueOf(name.replace("-","_").toUpperCase())==GenreEnum.GENRE) {
                        Genre genre = buildGenre(reader);
                        genres.add(genre);
                    }
                }
            }
        } catch (XMLStreamException | FileNotFoundException ex) {
            System.err.println(ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    @Override
    public void buildSetGenrePapers(String fileName) {
        FileInputStream inputStream = null;
        XMLStreamReader reader;
        String name;
        try {
            inputStream = new FileInputStream(new File(fileName));
            reader = inputFactory.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                int type = reader.next();
                if (type == XMLStreamConstants.START_ELEMENT) {
                    name = reader.getLocalName();
                    if (GenrePaperEnum.valueOf(name.replace("-","_").toUpperCase())==GenrePaperEnum.GENREPAPER) {
                        GenrePaperType genrePaperType = buildGenrePaper(reader);
                        genrePaperTypes.add(genrePaperType);
                    }
                }
            }
        } catch (XMLStreamException | FileNotFoundException ex) {
            System.err.println(ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private PaperEdition buildEdition(XMLStreamReader reader) throws XMLStreamException {
        PaperEdition paperEdition = new PaperEdition();

        paperEdition.setId(reader.getAttributeValue(null, PaperEditionEnum.ID.getValue()));
        paperEdition.setCategory(reader.getAttributeValue(null, PaperEditionEnum.CATEGORY.getValue()));
        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT:
                    name = reader.getLocalName();
                    switch (PaperEditionEnum.valueOf(name.toUpperCase().replace('-', '_'))) {
                        case TITLE:
                            paperEdition.setTitle(getXMLText(reader));
                            break;
                        case DESCRIPTION:
                            paperEdition.setDescription(getXMLText(reader));
                            break;
                        case PERIODICITY:
                            paperEdition.setPeriodicity(Integer.parseInt(getXMLText(reader)));
                            break;
                        case PRICE:
                            paperEdition.setPrice(Double.parseDouble(getXMLText(reader)));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    name = reader.getLocalName();
                    if (PaperEditionEnum.valueOf(name.replace("-","_").toUpperCase()) == PaperEditionEnum.PAPEREDITION) {
                        return paperEdition;
                    }
                    break;
            }
        }
        throw new XMLStreamException("Unknown element in tag OlsCard");
    }

    private GenrePaperType buildGenrePaper(XMLStreamReader reader) throws XMLStreamException {
        GenrePaperType genrePaperType = new GenrePaperType();

        genrePaperType.setId(reader.getAttributeValue(null, PaperEditionEnum.ID.getValue()));
        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT:
                    name = reader.getLocalName();
                    switch (GenrePaperEnum.valueOf(name.toUpperCase().replace('-', '_'))) {
                        case PAPEREDITIONID:
                            genrePaperType.setPaperEditionID(getXMLText(reader));
                            break;
                        case GENREID:
                            genrePaperType.setGenreID(getXMLText(reader));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    name = reader.getLocalName();
                    if (GenrePaperEnum.valueOf(name.replace("-","_").toUpperCase()) == GenrePaperEnum.GENREPAPER) {
                        return genrePaperType;
                    }
                    break;
            }
        }
        throw new XMLStreamException("Unknown element in tag OlsCard");
    }

    private Genre buildGenre(XMLStreamReader reader) throws XMLStreamException {
        Genre genre = new Genre();

        genre.setId(reader.getAttributeValue(null, GenreEnum.ID.getValue()));
        String name;
        while (reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamConstants.START_ELEMENT:
                    name = reader.getLocalName();
                    switch (GenreEnum.valueOf(name.toUpperCase().replace('-', '_'))) {
                        case NAME:
                            genre.setName(getXMLText(reader));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    name = reader.getLocalName();
                    if (GenreEnum.valueOf(name.replace("-","_").toUpperCase()) == GenreEnum.GENRE) {
                        return genre;
                    }
                    break;
            }
        }
        throw new XMLStreamException("Unknown element in tag OlsCard");
    }

    private String getXMLText(XMLStreamReader reader) throws XMLStreamException {
        String text = null;
        if (reader.hasNext()) {
            reader.next();
            text = reader.getText();
        }
        return text;
    }
}


