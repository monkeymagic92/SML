package com.sml.main;

import com.sml.common.ComController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController extends ComController {

    @Autowired
    private MainService service;

    @RequestMapping(value = "main/index", method = RequestMethod.GET)
    public String index(Model model) {


        model.addAttribute("name", "이재용");
        model.addAttribute("list", service.selectMain());

        return "main/mainIndex";
    }

    @RequestMapping(value = "main/ajax", method = RequestMethod.POST)
    @ResponseBody
    public MainVO ajaxTestPost(Model model, MainVO vo, HttpServletRequest request) {

        System.out.println("--test--");
        System.out.println(vo.getI_num());
        System.out.println(vo.getTitle());

        return vo;
    }
}
