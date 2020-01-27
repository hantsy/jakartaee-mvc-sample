package com.example.config;

import com.example.web.TaskController;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("mvc")
public class MvcConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes=new HashSet<>();
        classes.add(TaskController.class);
        classes.add(PrimitiveConverterProvider.class);
        classes.add(PostNotFoundExceptionMapper.class);
        
        return classes;
    }
}
