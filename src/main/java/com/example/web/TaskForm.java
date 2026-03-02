package com.example.web;

import java.io.Serializable;
import java.util.Objects;
import jakarta.mvc.binding.MvcBinding;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    public boolean equals(Object o) {
        if (!(o instanceof TaskForm taskForm)) return false;
        return Objects.equals(id, taskForm.id) && Objects.equals(name, taskForm.name) && Objects.equals(description, taskForm.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "TaskForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
