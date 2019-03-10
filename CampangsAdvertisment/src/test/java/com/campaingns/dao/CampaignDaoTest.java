package com.campaingns.dao;

import com.campaigns.dao.CampaignsDao;
import com.campaigns.domain.Ad;
import com.campaigns.domain.Campaign;
import com.campaigns.domain.Platform;
import com.campaigns.domain.Status;
import com.campaigns.domain.Summary;
import com.campaigns.webservices.config.DatabaseInitializer;
import com.campaigns.webservices.config.H2Connector;
import com.campaigns.webservices.config.IJdbcConnector;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 *
 * @author andrew
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CampaignDaoTest {
    private CampaignsDao campaignsDao;
    private IJdbcConnector con;
    private DatabaseInitializer initializer;
    
    public CampaignDaoTest() {
        con = new H2Connector();
        initializer = new DatabaseInitializer();
        campaignsDao = new CampaignsDao(con, initializer);
    }
    
    @Test
    public void createAdTest() {
        List<Platform> platforms = new ArrayList<>();
        platforms.add(Platform.WEB);
        platforms.add(Platform.ANDROID);
        Ad ad = new Ad(0, "some ad", platforms, "Asset url", Status.ACTIVE);
        
        int createAdId = campaignsDao.insertAd(ad, 1);
        Ad persistedAd = campaignsDao.getAdById(createAdId);
        List<Platform> persistedPlatforms = persistedAd.getPlatforms();
        
        assertNotNull(persistedAd);
        assertEquals(persistedAd.getAsserUrl(), "Asset url");
        assertEquals(persistedAd.getName(), "some ad");
        assertEquals(persistedAd.getStatus(), Status.ACTIVE);
        assertNotNull(persistedPlatforms);
        assertEquals(persistedPlatforms.size(), platforms.size());
        assertTrue(persistedPlatforms.containsAll(platforms));
        
        campaignsDao.deleteAd(createAdId);
        Ad cad = campaignsDao.getAdById(createAdId);
        assertNull(cad);
    }
    
    @Test
    public void updateAdTest() {
        List<Platform> platforms = new ArrayList<>();
        platforms.add(Platform.WEB);
        platforms.add(Platform.ANDROID);
        List<Platform> updatePlatforms = new ArrayList<>();
        updatePlatforms.add(Platform.IOS);
        Ad ad = new Ad(0, "some ad", platforms, "Asset url", Status.ACTIVE);        
        int updateAdId = campaignsDao.insertAd(ad, 1);
        
        Ad adUpdate = new Ad(updateAdId, "some update ad", updatePlatforms, "Asset update url", Status.PAUSED); 
        int updatedAdId = campaignsDao.updateAd(adUpdate, 1);
        Ad persistedAd = campaignsDao.getAdById(updateAdId);
        List<Platform> persistedPlatforms = persistedAd.getPlatforms();
        
        assertTrue(updatedAdId > 0);
        assertNotNull(persistedAd);
        assertEquals(persistedAd.getAsserUrl(), "Asset update url");
        assertEquals(persistedAd.getName(), "some update ad");
        assertEquals(persistedAd.getStatus(), Status.PAUSED);
        assertNotNull(persistedPlatforms);
        assertEquals(persistedPlatforms.size(), updatePlatforms.size());
        assertTrue(persistedPlatforms.containsAll(updatePlatforms));
        
        campaignsDao.deleteAd(updateAdId);
        Ad uad = campaignsDao.getAdById(updateAdId);
        assertNull(uad);
    }
    
    @Test
    public void createCampaignTest() {
        List<Ad> ads = new ArrayList<>();
        List<Platform> platforms = new ArrayList<>();
        platforms.add(Platform.WEB);
        platforms.add(Platform.ANDROID);
        Ad ad = new Ad(0, "some ad", platforms, "Asset url", Status.ACTIVE);
        ads.add(ad);
        Timestamp startDate = new Timestamp(99999999);
        Timestamp endDate = new Timestamp(199999999);
        Campaign campaign = new Campaign("some campaign", Status.FINISHED, startDate, endDate, ads);
        
        int createCampaignId = campaignsDao.insertCampaign(campaign);
        Campaign persistedCampaign = campaignsDao.getCampaignById(createCampaignId);
        assertNotNull(persistedCampaign);
        assertEquals(persistedCampaign.getName(), "some campaign");
        assertEquals(persistedCampaign.getStatus(), Status.FINISHED);
        assertEquals(persistedCampaign.getStartDate(), startDate);
        assertEquals(persistedCampaign.getEndDate(), endDate);
        List<Ad> perAds = persistedCampaign.getAds();
        assertNotNull(perAds);
        assertTrue(perAds.size() == 1);
        List<Platform> perPlatforms = perAds.get(0).getPlatforms();
        assertNotNull(perPlatforms);
        assertTrue(perPlatforms.size() == 2);
        
        campaignsDao.deleteCampaign(createCampaignId);
        Campaign ccam = campaignsDao.getCampaignById(createCampaignId);
        assertNull(ccam);
    }
    
    @Test
    public void updateCampaignTest() {
        List<Ad> ads = new ArrayList<>();
        List<Platform> platforms = new ArrayList<>();
        platforms.add(Platform.WEB);
        platforms.add(Platform.ANDROID);
        Ad ad = new Ad(0, "some ad", platforms, "Asset url", Status.ACTIVE);
        ads.add(ad);
        Timestamp startDate = new Timestamp(99999999);
        Timestamp endDate = new Timestamp(199999999);
        Timestamp uStartDate = new Timestamp(899999999);
        Timestamp uEndDate = new Timestamp(1099999999);
        Campaign campaign = new Campaign("some campaign", Status.FINISHED, startDate, endDate, ads);
        Campaign campaignUpdate = new Campaign("some other campaign", Status.ACTIVE, uStartDate, uEndDate, ads);
        
        int updateCampaignId = campaignsDao.insertCampaign(campaign);
        campaignUpdate.setId(updateCampaignId);
        int upResult = campaignsDao.updateCampaign(campaignUpdate);
        assertTrue(upResult > 0);
        
        Campaign persistedCampaign = campaignsDao.getCampaignById(updateCampaignId);
        assertNotNull(persistedCampaign);
        assertEquals(persistedCampaign.getName(), "some other campaign");
        assertEquals(persistedCampaign.getStatus(), Status.ACTIVE);
        assertEquals(persistedCampaign.getStartDate(), uStartDate);
        assertEquals(persistedCampaign.getEndDate(), uEndDate);
        List<Ad> perAds = persistedCampaign.getAds();
        assertNotNull(perAds);
        assertTrue(perAds.size() == 1);
        List<Platform> perPlatforms = perAds.get(0).getPlatforms();
        assertNotNull(perPlatforms);
        assertTrue(perPlatforms.size() == 2);
        
        campaignsDao.deleteCampaign(updateCampaignId);
        Campaign ucam = campaignsDao.getCampaignById(updateCampaignId);
        assertNull(ucam);
    }
    
    @Test
    public void getSummaryTest() {
        List<Summary> summarySortedById = campaignsDao.getSummary(3, null, null, "id");
        assertNotNull(summarySortedById);
        assertTrue(summarySortedById.size() == 3);
        
        List<Summary> summaryFilteredByName = campaignsDao.getSummary(6, "Inna", null, "id");
        assertNotNull(summaryFilteredByName);
        assertTrue(summaryFilteredByName.size() == 1);
        
        List<Summary> summaryFilteredByStatus = campaignsDao.getSummary(6, null, "1", "id");
        assertNotNull(summaryFilteredByStatus);
        assertTrue(summaryFilteredByStatus.size() > 0);
        
        List<Summary> summarySortedByName = campaignsDao.getSummary(2, null, null, "name");
        assertNotNull(summarySortedByName);
        assertTrue(summarySortedByName.size() == 2);
        
        List<Summary> summarySortedByStatus = campaignsDao.getSummary(2, null, null, "status");
        assertNotNull(summarySortedByStatus);
        assertTrue(summarySortedByStatus.size() == 2);
        
        List<Summary> summarySortedByCount = campaignsDao.getSummary(2, null, null, "count(ADS.id)");
        assertNotNull(summarySortedByCount);
        assertTrue(summarySortedByCount.size() == 2);
        
        assertFalse(summarySortedByStatus.containsAll(summarySortedById));
        assertFalse(summarySortedByStatus.containsAll(summarySortedByCount));
        assertFalse(summarySortedByName.containsAll(summarySortedByCount));
    }
}
