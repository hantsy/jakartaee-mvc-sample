package com.example.it;

import com.example.Bootstrap;
import com.example.config.MvcConfig;
import com.example.domain.Task;
import com.example.web.TaskController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author hantsy
 */
@RunWith(Arquillian.class)
public class HomeScreenHtmlUnitTest {

    private static final Logger LOGGER = Logger.getLogger(HomeScreenHtmlUnitTest.class.getName());

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        File[] extraJars = Maven.resolver().loadPomFromFile("pom.xml")
                .addDependency(
                        MavenDependencies
                                .createDependency("org.eclipse.krazo:krazo-jersey:1.0.0", ScopeType.RUNTIME, false)
                )
                //                .resolve(
                //                        "org.eclipse.krazo:krazo-core:1.0.0",
                //                        "jakarta.mvc.javax.mvc-api:1.0.0"
                //                )
                //                .withTransitivity()
                .importRuntimeDependencies().resolve().withTransitivity()
                .asFile();
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addAsLibraries(extraJars)
                .addPackage(Bootstrap.class.getPackage())
                .addPackage(Task.class.getPackage())
                .addPackage(MvcConfig.class.getPackage())
                .addPackage(TaskController.class.getPackage())
                //Add JPA persistence configuration.
                //WARN: In a war archive, persistence.xml should be put into /WEB-INF/classes/META-INF/, not /META-INF
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsResource("messages.properties")
                .addAsResource("messages_zh_CN.properties")
                // Enable CDI
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // add template resources.
                .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                        .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                        "/", Filters.include(".*\\.(xhtml|css|xml)$")
                );

        LOGGER.log(Level.INFO, "deployment unit:{0}", war.toString(true));
        return war;
    }

    @ArquillianResource
    private URL deploymentUrl;

    private WebClient webClient;

    @Before
    public void setUp() {
        webClient = new WebClient();
        webClient.getOptions()
                .setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions()
                .setRedirectEnabled(true);
    }

    @Test
    public void testHomePageElements() throws IOException {
        final String url = deploymentUrl.toExternalForm();
        LOGGER.log(Level.INFO, "deploymentUrl: {0}", url);
        final HtmlPage page = webClient.getPage(url + "mvc/tasks");

        DomElement todoTasksNode = page.getElementById("todotasks");
        List<HtmlElement> taskList = todoTasksNode.getElementsByTagName("li");
        assertTrue(taskList.size() == 2);

        if (!taskList.isEmpty()) {
            HtmlElement firstTasNode = taskList.get(0);

            List<HtmlElement> buttonNodes = firstTasNode.getElementsByTagName("button");
            assertTrue(buttonNodes.size() == 1);

            HtmlButton startButton = (HtmlButton) buttonNodes.get(0);
            WebResponse res = startButton.click().getWebResponse();

            if (res.getStatusCode() == 200) {
                final HtmlPage page2 = webClient.getPage(url + "mvc/tasks");
                assertTrue(page2.getElementById("todotasks").getElementsByTagName("li").size() == 1);
                assertTrue(page2.getElementById("doingtasks").getElementsByTagName("li").size() == 1);
            }
        }

    }

}
