package com.online.system;

import com.online.system.web.entity.OperationLog;
import com.online.system.web.service.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SystemApplicationTests {

    @Autowired
    private LogService logService;

    @Test
    void contextLoads() {
        logService.add(new OperationLog().setOperSys("1"));
    }

}
