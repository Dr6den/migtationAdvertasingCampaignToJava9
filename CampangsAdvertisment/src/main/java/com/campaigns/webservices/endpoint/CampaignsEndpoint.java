package com.campaigns.webservices.endpoint;

import com.campaigns.domain.Ad;
import com.campaigns.domain.Campaign;
import com.campaigns.domain.Summary;
import com.campaigns.domain.service.CampaignsService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class CampaignsEndpoint
{
    private CampaignsService service;
    
    @Inject
    public CampaignsEndpoint(CampaignsService service) {
       this.service = service; 
    }
    
    @GET
    @Path("/getSummary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSummary(@QueryParam("maxItems") Integer maxItems, @QueryParam("filterByName") String filterByName,
            @QueryParam("filterByStatus") String filterByStatus, @QueryParam("sortingParameter") String sortingParameter) {
        if(maxItems == null) {
            maxItems = 3;
        }
        if(sortingParameter == null) {
            sortingParameter = "id";
        } else if(sortingParameter.equalsIgnoreCase("adsCount")){
            sortingParameter = "count(ADS.id)";
        } else if(!sortingParameter.equalsIgnoreCase("status") && !sortingParameter.equalsIgnoreCase("name")) {
            sortingParameter = "id";
        }
        if(filterByName != null) {
            if(!filterByName.matches("[a-zA-Z]+")) {
                return Response.ok().status(406).entity("Please provide correct filter name").build();
            }
        }
        if(filterByStatus != null) {
            if(!filterByStatus.matches("[1-3]+")) {
                return Response.ok().status(406).entity("Please provide correct filter status").build();
            }
        }
        List<Summary> summaries = service.getSummary(maxItems, filterByName, filterByStatus, sortingParameter);
        return Response.ok().status(200).entity(summaries.toArray(new Summary[summaries.size()])).build();
    }
    
    @GET
    @Path("/getAd/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAd(@PathParam("id") Integer id) {
        String strId = "" + id;
        if (strId.isEmpty() || !strId.matches("\\d+")) {
            return Response.ok().status(406).entity("Please consume id of the ad as number").build();
        } 
        return Response.ok().status(200).entity(service.getAd(id)).build();
    }
    
    @GET
    @Path("/getCampaign/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCampaign(@PathParam("id") Integer id) {
        String strId = "" + id;
        if (strId.isEmpty() || !strId.matches("\\d+")) {
            return Response.ok().status(406).entity("Please consume id of the campaign as number").build();
        } 
        return Response.ok().status(200).entity(service.getCampaign(id)).build();
    }
    
    @POST
    @Path("/createCampaign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCampaign(Campaign cam) {
        if (cam == null) {
            return Response.ok().status(406).entity("Please provide campaign's id as number").build();
        } 
        return Response.ok().status(200).entity(service.createCampaign(cam)).build();
    }
    
    @PUT
    @Path("/updateCampaign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCampaign(Campaign cam) {
        if (cam == null) {
            return Response.ok().status(406).entity("Please provide campaign's id as number").build();
        } 
        return Response.ok().status(200).entity(service.updateCampaign(cam)).build();
    }
    
    @PUT
    @Path("/updateAd/{campaign_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAd(@PathParam("campaign_id") Integer campaignId, Ad ad) {
        String strId = "" + campaignId;
        if (strId.isEmpty() || !strId.matches("\\d+") || ad == null) {
            return Response.ok().status(406).entity("Please provide ad's campaign id as number").build();
        } 
        return Response.ok().status(200).entity(service.updateAd(ad, campaignId)).build();
    }
    
    @POST
    @Path("/createAd/{campaign_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAd(@PathParam("campaign_id") Integer campaignId, Ad ad) {
        String strId = "" + campaignId;
        if (strId.isEmpty() || !strId.matches("\\d+") || ad == null) {
            return Response.ok().status(406).entity("Please provide ad's campaign id as number").build();
        } 
        return Response.ok().status(200).entity(service.createAd(ad, campaignId)).build();
    }
    
    @DELETE
    @Path("/deleteAd/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAd(@PathParam("id") Integer id) {
        service.deleteAd(id);
        return Response.ok().status(200).entity("ad was deleted").build();
    }
	
    @DELETE
    @Path("/deleteCampaign/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCampaign(@PathParam("id") Integer id) {
        service.deleteCampaign(id);
        return Response.ok().status(200).entity("ad was deleted").build();
    }
}