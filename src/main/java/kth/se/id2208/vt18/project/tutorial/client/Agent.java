package kth.se.id2208.vt18.project.tutorial.client;

import kth.se.id2208.vt18.project.tutorial.services.common.utils.DataUtils;
import kth.se.id2208.vt18.project.tutorial.services.ontology.SemWebProgramOntology;
import org.apache.jena.atlas.web.TypedInputStream;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.web.HttpOp;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Client application that consumes the semantic web services and performes some aggregating queries.
 * Demonstrates two way to query with Jena, with SPARQL and through the functions in the Jena API.
 *
 * NOTICE: that you can only run this agent after you have started the web services (localhost:8080).
 * @author Kim Hammar on 2018-01-13.
 */
public class Agent {
    private OntModel ontModel = DataUtils.readOntologyHttp(DataUtils.ontURL);//Read the ontology from the ontology service

    //Startup the agent
    public static void main(String[] args) {
        new Agent();
    }

    public Agent() {
        //Let the agent start from the Program-service and jump links to collect the total coursework for the program
        getTotalCourseWorkForProgram(DataUtils.semWebProgramURL);
        //Let the agent start from the Program-service and then jump to DBPedia to collect information about Universities
        getInfoAboutAllUniversitiesParticipatingInTheProgram(DataUtils.semWebProgramURL);

        //Download the RDF from the different endpoints.
        //Purpose of this is so you can study the created RDF and see if there was any mistake
        //Ideally you can even verify the RDF with unit-tests. See resources folder to inspect the RDF and OWL files.
        downloadRDF(DataUtils.KTHURL, "src/main/resources/kth.rdf");
        downloadRDF(DataUtils.chalmersURL, "src/main/resources/chalmers.rdf");
        downloadRDF(DataUtils.semWebProgramURL, "src/main/resources/semwebprogram.rdf");
        downloadOWL(DataUtils.ontURL, "src/main/resources/downloaded_ontology.owl");
    }

    /**
     * Imagine your agent's task is to collect all of the coursework for a given university program and you only have
     * access to the entrypoint of the program.
     * <p>
     * This is a method for starting at the program endpoint and jumping links to collect all the coursework for the program.
     * Uses JENA API to programmatically access the resources (instead of using SPARQL).
     *
     * @param programURI Starting point.
     */
    public void getTotalCourseWorkForProgram(String programURI) {
        TypedInputStream inputStream = HttpOp.execHttpGet(programURI, "application/rdf+xml");
        Model programModel = ModelFactory.createDefaultModel();
        programModel.read(inputStream, null);
        RDFNode program = getProgram(programModel);
        ArrayList<RDFNode> courses = getCoursesFromProgram(programModel, program);
        ArrayList<RDFNode> courseWorks = new ArrayList();
        for (RDFNode course : courses) {
            inputStream = HttpOp.execHttpGet(course.asLiteral().getString(), "application/rdf+xml");
            Model courseModel = ModelFactory.createDefaultModel();
            courseModel.read(inputStream, null);
            RDFNode courseNode = getCourse(courseModel);
            ArrayList<RDFNode> courseWork = getCourseWork(courseModel, courseNode);
            courseWorks.addAll(courseWork);
        }
        System.out.println("Total CourseWork for program: " + programURI);
        printCourseWork(courseWorks);
        System.out.println();
    }

