package com.campaigns.webservices.config;

import java.sql.Connection;

/**
 *
 * @author andrew
 */
public interface IJdbcConnector {
    public Connection getConnection();
}
