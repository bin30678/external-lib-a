package com.external.liba.dao;

import com.external.liba.utils.GenericDao;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 外部 JAR A 的 DAO
 * 支援由外部傳入 Connection，或透過 GenericDao.getConnection1() 自主取得 cxfdemo2 資料庫連線。
 */
@Repository("legacyAS400Dao")
public class LegacyAS400Dao {

    /**
     * 自主取得 cxfdemo2 連線並查詢所有 ACTIVE 狀態客戶
     */
    public List<String> queryActiveCustomers() throws Exception {
        Connection conn = null;
        try {
            conn = GenericDao.getConnection1();
            if (conn == null) {
                throw new IllegalStateException("無法取得 cxfdemo2 資料庫連線 (java:comp/env/jdbc/cxfdemo2)");
            }
            return queryActiveCustomers(conn);
        } finally {
            GenericDao.closeConnection(conn);
        }
    }

    /**
     * 由外部傳入連線查詢 ACTIVE 狀態客戶（不關閉傳入之連線）
     */
    public List<String> queryActiveCustomers(Connection conn) throws Exception {
        List<String> result = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT customer_name FROM customers WHERE status = 'ACTIVE'");
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("customer_name"));
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (ps != null) try { ps.close(); } catch (Exception e) {}
        }
        return result;
    }

    /**
     * 自主取得 cxfdemo2 連線並查詢全部客戶名稱
     */
    public List<String> queryAllCustomerNames() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> result = new ArrayList<String>();
        try {
            conn = GenericDao.getConnection1();
            if (conn == null) {
                throw new IllegalStateException("無法取得 cxfdemo2 資料庫連線 (java:comp/env/jdbc/cxfdemo2)");
            }
            ps = conn.prepareStatement("SELECT customer_name FROM customers");
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("customer_name"));
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (ps != null) try { ps.close(); } catch (Exception e) {}
            GenericDao.closeConnection(conn);
        }
        return result;
    }

    /**
     * 自主取得 cxfdemo2 連線並依狀態統計客戶數量
     */
    public int countCustomersByStatus(String status) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = GenericDao.getConnection1();
            if (conn == null) {
                throw new IllegalStateException("無法取得 cxfdemo2 資料庫連線 (java:comp/env/jdbc/cxfdemo2)");
            }
            ps = conn.prepareStatement("SELECT COUNT(*) FROM customers WHERE status = ?");
            ps.setString(1, status);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (ps != null) try { ps.close(); } catch (Exception e) {}
            GenericDao.closeConnection(conn);
        }
    }

    /**
     * 自主取得 cxfdemo2 連線並新增 ACTIVE 客戶
     */
    public int insertCustomer(String customerName) throws Exception {
        return insertCustomer(customerName, "ACTIVE");
    }

    /**
     * 自主取得 cxfdemo2 連線並新增指定狀態客戶
     */
    public int insertCustomer(String customerName, String status) throws Exception {
        Connection conn = null;
        try {
            conn = GenericDao.getConnection1();
            if (conn == null) {
                throw new IllegalStateException("無法取得 cxfdemo2 資料庫連線 (java:comp/env/jdbc/cxfdemo2)");
            }
            return insertCustomer(conn, customerName, status);
        } finally {
            GenericDao.closeConnection(conn);
        }
    }

    /**
     * 由外部傳入連線新增 ACTIVE 客戶（不關閉傳入之連線）
     */
    public int insertCustomer(Connection conn, String customerName) throws Exception {
        return insertCustomer(conn, customerName, "ACTIVE");
    }

    /**
     * 由外部傳入連線新增指定狀態客戶（不關閉傳入之連線）
     */
    public int insertCustomer(Connection conn, String customerName, String status) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO customers (customer_name, status) VALUES (?, ?)");
            ps.setString(1, customerName);
            ps.setString(2, status != null ? status : "ACTIVE");
            return ps.executeUpdate();
        } finally {
            if (ps != null) try { ps.close(); } catch (Exception e) {}
        }
    }
}
