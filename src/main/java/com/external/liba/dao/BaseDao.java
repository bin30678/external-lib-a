package com.external.liba.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 外部模組 BaseDao 抽象類別，結構與主專案 BaseDao 保持一致。
 * 持有 SqlSessionTemplate、JdbcTemplate、DataSource 三個欄位。
 */
public abstract class BaseDao {

    protected final SqlSessionTemplate sqlSessionTemplate;
    protected final JdbcTemplate jdbcTemplate;
    protected final DataSource dataSource;

    protected BaseDao(SqlSessionTemplate sqlSessionTemplate,
                      JdbcTemplate jdbcTemplate,
                      DataSource dataSource) {
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
