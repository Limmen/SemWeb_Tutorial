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
public class Paper extends Literature {

    public Paper(String ID, List<Person> author, String description) {
        super(ID, author, description);
    }

    public Resource toRDF(OntModel ontModel, Model rdfModel) {
        Resource paper = rdfModel.createResource(ID);
        paper.addProperty(RDF.type, ontModel.getOntClass(SemWebProgramOntology.Paper));
        paper.addProperty(RDFS.comment, "Paper");
        for(Person author: getAuthors()){
            paper.addProperty(ontModel.getProperty(SemWebProgramOntology.hasAuthor), author.toRDF(ontModel,rdfModel));
        }
        paper.addProperty(ontModel.getProperty(SemWebProgramOntology.hasLiteratureTitle), getDescription());
        return paper;
    }
}
