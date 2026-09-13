package com.external.liba;

import com.external.liba.dao.LegacyAS400Dao;
import com.external.liba.service.LegacyAS400Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * 外部 JAR A 的 Spring 整合測試
 * 透過 Spring 讀取 applicationContext-liba.xml，將所有 Bean 註冊並委派給 Spring 容器管理與注入
 */
@SpringJUnitConfig(locations = "classpath:applicationContext-liba.xml")
public class LegacyAS400ServiceTest {

    @Resource
    private ApplicationContext applicationContext;

    @Resource(name = "legacyAS400Service")
    private LegacyAS400Service service;

    @Resource(name = "legacyAS400Dao")
    private LegacyAS400Dao dao;

    @Test
    public void testSpringContextLoaded() {
        Assertions.assertNotNull(applicationContext, "Spring ApplicationContext 必須成功載入");
        Assertions.assertNotNull(service, "Spring 必須透過 component-scan 成功建立並注入 LegacyAS400Service");
        Assertions.assertNotNull(dao, "Spring 必須透過 component-scan 成功建立並注入 LegacyAS400Dao");
        Assertions.assertTrue(applicationContext.containsBean("legacyAS400Service"));
        Assertions.assertTrue(applicationContext.containsBean("legacyAS400Dao"));
    }

    @Test
    public void testGetActiveCustomers() throws Exception {
        // 模擬 Connection，由 Spring 容器管理的 Service 呼叫注入的 DAO 執行查詢
        Connection mockConn = Mockito.mock(Connection.class);
        PreparedStatement mockPs = Mockito.mock(PreparedStatement.class);
        ResultSet mockRs = Mockito.mock(ResultSet.class);

        Mockito.when(mockConn.prepareStatement(Mockito.anyString())).thenReturn(mockPs);
        Mockito.when(mockPs.executeQuery()).thenReturn(mockRs);
        Mockito.when(mockRs.next()).thenReturn(true, false);
        Mockito.when(mockRs.getString("customer_name")).thenReturn("Alice");

        List<String> customers = service.getActiveCustomers(mockConn);
        Assertions.assertEquals(1, customers.size());
        Assertions.assertEquals("Alice", customers.get(0));

        // 驗證 DAO 有正確關閉 ResultSet 與 Statement，且沒有關閉 Connection
        Mockito.verify(mockRs, Mockito.times(1)).close();
        Mockito.verify(mockPs, Mockito.times(1)).close();
        Mockito.verify(mockConn, Mockito.never()).close();
    }

    @Test
    public void testAddCustomer() throws Exception {
        Connection mockConn = Mockito.mock(Connection.class);
        PreparedStatement mockPs = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockConn.prepareStatement(Mockito.anyString())).thenReturn(mockPs);
        Mockito.when(mockPs.executeUpdate()).thenReturn(1);

        int rows = service.addCustomer(mockConn, "Bob");
        Assertions.assertEquals(1, rows);

        Mockito.verify(mockPs, Mockito.times(1)).setString(1, "Bob");
        Mockito.verify(mockPs, Mockito.times(1)).close();
        Mockito.verify(mockConn, Mockito.never()).close();
    }
}
