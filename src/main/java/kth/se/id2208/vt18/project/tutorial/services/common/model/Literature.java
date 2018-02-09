package kth.se.id2208.vt18.project.tutorial.services.common.model;

import java.util.List;

/**
 * @author Kim Hammar on 2018-01-13.
 */
public abstract class Literature extends Concept {

    private List<Person> authors;
    private String description;

    public Literature(String ID, List<Person> authors, String description) {
        super(ID);
        this.authors = authors;
        this.description = description;
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }
}
