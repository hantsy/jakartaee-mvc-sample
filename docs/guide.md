# Building a web application with Jakarta MVC and Eclipse Krazo

The Model-View-Controller (MVC 1.0) Specification (JSR371) was initially proposed as part of Java EE 8,  but finally it missed the Java EE 8 release train. Luckily the development of the MVC spec was still living as a standalone project, more info please go to [mvc-sepc.org](https://www.mvc-spec.org/).

In the Java EE/Jakarta EE system,  JSF was the standard spec to create web applications. The effort of the MVC spec is providing an alternative approach to build web applications using action like concept instead of the existing components/events based philosophy. 

The MVC spec is built on the top of the JAX-RS spec. Currently version 1.0 archived [Final Release](https://www.mvc-spec.org/spec/). [Eclipse Krazo](https://projects.eclipse.org/projects/ee4j.krazo) is the only implementation of this spec till now. Krazo provides adapters for the existing JAX-RS providers and Jakarta EE compatible servers.

* [Jersey](https://github.com/eclipse-ee4j/jersey)([Glassfish](https://projects.eclipse.org/projects/ee4j.glassfish) and [Payara Server](https://www.payara.fish/))
* [RESTEasy](https://resteasy.github.io/)([WildFly](https://wildfly.org/))
* [Apache CXF](https://cxf.apache.org/)([Open Liberty](https://openliberty.io/))

In this post, we will reuse the work of  [Jakarta EE 8 starter](https://github.com/hantsy/jakartaee8-starter) and reimplement the Kanban sample that we have done in [the last post using Jakarta Server Faces](https://medium.com/@hantsy/building-a-jakarta-server-faces-application-cc424f5feb0d).

## Configuring MVC

Add the following dependencies.

```xml
<dependency>
    <groupId>javax.mvc</groupId>
    <artifactId>javax.mvc-api</artifactId>
    <version>1.0.0</version>
</dependency>

<dependency>
    <groupId>org.eclipse.krazo</groupId>
    <artifactId>krazo-core</artifactId>
    <version>${krazo.version}</version>
</dependency>
```

The `javax.mvc-api` includes APIs defined in MVC spec 1.0, and the  `krazo-core` is the core implementation of MVC 1.0.  To use MVC with the underlay JAX-RS providers, you should add extra adapters to your dependencies.

To use MVC with Jersey, such as running the application on Glassfish or Payara Server.

```xml
<dependency>
    <groupId>org.eclipse.krazo</groupId>
    <artifactId>krazo-jersey</artifactId>
    <version>${krazo.version}</version>
</dependency>
```

To use MVC with RESTEasy, such as running the application on WildFly.

```xml
<dependency>
    <groupId>org.eclipse.krazo</groupId>
    <artifactId>krazo-resteasy</artifactId>
    <version>${krazo.version}</version>
</dependency>
```

To use MVC with Apache CXF , such as running the application on TomEE or Open Liberty.

```xml
<dependency>
    <groupId>org.eclipse.krazo</groupId>
    <artifactId>krazo-cxf</artifactId>
    <version>${krazo.version}</version>
</dependency>
```

Like JAX-RS, you need to declare a  JAX-RS `Application` to activate  MVC.

```java
@ApplicationPath("mvc")
public class MvcConfig extends Application {}
```



## Exploring the Model-View-Controller pattern

Like Spring MVC, MVC introduced a new annotation `@Controller` to indicate if a JAX-RS bean is a  MVC controller.

 ```java
@Path("tasks")
@Controller
@RequestScoped
public class TaskController {

    @Inject
    Logger log;

    @Inject
    private Models models;

    @Inject
    private BindingResult validationResult;

    @Inject
    TaskRepository taskRepository;

    @Inject
    AlertMessage flashMessage;

    @GET
    @View("tasks.xhtml")
    public void allTasks() {
        log.log(Level.INFO, "fetching all tasks");

        List<Task> todotasks = taskRepository.findByStatus(Task.Status.TODO);
        List<Task> doingtasks = taskRepository.findByStatus(Task.Status.DOING);
        List<Task> donetasks = taskRepository.findByStatus(Task.Status.DONE);

        log.log(Level.INFO, "got all tasks: todotasks@{0}, doingtasks@{1}, donetasks@{2}", new Object[]{todotasks.size(), doingtasks.size(), donetasks.size()});

        models.put("todotasks", todotasks);
        models.put("doingtasks", doingtasks);
        models.put("donetasks", donetasks);

    }

    @GET
    @Path("{id}")
    public Viewable taskDetails(@PathParam("id") @NotNull Long id) {
        log.log(Level.INFO, "get task by id@{0}", id);
        Task task = taskRepository.findById(id);

        models.put("details", task);
        return new Viewable("details.xhtml");
    }

    @GET
    @Path("new")
    public Viewable add() {
        log.log(Level.INFO, "add new task");
        TaskForm form = new TaskForm();
        models.put("task", form);
        return new Viewable("add.xhtml");
    }

    @POST
    @CsrfProtected
    //@ValidateOnExecution(type = ExecutableType.NONE)
    public Response save(@Valid @BeanParam TaskForm form) {
        log.log(Level.INFO, "saving new task @{0}", form);

        if (validationResult.isFailed()) {
            AlertMessage alert = AlertMessage.danger("Validation voilations!");
            validationResult.getAllErrors()
                    .stream()
                    .forEach((ParamError t) -> {
                        alert.addError(t.getParamName(), "", t.getMessage());
                    });
            models.put("errors", alert);
            models.put("task", form);
            return Response.status(BAD_REQUEST).entity("add.xhtml").build();
        }

        Task task = new Task();
        task.setName(form.getName());
        task.setDescription(form.getDescription());

        taskRepository.save(task);

        flashMessage.notify(Type.success, "Task was created successfully!");

        return Response.ok("redirect:tasks").build();
    }

    @GET
    @Path("{id}/edit")
    public Viewable edit(@PathParam("id") Long id) {
        log.log(Level.INFO, "edit task @{0}", id);

        Task task = taskRepository.findById(id);

        TaskForm form = new TaskForm();
        form.setId(task.getId());
        form.setName(task.getName());
        form.setDescription(task.getDescription());
        models.put("task", form);
        return new Viewable("edit.xhtml");
    }

    @PUT
    @Path("{id}")
    @CsrfProtected
    public Response update(@PathParam(value = "id") Long id, @Valid @BeanParam TaskForm form) {
        log.log(Level.INFO, "updating existed task@id:{0}, form data:{1}", new Object[]{id, form});

        if (validationResult.isFailed()) {
            AlertMessage alert = AlertMessage.danger("Validation voilations!");
            validationResult.getAllErrors()
                    .stream()
                    .forEach((ParamError t) -> {
                        alert.addError(t.getParamName(), "", t.getMessage());
                    });
            models.put("errors", alert);
            models.put("task", form);
            return Response.status(BAD_REQUEST).entity("edit.xhtml").build();
        }

        Task task = taskRepository.findById(id);

        task.setName(form.getName());
        task.setDescription(form.getDescription());

        taskRepository.update(task);

        flashMessage.notify(Type.info, "Task was updated successfully!");

        return Response.ok("redirect:tasks").build();
    }

    @PUT
    @Path("{id}/status")
    //@CsrfProtected
    public Response updateStatus(@PathParam(value = "id") Long id, @NotNull @FormParam(value = "status") String status) {
        log.log(Level.INFO, "updating status of the existed task@id:{0}, status:{1}", new Object[]{id, status});

        Task task = taskRepository.findById(id);

        task.setStatus(Task.Status.valueOf(status));

        taskRepository.update(task);

        flashMessage.notify(Type.info, "Task status was updated successfully!");

        return Response.ok("redirect:tasks").build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        log.log(Level.INFO, "deleting task @{0}", id);
        Task task = taskRepository.findById(id);
        taskRepository.delete(task);

        AlertMessage flashMessage = AlertMessage.danger("Task was deleted!");
        models.put("flashMessage", flashMessage);
        return Response.ok("redirect:tasks").build();
    }

    @PostConstruct
    private void init() {
        log.config(() -> this.getClass().getSimpleName() + " created");
    }
}
 ```

The `@Controller` can be placed on the classes or methods, if it is on the classes, all methods can be used to handling requests from/for views.

The `Models` is a helper bean to organize the models that be put into the view, which is very similar with `ModelMap` in Spring MVC. Any CDI compatible scoped beans can be used as models in MVC.

Like Spring MVC, Eclipse Krazo also provides a [HiddenMethodFilter](https://github.com/eclipse-ee4j/krazo/blob/master/core/src/main/java/org/eclipse/krazo/forms/HiddenMethodFilter.java) to delegate form post handling to `PUT`, `PATCHP`, `DELETE` methods in the controller. Just need to add a hidden field `_method` in the form in the view.

The `AlertMessage` is a `RedirectScope` bean, similar with `Flash` scoped models in other framework which can be alive in the `Post-Redirect-Get` lifecycle. It is very useful to send feedback messages to client.

There are several approaches to resolve a view in the methods of the `@Controller`.
   * Adding a `@View` on the void method.
   * Return a `@Viewable`.
   * Return a `Response.entity` to set the view path.
   * Return a `String`.

MVC will search the views in the `WEB-INF/views` folder at runtime. If you want to change the location of the view folder, override the `getProperties` method in your `Application` class, set the value of `ViewEngine.VIEW_FOLDER`.

```java
@Override
public Map<String, Object> getProperties() { 
    final Map<String, Object> map = new HashMap<>();
    map.put(ViewEngine.VIEW_FOLDER, "/jsp/"); 
    return map; 
}
```

In the background, MVC uses a `ViewEngine` to resolve views and render views. Besides Facelets and JSP, Eclipse Krazo supports a collection of ViewEngines, check the [search result](https://mvnrepository.com/artifact/org.eclipse.krazo.ext) of groupId *org.eclipse.krazo.ext* in the https://www.mvnrepository.org. You need to add additional dependencies to support these view engines. For example, to use mustache in MVC, add the following dependency.

```xml
<!-- https://mvnrepository.com/artifact/org.eclipse.krazo.ext/krazo-mustache -->
<dependency>
    <groupId>org.eclipse.krazo.ext</groupId>
    <artifactId>krazo-mustache</artifactId>
    <version>1.0.0</version>
</dependency>
```

In this sample application, we use Facelets as view engine.  The layout and templates are similar with [the Jakarta Server Faces version we have done in the former post](https://medium.com/@hantsy/building-a-jakarta-server-faces-application-cc424f5feb0d).

Let's have a look at the task home page, aka */src/main/webapp/WEB-INF/views/tasks.xhtml*.

```xml
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
                xmlns:ui="http://xmlns.jcp.org/jsf/facelets"
                xmlns:f="http://xmlns.jcp.org/jsf/core"
                xmlns:h="http://xmlns.jcp.org/jsf/html"
                xmlns:c="http://java.sun.com/jsp/jstl/core"
                template="/WEB-INF/layout/template.xhtml">

    <ui:define name="pageTitle">TASK LIST</ui:define>
    <ui:define name="content">
        <div class="row">
            <div class="col-md-4 col-xs-12">
                <div class="card">
                    <!-- Default panel contents -->
                    <div class="card-header">
                        <i class="fa fa-list-alt" aria-hidden="true"></i>
                        TODO
                    </div>
                    <div class="card-body">
                        <p class="card-text">Tasks newly added in the backlog.</p>
                    </div>

                    <!-- List group -->
                    <c:if test="${not empty todotasks}">
                        <ul id="todotasks" class="list-group list-group-flush">
                            <c:forEach var="task" begin="0" items="${todotasks}">
                                <li class="list-group-item">
                                    <h4>
                                        <span>#${task.id} ${task.name}</span> <span class="pull-right">
                                            <c:set var="taskUrl" value="#{request.contextPath}/mvc/tasks/#{task.id}" /> 
                                            <c:set var="taskEditUrl" value="#{request.contextPath}/mvc/tasks/#{task.id}/edit" /> 
                                            <a href="${taskUrl}"> 
                                                <span class="fa fa-file" aria-hidden="true"></span>
                                            </a> 
                                            <a href="${taskEditUrl}"> 
                                                <span class="fa fa-edit" aria-hidden="true"></span>
                                            </a>
                                        </span>
                                    </h4>
                                    <p>${task.description}</p>
                                    <p>
                                        <c:set var="markDoingUrl"
                                               value="#{request.contextPath}/mvc/tasks/#{task.id}/status" />
                                        <form action="${markDoingUrl}" method="post">
<!--                                            <input type="hidden" name="${mvc.csrf.name}"
                                                   value="${mvc.csrf.token}"/>-->
                                            <input type="hidden" name="_method" value="PUT"></input>
                                            <input type="hidden" name="status" value="DOING"></input>
                                            <button type="submit" class="btn btn-sm btn-primary">
                                                <span class="fa fa-play" aria-hidden="true"></span>
                                                START
                                            </button>
                                        </form>
                                    </p>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
            </div>

            <div id="doingtasks" class="col-md-4 col-xs-12">
                <div class="card">
                    <!-- Default panel contents -->
                    <div class="card-header">
                        <i class="fa fa-cog" aria-hidden="true"></i>
                        WORK IN PROGRESS
                    </div>
                    <div class="card-body">
                        <p class="card-text">Tasks had been assigned and started.</p>
                    </div>

                    <!-- List group -->
                    <c:if test="${not empty doingtasks}">
                        <ul id="doingtasks" class="list-group">
                            <c:forEach var="task" begin="0" items="${doingtasks}">
                                <li class="list-group-item">
                                    <h4>#${task.id} ${task.name}</h4>
                                    <p>${task.description}</p>
                                    <p>
                                        <c:set var="markDoneUrl"
                                               value="#{request.contextPath}/mvc/tasks/#{task.id}/status" />
                                        <form action="${markDoneUrl}" method="post">
<!--                                            <input type="hidden" name="${mvc.csrf.name}"
                                                   value="${mvc.csrf.token}"/>-->
                                            <input type="hidden" name="_method" value="PUT"></input>
                                            <input type="hidden" name="status" value="DONE"></input>
                                            <button type="submit" class="btn btn-sm btn-success">
                                                <span class="fa fa-check" aria-hidden="true"></span>
                                                DONE
                                            </button>
                                        </form>
                                    </p>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
            </div>
            <div id="donetasks" class="col-md-4 col-xs-12">
                <div class="card">
                    <!-- Default panel contents -->
                    <div class="card-header">
                        <i class="fa fa-check-circle" aria-hidden="true"></i>
                        DONE
                    </div>
                    <div class="card-body">
                        <p class="card-text">Tasks had been done successfully.</p>
                    </div>

                    <!-- List group -->
                    <c:if test="${not empty donetasks}">
                        <ul id="donetasks" class="list-group">
                            <c:forEach var="task" begin="0" items="${donetasks}">
                                <li class="list-group-item">
                                    <h4>#${task.id} ${task.name}</h4>
                                    <p>${task.description}</p>
                                    <p>
                                        <c:set var="deleteUrl" value="#{request.contextPath}/mvc/tasks/#{task.id}" />
                                        <form action="${deleteUrl}" method="post">
                                            <input type="hidden" name="_method" value="DELETE"></input>
                                            <button type="submit" class="btn btn-sm btn-danger">
                                                <span class="fa fa-trash" aria-hidden="true"></span>
                                                DELETE
                                            </button>
                                        </form>
                                    </p>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
            </div>
        </div>
    </ui:define>
</ui:composition>
```

It is almost same as the former version we used in the JSF sample.  The models in the `Models` can be resolved by EL directly in the view template.

Let's move to adding a task which is a little different from JSF.

Use a bean to wrap all fields in the *add task* form.

```java

public class TaskForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @FormParam("id")
    private Long id;

    @NotBlank
    @FormParam("name")
    @MvcBinding
    private String name;

    @NotBlank
    @Size(min = 10, max = 2000)
    @FormParam("description")
    @MvcBinding
    private String description;
    ...
}    
```

There is a  `@MvcBinding` applied on the fields which stops the JAX-RS raising an exception handler to handle the validation failure when submitting the form, it allows you to capture the failure errors in the `BindingResult`, and handle it yourself.

```java
if (validationResult.isFailed()) {
    AlertMessage alert = AlertMessage.danger("Validation voilations!");
    validationResult.getAllErrors()
        .stream()
        .forEach((ParamError t) -> {
            alert.addError(t.getParamName(), "", t.getMessage());
        });
    models.put("errors", alert);
    models.put("task", form);
    return Response.status(BAD_REQUEST).entity("add.xhtml").build();
}
```

On the `save` method, it is annotated with a `@CsrfProtected` annotation  which means this method will validate the CSRF value when submitting the form. You also need to add a csrf field in the form.

```xml
<input type="hidden" name="${mvc.csrf.name}"                    value="${mvc.csrf.token}"/>
```
The `mvc` is a request-scoped `MvcContext` bean, check the source codes of Eclipse Krazo's implementation - [`MvcContextImpl`](https://github.com/eclipse-ee4j/krazo/blob/master/core/src/main/java/org/eclipse/krazo/MvcContextImpl.java) .

> More info about the  CSRF, check [here](https://owasp.org/www-community/attacks/csrf).

In the above view templates, we use Facelets `request` implicit object to compute the Uri in the form action or links,  it looks a little tedious.  MVC provide a simple approach to generate  Uri via   `mvc` by referring the controller method directly in the the templates.

* You  can use `#{mvc.uri('TaskContrller#save')` as the form action in the *add.xhtml*. Or
* Add  a `@UriRef("saveTask")` on the save method in the controller, refer it as `#{mvc.uri('saveTask')`  in the view.

We will skip the the backend beans in this post, for example `Task`, `TaskRepository`, `Bootstrap` which are similar with the ones we used in the  former JSF sample. Check the [source codes](https://github.com/hantsy/jakartaee-mvc-sample) from my Github and explore them yourself.

## Misc: LocalResolver

Like Spring MVC and other frameworks, MVC provides a `LocalResolver` to allow you resolve `Locale` yourself, it allows you to switch between different languages when you are building a i18n application.

For example,  to resolve Locale from a query parameter `lang`, create a `QueryParamLocalResolver` like this.

```java
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
        // TODO 
        // you can sync the locale value into HttpSession.
        
        return queryLang != null ? Locale.forLanguageTag(queryLang) : null;
    }
}
```

The `Locale` value can be accessed in `MvcContext`. Assume there is a request-scoped bean to read messages from a properties file.

```java
@RequestScoped
@Named("msg")
public class Messages {

    private static final String BASE_NAME = "messages";

    @Inject
    private MvcContext mvcContext;

    //for production, it is better to use a cache to load the messages.properties at application startup.
    public final String get(final String key) {
        final ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, mvcContext.getLocale());

        return bundle.containsKey(key) ? bundle.getString(key) : formatUnknownKey(key);
    }

    private static String formatUnknownKey(final String key) {
        return String.format("???%s???", key);
    }
}
```

And  add a `LocaleController` and `locale.xhtml` to verify the result.

```java
@Path("locale")
@Controller
@RequestScoped
public class LocaleController {

    @Inject
    MvcContext mvc;

    @Inject
    Models models;

    @Inject
    Logger log;

    @GET
    public String get() {
        Locale locale = mvc.getLocale();
        models.put("locale", locale);
        return "locale.xhtml";
    }

}
```

```xml
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
                xmlns:ui="http://xmlns.jcp.org/jsf/facelets"
                xmlns:f="http://xmlns.jcp.org/jsf/core"
                xmlns:h="http://xmlns.jcp.org/jsf/html"
                xmlns:c="http://java.sun.com/jsp/jstl/core"
                template="/WEB-INF/layout/template.xhtml">
    <ui:define name="pageTitle">Locale</ui:define>
    <ui:define name="content">
        <div class="row">
            <div class="col-md-12">
                <dl class="dl-horizontal">
                    <dt>Locale</dt>
                    <dd>${locale}</dd>
                    
                    <dt>Message</dt>
                    <dd>#{msg.get('greeting')}</dd>
                </dl>
            </div>
        </div>
    </ui:define>
</ui:composition>
```

When running the application, and access http://localhost:8080/jakarta-mvc-sample/mvc/locale?lang=en and http://localhost:8080/jakarta-mvc-sample/mvc/locale?lang=zh-CN to see the messages changed in the *locale.xhtml* view.

Compared to JSF, MVC is a new and young spec. I think it will be accepted by developers and become mature as time goes by.

There are a few issues I encountered when writing this sample application. Especially, there are some blocking issues when using Apache CXF and Open Liberty. 

* I have prepared a simple tests with Arquillian Drone and Arquillian Graphene, but it is failed on  Payara embedded container(I only tests on Payara Managed and Payara Embedded).

* The `MvcBinding` and form validation did not work on CXF/Open Liberty.

   









  