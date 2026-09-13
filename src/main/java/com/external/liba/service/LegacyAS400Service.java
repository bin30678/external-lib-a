package com.external.liba.service;

import com.external.liba.dao.LegacyAS400Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.util.List;

/**
 * 外部 JAR A 的 Service
 * DAO 由 Spring 注入，但 Connection 由外部主專案傳入
 */
@Service("legacyAS400Service")
public class LegacyAS400Service {

    @Resource(name = "legacyAS400Dao")
    private LegacyAS400Dao legacyAS400Dao;

    // Spring setter injection
    public void setLegacyAS400Dao(LegacyAS400Dao legacyAS400Dao) {
        this.legacyAS400Dao = legacyAS400Dao;
    }

    public List<String> getActiveCustomers(Connection conn) throws Exception {
        System.out.println("=== [External JAR A] getActiveCustomers called ===");
        return legacyAS400Dao.queryActiveCustomers(conn);
    }

    public int addCustomer(Connection conn, String customerName) throws Exception {
        System.out.println("=== [External JAR A] addCustomer called: " + customerName + " ===");
        return legacyAS400Dao.insertCustomer(conn, customerName);
    }
}
