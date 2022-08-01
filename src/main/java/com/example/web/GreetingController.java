package com.example.web;

import java.util.logging.Logger;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.UriRef;
import jakarta.mvc.binding.BindingResult;
import jakarta.mvc.binding.MvcBinding;
import jakarta.mvc.binding.ParamError;
import jakarta.mvc.security.CsrfProtected;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 *
 * @author hantsy
 */
@Path("greeting")
@Controller
@RequestScoped
public class GreetingController {

    @Inject
    BindingResult bindingResult;

    @Inject
    Models models;

    @Inject
    AlertMessage flashMessage;

    @Inject
    Logger log;

    @GET
    public String get() {
        return "greeting.xhtml";
    }

    @POST
    @UriRef("greeting-post")
    public String post(
            @FormParam("greeting")
            @MvcBinding
            @NotBlank String greeting) {
        if (bindingResult.isFailed()) {
            AlertMessage alert = AlertMessage.danger("Validation voilations!");
            bindingResult.getAllErrors()
                    .stream()
                    .forEach((ParamError t) -> {
                        alert.addError(t.getParamName(), "", t.getMessage());
                    });
            models.put("errors", alert);
            log.info("mvc binding failed.");
            return "greeting.xhtml";
        }

        log.info("redirect to greeting page.");
        flashMessage.notify(AlertMessage.Type.success, "Message:" + greeting);
        return "redirect:greeting";
    }

}
