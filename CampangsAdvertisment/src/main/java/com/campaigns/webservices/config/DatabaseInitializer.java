package com.campaigns.webservices.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author andrew
 */
public class DatabaseInitializer {
    public static void main(String[] args0) {
        H2Connector connector = new H2Connector();
        Connection con = connector.getConnection();
        PreparedStatement createPlatformStatement = null;
        PreparedStatement createAdStatement = null;
        PreparedStatement createCampaignStatement = null;
        
        PreparedStatement dropPlatformStatement = null;
        PreparedStatement dropAdStatement = null;
        PreparedStatement dropCampaignStatement = null;       

        String createPlatformQuery = "CREATE TABLE PLATFORM(name int, ad_id INT, FOREIGN KEY (ad_id)  REFERENCES ADS (id))";
        String dropPlatformQuery = "DROP TABLE IF EXISTS PLATFORM";
        String insertPlatformQuery = "INSERT INTO PLATFORM" + "(name, ad_id) values" + "(?,?)";
        String selectPlatformQuery = "select * from PLATFORM";
        
        String createAdQuery = "CREATE TABLE ADS(id int primary key auto_increment, name varchar(255), status int, asset_url varchar(255), campaign_id INT,"
                + " FOREIGN KEY (campaign_id)  REFERENCES CAMPAIGNS (id));";
        String dropAdQuery = "DROP TABLE IF EXISTS ADS";
        String insertAdQuery = "INSERT INTO ADS" + "(name, status, asset_url, campaign_id) values" + "(?,?,?,?)";
        String selectAdQuery = "select * from ADS";
        
        String createCampaignQuery = "CREATE TABLE CAMPAIGNS(id int primary key auto_increment, name varchar(255), status int, start_date timestamp, "
                + "end_date timestamp)";         
        String dropCampaignsQuery = "DROP TABLE IF EXISTS CAMPAIGNS";
        String insertCampaignQuery = "INSERT INTO CAMPAIGNS" + "(name, status, start_date, end_date) values" + "(?,?,?,?)";
        String selectCampaignQuery = "select * from CAMPAIGNS";
        
        try {
            con.setAutoCommit(false);

            dropPlatformStatement = con.prepareStatement(dropPlatformQuery);
            dropPlatformStatement.execute();
            dropPlatformStatement.close();
            
            dropAdStatement = con.prepareStatement(dropAdQuery);
            dropAdStatement.execute();
            dropAdStatement.close();
            
            dropCampaignStatement = con.prepareStatement(dropCampaignsQuery);
            dropCampaignStatement.execute();
            dropCampaignStatement.close();
            
            createCampaignStatement = con.prepareStatement(createCampaignQuery);
            createCampaignStatement.execute();
            createCampaignStatement.close();
            
            createAdStatement = con.prepareStatement(createAdQuery);
            createAdStatement.execute();
            createAdStatement.close();
            
            createPlatformStatement = con.prepareStatement(createPlatformQuery);
            createPlatformStatement.execute();
            createPlatformStatement.close();            

            int firstCamId = executeInserCampaignStatement(insertCampaignQuery, con, "Inna", 1, new Timestamp(1920,1,1,1,1,1,1), new Timestamp(1920,1,1,1,1,1,1));
            int secondCamId = executeInserCampaignStatement(insertCampaignQuery, con, "Patti", 2, new Timestamp(1920,1,1,1,1,1,1), new Timestamp(1920,1,1,1,1,1,1));
            int thirdCamId = executeInserCampaignStatement(insertCampaignQuery, con, "Faith", 3, new Timestamp(1920,1,1,1,1,1,1), new Timestamp(1920,1,1,1,1,1,1));
            int firstAdId = executeInsertAdStatement(insertAdQuery, con, "Nadia", 1, "Nadia's asset", firstCamId);
            int secondAdId = executeInsertAdStatement(insertAdQuery, con, "Greta", 1, "Greta's asset", firstCamId);
            int thirdAdId = executeInsertAdStatement(insertAdQuery, con, "Lussi", 2, "Lussi's asset", secondCamId);
            int forthAdId = executeInsertAdStatement(insertAdQuery, con, "Maria", 2, "Maria's asset", secondCamId);
            int fifthAdId = executeInsertAdStatement(insertAdQuery, con, "Hope", 3, "Hope's asset", thirdCamId);
            int sixthAdId = executeInsertAdStatement(insertAdQuery, con, "Jane", 3, "Jane's asset", thirdCamId);
            executeInsertPlatformStatement(insertPlatformQuery, con, 0, firstAdId);
            executeInsertPlatformStatement(insertPlatformQuery, con, 0, secondAdId);
            executeInsertPlatformStatement(insertPlatformQuery, con, 0, thirdAdId);
            executeInsertPlatformStatement(insertPlatformQuery, con, 1, forthAdId);
            executeInsertPlatformStatement(insertPlatformQuery, con, 1, fifthAdId);
            executeInsertPlatformStatement(insertPlatformQuery, con, 1, sixthAdId);
            executeInsertPlatformStatement(insertPlatformQuery, con, 2, firstAdId);
            executeInsertPlatformStatement(insertPlatformQuery, con, 2, secondAdId);
            executeInsertPlatformStatement(insertPlatformQuery, con, 2, thirdAdId);
            
            selectAllPlatforms(selectPlatformQuery, con);
            selectAllAds(selectAdQuery, con);
            selectAllCampaigns(selectCampaignQuery, con);

            con.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /*try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseInitializer.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }
    
    public static void executeInsertPlatformStatement(String query, Connection connection, int id, int ad_id) throws SQLException {
        PreparedStatement insertStatement = null;
        insertStatement = connection.prepareStatement(query);
        insertStatement.setInt(1, id);
        insertStatement.setInt(2, ad_id);
        insertStatement.executeUpdate();
        insertStatement.close();
    }
    
    public static int executeInsertAdStatement(String query, Connection connection, String name, int status, String assetUrl, int campaign_id) throws SQLException {
        int id = 0;
        String[] columnNames = new String[] { "id" };
        PreparedStatement insertStatement = null;
        insertStatement = connection.prepareStatement(query, columnNames);
        insertStatement.setString(1, name);
        insertStatement.setInt(2, status);
        insertStatement.setString(3, assetUrl);
        insertStatement.setInt(4, campaign_id);
        if (insertStatement.executeUpdate() > 0) {
            java.sql.ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            if ( generatedKeys.next() ) {
                id = generatedKeys.getInt(1);
            }
        }
        insertStatement.close();
        return id;
    }
    
    public static int executeInserCampaignStatement(String query, Connection connection, String name, int status, Timestamp startDate, Timestamp endDate) throws SQLException {
        int id = 0;
        String[] columnNames = new String[] { "id" };
        PreparedStatement insertStatement = null;
        insertStatement = connection.prepareStatement(query, columnNames);
        insertStatement.setString(1, name);
        insertStatement.setInt(2, status);
        insertStatement.setTimestamp(3, startDate);
        insertStatement.setTimestamp(4, endDate);
        if (insertStatement.executeUpdate() > 0) {
            java.sql.ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            if ( generatedKeys.next() ) {
                id = generatedKeys.getInt(1);
            }
        }
        insertStatement.close();
        return id;
    }
    
    public static void selectAllCampaigns(String query, Connection connection) throws SQLException {
        PreparedStatement selectStatement = null;
        selectStatement = connection.prepareStatement(query);
        ResultSet rs = selectStatement.executeQuery();
        System.out.println("CAMPAIGNS: ");
        while (rs.next()) {
            System.out.println("Id " + rs.getInt("id") + " Name " + rs.getString("name") + " Status " + rs.getInt("status") + " Start date " 
                    + rs.getTimestamp("start_date")  + " End date " + rs.getTimestamp("end_date"));
        }
        selectStatement.close();
    }
    
    public static void selectAllAds(String query, Connection connection) throws SQLException {
        PreparedStatement selectStatement = null;
        selectStatement = connection.prepareStatement(query);
        ResultSet rs = selectStatement.executeQuery();
        System.out.println("ADS: ");
        while (rs.next()) {
            System.out.println("Id " + rs.getInt("id") + " Name " + rs.getString("name") + " Status " + rs.getInt("status") + " Asset " 
                    + rs.getString("asset_url")  + " Campaign REF " + rs.getInt("campaign_id"));
        }
        selectStatement.close();
    }
    
    public static void selectAllPlatforms(String query, Connection connection) throws SQLException {
        PreparedStatement selectStatement = null;
        selectStatement = connection.prepareStatement(query);
        ResultSet rs = selectStatement.executeQuery();
        System.out.println("PLATFORMS: ");
        while (rs.next()) {
            System.out.println("Id " + rs.getInt("name") + " AD REF " + rs.getInt("ad_id"));
        }
        selectStatement.close();
    }
}
