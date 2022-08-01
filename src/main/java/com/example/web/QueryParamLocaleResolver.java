package com.example.web;
// https://www.mvc-spec.org/learn/cookbook/custom_localeresolver_en.html

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mvc.locale.LocaleResolver;
import jakarta.mvc.locale.LocaleResolverContext;
import jakarta.ws.rs.core.UriInfo;

/**
 * Resolver to get the {@link Locale} to use from the requests query param <i>lang</i>.
 *
 * In case there is no request param with this name, the {@link Locale} will be resolved by a higher prioritised
 * implementation.
 *
 * Example usage:
 * <pre>
 * {@code
 * # Use default locale
 * curl -X GET <your-url>
 *
 * # Set german locale by query param
 * curl -X GET <your-url>?lang=de-DE
 * }
 * </pre>
 *
 * @author Tobias Erdle
 */
@Priority(1)
@ApplicationScoped
public class QueryParamLocaleResolver implements LocaleResolver {
    
    @Inject
    Logger log;
    
    @Override
    public Locale resolveLocale(final LocaleResolverContext context) {
        final String queryLang = context.getUriInfo()
                .getQueryParameters()
                .getFirst("lang");
        log.log(Level.INFO, "QueryParamLocaleResolver::resolveLocale:lang:{0}", queryLang);
        return queryLang != null ? Locale.forLanguageTag(queryLang) : null;
    }
}
