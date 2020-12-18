package semweb.culturaweb;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.jena.rdf.model.Literal;


@Data
@Getter
@Setter
public class Cinema {
    private String type;
    private Literal name;
    private Literal fauteuils;
    private Literal commune;
    private Literal ecran3D;
    private Literal ecrans;
}
