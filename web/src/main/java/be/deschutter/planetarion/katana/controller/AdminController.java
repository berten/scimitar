package be.deschutter.planetarion.katana.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @RequestMapping("admin/dashboard")
    public ModelAndView dashboard() {

        return new ModelAndView("admin/dashboard");
    }

}
