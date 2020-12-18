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
        sendOntologyToFuseki(accessor, modelSalleSpectacle(),"src/main/resources/theatres-et-salles-de-spectacles.ttl");
        sendOntologyToFuseki(accessor, modelCinema(),"src/main/resources/Liste_Cinema_2019.ttl");
    }

    public static void sendOntologyToFuseki(RDFConnection accessor, Model model, String File) {
        if (accessor != null) {
            try {
                model.read(new FileInputStream(File), null, "TURTLE");
                accessor.load(model);
            } catch (Exception e) {
                System.out.println("error");
            }
        }
    }

    public static Model modelSalleSpectacle() throws IOException {
        int i = 0;
        String geo_point, nom_equipement, gestionnaire, telephone, site_web, id_secteur_postal, ville, secteur, quartier, jauge_salle_de_spectacle, oid;

        BufferedReader in = new BufferedReader(new FileReader("src/main/resources/theatres-et-salles-de-spectacles.txt"));
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
                jauge_salle_de_spectacle = words[16];
                oid = words[17];

                Resource resourceId = model.createResource(ex + oid);

                resourceId.addProperty(FOAF.name, nom_equipement);
                resourceId.addProperty(RDFS.comment, jauge_salle_de_spectacle);
                resourceId.addProperty(VCARD.Orgname, gestionnaire);
                resourceId.addProperty(VCARD.TEL, telephone);
                resourceId.addProperty(FOAF.homepage, site_web);
                resourceId.addProperty(VCARD.ADR, ville);
                resourceId.addProperty(model.createProperty(ex + "secteurpostal"), id_secteur_postal);

                Property propGeo = model.createProperty(geo + "Point");
                resourceId.addProperty(propGeo, geo_point);
            }
            i++;
            try {
                model.write(new FileOutputStream("src/main/resources/theater_salle_spectacle.ttl"), "TURTLE");
            } catch (Exception e) {
                System.out.println("error");
            }
        }
        in.close();
        return model;
    }

    public static Model modelCinema() throws IOException {
        BufferedReader in;
        String line;

        String NumAuto,NomEtab, Ecrans, Fauteuils, CodeCommune, Commune, Ecran3D;
        String ex = "http://example.org/";
        String geo = "http://www.w3.org/2003/01/geo/wgs84_pos#";
        String xsd = "http://www.w3.org/2001/XMLSchema#";
        String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
        Model model = ModelFactory.createDefaultModel();

        model.setNsPrefix("ex", ex);
        model.setNsPrefix("geo", geo);
        model.setNsPrefix("xsd", xsd);
        model.setNsPrefix("rdfs", rdfs);

        in = new BufferedReader(new FileReader("src/main/resources/Liste_Cinema_2019.csv"));

        while ((line = in.readLine()) != null )
        {
            String[] words = line.split(";");
            NumAuto = words[0];
            NomEtab= words[1];
            Ecrans= words[2];
            Fauteuils= words[3];
            CodeCommune= words[4];
            Commune= words[5];
            Ecran3D= words[8];

            Resource resourceId = model.createResource(ex + NumAuto);
            resourceId.addProperty(RDF.type, "Cinema");
            resourceId.addProperty(RDFS.label, NomEtab);

            Property propEcrans=model.createProperty(ex+"Ecrans");
            resourceId.addProperty(propEcrans, Ecrans);

            Property propFauteuils=model.createProperty(ex+"Fauteuils");
            resourceId.addProperty(propFauteuils, Fauteuils);

            Property propEcran3d=model.createProperty(ex+"Ecran3D");
            resourceId.addProperty(propEcran3d, Ecran3D);

            Property propCodeCommune=model.createProperty(geo+"CodeCommune");
            resourceId.addProperty(propCodeCommune, CodeCommune);

            Property propCommune=model.createProperty(geo+"Commune");
            resourceId.addProperty(propCommune, Commune,"fr");
            try {
                model.write(new FileOutputStream("src/main/resources/Liste_Cinema_2019.ttl"), "TURTLE");
            } catch (Exception e) {
                System.out.println("error");
            }
        }
        in.close();
        return model;
    }

    public static List<SalleSpectacle> findAllSalleSpectacle() {
        List<SalleSpectacle> listeSalles = new ArrayList<>();
        QueryExecution qExec = accessor.query("\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                "\n" +
                "\n" +
                "SELECT DISTINCT ?name ?link ?ville ?tel ?gestionnaire ?geolocalisation\n" +
                "WHERE {\n" +
                "  ?subject foaf:name ?name; \n" +
                "           foaf:homepage ?link; \n" +
                "           vcard:ADR ?ville;\n" +
                "           vcard:TEL ?tel;\n" +
                "           vcard:Orgname ?gestionnaire;\n" +
                "           geo:Point ?geolocalisation\n" +
                "           \n" +
                "}\n" +
                "LIMIT 25");
        ResultSet rs = qExec.execSelect();
        while (rs.hasNext()) {
            SalleSpectacle salleSpectacle = new SalleSpectacle();
            QuerySolution qs = rs.next();
            salleSpectacle.setName(qs.getLiteral("name"));
            salleSpectacle.setSite_web(qs.getLiteral("link"));
            salleSpectacle.setVille(qs.getLiteral("ville"));
            salleSpectacle.setTelephone(qs.getLiteral("tel"));
            salleSpectacle.setGestionnaire(qs.getLiteral("gestionnaire"));
            salleSpectacle.setGeo_point(qs.getLiteral("geolocalisation"));
            listeSalles.add(salleSpectacle);
        }
        System.out.println(listeSalles);
        return listeSalles;
    }

    public static List<Cinema> findAllCinema() {
        List<Cinema> listeCinema = new ArrayList<>();
        QueryExecution qExec = accessor.query(
                "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                        "                PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "                PREFIX ex: <http://example.org/>   \n" +
                        "                SELECT DISTINCT ?name ?commune ?ecran ?ecran3D ?fauteuils\n" +
                        "                WHERE {\n" +
                        "                  ?subject rdfs:label ?name;\n" +
                        "                           geo:Commune ?commune;\n" +
                        "                           ex:Ecrans ?ecran;\n" +
                        "                           ex:Ecran3D ?ecran3D;\n" +
                        "                          ex:Fauteuils ?fauteuils\n" +
                        "               \n" +
                        "                }\n" +
                        "                LIMIT 25");
        ResultSet rs = qExec.execSelect();
        while (rs.hasNext()) {
            Cinema cinema = new Cinema();
            QuerySolution qs = rs.next();
            cinema.setName(qs.getLiteral("name"));
            cinema.setType("Cinema");
            cinema.setEcrans(qs.getLiteral("ecran"));
            cinema.setEcran3D(qs.getLiteral("ecran3D"));
            cinema.setFauteuils(qs.getLiteral("fauteuils"));
            cinema.setCommune(qs.getLiteral("commune"));
            listeCinema.add(cinema);
        }
        return listeCinema;
    }

}
