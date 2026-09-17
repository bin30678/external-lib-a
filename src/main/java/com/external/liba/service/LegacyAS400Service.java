package com.external.liba.service;

import com.external.liba.dao.LegacyAS400Dao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.util.List;

/**
 * 外部 JAR A 的 Service
 * 依據規範：Service 層方法必須由外部呼叫端傳入 Connection 與對應參數，委派給 DAO 執行。
 */
@Service("legacyAS400Service")
public class LegacyAS400Service {

    @Resource(name = "legacyAS400Dao")
    private LegacyAS400Dao legacyAS400Dao;

    public void setLegacyAS400Dao(LegacyAS400Dao legacyAS400Dao) {
        this.legacyAS400Dao = legacyAS400Dao;
    }

    public List<String> getActiveCustomers(Connection conn) throws Exception {
        System.out.println("=== [External JAR A] getActiveCustomers called ===");
        return legacyAS400Dao.queryActiveCustomers(conn);
    }

    public List<String> getCustomersByStatus(Connection conn, String status) throws Exception {
        System.out.println("=== [External JAR A] getCustomersByStatus called for: " + status + " ===");
        return legacyAS400Dao.queryCustomersByStatus(conn, status);
    }

    public List<String> getAllCustomerNames(Connection conn) throws Exception {
        System.out.println("=== [External JAR A] getAllCustomerNames called ===");
        return legacyAS400Dao.queryAllCustomerNames(conn);
    }

    public int getCustomerCountByStatus(Connection conn, String status) throws Exception {
        System.out.println("=== [External JAR A] getCustomerCountByStatus called for: " + status + " ===");
        return legacyAS400Dao.countCustomersByStatus(conn, status);
    }

    public int addCustomer(Connection conn, String customerName) throws Exception {
        System.out.println("=== [External JAR A] addCustomer called: " + customerName + " ===");
        return legacyAS400Dao.insertCustomer(conn, customerName);
    }

    public int addCustomer(Connection conn, String customerName, String status) throws Exception {
        System.out.println("=== [External JAR A] addCustomer called: " + customerName + ", status: " + status + " ===");
        return legacyAS400Dao.insertCustomer(conn, customerName, status);
    }

    public int modifyCustomerStatus(Connection conn, String customerName, String newStatus) throws Exception {
        System.out.println("=== [External JAR A] modifyCustomerStatus called for: " + customerName + " -> " + newStatus + " ===");
        return legacyAS400Dao.updateCustomerStatus(conn, customerName, newStatus);
    }
}
