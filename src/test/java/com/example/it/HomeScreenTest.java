package com.example.it;

import com.example.Bootstrap;
import com.example.config.MvcConfig;
import com.example.domain.Task;
import com.example.web.TaskController;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit5.ArquillianExtension;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hantsy
 */
@ExtendWith(ArquillianExtension.class)
public class HomeScreenTest {

    private static final Logger LOGGER = Logger.getLogger(HomeScreenTest.class.getName());

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        // Glassfish 7.0 includes built-in MVC
       // File[] extraJars = Maven.resolver().loadPomFromFile("pom.xml")

                //.addDependency(
                //        MavenDependencies
                //                .createDependency("org.eclipse.krazo:krazo-jersey:3.0.1", ScopeType.RUNTIME, false)
                //)
                //                .resolve(
                //                        "org.eclipse.krazo:krazo-core:1.0.0",
                //                        "jakarta.mvc.javax.mvc-api:1.0.0"
                //                )
                //                .withTransitivity()
                //.importRuntimeDependencies().resolve().withTransitivity()
                ////.asFile();
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                //.addAsLibraries(extraJars)
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
                        "/", Filters.include(".*\\.(html|xhtml|css|xml)$")
                );

        LOGGER.log(Level.INFO, "deployment unit:{0}", war.toString(true));
        return war;
    }

    @ArquillianResource
    private URL deploymentUrl;

    @Drone
    private WebDriver browser;

    @Page
    HomePage homePage;

    @Test
    public void testHomePage(@InitialPage IndexPage idxPage) {
        idxPage.clickStartButton();
        homePage.assertOnHomePage();
        homePage.assertTodoTasksSize(2);
    }
}
