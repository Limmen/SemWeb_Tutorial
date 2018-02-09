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
public class PassFail extends Grading {

    public PassFail(String ID) {
        super(ID);
    }

    public Resource toRDF(OntModel ontModel, Model rdfModel) {
        Resource PassFail = rdfModel.createResource(ID);
        PassFail.addProperty(RDF.type, ontModel.getOntClass(SemWebProgramOntology.PassFail));
        PassFail.addProperty(RDFS.comment, "Pass-Fail grading of coursework");
        return PassFail;
    }
}
