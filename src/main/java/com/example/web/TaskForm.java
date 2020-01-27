package com.example.web;

import java.io.Serializable;
import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.mvc.binding.MvcBinding;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@RequestScoped
public class TaskForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @FormParam("name")
    @MvcBinding
    private String name;

    @NotBlank
    @Size(min = 10, max = 2000)
    @FormParam("description")
    @MvcBinding
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaskForm other = (TaskForm) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TaskForm{" + "name=" + name + ", description=" + description + '}';
    }

}
