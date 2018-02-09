# ID2208 Programming The Semantic Web Tutorial for Project VT18

Java implementation of a semantic web service using the whole stack of OWL, RDF, SPARQL processing.
See associated PDF tutorial description in `/doc/`

## Installation

>  Maven project, use the following commands to compile, build, create WAR file, run tests respectively.

```sh
mvn compile
mvn clean install
mvn compile war:war
mvn test
```

## Deploy the WebApplication on Tomcat from the Shell (Use your IDE support instead if you can)
> First create the war file
```sh
mvn compile war:war
```

> You should now have a war file in `/target/project_18_tutorial.war`. Next step is to copy the war file to the TOMCAT installation folder and removing any old war there
```sh
rm −rf tomcat-install-dir-path/webapps/ROOT∗
cp target/project_18_tutorial.war tomcat-install-dir/webapps/ROOT.war
```
> Start Tomcat
```sh
tomcat-install-dir-path/bin/catalina.sh start
```

> Check that Tomcat deployed the WAR correctly, go with browser to: `http://localhost:8080/manager` to see all deployed webapps.
> Note that this project assumes that the application is deployed at the root, i.e localhost:8080/... and not localhost:8080/projectname/...
> It might be that Tomcat by default deploys to localhost:8080/projectname/..., then you must configure this or change the URLs in the code to account for this.
> If you name the war as "ROOT.war" most likely this will not be a problem.

> Stop the server when done
```sh
tomcat-install-dir-path/bin/catalina.sh stop
```

## Test the services

> Use curl or wget

```sh
curl -H "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/institutes/kth/
curl -H "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/institutes/chalmers/
curl -H "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/programs/semwebprogram
curl -H "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/ontologies/semwebprogram
```

```sh
wget --header "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/institutes/kth/
wget --header "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/institutes/chalmers/
wget --header "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/programs/semwebprogram
wget --header "Accept: application/rdf+xml" http://localhost:8080/id2208/tutorial/ontologies/semwebprogram
```
> Or point your browser to one of the URLs (but then you will get the text/html view and not the RDF view)
## Run Client

> **After** you have started the services on localhost:8080 you can run the client from inside the IDE or with the following command:

```sh
mvn exec:java -Dexec.mainClass="kth.se.id2208.vt18.project.tutorial.client.Agent"
```

## Inspect the RDF/OWL

See `/src/main/resources/`

## License

MIT - KTH ID2208

## Author
13/1 - 2018

Kim Hammar

kimham@kth.se
