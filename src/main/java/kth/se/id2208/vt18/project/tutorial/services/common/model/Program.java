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
public class Program extends Concept {

    private List<Course> courses;
    private List<String> universities;
    private String programName;
    private float credits;

    public Program(String ID, List<Course> courses, List<String> universities, String programName, float credits) {
        super(ID);
        this.courses = courses;
        this.universities = universities;
        this.programName = programName;
        this.credits = credits;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<String> getUniversities() {
        return universities;
    }

    public String getProgramName() {
        return programName;
    }

    public float getCredits() {
        return credits;
    }

    public Resource toRDF(OntModel ontModel, Model rdfModel) {
        Resource program = rdfModel.createResource(ID);
        program.addProperty(RDF.type, ontModel.getOntClass(SemWebProgramOntology.Program));
        program.addProperty(RDFS.comment, "University Program");
        program.addProperty(ontModel.getProperty(SemWebProgramOntology.isWorthCredits), Float.toString(credits));
        for(Course course : courses){
            program.addProperty(ontModel.getProperty(SemWebProgramOntology.hasCourse), course.ID);
        }
        for(String uni : universities){
            program.addProperty(ontModel.getProperty(SemWebProgramOntology.isTaughtAt), uni);
        }
        program.addProperty(ontModel.getProperty(SemWebProgramOntology.hasProgramName), programName);
        return program;
    }
}
