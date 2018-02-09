package kth.se.id2208.vt18.project.tutorial.services.common.model;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

/**
 * @author Kim Hammar on 2018-01-13.
 */
public abstract class Concept {
    public String ID;

    public Concept(String ID){
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public abstract Resource toRDF(OntModel ontModel, Model rdfModel);
}
