package org.eclipse.krazo.engine;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mvc.engine.ViewEngine;
import jakarta.mvc.engine.ViewEngineContext;
import jakarta.mvc.engine.ViewEngineException;
import jakarta.servlet.ServletException;
import java.io.IOException;

/**
 * Implementation of the JSF Facelets engine. Uses a method in its base class to forward
 * a request back to the servlet container.
 *
 * @author Manfred Riem
 * @author Santiago Pericas-Geertsen
 * @see ViewEngineBase#resolveView(jakarta.mvc.engine.ViewEngineContext)
 */
@Priority(ViewEngine.PRIORITY_BUILTIN)
@ApplicationScoped
public class FaceletsViewEngine extends ServletViewEngine {

    /**
     * Assumes that any view that ends with {@code .xhtml} is a facelet.
     *
     * @param view the name of the view.
     * @return {@code true} if supported or {@code false} if not.
     */
    @Override
    public boolean supports(String view) {
        return view.endsWith(".xhtml");
    }

    /**
     * Forwards request to servlet container.
     *
     * @param context view engine context.
     * @throws ViewEngineException if any error occurs.
     */
    @Override
    public void processView(ViewEngineContext context) throws ViewEngineException {
        try {
            forwardRequest(context, "*.xhtml");
        } catch (ServletException | IOException e) {
            throw new ViewEngineException(e);
        }
    }
}