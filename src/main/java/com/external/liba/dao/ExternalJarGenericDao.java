package com.external.liba.dao;

import org.springframework.stereotype.Repository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 外部 JAR 內部所包含的 GenericDao。
 * 依據使用者確認之公司現況：
 * 外部 JAR 內部擁有自己的 GenericDao，不依賴 Spring 自動注入，
 * 而是直接執行 new InitialContext().lookup(...) 查找主應用環境所提供的 JNDI 資源。
 * 此類別為真實打包於 external-lib-a 依賴中的 DAO，標記 @Repository 供 Spring component-scan 註冊。
 */
@Repository("externalJarGenericDao")
public class ExternalJarGenericDao {

    public static final String DEFAULT_JNDI_NAME = "java:comp/env/jdbc/cxfdemo2";

    public Connection getConnectionFromJndi(String jndiName) throws NamingException, SQLException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(jndiName);
        if (ds == null) {
            throw new IllegalStateException("JNDI lookup returned null for " + jndiName);
        }
        return ds.getConnection();
    }

    /**
     * 模擬外部 JAR 內部呼叫：自行 lookup JNDI 取連線並查詢 ACTIVE 客戶
     */
    public List<String> queryActiveCustomersViaInternalLookup() throws Exception {
        return queryActiveCustomersViaInternalLookup(DEFAULT_JNDI_NAME);
    }

    public List<String> queryActiveCustomersViaInternalLookup(String jndiName) throws Exception {
        try (Connection conn = getConnectionFromJndi(jndiName)) {
            return new LegacyAS400Dao().queryActiveCustomers(conn);
        }
    }

    /**
     * 模擬外部 JAR 內部呼叫：自行 lookup JNDI 取連線並查詢 INACTIVE 客戶數量
     */
    public int queryInactiveCustomerCountViaInternalLookup() throws Exception {
        return queryInactiveCustomerCountViaInternalLookup(DEFAULT_JNDI_NAME);
    }

    public int queryInactiveCustomerCountViaInternalLookup(String jndiName) throws Exception {
        try (Connection conn = getConnectionFromJndi(jndiName)) {
            List<String> inactive = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT customer_name FROM customers WHERE status = 'INACTIVE'");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inactive.add(rs.getString("customer_name"));
                }
            }
            return inactive.size();
        }
    }

    /**
     * 外部 JAR 內部交易邊界驗證方法：
     * 自行 lookup JNDI 取連線，插入客戶紀錄，依參數執行 commit 或 rollback。
     * 用於驗證外部 JAR 內部連線與主應用 Spring 交易的獨立性。
     */
    public boolean insertCustomerViaInternalLookup(String customerName, boolean commit) throws Exception {
        return insertCustomerViaInternalLookup(DEFAULT_JNDI_NAME, customerName, commit);
    }

    public boolean insertCustomerViaInternalLookup(String jndiName, String customerName, boolean commit) throws Exception {
        Connection conn = null;
        try {
            conn = getConnectionFromJndi(jndiName);
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO customers (customer_name, status) VALUES (?, 'ACTIVE')")) {
                ps.setString(1, customerName);
                ps.executeUpdate();
            }
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return true;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ignored) {}
            }
        }
    }
}
