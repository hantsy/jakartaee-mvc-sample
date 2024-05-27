package com.example.it;

import com.example.Bootstrap;
import com.example.config.MvcConfig;
import com.example.domain.Task;
import com.example.web.TaskController;
import org.htmlunit.WebClient;
import org.htmlunit.htmlunit.WebResponse;
import org.htmlunit.htmlunit.html.DomElement;
import org.htmlunit.htmlunit.html.HtmlButton;
import org.htmlunit.htmlunit.html.HtmlElement;
import org.htmlunit.htmlunit.html.HtmlPage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author hantsy
 */
@ExtendWith(ArquillianExtension.class)
public class HomeScreenHtmlUnitTest {

    private static final Logger LOGGER = Logger.getLogger(HomeScreenHtmlUnitTest.class.getName());

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
//        File[] extraJars = Maven.resolver().loadPomFromFile("pom.xml")
//                .addDependency(
//                        MavenDependencies
//                                .createDependency("org.eclipse.krazo:krazo-jersey:1.0.0", ScopeType.RUNTIME, false)
//                )
//                //                .resolve(
//                //                        "org.eclipse.krazo:krazo-core:1.0.0",
//                //                        "jakarta.mvc.javax.mvc-api:1.0.0"
//                //                )
//                //                .withTransitivity()
//                .importRuntimeDependencies().resolve().withTransitivity()
//                .asFile();
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                // .addAsLibraries(extraJars)
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

    @BeforeEach
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
        assertEquals(2, taskList.size());

        if (!taskList.isEmpty()) {
            HtmlElement firstTasNode = taskList.get(0);

            List<HtmlElement> buttonNodes = firstTasNode.getElementsByTagName("button");
            assertEquals(1, buttonNodes.size());

            HtmlButton startButton = (HtmlButton) buttonNodes.get(0);
            WebResponse res = startButton.click().getWebResponse();

            if (res.getStatusCode() == 200) {
                final HtmlPage page2 = webClient.getPage(url + "mvc/tasks");
                assertEquals(1, page2.getElementById("todotasks").getElementsByTagName("li").size());
                assertEquals(1, page2.getElementById("doingtasks").getElementsByTagName("li").size());
            }
        }

    }

}
