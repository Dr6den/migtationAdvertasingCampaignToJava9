package com.campaigns.domain.service;

import com.campaigns.dao.CampaignsDao;
import com.campaigns.domain.Ad;
import com.campaigns.domain.Campaign;
import com.campaigns.domain.Summary;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author andrew
 */
public class CampaignsService {
    private CampaignsDao dao;
    
    @Inject
    public CampaignsService(CampaignsDao dao) {
        this.dao = dao;
    }
    
    public Campaign getCampaign(int id) {
        return dao.getCampaignById(id);
    }
    
    public Ad getAd(int id) {
        return dao.getAdById(id);
    }
    
    public List<Summary> getSummary(int numberOfItems, String filterByName, String filterByStatus, String sortingParameter) {
        return dao.getSummary(numberOfItems, filterByName, filterByStatus, sortingParameter);
    }
    
    public void deleteCampaign(int id) {
        dao.deleteCampaign(id);
    }
    
    public void deleteAd(int id) {
        dao.deleteAd(id);
    }
    
    public int updateCampaign(Campaign cam) {
        return dao.updateCampaign(cam);
    }
    
    public int updateAd(Ad ad, int campaignId) {
        return dao.updateAd(ad, campaignId);
    }
    
    public int createCampaign(Campaign cam) {
        return dao.insertCampaign(cam);
    }
    
    public int createAd(Ad ad, int campaignId) {
        return dao.insertAd(ad, campaignId);
    }
}
