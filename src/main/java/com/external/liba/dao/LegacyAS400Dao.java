package com.external.liba.dao;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 外部 JAR A 的 DAO
 * 所有方法都是傳 Connection 進來，自己不拿也不關
 */
@Repository("legacyAS400Dao")
public class LegacyAS400Dao {

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
            // 只關 ResultSet 和 PreparedStatement，Connection 不關 (由主專案關)
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (ps != null) try { ps.close(); } catch (Exception e) {}
        }
        return result;
    }

    public int insertCustomer(Connection conn, String customerName) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO customers (customer_name, status) VALUES (?, 'ACTIVE')");
            ps.setString(1, customerName);
            return ps.executeUpdate();
        } finally {
            if (ps != null) try { ps.close(); } catch (Exception e) {}
        }
    }
}
