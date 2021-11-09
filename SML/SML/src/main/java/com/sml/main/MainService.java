package com.sml.main;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {

    //protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private MainMapper mapper;

    public List<MainVO> selectMain() {
        return mapper.selectMain();
    }

    // Controller <-> SErvice
}
