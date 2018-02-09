package kth.se.id2208.vt18.project.tutorial.services.ontology;

import com.sun.jersey.spi.resource.Singleton;
import kth.se.id2208.vt18.project.tutorial.services.common.utils.DataUtils;
import org.apache.jena.ontology.OntModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;

/**
 * Endpoint representing a webservice for the Semantic Web Program ontology.
 * <p>
 * To visit, open browser to http://localhost:8080/id2208/tutorial/ontologies/semwebprogram
 * or from shell
 * curl -H "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/ontologies/semwebprogram
 * curl -H "Accept: text/html" http://localhost:8080/id2208/tutorial/ontologies/semwebprogram
 *
 * @author Kim Hammar on 2018-01-13.
 */
@Path("/id2208/tutorial/ontologies/semwebprogram")
@Singleton
public class SemWebProgramOntology {
    /**
     * Some static strings of the URIs to make the procedure of creating RDF pogramatically a bit more pleasant..
     * In practical applications don't do this, use triple-store.
     */
    //Types
    public static final String Course = DataUtils.ontNS + "Course";
    public static final String CourseWork = DataUtils.ontNS + "CourseWork";
    public static final String Exam = DataUtils.ontNS + "Exam";
    public static final String Project = DataUtils.ontNS + "Project";
    public static final String Homework = DataUtils.ontNS + "Homework";
    public static final String Grading = DataUtils.ontNS + "Grading";
    public static final String AtoF = DataUtils.ontNS + "AtoF";
    public static final String PassFail = DataUtils.ontNS + "PassFail";
    public static final String Literature = DataUtils.ontNS + "Literature";
    public static final String Book = DataUtils.ontNS + "Book";
    public static final String Paper = DataUtils.ontNS + "Paper";
    public static final String Program = DataUtils.ontNS + "Program";
    public static final String University = "http://dbpedia.org/ontology/University";
    public static final String Person = "http://xmlns.com/foaf/0.1/Person";
    //ObjectProperties
    public static final String personName = "http://xmlns.com/foaf/0.1/name";
    public static final String personHomePage = "http://xmlns.com/foaf/0.1/homepage";
    public static final String hasAuthor = DataUtils.ontNS + "hasAuthor";
    public static final String hasCourse = DataUtils.ontNS + "hasCourse";
    public static final String hasCourseWork = DataUtils.ontNS + "hasCourseWork";
    public static final String hasExaminationPolicy = DataUtils.ontNS + "hasExaminationPolicy";
    public static final String isPartOfProgram = DataUtils.ontNS + "isPartOfProgram";
    public static final String isRelatedTo = DataUtils.ontNS + "isRelatedTo";
    public static final String isTaughtAt = DataUtils.ontNS + "isTaughtAt";
    //DataProperties
    public static final String hasCourseName = DataUtils.ontNS + "hasCourseName";
    public static final String hasCourseWorkDescription = DataUtils.ontNS + "hasCourseWorkDescription";
    public static final String hasLiteratureTitle = DataUtils.ontNS + "hasLiteratureTitle";
    public static final String hasProgramName = DataUtils.ontNS + "hasProgramName";
    public static final String isWorthCredits = DataUtils.ontNS + "isWorthCredits";

    private OntModel ontModel = DataUtils.readOntologyFileSystem(DataUtils.FLIGHTS_ONTOLOGY_FILE_PATH);

    /**
     * Return HTML page if a user requested the page
     *
     * @return HTML of the courses (TODO convert this to proper HTML representation)
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String kth() {
        StringWriter stringWriter = new StringWriter();
        ontModel.write(stringWriter, "RDF/XML");
        return "<h1> semwebontology </h1>\n" + stringWriter.toString();
    }

    /**
     * Return RDF/XML if some agent requested the page with accept header "application/rdf+xml"
     *
     * @return RDF/XML of the courses
     */
    @GET
    @Produces("application/rdf+xml")
    public String flightsOntology() {
        StringWriter stringWriter = new StringWriter();
        ontModel.write(stringWriter, "RDF/XML");
        return stringWriter.toString();
    }
}