    /**
     * Imagine your agent's task is to gather all universities involved in the university program and return a short
     * description of each university. In addition, the returns should be sorted by the number of students per university.
     * <p>
     * This method starts by fetching the RDF at the program entrypoint, then it does a local SPARQL query to extract
     * the universities of the program, which links to DBPedia entries for the universities,
     * then it does a remote SPARQL query to the SPARQL endpoint http://dbpedia.org/sparql
     * and collects a short description of each university as well as the number of students. Finally the method prints
     * the universities in order of number of students.
     *
     * @param programURI Starting point
     */
    public void getInfoAboutAllUniversitiesParticipatingInTheProgram(String programURI) {
        TypedInputStream inputStream = HttpOp.execHttpGet(programURI, "application/rdf+xml");
        Model programModel = ModelFactory.createDefaultModel();
        programModel.read(inputStream, null);

        /**
         * SPARQL Query for selecting all universities from the program.
         *
         * PREFIX  smp2: <http://localhost:8080/id2208/tutorial/programs/semwebprogram#>
         * PREFIX  smp:  <http://localhost:8080/id2208/tutorial/ontologies/semwebprogram#>

         * SELECT  ?uni
         * WHERE
         * {
         * smp2:semwebprogram
         * smp:isTaughtAt  ?uni
         * }
         */
        String queryString = "PREFIX smp: <" + DataUtils.ontNS + ">\n" +
                "PREFIX smp2: <" + DataUtils.semWebProgramNS + ">\n" +
                "SELECT ?uni WHERE {\n" +
                "smp2:semwebprogram smp:isTaughtAt ?uni .\n" +
                "}";

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, programModel);
        ResultSet results = qexec.execSelect();
        Map<Integer, String> sortedUnis = new TreeMap<Integer, String>();
        while (results.hasNext()) {
            QuerySolution querySolution = results.nextSolution();
            String uni = querySolution.getLiteral("uni").getString();
            /**
             * SPARQL Query for selecting comment and number of students from DBPedia resource representing the university
             *
             * PREFIX  dbo:  <http://dbpedia.org/resource/>
             * PREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>
             * PREFIX  smp:  <http://localhost:8080/id2208/tutorial/ontologies/semwebprogram#>
             * PREFIX  dbo2: <http://dbpedia.org/ontology/>
             *
             * SELECT  ?comment ?nmbrStudents
             * WHERE
             * {
             * dbo:Royal_Institute_of_Technology
             * rdfs:comment ?comment ;
             * dbo2:numberOfStudents  ?nmbrStudents
             * FILTER ( lang(?comment) = "en" )
             * }
             */
            queryString = "PREFIX smp: <" + DataUtils.ontNS + ">\n" +
                    "PREFIX dbo: <http://dbpedia.org/resource/>\n" +
                    "PREFIX dbo2: <http://dbpedia.org/ontology/>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "SELECT ?comment ?nmbrStudents WHERE {\n" +
                    "<" + uni + ">" + " rdfs:comment ?comment .\n" +
                    "<" + uni + ">" + " dbo2:numberOfStudents ?nmbrStudents .\n" +
                    "FILTER (lang(?comment) = 'en')\n" +
                    "}";
            String service = "http://dbpedia.org/sparql";
            query = QueryFactory.create(queryString);
            QueryEngineHTTP serviceRequest = QueryExecutionFactory.createServiceRequest(service, query);
            ResultSet results2 = serviceRequest.execSelect();
            while (results2.hasNext()) {
                QuerySolution querySolution2 = results2.nextSolution();
                String comment = querySolution2.getLiteral("comment").getString();
                int numberOfStudents = querySolution2.getLiteral("nmbrStudents").getInt();
                sortedUnis.put(numberOfStudents, uni + "\n" + comment);
            }
        }
        System.out.println("Universities sorted by number of students, participating in the program: " + programURI);
        for (Map.Entry<Integer, String> entry : sortedUnis.entrySet()) {
            System.out.println("---------- University ----------");
            System.out.println("Number of Students:" + entry.getKey());
            System.out.println("Description: \n" + entry.getValue());
        }
        System.out.println();
    }

