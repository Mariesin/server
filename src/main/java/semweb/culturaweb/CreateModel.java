package semweb.culturaweb;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CreateModel {
    public static String datasetURL = "http://localhost:3030/semweb";
    public static String sparqlEndpoint = datasetURL + "/sparql";
    public static String sparqlUpdate = datasetURL + "/update";
    public static String graphStore = datasetURL + "/data";
    public static RDFConnection accessor = RDFConnectionFactory.connect(sparqlEndpoint, sparqlUpdate, graphStore);

    public static void main(String[] args) throws IOException {
        Model modelSalleSpectacle = modelSalleSpectacle();
        //sendOntologyToFuseki(accessor, modelSalleSpectacle);
        //List<Literal> listname = findName();
    }

    public static void sendOntologyToFuseki(RDFConnection accessor, Model model) {
        if (accessor != null) {
            try {
                model.read(new FileInputStream("/home/sinama/Documents/M2/Web sementique/theater_salle_spectacle.ttl"), null, "TURTLE");
                accessor.load(model);
            } catch (Exception e) {
                System.out.println("error");
            }
        }
    }

    public static Model modelSalleSpectacle() throws IOException {
        int i = 0;

        String geo_point, nom_equipement, gestionnaire, telephone, site_web, id_secteur_postal, ville, secteur, quartier, jauge_salle_de_spectacle, oid;

        BufferedReader in = new BufferedReader(new FileReader("/home/sinama/Documents/M2/Web sementique/theatres-et-salles-de-spectacles.txt"));
        String line;
        String ex = "http://example.org/";
        String geo = "http://www.w3.org/2003/01/geo/wgs84_pos#";
        String xsd = "http://www.w3.org/2001/XMLSchema#";
        String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
        String foaf = "http://xmlns.com/foaf/0.1/";
        String vcard = "http://www.w3.org/2001/vcard-rdf/3.0#";
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("ex", ex);
        model.setNsPrefix("geo", geo);
        model.setNsPrefix("xsd", xsd);
        model.setNsPrefix("rdfs", rdfs);
        model.setNsPrefix("foaf", foaf);
        model.setNsPrefix("vcard", vcard);
//Geo Point;Geo Shape;nom_equipement;gestionnaire;telephone;site_web;acces_metro;acces_bus;x;y;numero;lib_off;id_secteur_postal;ville;secteur;quartier;jauge_salle_de_spectacle;oid
        while ((line = in.readLine()) != null) {
            if (i > 0) {
                // Recuperation des informations
                String[] words = line.split(";");
                geo_point = words[0];
                nom_equipement = words[2];
                gestionnaire = words[3];
                telephone = words[4];
                site_web = words[5];
                id_secteur_postal = words[12];
                ville = words[13];
                secteur = words[14];
                quartier = words[15];
                jauge_salle_de_spectacle = words[16];
                oid = words[17];

                Resource resourceId = model.createResource(ex + oid);

                resourceId.addProperty(FOAF.name, nom_equipement, "fr");
                resourceId.addProperty(RDFS.comment, jauge_salle_de_spectacle);
                resourceId.addProperty(VCARD.Orgname, gestionnaire);
                resourceId.addProperty(VCARD.TEL, telephone);
                resourceId.addProperty(FOAF.homepage, site_web);
                resourceId.addProperty(VCARD.ADR, ville + " secteur " +secteur + " quartier " + quartier + " " + id_secteur_postal);

                Property propGeo = model.createProperty(geo + "Point");
                resourceId.addProperty(propGeo, geo_point);
            }
            i++;
            //model.write(System.out, "TURTLE");
            try {
                model.write(new FileOutputStream("/home/sinama/Documents/M2/Web sementique/theater_salle_spectacle.ttl"), "TURTLE");
            } catch (Exception e) {
                System.out.println("error");
            }
        }
        in.close();
        return model;
    }

    public static List<Literal> findName() {
        List<Literal> listeName = new ArrayList<>();
        QueryExecution qExec = accessor.query("PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" +
                "\r\n" +
                "SELECT DISTINCT ?name \r\n" +
                "WHERE {\r\n" +
                "  ?subject foaf:name ?name\r\n" +
                "}\r\n" +
                "LIMIT 25");
        ResultSet rs = qExec.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            Literal subject = qs.getLiteral("name");
            listeName.add(subject);
        }
        qExec.close();
        accessor.close();
        System.out.println(listeName);
        return listeName;
    }
}
