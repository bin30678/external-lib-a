package com.external.liba.dao;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 外部 JAR A 的 DAO
 * 依據規範：所有資料庫操作方法皆必須傳入 Connection 與對應參數，連線生命週期由外部主專案統一控管。
 */
@Repository("legacyAS400Dao")
public class LegacyAS400Dao {

    /**
     * 查詢指定狀態之客戶清單
     */
    public List<String> queryCustomersByStatus(Connection conn, String status) throws Exception {
        if (conn == null) {
            throw new IllegalArgumentException("Connection 不能為 null");
        }
        List<String> result = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT customer_name FROM customers WHERE status = ?");
            ps.setString(1, status);
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("customer_name"));
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
        }
        return result;
    }

    /**
     * 查詢所有 ACTIVE 狀態客戶
     */
    public List<String> queryActiveCustomers(Connection conn) throws Exception {
        return queryCustomersByStatus(conn, "ACTIVE");
    }

    /**
     * 查詢全部客戶名稱
     */
    public List<String> queryAllCustomerNames(Connection conn) throws Exception {
        if (conn == null) {
            throw new IllegalArgumentException("Connection 不能為 null");
        }
        List<String> result = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT customer_name FROM customers");
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("customer_name"));
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
        }
        return result;
    }

    /**
     * 依狀態統計客戶數量
     */
    public int countCustomersByStatus(Connection conn, String status) throws Exception {
        if (conn == null) {
            throw new IllegalArgumentException("Connection 不能為 null");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT COUNT(*) FROM customers WHERE status = ?");
            ps.setString(1, status);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
        }
    }

    /**
     * 新增 ACTIVE 客戶
     */
    public int insertCustomer(Connection conn, String customerName) throws Exception {
        return insertCustomer(conn, customerName, "ACTIVE");
    }

    /**
     * 新增指定狀態之客戶
     */
    public int insertCustomer(Connection conn, String customerName, String status) throws Exception {
        if (conn == null) {
            throw new IllegalArgumentException("Connection 不能為 null");
        }
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO customers (customer_name, status) VALUES (?, ?)");
            ps.setString(1, customerName);
            ps.setString(2, status != null ? status : "ACTIVE");
            return ps.executeUpdate();
        } finally {
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
        }
    }

    /**
     * 更新客戶狀態
     */
    public int updateCustomerStatus(Connection conn, String customerName, String newStatus) throws Exception {
        if (conn == null) {
            throw new IllegalArgumentException("Connection 不能為 null");
        }
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE customers SET status = ? WHERE customer_name = ?");
            ps.setString(1, newStatus);
            ps.setString(2, customerName);
            return ps.executeUpdate();
        } finally {
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
        }
    }
}
