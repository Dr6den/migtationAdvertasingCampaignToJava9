package com.campaigns.dao;

import java.sql.Connection;

/**
 *
 * @author andrew
 */
public interface IJdbcConnector {
    public Connection getConnection();
}
