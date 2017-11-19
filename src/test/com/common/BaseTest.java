package com.common;

import com.wy.insuranceadmin.dao.common.UserAccessRecordDao;
import com.wy.insuranceadmin.util.SpringContextHolder;
import com.wy.insuranceadmin.util.UUIDUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ContextConfiguration(locations = "classpath:/spring/applicationContext.xml")
@WebAppConfiguration
@TransactionConfiguration(transactionManager = "interfaceReadTransactionManager", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserAccessRecordDao userAccessRecordDao;


    @Before
    public void setup() {
        MockMvcBuilders.webAppContextSetup(this.wac).build();
        SpringContextHolder.getInstance().setApplicationContext(wac);

    }
}