package com.elyashevich.library.builder;


import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.entity.union.GenrePaperType;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractPaperBuilder {
    protected Set<PaperEdition> paperEditions;
    protected Set<Genre> genres;
    protected Set<GenrePaperType>genrePaperTypes;
    public AbstractPaperBuilder() {
        paperEditions = new LinkedHashSet<>();
        genres = new LinkedHashSet<>();
        genrePaperTypes = new LinkedHashSet<>();
    }
    public Set<PaperEdition> getPapers() {
        return Collections.unmodifiableSet(paperEditions);
    }
    public Set<Genre> getGenres() {
        return Collections.unmodifiableSet(genres);
    }
    public Set<GenrePaperType> getGenrePaperTypes() {
        return Collections.unmodifiableSet(genrePaperTypes);
    }
    public abstract void buildSetPaperEdition(String fileName);
    public abstract void buildSetGenres(String fileName);
    public abstract void buildSetGenrePapers(String fileName);
}
