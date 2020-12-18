package semweb.culturaweb;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.jena.rdf.model.Literal;


@Data
@Getter
@Setter
public class SalleSpectacle {
    private Literal geo_point;
    private Literal name;
    private Literal gestionnaire;
    private Literal telephone;
    private Literal site_web;
    private Literal id_secteur_postal;
    private Literal ville;
    private Literal secteur;
    private Literal quartier;
    private Literal jauge_salle_de_spectacle;
    private Literal oid;
}
