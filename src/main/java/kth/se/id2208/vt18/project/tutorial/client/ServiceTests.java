package kth.se.id2208.vt18.project.tutorial.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

/**
 * Unit Test suite for the services using Jersey testclient.
 *
 * @author Kim Hammar on 2018-01-13.
 */
public class ServiceTests {
    private ClientConfig clientConfig;
    private Client client;
    private WebResource webResource;

    @Before
    public void setUp() throws Exception {
        clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);
        webResource = client.resource("http://localhost:8080");
    }

    /**
     * Tests that the services support both HTML and RDF
     */
    @Test
    public void APITest(){
        //KTH
        ClientResponse clientResponse = webResource.path("/id2208/tutorial/institutes/kth").accept(MediaType.TEXT_HTML).get(ClientResponse.class);
        Assert.assertEquals(200, clientResponse.getStatus());
        clientResponse = webResource.path("/id2208/tutorial/institutes/kth").accept("application/rdf+xml").get(ClientResponse.class);
        Assert.assertEquals(200, clientResponse.getStatus());


        //Chalmers
        clientResponse = webResource.path("/id2208/tutorial/institutes/chalmers").accept(MediaType.TEXT_HTML).get(ClientResponse.class);
        Assert.assertEquals(200, clientResponse.getStatus());
        clientResponse = webResource.path("/id2208/tutorial/institutes/chalmers").accept("application/rdf+xml").get(ClientResponse.class);
        Assert.assertEquals(200, clientResponse.getStatus());

        //semwebprogram
        clientResponse = webResource.path("/id2208/tutorial/programs/semwebprogram").accept(MediaType.TEXT_HTML).get(ClientResponse.class);
        Assert.assertEquals(200, clientResponse.getStatus());
        clientResponse = webResource.path("/id2208/tutorial/programs/semwebprogram").accept("application/rdf+xml").get(ClientResponse.class);
        Assert.assertEquals(200, clientResponse.getStatus());

        //ontology
        clientResponse = webResource.path("/id2208/tutorial/ontologies/semwebprogram").accept(MediaType.TEXT_HTML).get(ClientResponse.class);
        Assert.assertEquals(200, clientResponse.getStatus());
        clientResponse = webResource.path("/id2208/tutorial/ontologies/semwebprogram").accept("application/rdf+xml").get(ClientResponse.class);
        Assert.assertEquals(200, clientResponse.getStatus());

    }
}
