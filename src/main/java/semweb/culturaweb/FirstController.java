package semweb.culturaweb;

import org.apache.jena.rdf.model.Literal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class FirstController {
    CreateModel m = new CreateModel();
    List<Literal> list = m.findName();

    @RequestMapping(value = "/liste-cultura")
    public String getAll(Model model) {
        model.addAttribute("salle", list);
        return "liste-cultura";
    }
}