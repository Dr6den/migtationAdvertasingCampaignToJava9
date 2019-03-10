package com.campaigns.dao;

import com.campaigns.domain.Ad;
import com.campaigns.domain.Campaign;
import com.campaigns.domain.Platform;
import com.campaigns.domain.Status;
import com.campaigns.domain.Summary;
import com.campaigns.webservices.config.DatabaseInitializer;
import com.campaigns.webservices.config.IJdbcConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author andrew
 */
public class CampaignsDao {
    private IJdbcConnector connector;
    private DatabaseInitializer databaseInitializer;
    
    @Inject
    public CampaignsDao(IJdbcConnector con, DatabaseInitializer initializer) {
        this.connector = con;
        this.databaseInitializer = initializer;
        //initializer.createDatabase();
    }    
    
    public List<Summary> getSummary(int limit, String filterByName, String filterByStatus, String sortingParameter) {
        List<Summary> summaries = new ArrayList<>();
        try {
            Connection con = connector.getConnection();
            String query = "SELECT CAMPAIGNS.id, CAMPAIGNS.name, CAMPAIGNS.status, count(ADS.id) as ads_count FROM CAMPAIGNS JOIN ADS"
                    + " ON ADS.campaign_id = CAMPAIGNS.id ";
            if(filterByName != null) {
                query = query + "WHERE CAMPAIGNS.name = '" + filterByName + "' ";
            } else if(filterByStatus != null) {
                query = query + "WHERE CAMPAIGNS.status = '" + filterByStatus + "' ";
            }
            query = query + "GROUP BY CAMPAIGNS.id ORDER BY CAMPAIGNS." + sortingParameter +  " DESC LIMIT " + limit;
            PreparedStatement selectStatement = con.prepareStatement(query);
            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                Summary summary = new Summary();
                summary.setId(rs.getInt("id"));
                summary.setName("NADIA11");
                summary.setStatus(Status.fromInteger(rs.getInt("status")));
                summary.setNumberOfAds(rs.getInt("ads_count"));
                summaries.add(summary);
            }
            selectStatement.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return summaries;
    }
    
