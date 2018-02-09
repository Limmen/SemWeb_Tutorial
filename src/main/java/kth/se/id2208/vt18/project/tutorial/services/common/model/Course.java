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
public class Course extends Concept {
    private String courseName;
    private List<CourseWork> courseWork;
    private Program program;
    private float credits;
    private String university;

    public Course(String ID, String courseName, List<CourseWork> courseWork, Program program, float credits, String university) {
        super(ID);
        this.courseName = courseName;
        this.courseWork = courseWork;
        this.program = program;
        this.credits = credits;
        this.university = university;
    }

    public String getCourseName() {
        return courseName;
    }

    public List<CourseWork> getCourseWork() {
        return courseWork;
    }

    public Program getProgram() {
        return program;
    }

    public float getCredits() {
        return credits;
    }

    public String getUniversity() {
        return university;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Resource toRDF(OntModel ontModel, Model rdfModel) {
        Resource course = rdfModel.createResource(ID);
        course.addProperty(RDF.type, ontModel.getOntClass(SemWebProgramOntology.Course));
        course.addProperty(RDFS.comment, "University Course");
        course.addProperty(ontModel.getProperty(SemWebProgramOntology.isWorthCredits), Float.toString(credits));
        course.addProperty(ontModel.getProperty(SemWebProgramOntology.hasCourseName), courseName);
        for (CourseWork cw : courseWork) {
            course.addProperty(ontModel.getProperty(SemWebProgramOntology.hasCourseWork), cw.toRDF(ontModel,rdfModel));
        }
        course.addProperty(ontModel.getProperty(SemWebProgramOntology.isPartOfProgram), program.ID);
        course.addProperty(ontModel.getProperty(SemWebProgramOntology.isTaughtAt), university);
        return course;
    }
}
