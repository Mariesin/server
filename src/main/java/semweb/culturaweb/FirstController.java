package semweb.culturaweb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
public class FirstController {
    CreateModel m = new CreateModel();
    List<SalleSpectacle> listSalleSpectacle = m.findAllSalleSpectacle();
    List<Cinema> listCinema = m.findAllCinema();

    @RequestMapping(value = "/liste-cultura")
    public String getAllSalleSpectacle(Model model) {
        System.out.println(listSalleSpectacle);
        model.addAttribute("salleSpectacle", listSalleSpectacle);
        return "liste-cultura";
    }

    @RequestMapping(value = "/liste-cinema")
    public String getAllCinema(Model model) {
        System.out.println(listCinema);
        model.addAttribute("salleCinema", listCinema);
        return "liste-cinema";
    }
}