    /**
     * Helper Function to download a RDF file from online and saving it to fileName.
     *
     * @param URL      URL to download from
     * @param fileName filepath to save to
     */
    public void downloadRDF(String URL, String fileName) {
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            TypedInputStream inputStream = HttpOp.execHttpGet(URL, "application/rdf+xml");
            Model model = ModelFactory.createDefaultModel();
            model.read(inputStream, null);
            RDFDataMgr.write(fos, model, Lang.RDFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper Function to download a OWL file from online and saving it to fileName.
     *
     * @param URL      URL to download from
     * @param fileName filepath to save to
     */
    public void downloadOWL(String URL, String fileName) {
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            TypedInputStream inputStream = HttpOp.execHttpGet(URL, "application/rdf+xml");
            OntDocumentManager ontDocumentManager = new OntDocumentManager();
            OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
            ontModelSpec.setDocumentManager(ontDocumentManager);
            OntModel ontModel = ModelFactory.createOntologyModel(ontModelSpec, null);
            ontModel.read(inputStream, null);
            ontModel.write(fos, "RDF/XML");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to print a List of CourseWork RDFNodes. uses Jena API to access nested elements and print,
     * (could have used SPARQL instead)
     *
     * @param coursework List of CourseWork RDFNodes
     */
    private void printCourseWork(ArrayList<RDFNode> coursework) {
        for (RDFNode cw : coursework) {
            System.out.println("---------- Course Work ----------");
            Model model = cw.getModel();
            Statement comment = cw.asResource().getProperty(RDFS.comment);
            System.out.println("Type:" + comment.getLiteral().getString());
            Statement credits = cw.asResource().getProperty(ontModel.getProperty(SemWebProgramOntology.isWorthCredits));
            System.out.println("Credits: " + credits.getLiteral().getString());
            Statement description = cw.asResource().getProperty(ontModel.getProperty(SemWebProgramOntology.hasCourseWorkDescription));
            System.out.println("Description: " + description.getLiteral().getString());
            Statement examination = cw.asResource().getProperty(ontModel.getProperty(SemWebProgramOntology.hasExaminationPolicy)).getResource().getProperty(RDFS.comment);
            System.out.println("Examination: " + examination.getLiteral().getString());
            Selector literatureSelector = new SimpleSelector(cw.asResource(), ontModel.getProperty(SemWebProgramOntology.isRelatedTo), (String) null);
            StmtIterator stmtIterator = model.listStatements(literatureSelector);
            System.out.println("### Related Literature ###");
            while (stmtIterator.hasNext()) {
                RDFNode literature = stmtIterator.nextStatement().getObject();
                Statement literatureComment = literature.asResource().getProperty(RDFS.comment);
                System.out.println("Type: " + literatureComment.getLiteral().getString());
                Statement literatureTitle = literature.asResource().getProperty(ontModel.getProperty(SemWebProgramOntology.hasLiteratureTitle));
                System.out.println("Title: " + literatureTitle.getLiteral().getString());
                Selector authorsSelector = new SimpleSelector(literature.asResource(), ontModel.getProperty(SemWebProgramOntology.hasAuthor), (String) null);
                StmtIterator stmtIterator1 = model.listStatements(authorsSelector);
                System.out.println("## Authors ##");
                while (stmtIterator1.hasNext()) {
                    RDFNode author = stmtIterator1.nextStatement().getObject();
                    Statement name = author.asResource().getProperty(ontModel.getProperty(SemWebProgramOntology.personName));
                    Statement homepage = author.asResource().getProperty(ontModel.getProperty(SemWebProgramOntology.personHomePage));
                    System.out.println("Name: " + name.getLiteral().getString());
                    System.out.println("Homepage: " + homepage.getLiteral().getString());
                }
            }
        }
    }

    /**
     * Helper function to select all triples <Node, type, Program>, in this tutorial there is only one program per service
     * so no need to iterate, just return the first triple.
     *
     * @param programModel
     * @return
     */
    private RDFNode getProgram(Model programModel) {
        Selector programSelector = new SimpleSelector(null, RDF.type, ontModel.getOntClass(SemWebProgramOntology.Program));
        return programModel.listStatements(programSelector).nextStatement().getSubject();
    }

    /**
     * Helper function to select all triples <Node, type, Course>, in this tutorial there is only one course per service
     * so no need to iterate, just return the first triple.
     *
     * @param courseModel
     * @return
     */
    private RDFNode getCourse(Model courseModel) {
        Selector courseSelector = new SimpleSelector(null, RDF.type, ontModel.getOntClass(SemWebProgramOntology.Course));
        return courseModel.listStatements(courseSelector).nextStatement().getSubject();
    }

    /**
     * Helper function to select the triples <Program, hasCourse, Course> from a given program and then collecting
     * all courses in a list and return.
     *
     * @param programModel Model
     * @param program      Program
     * @return All courses of program
     */
    private ArrayList<RDFNode> getCoursesFromProgram(Model programModel, RDFNode program) {
        ArrayList<RDFNode> courses = new ArrayList();
        Selector coursesSelector = new SimpleSelector(program.asResource(), ontModel.getProperty(SemWebProgramOntology.hasCourse), (String) null);
        StmtIterator stmtIterator = programModel.listStatements(coursesSelector);
        while (stmtIterator.hasNext()) {
            courses.add(stmtIterator.nextStatement().getObject());
        }
        return courses;
    }

    /**
     * Helper function to select the triples <Course, hasCourseWork, CourseWork> from a given course and then collecting
     * all coursework in a list and return.
     *
     * @param courseModel Model
     * @param course      Course
     * @return List of coursework for the course
     */
    private ArrayList<RDFNode> getCourseWork(Model courseModel, RDFNode course) {
        ArrayList<RDFNode> courseWork = new ArrayList();
        Selector coursesSelector = new SimpleSelector(course.asResource(), ontModel.getProperty(SemWebProgramOntology.hasCourseWork), (String) null);
        StmtIterator stmtIterator = courseModel.listStatements(coursesSelector);
        while (stmtIterator.hasNext()) {
            courseWork.add(stmtIterator.nextStatement().getObject());
        }
        return courseWork;
    }
}
