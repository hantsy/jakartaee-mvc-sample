#  JakartaEE MVC Sample 

[![build](https://github.com/hantsy/jakartaee-mvc-sample/actions/workflows/build.yml/badge.svg)](https://github.com/hantsy/jakartaee-mvc-sample/actions/workflows/build.yml)

As an alternative to Jakarta Faces, which is used to build web UIs with components, the [Jakarta MVC](https://www.mvc-spec.org/) spec is similar to traditional MVC frameworks, such as Apache Struts and Spring MVC, and provides the capability to build action-based web applications.

The repository has already been upgraded to Jakarta EE 10.

* The Jakarta EE 8-based code was archived and also tagged with [v1.0 tag](https://github.com/hantsy/jakartaee-mvc-sample/releases/tag/v1.0).

[Jakarta MVC](https://www.mvc-spec.org/) is based on the existing  JAX-RS specs, reuses the JAX-RS APIs, and adds additional APIs for specific *action* features.

[Eclipse Krazo](https://projects.eclipse.org/projects/ee4j.krazo) is the only implementation of Jakarta MVC, which supports the following Jakarta REST implementors.

* Jersey (Glassfish/Payara)
* Resteasy( Wildfly)
* <del>Apache CXF</del> MVC 2.0 remove CXF support.

![home](./home.png)

![task list](./tasks.png)

## Docs

* [Building a web application with Jakarta MVC and Eclipse Krazo(Jakarta EE 8)](./docs/guide.md)

## Build

1. Clone a copy of the source code.

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
