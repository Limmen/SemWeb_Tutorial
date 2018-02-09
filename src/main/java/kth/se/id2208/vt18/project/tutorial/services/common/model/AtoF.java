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
public class AtoF extends Grading {

    public AtoF(String ID) {
        super(ID);
    }

    public Resource toRDF(OntModel ontModel, Model rdfModel) {
        Resource AtoF = rdfModel.createResource(ID);
        AtoF.addProperty(RDF.type, ontModel.getOntClass(SemWebProgramOntology.AtoF));
        AtoF.addProperty(RDFS.comment, "A-to-F grading of coursework");
        return AtoF;
    }
}
