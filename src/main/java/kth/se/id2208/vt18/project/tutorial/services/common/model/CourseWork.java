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
public class CourseWork extends Concept {
    private Grading grading;
    private List<Literature> literatures;
    private float credits;
    private String description;

    public CourseWork(String ID, Grading grading, List<Literature> literatures, float credits, String description) {
        super(ID);
        this.grading = grading;
        this.literatures = literatures;
        this.credits = credits;
        this.description = description;
    }

    public Grading getGrading() {
        return grading;
    }

    public List<Literature> getLiteratures() {
        return literatures;
    }

    public Resource toRDF(OntModel ontModel, Model rdfModel) {
        Resource courseWork = rdfModel.createResource(ID);
        courseWork.addProperty(RDF.type, ontModel.getOntClass(SemWebProgramOntology.CourseWork));
        courseWork.addProperty(RDFS.comment, "CourseWork part of some university course");
        courseWork.addProperty(ontModel.getProperty(SemWebProgramOntology.hasExaminationPolicy), grading.toRDF(ontModel,rdfModel));
        courseWork.addProperty(ontModel.getProperty(SemWebProgramOntology.isWorthCredits), Float.toString(credits));
        for(Literature literature : literatures){
            literature.toRDF(ontModel,rdfModel);
            courseWork.addProperty(ontModel.getProperty(SemWebProgramOntology.isRelatedTo), rdfModel.getResource(literature.ID));
        }
        courseWork.addProperty(ontModel.getProperty(SemWebProgramOntology.hasCourseWorkDescription), description);
        return courseWork;
    }
}
