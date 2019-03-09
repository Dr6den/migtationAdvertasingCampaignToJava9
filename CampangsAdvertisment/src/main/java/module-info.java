/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module AdvertizingCampaigns {
    requires javax.servlet.api;
    requires jersey.container.servlet;
    requires jersey.container.servlet.core;
    requires javax.inject;
    requires jersey.common;
    requires javax.annotation.api;
    requires jersey.guava;
    requires osgi.resource.locator;
    requires jersey.server;
    requires jersey.client;
    requires jersey.media.jaxb;
    //requires validation.api;
    requires javax.ws.rs.api;
    requires jackson.jaxrs.json.provider;
    requires jackson.jaxrs.base;
    requires jackson.core;
    requires jackson.databind;
    requires jackson.annotations;
    requires jackson.module.jaxb.annotations;
    //requires hk;
    requires hk2.utils;
    requires hk2.api;
    requires aopalliance.repackaged;
    requires config.types;
    requires hk2.core;
    requires hk2.config;
    //requires tiger.types;
    requires bean.validator;
    requires hk2.locator;
    requires javassist;
    requires hk2.runlevel;
    //requires class.model;
    requires asm.all.repackaged;
    requires com.h2database;
    requires java.sql;
    //requires advertizing.campaigns;
}
