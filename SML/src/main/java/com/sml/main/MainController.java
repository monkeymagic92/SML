package com.sml.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.log4j.Logger;

@Controller
public class MainController {

    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private MainService service;

    @RequestMapping(value = "main/index", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("name", "이재용");
        model.addAttribute("list", service.selectMain());

        return "main/mainIndex";
    }
}
