package kth.se.id2208.vt18.project.tutorial.services.chalmers;

import com.sun.jersey.spi.resource.Singleton;
import kth.se.id2208.vt18.project.tutorial.services.common.utils.DataUtils;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;

/**
 * Endpoint representing a webservice for Chalmers to publish courses.
 * Upon startup the service creates some dummy-data and converts it to RDF, ready to serve future requests.
 *
 * To visit, open browser to http://localhost:8080/id2208/tutorial/institutes/chalmers/
 * or from shell
 * curl -H "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/institutes/chalmers/
 * curl -H "Accept: text/html" http://localhost:8080/id2208/tutorial/institutes/chalmers/
 *
 * @author Kim Hammar on 2018-01-13.
 */
@Path("/id2208/tutorial/institutes/chalmers")
@Singleton
public class Chalmers {

    //Read ontology from filesystem so we are not dependent on start-order of services, i.e dont have to wait until ontology service is up
    private OntModel ontModel = DataUtils.readOntologyFileSystem(DataUtils.FLIGHTS_ONTOLOGY_FILE_PATH);
    //Construct the RDF upon starup, in practice you would read from a triple-store instead
    private Model rdfModel = constructRDF();

    /**
     * Return HTML page if a user requested the page
     *
     * @return HTML of the courses (TODO convert this to proper HTML representation)
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String chalmers() {
        StringWriter stringWriter = new StringWriter();
        rdfModel.write(stringWriter, "RDF/XML");
        return "<h1> Chalmers Courses</h1>\n" + stringWriter.toString();
    }

    /**
     * Return RDF/XML if some agent requested the page with accept header "application/rdf+xml"
     *
     * @return RDF/XML of the courses
     */
    @GET
    @Produces("application/rdf+xml")
    public String arlanda() {
        StringWriter stringWriter = new StringWriter();
        rdfModel.write(stringWriter, "RDF/XML");
        return stringWriter.toString();
    }

    private Model constructRDF(){
        Model rdfModel = ModelFactory.createDefaultModel();
        rdfModel.setNsPrefix("smp", DataUtils.ontNS);
        DataUtils.ID2288.toRDF(ontModel, rdfModel);
        return rdfModel;
    }
}
