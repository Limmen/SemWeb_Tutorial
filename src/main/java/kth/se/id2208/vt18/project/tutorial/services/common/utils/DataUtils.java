package kth.se.id2208.vt18.project.tutorial.services.common.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import kth.se.id2208.vt18.project.tutorial.services.common.model.*;
import org.apache.jena.atlas.web.TypedInputStream;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.web.HttpOp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Some common functionality used by the different services in their RDF processing.
 *
 * @author Kim Hammar on 2018-01-13.
 */
public final class DataUtils {
    public static final int PORT = 8080;
    //Some namespaces and locations
    public static final String FLIGHTS_ONTOLOGY_FILE_PATH = "semwebprogram.owl";
    public static final String KTH = "http://dbpedia.org/resource/Royal_Institute_of_Technology";
    public static final String chalmers = "http://dbpedia.org/resource/Chalmers_University_of_Technology";
    public static final String KTHURL = "http://localhost:" + DataUtils.PORT + "/id2208/tutorial/institutes/kth";
    public static final String KTHNS = "http://localhost:" + DataUtils.PORT + "/id2208/tutorial/institutes/kth#";
    public static final String chalmersURL = "http://localhost:" + DataUtils.PORT + "/id2208/tutorial/institutes/chalmers";
    public static final String chalmersNS = "http://localhost:" + DataUtils.PORT + "/id2208/tutorial/institutes/chalmers#";
    public static final String semWebProgramURL = "http://localhost:" + DataUtils.PORT + "/id2208/tutorial/programs/semwebprogram";
    public static final String semWebProgramNS = "http://localhost:" + DataUtils.PORT + "/id2208/tutorial/programs/semwebprogram#";
    public static final String ontURL = "http://localhost:" + DataUtils.PORT + "/id2208/tutorial/ontologies/semwebprogram";
    public static final String ontNS = "http://localhost:" + DataUtils.PORT + "/id2208/tutorial/ontologies/semwebprogram#";

    //Initialize the dummy-data
    public static Course ID2208 = createID2208();
    public static Course ID2288 = createID2288();
    public static Program semWebProgram = createSemWebProgram();

    /**
     * Helper function to create some dummy data for a university program.
     * Should be replaced by a proper Triple-Store in practice.
     *
     * @return Semantic Web Program
     */
    public static Program createSemWebProgram() {
        List courses = new ArrayList();
        courses.add(DataUtils.ID2208);
        courses.add(DataUtils.ID2288);
        List unis = new ArrayList();
        unis.add(DataUtils.KTH);
        unis.add(DataUtils.chalmers);
        Program semwebProgram = new Program(
                semWebProgramNS + "semwebprogram",
                courses,
                unis,
                "International Cross University Semantic Web Master Program",
                15.0f);
        ID2208.setProgram(semwebProgram);
        ID2288.setProgram(semwebProgram);
        return semwebProgram;
    }

    /**
     * Helper function to create some dummy data for a university course.
     * Should be replaced by a proper Triple-Store in practice.
     *
     * @return ID2208
     */
    public static Course createID2208() {
        List authors = new ArrayList();
        Person jimHendler = new Person(KTHNS + "JimHendler", "Jim Hendler", "http://dblp.uni-trier.de/pers/h/Hendler:James_A=");
        Person deanAllemang = new Person(KTHNS + "DeanAllemang", "Dean Allemang", "http://dblp.uni-trier.de/pers/a/Allemang:Dean");
        authors.add(jimHendler);
        authors.add(deanAllemang);
        Book semwebWorkingOntologist = new Book(
                KTHNS + "semwebWorkingOntologist",
                authors,
                "Semantic Web for the Working Ontologist: Effective Modeling in RDFS and OWL");
        List literature = new ArrayList();
        literature.add(semwebWorkingOntologist);
        Project project = new Project(
                KTHNS + "ID2208Project",
                new PassFail(KTHNS + "ID2208PassFail"),
                literature,
                7.5f,
                "Course Project: Implement a Semantic Web Service using the full stack of OWL,RDF,SPARQL,WebService. Deliverables: Code, Presentation, Report"
        );
        List coursework = new ArrayList();
        coursework.add(project);
        Course ID2208 = new Course(
                KTHNS + "ID2208",
                "ID2208 - Programming Web Services",
                coursework,
                semWebProgram,
                7.5f,
                DataUtils.KTH);
        return ID2208;
    }

