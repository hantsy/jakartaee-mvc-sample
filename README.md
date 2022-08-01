#  Jakarta EE MVC Sample 



![Compile and build](https://github.com/hantsy/jakartaee-mvc-sample/workflows/build/badge.svg)

> NOTE: The source codes is updated to Jakarta EE 10, and tested on GlassFish 7.0.0.M10 and WildFly Preview 27.0.0.Final. OpenLiberty support will be added in future when it gets complete support of Jakarta EE 10.

> The Jakarta EE 8 based codes was archived and also tagged with [v1.0 tag](https://github.com/hantsy/jakartaee-mvc-sample/releases/tag/v1.0).

As an alternative of Jakarta  Faces which is used to build web UI with components, Jakarta EE MVC spec is similar with the transactional MVC framework, such as Apache Struts,  Spring MVC, etc. which provides capability for building action-based web applications.

In this sample application, we will build a web application with [Jakarta MVC specification](https://www.mvc-spec.org/).  Jakarta MVC is based on the existing  JAX-RS specs, it reuses the JAX-RS APIs, and add some additional APIs   to  the specific *action* features.

[Eclipse Krazo](https://projects.eclipse.org/projects/ee4j.krazo) is the  reference implementation of Jakarta MVC, it supports the following JAXRS platforms.

* Jersey (Glassfish/Payara)
* Resteasy( Wildfly)
* <del>Apache CXF</del> MVC 2.0 remove CXF support.



![home](./home.png)

## Docs

* [Building a web application with Jakarta MVC and Eclipse Krazo(Jakarta EE 8)](./docs/guide.md)

## Build

1. Clone a copy of the source codes.

   ```bash
   git clone https://github.com/hantsy/jakartaee-mvc-sample
   ```

2. Run on Glassfish, Wildfly or Open Liberty.

   ```bash
   mvn clean package cargo:run -pglassfish
   mvn clean wildfly:run -Pwildfly
   mvn clean liberty:create dependency:copy liberty:run -Popenliberty
   ```
   

## Reference 

* [MVC spec](https://www.mvc-spec.org/)
* [Eclipse Krazo](https://projects.eclipse.org/projects/ee4j.krazo)
