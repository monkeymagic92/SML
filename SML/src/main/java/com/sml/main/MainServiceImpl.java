package com.sml.main;

import com.sml.utils.util.Bind;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainServiceImpl {

    //protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private MainMapper mapper;

    public List<MainVO> selectMain(HttpServletRequest request) throws Exception {

        Bind bind = new Bind(request);


        return mapper.selectMain();
    }

    public String insertMain(Map<String, Object> map) throws Exception {

        String chk = "";

        System.out.println(map);
        mapper.insertMain(map);

        //mapper.insertMain2(map);

        chk = "success";

        return chk;
    }

}
