package com.sml.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sml.common.ComController;
import com.sml.core.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
public class MainController extends ComController {

    @Autowired
    private MainService service;

    // resources - config - config-properties 파일안에 있는 프로퍼티값을 자바에서 사용하기위함
    @Value("${db.maria.username}")
    private String userName;

    @RequestMapping(value = "main/index", method = RequestMethod.GET)
    public String index(Model model) {

        System.out.println("--jytest--");
        System.out.println(userName);
        System.out.println("--jytest--");

        model.addAttribute("name", "이재용");
        model.addAttribute("list", service.selectMain());

        return "main/mainIndex";
    }

    @RequestMapping(value = "main/index", method = RequestMethod.POST)
    @ResponseBody
    public MainVO ajaxTestPost(Model model, MainVO vo, HttpServletRequest request) {

        System.out.println("--test--");
        System.out.println(vo.getI_num());
        System.out.println(vo.getTitle());
        
        return vo;
    }

    // 부트스트랩 테스트용
    @RequestMapping(value = "main/test", method = RequestMethod.GET)
    public String test(Model model) {
        return "main/test";
    }
}
