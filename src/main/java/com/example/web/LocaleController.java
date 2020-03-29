package com.example.web;

import java.util.Locale;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.MvcContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author hantsy
 */
@Path("locale")
@Controller
@RequestScoped
public class LocaleController {

    @Inject
    MvcContext mvc;

    @Inject
    Models models;

    @Inject
    Logger log;

    @GET
    public String get() {
        Locale locale = mvc.getLocale();
        models.put("locale", locale);
        return "locale.xhtml";
    }

}