    public Campaign getCampaignById(int id) {
        Campaign cam = null;
        try {
            String query = "select * from CAMPAIGNS where id = ?";
            Connection con = connector.getConnection();
            PreparedStatement selectStatement = con.prepareStatement(query);
            selectStatement.setInt(1, id);
            ResultSet rs = selectStatement.executeQuery();
            
            while (rs.next()) {
                cam = new Campaign();
                cam.setId(id);
                cam.setName(rs.getString("name"));
                cam.setStatus(Status.fromInteger(rs.getInt("status")));
                cam.setStartDate(rs.getTimestamp("start_date"));
                cam.setEndDate(rs.getTimestamp("end_date"));
            }
            selectStatement.close();
            if(cam != null) {
                cam.setAds(getAdsForCampaign(id));
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cam;
    }
    
    public Ad getAdById(int id) {
        Ad ad = null;
        try {
            String query = "select * from ADS where id = ?";
            Connection con = connector.getConnection();
            PreparedStatement selectStatement = con.prepareStatement(query);
            selectStatement.setInt(1, id);
            ResultSet rs = selectStatement.executeQuery();
            
            while (rs.next()) {
                ad = new Ad();
                ad.setId(id);
                ad.setName(rs.getString("name"));
                ad.setStatus(Status.fromInteger(rs.getInt("status")));
                ad.setAsserUrl(rs.getString("asset_url"));
            }
            selectStatement.close();
            if (ad != null) {
                ad.setPlatforms(getPlatformsForAd(id, con));
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ad;
    }
    
    public List<Ad> getAdsForCampaign(int id) {
        List<Ad> ads = new ArrayList<>();
        try {
            String query = "select * from ADS where campaign_id = ?";
            Connection con = connector.getConnection();
            PreparedStatement selectStatement = con.prepareStatement(query);
            selectStatement.setInt(1, id);
            ResultSet rs = selectStatement.executeQuery();
            
            while (rs.next()) {
                Ad ad = new Ad();
                ad.setId(rs.getInt("id"));
                ad.setName(rs.getString("name"));
                ad.setStatus(Status.fromInteger(rs.getInt("status")));
                ad.setAsserUrl(rs.getString("asset_url"));
                ad.setPlatforms(getPlatformsForAd(ad.getId(), con));
                ads.add(ad);
            }
            selectStatement.close();            
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ads;
    }
    
    private List<Platform> getPlatformsForAd(int adId, Connection con) throws SQLException {
        List<Platform> platforms = new ArrayList<>();
        String query = "select * from PLATFORM where ad_id = ?";
        PreparedStatement selectStatement = con.prepareStatement(query);
        selectStatement.setInt(1, adId);
        ResultSet rs = selectStatement.executeQuery();
        
        while (rs.next()) {
            platforms.add(Platform.fromInteger(rs.getInt("name")));
        }
        selectStatement.close();
        return platforms;
    }
    
    public int updateCampaign(Campaign cam) {
        int result = 0;
        try {            
            Connection con = connector.getConnection();
            String query = "UPDATE CAMPAIGNS SET name = ?, status = ?, start_date = ?, end_date = ? where id = ?";
            
            PreparedStatement updateStatement = null;
            updateStatement = con.prepareStatement(query);
            updateStatement.setString(1, cam.getName());
            updateStatement.setInt(2, cam.getStatus().getValue());
            updateStatement.setTimestamp(3, cam.getStartDate());
            updateStatement.setTimestamp(4, cam.getEndDate());
            updateStatement.setInt(5, cam.getId());
            
            result = updateStatement.executeUpdate();
            updateStatement.close();
            updateAds(cam.getId(), cam.getAds());
            con.commit();
            con.close();            
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int updateAd(Ad ad, int campaignId) {
        int result = 0;
        try {            
            Connection con = connector.getConnection();
            String query = "UPDATE ADS SET name = ?, status = ?, asset_url = ?, campaign_id = ? where id = ?";
            
            PreparedStatement updateStatement = null;
            updateStatement = con.prepareStatement(query);
            updateStatement.setString(1, ad.getName());
            updateStatement.setInt(2, ad.getStatus().getValue());
            updateStatement.setString(3, ad.getAsserUrl());
            updateStatement.setInt(4, campaignId);
            updateStatement.setInt(5, ad.getId());
            
            result = updateStatement.executeUpdate();
            updateStatement.close();
            dropPlatformsForSpecifiedAd(ad.getId(), con);
            insertPlatforms(ad.getId(), ad.getPlatforms(), con);
            con.commit();
            con.close();            
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public int insertCampaign(Campaign cam) {
        int id = 0;
        try {            
            String[] columnNames = new String[] { "id" };
            Connection con = connector.getConnection();
            String query = "INSERT INTO CAMPAIGNS" + "(name, status, start_date, end_date) values" + "(?,?,?,?)";
            
            PreparedStatement insertStatement = null;
            insertStatement = con.prepareStatement(query, columnNames);
            insertStatement.setString(1, cam.getName());
            insertStatement.setInt(2, cam.getStatus().getValue());
            insertStatement.setTimestamp(3, cam.getStartDate());
            insertStatement.setTimestamp(4, cam.getEndDate());
            if (insertStatement.executeUpdate() > 0) {
                java.sql.ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if ( generatedKeys.next() ) {
                    id = generatedKeys.getInt(1);
                }
            }
            insertStatement.close();
            insertAds(id, cam.getAds());
            con.commit();
            con.close();            
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    public int insertAd(Ad ad, int campaignId) {
        int id = 0;
        try {            
            String[] columnNames = new String[] { "id" };
            Connection con = connector.getConnection();
            String query = "INSERT INTO ADS" + "(name, status, asset_url, campaign_id) values" + "(?,?,?,?)";
            
            PreparedStatement insertStatement = null;
            insertStatement = con.prepareStatement(query, columnNames);
            insertStatement.setString(1, ad.getName());
            insertStatement.setInt(2, ad.getStatus().getValue());
            insertStatement.setString(3, ad.getAsserUrl());
            insertStatement.setInt(4, campaignId);
            if (insertStatement.executeUpdate() > 0) {
                java.sql.ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if ( generatedKeys.next() ) {
                    id = generatedKeys.getInt(1);
                }
            }
            insertStatement.close();
            insertPlatforms(id, ad.getPlatforms(), con);
            con.commit();
            con.close();            
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    private void insertAds(int id, List<Ad> ads) {
        ads.forEach((Ad ad) -> insertAd(ad, id));
    }
    
    private void deleteAds(List<Ad> ads) {
        ads.forEach((Ad ad) -> deleteAd(ad.getId()));
    }
    
    private void updateAds(int id, List<Ad> ads) {
        ads.forEach((Ad ad) -> updateAd(ad, id));
    }
    
    private void insertPlatforms(int ad_id, List<Platform> platforms, Connection con) {
        platforms.forEach((Platform p) -> executeInsertPlatformStatement(con, p.getValue(), ad_id));
    }
    
    private void executeInsertPlatformStatement(Connection connection, int id, int ad_id) {
        try {
            String query = "INSERT INTO PLATFORM" + "(name, ad_id) values" + "(?,?)";
            PreparedStatement insertStatement = connection.prepareStatement(query);
            insertStatement.setInt(1, id);
            insertStatement.setInt(2, ad_id);
            insertStatement.executeUpdate();
            insertStatement.close();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dropPlatformsForSpecifiedAd(int adId, Connection connection) {
        try {
            String query = "DELETE FROM PLATFORM WHERE ad_id = ?";
            PreparedStatement insertStatement = connection.prepareStatement(query);
            insertStatement.setInt(1, adId);
            insertStatement.executeUpdate();
            insertStatement.close();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteAd(int id) {
        try {
            Connection con = connector.getConnection();
            dropPlatformsForSpecifiedAd(id, con);
            String query = "DELETE FROM ADS WHERE id = ?";
            PreparedStatement insertStatement = con.prepareStatement(query);
            insertStatement.setInt(1, id);
            insertStatement.executeUpdate();
            insertStatement.close();
            con.commit();            
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteCampaign(int id) {
        Campaign cam = this.getCampaignById(id);
        this.deleteAds(cam.getAds());
        try {
            Connection con = connector.getConnection();
            String query = "DELETE FROM CAMPAIGNS WHERE id = ?";
            PreparedStatement insertStatement = con.prepareStatement(query);
            insertStatement.setInt(1, id);
            insertStatement.executeUpdate();
            insertStatement.close();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void selectAllPlatforms() {
        try {
            Connection con = connector.getConnection();
            String query = "SELECT * FROM PLATFORM";
            PreparedStatement selectStatement = con.prepareStatement(query);
            ResultSet rs = selectStatement.executeQuery();
            System.out.println("PLATFORMS: ");
            while (rs.next()) {
                System.out.println("Id " + rs.getInt("name") + " AD REF " + rs.getInt("ad_id"));
            }
            selectStatement.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CampaignsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
