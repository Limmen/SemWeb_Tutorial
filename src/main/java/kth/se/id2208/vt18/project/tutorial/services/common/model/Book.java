package kth.se.id2208.vt18.project.tutorial.services.common.model;

import kth.se.id2208.vt18.project.tutorial.services.ontology.SemWebProgramOntology;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.util.List;

/**
 * @author Kim Hammar on 2018-01-13.
 */
public class Book extends Literature {

    public Book(String ID, List<Person> author, String description) {
        super(ID, author, description);
    }

    public Resource toRDF(OntModel ontModel, Model rdfModel) {
        Resource book = rdfModel.createResource(ID);
        book.addProperty(RDF.type, ontModel.getOntClass(SemWebProgramOntology.Book));
        book.addProperty(RDFS.comment, "Book");
        for(Person author: getAuthors()){
            book.addProperty(ontModel.getProperty(SemWebProgramOntology.hasAuthor), author.toRDF(ontModel,rdfModel));
        }
        book.addProperty(ontModel.getProperty(SemWebProgramOntology.hasLiteratureTitle), getDescription());
        return book;
    }
}
