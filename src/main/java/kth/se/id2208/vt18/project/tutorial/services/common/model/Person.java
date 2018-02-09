package kth.se.id2208.vt18.project.tutorial.services.common.model;

import kth.se.id2208.vt18.project.tutorial.services.ontology.SemWebProgramOntology;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 * @author Kim Hammar on 2018-01-13.
 */
public class Person extends Concept {

    private String name;
    private String homepage;

    public Person(String ID, String name, String homepage) {
        super(ID);
        this.name = name;
        this.homepage = homepage;
    }

    public String getName() {
        return name;
    }

    public String getHomepage() {
        return homepage;
    }

    public Resource toRDF(OntModel ontModel, Model rdfModel) {
        Resource person = rdfModel.createResource(ID);
        person.addProperty(RDF.type, ontModel.getOntClass(SemWebProgramOntology.Person));
        person.addProperty(RDFS.comment, "Person");
        person.addProperty(ontModel.getProperty(SemWebProgramOntology.personName), name);
        person.addProperty(ontModel.getProperty(SemWebProgramOntology.personHomePage), homepage);
        return person;
    }
}
