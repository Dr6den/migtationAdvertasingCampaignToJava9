package com.campaigns.webservices.config;

import com.campaigns.dao.CampaignsDao;
import com.campaigns.domain.service.CampaignsService;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import com.campaigns.webservices.endpoint.CampaignsEndpoint;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import javax.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@ApplicationPath("/resource")
public class ApplicationConfig extends ResourceConfig
{

    public ApplicationConfig() {
        register(JacksonJsonProvider.class);
        registerClasses(CampaignsEndpoint.class, CampaignsService.class, CampaignsDao.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindAsContract(CampaignsService.class);
            }
        });
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindAsContract(CampaignsDao.class);
            }
        });
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindAsContract(DatabaseInitializer.class);
            }
        });
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(H2Connector.class).to(IJdbcConnector.class).in(Singleton.class);
            }
        });

    }

}
