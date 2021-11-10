package com.sml.main;

import com.sml.common.ComController;
import com.sml.util.TimeMaximum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MainController extends ComController {

    @Autowired
    private MainService service;

    // resources - config - config-properties 파일안에 있는 프로퍼티값을 자바에서 사용하기위함 (설정: context-commmon - <til:properties ..>)
    @Value("#{systemProp['db.maria.username']}")
    private String userName;

    @RequestMapping(value = "main/index", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {

        // 인터셉터에서 값 가져오기
        String interceptorTest = (String) request.getAttribute("interceptorTest");
        System.out.println(interceptorTest);

        // 프로퍼티값 가져오기
        System.out.println("propertie value : " + userName);

        model.addAttribute("name", "이재용");
        model.addAttribute("list", service.selectMain());


        /*------ 시,일,월 얼마나 경과했는지 알려주는 로직 ------*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse("2021-11-08 12:33:33"); // 예시1
            //date = sdf.parse("2021-11-05 12:34:56");  예시2
            //date = sdf.parse("2020-05-08 12:11:24");  예시3

        } catch (Exception e) {
            e.printStackTrace();
        }
        String strR_dt = TimeMaximum.calculateTime(date);
        System.out.println("경과시간 : " + strR_dt);

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

    // 부트스트랩 테스트용
    @RequestMapping(value = "main/test", method = RequestMethod.GET)
    public String test(Model model) {
        return "main/bootTest";
    }
}
