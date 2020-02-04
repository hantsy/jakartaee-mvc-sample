package com.example.config;

import com.example.web.GreetingController;
import com.example.web.LocaleController;
import com.example.web.TaskController;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.krazo.binding.convert.MvcConverterProvider;
import org.eclipse.krazo.core.ViewResponseFilter;
import org.eclipse.krazo.core.ViewableWriter;
import org.eclipse.krazo.forms.HiddenMethodFilter;
import org.eclipse.krazo.jaxrs.PostMatchingRequestFilter;
import org.eclipse.krazo.jaxrs.PreMatchingRequestFilter;
import org.eclipse.krazo.security.CsrfExceptionMapper;
import org.eclipse.krazo.security.CsrfProtectFilter;
import org.eclipse.krazo.security.CsrfValidateFilter;

@ApplicationPath("mvc")
public class MvcConfig extends Application {

//    @Override
//    public Set<Class<?>> getClasses() {
//        Set<Class<?>> classes = new HashSet<>();
//        classes.add(TaskController.class);
//        classes.add(GreetingController.class);
//        classes.add(LocaleController.class);
//        classes.add(PrimitiveConverterProvider.class);
//        classes.add(PostNotFoundExceptionMapper.class);
//        
//        // add krazo feature.
//        classes.add(ViewResponseFilter.class);
//        classes.add(ViewableWriter.class);
//        classes.add(CsrfValidateFilter.class);
//        classes.add(CsrfProtectFilter.class);
//        classes.add(CsrfExceptionMapper.class);
//        classes.add(PreMatchingRequestFilter.class);
//        classes.add(PostMatchingRequestFilter.class);
//        classes.add(MvcConverterProvider.class);
//        classes.add(HiddenMethodFilter.class);
//
//        return classes;
//    }

    /**
     *
     *
     * ViewEngine.VIEW_FOLDER
     *
     * Csrf.CSRF_PROTECTION
     *
     * Csrf.CSRF_HEADER_NAME
     *
     *
     *
     * @Override public Map<String, Object> getProperties() { final Map<String, Object> map = new HashMap<>();
     * map.put(ViewEngine.VIEW_FOLDER, "/jsp/"); return map; }
     */
}
