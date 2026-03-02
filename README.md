#  Jakarta EE MVC Sample 



[![build](https://github.com/hantsy/jakartaee-mvc-sample/actions/workflows/build.yml/badge.svg)](https://github.com/hantsy/jakartaee-mvc-sample/actions/workflows/build.yml)

As an alternative of Jakarta Faces which is used to build web UI with components, [Jakarta MVC](https://www.mvc-spec.org/) spec is similar with the transactional MVC framework, such as Apache Struts,  Spring MVC, etc. which provides capability for building action-based web applications.

The repository has already been upgraded to Jakarta EE 10.

* For the Jakarta EE 8 based codes was archived and also tagged with [v1.0 tag](https://github.com/hantsy/jakartaee-mvc-sample/releases/tag/v1.0).

[Jakarta MVC](https://www.mvc-spec.org/) is based on the existing  JAX-RS specs, it reuses the JAX-RS APIs, and add some additional APIs to the specific *action* features.

[Eclipse Krazo](https://projects.eclipse.org/projects/ee4j.krazo) is the only implementation of Jakarta MVC, it supports the following Jakarta REST implementors.

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

2. Run on Glassfish.

   ```bash
   mvn clean package cargo:run -pglassfish
   ```
   
3. Run test against GlassFish managed adapter.

   ```bash
   mvn clean verify -Parg-glassfish-managed
   ```

> [!WARNING]
> To reduce maintenance costs, I have removed the configuration for running the applications on WildFly, OpenLiberty, Payara, etc. If you need to run it on these application servers, please refer to [jakartaee9-starter-boilerplate](https://github.com/hantsy/jakartaee9-starter-boilerplate) or  [jakartaee10-starter-boilerplate](https://github.com/hantsy/jakartaee10-starter-boilerplate) and add the configuration yourself.  

## Reference 

* [MVC spec](https://www.mvc-spec.org/)
* [Eclipse Krazo](https://projects.eclipse.org/projects/ee4j.krazo)
