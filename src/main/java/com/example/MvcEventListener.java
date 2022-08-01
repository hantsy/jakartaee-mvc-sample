package com.example;

import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.mvc.event.AfterControllerEvent;
import jakarta.mvc.event.AfterProcessViewEvent;
import jakarta.mvc.event.BeforeControllerEvent;
import jakarta.mvc.event.BeforeProcessViewEvent;
import jakarta.mvc.event.ControllerRedirectEvent;

@ApplicationScoped
public class MvcEventListener {

    @Inject
    Logger LOGGER;

    private void onControllerMatched(@Observes BeforeControllerEvent event) {
        LOGGER.info(() -> "Controller matched for " + event.getUriInfo().getRequestUri());
    }

    private void onViewEngineSelected(@Observes BeforeProcessViewEvent event) {
        LOGGER.info(() -> "View engine: " + event.getEngine());
    }

    private void onAfterControllerEvent(@Observes AfterControllerEvent event) {
        LOGGER.info(() -> "AfterControllerEvent:: " + event.getResourceInfo());
    }

    private void onAfterProcessViewEvent(@Observes AfterProcessViewEvent event) {
        LOGGER.info(() -> "AfterProcessViewEvent:: " + event);
    }
    
     private void onControllerRedirectEvent(@Observes ControllerRedirectEvent event) {
        LOGGER.info(() -> "ControllerRedirectEvent:: " + event);
    }

    @PostConstruct
    private void init() {
        LOGGER.config(() -> this.getClass().getSimpleName() + " created");
    }
}