    /**
     * Helper function to create some dummy data for a university course.
     * Should be replaced by a proper Triple-Store in practice.
     *
     * @return ID2288
     */
    public static Course createID2288() {
        List authors = new ArrayList();
        Person josejulioAlferes = new Person(chalmersNS + "JoseJulioAlferes", "José Júlio Alferes", "http://dblp.org/pers/hd/a/Alferes:Jos=eacute=_J=uacute=lio");
        Person carlosViegasDamasio = new Person(chalmersNS + "CarlosViegasDamasio", "Carlos Viegas Damásio", "http://dblp.org/pers/hd/d/Dam=aacute=sio:Carlos_Viegas");
        Person luizMonizPereira = new Person(chalmersNS + "LuizMonizPereira", "Luís Moniz Pereira", "http://dblp.org/pers/hd/p/Pereira:Lu=iacute=s_Moniz");
        authors.add(josejulioAlferes);
        authors.add(carlosViegasDamasio);
        authors.add(luizMonizPereira);
        Paper semwebLogicProgramming = new Paper(
                chalmersNS + "SemWebLogicProgramming",
                authors,
                "Semantic Web Logic Programming Tools");
        List literature = new ArrayList();
        literature.add(semwebLogicProgramming);
        Exam exam = new Exam(
                chalmersNS + "ID2288Exam",
                new AtoF(chalmersNS + "ID2288AtoF"),
                literature,
                4f,
                "Final Exam of Course, covers all topics and chapters from course literature"
        );
        HomeWork homeWork = new HomeWork(
                chalmersNS + "ID2288Homework",
                new PassFail(chalmersNS + "ID2288PassFail"),
                literature,
                3.5f,
                "Homework to create a semantic web reasoner using predicate logic on top of OWL"
        );
        List coursework = new ArrayList();
        coursework.add(exam);
        coursework.add(homeWork);
        Course ID2288 = new Course(
                chalmersNS + "ID2288",
                "ID2288 - Logic Programming for the Semantic Web",
                coursework,
                semWebProgram,
                7.5f,
                DataUtils.chalmers);
        return ID2288;
    }

    /**
     * Reads an ontology in the given file path
     *
     * @param path
     * @return
     */
    public static OntModel readOntologyFileSystem(String path) {
        OntDocumentManager ontDocumentManager = new OntDocumentManager();
        OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
        ontModelSpec.setDocumentManager(ontDocumentManager);
        OntModel ontModel = ModelFactory.createOntologyModel(ontModelSpec, null);
        try {
            ontModel.read(new ByteArrayInputStream(DataUtils.readResource(path, Charsets.UTF_8).getBytes()), "RDF/XML");
        } catch (IOException e) {
            throw new IllegalArgumentException("File: " + path + " not found");
        }

        return ontModel;
    }

    /**
     * Reads a resource from resources folder
     *
     * @param fileName
     * @param charset
     * @return
     * @throws IOException
     */
    public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }

    /**
     * Reads an ontology from the web
     *
     * @param url
     * @return
     */
    public static OntModel readOntologyHttp(String url) {
        TypedInputStream inputStream = HttpOp.execHttpGet(url, "application/rdf+xml");
        OntDocumentManager ontDocumentManager = new OntDocumentManager();
        OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
        ontModelSpec.setDocumentManager(ontDocumentManager);
        OntModel ontModel = ModelFactory.createOntologyModel(ontModelSpec, null);
        ontModel.read(inputStream, null);
        return ontModel;
    }
}

