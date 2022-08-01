/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config;

import com.example.domain.TaskNotFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.inject.Inject;
import jakarta.mvc.Models;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author hantsy
 */
@Provider
public class PostNotFoundExceptionMapper implements ExceptionMapper<TaskNotFoundException> {

    @Inject Logger log;
    //private static Logger log = Logger.getLogger(PostNotFoundExceptionMapper.class.getName());

    @Inject
    Models models;

    @Override
    public Response toResponse(TaskNotFoundException exception) {
        log.log(Level.INFO, "handling exception : PostNotFoundException");
        models.put("error", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity("error.xhtml").build();
    }

}
