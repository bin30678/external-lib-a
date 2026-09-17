package com.external.liba.service;

import com.external.liba.dao.LegacyAS400Dao;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.sql.Connection;
import java.util.List;

/**
 * 外部 JAR A 的 Service
 * 提供操作 cxfdemo2 (java:comp/env/jdbc/cxfdemo2) customers 表的業務邏輯。
 * 支援自主連線操作或外部傳入連線操作。
 */
@Service("legacyAS400Service")
public class LegacyAS400Service {

    @Resource(name = "legacyAS400Dao")
    private LegacyAS400Dao legacyAS400Dao;

    public void setLegacyAS400Dao(LegacyAS400Dao legacyAS400Dao) {
        this.legacyAS400Dao = legacyAS400Dao;
    }

    /**
     * 自主取得連線並取得 ACTIVE 客戶清單
     */
    public List<String> getActiveCustomers() throws Exception {
        System.out.println("=== [External JAR A] getActiveCustomers (Self-managed Connection) called ===");
        return legacyAS400Dao.queryActiveCustomers();
    }

    /**
     * 由外部傳入連線取得 ACTIVE 客戶清單
     */
    public List<String> getActiveCustomers(Connection conn) throws Exception {
        System.out.println("=== [External JAR A] getActiveCustomers (Caller-provided Connection) called ===");
        return legacyAS400Dao.queryActiveCustomers(conn);
    }

    /**
     * 自主取得連線並取得所有客戶清單
     */
    public List<String> getAllCustomerNames() throws Exception {
        System.out.println("=== [External JAR A] getAllCustomerNames called ===");
        return legacyAS400Dao.queryAllCustomerNames();
    }

    /**
     * 自主取得連線並統計指定狀態客戶數量
     */
    public int getCustomerCountByStatus(String status) throws Exception {
        System.out.println("=== [External JAR A] getCustomerCountByStatus called for: " + status + " ===");
        return legacyAS400Dao.countCustomersByStatus(status);
    }

    /**
     * 自主取得連線並新增 ACTIVE 客戶
     */
    public int addCustomer(String customerName) throws Exception {
        System.out.println("=== [External JAR A] addCustomer (Self-managed Connection) called: " + customerName + " ===");
        return legacyAS400Dao.insertCustomer(customerName);
    }

    /**
     * 自主取得連線並新增指定狀態客戶
     */
    public int addCustomer(String customerName, String status) throws Exception {
        System.out.println("=== [External JAR A] addCustomer called: " + customerName + ", status: " + status + " ===");
        return legacyAS400Dao.insertCustomer(customerName, status);
    }

    /**
     * 由外部傳入連線新增 ACTIVE 客戶
     */
    public int addCustomer(Connection conn, String customerName) throws Exception {
        System.out.println("=== [External JAR A] addCustomer (Caller-provided Connection) called: " + customerName + " ===");
        return legacyAS400Dao.insertCustomer(conn, customerName);
    }
}
