package com.sml.main;

import com.sml.utils.common.CommonController;
import com.sml.utils.util.Bind;
import com.sml.utils.util.TimeMaximum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController extends CommonController {

    @Autowired
    private MainServiceImpl service;

    // resources - config - config-properties 파일안에 있는 프로퍼티값을 자바에서 사용하기위함 (설정: context-commmon.xml - <util:properties ..>)
    @Value("#{systemProp['db.maria.username']}")
    private String userName;

    // 로그인 페이지 작업하기
    @RequestMapping(value = "main/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "main/login";
    }








    @RequestMapping(value = "main/index", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) throws Exception {

        // 인터셉터에서 값 가져오기
        String interceptorTest = (String) request.getAttribute("interceptorTest");
        System.out.println(interceptorTest);

        // 프로퍼티값 가져오기
        System.out.println("propertie value : " + userName);

        model.addAttribute("name", "이재용");
        model.addAttribute("list", service.selectMain(request));


        /*------ 시,일,월 얼마나 경과했는지 알려주는 로직 ------*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse("2021-11-08 12:33:33"); // 예시1
            // date = sdf.parse("2021-11-05 12:34:56");  예시2
            //date = sdf.parse("2020-05-08 12:11:24");  예시3

        } catch (Exception e) {
            e.printStackTrace();
        }

        String strR_dt = TimeMaximum.calculateTime(date);
        System.out.println("경과시간 : " + strR_dt);

        return "main/index";
    }


    @RequestMapping(value = "main/ajax2", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ajax2(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        String chk = "";
        Bind bind = new Bind(request);
        map = bind.getDto();

        service.insertMain(map);

        return map;
    }



    // 부트스트랩 테스트용
    @RequestMapping(value = "main/test", method = RequestMethod.GET)
    public String test(Model model) {
        return "main/bootTest";
    }
}
