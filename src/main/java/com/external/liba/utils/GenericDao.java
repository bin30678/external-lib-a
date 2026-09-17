package com.external.liba.utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 外部模組通用資料庫連線工具類別 (GenericDao)。
 * 模擬外部 JAR 內部獨立進行 JNDI lookup：
 * - getConnection1(): cxfdemo2 資料庫連線 (java:comp/env/jdbc/cxfdemo2)
 * - getConnection2(): as400_c 資料庫連線 (java:comp/env/jdbc/as400_c)
 */
public class GenericDao {

    public GenericDao() {
    }

    /** 路徑 1: cxfdemo2 資料庫連線 */
    public static Connection getConnection1() {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/cxfdemo2");
            return ds.getConnection();
        } catch (Exception e) {
            return null;
        }
    }

    /** 路徑 2: as400_c 資料庫連線 */
    public static Connection getConnection2() {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/as400_c");
            return ds.getConnection();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 安全關閉資料庫連線
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
