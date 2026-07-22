package com.example.it.pageobject;

import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author hantsy
 */
@Location("mvc/tasks")
public class HomePageObject {

    @FindBy(id = "todotasks")
    private WebElement todotasks;

    @FindBy(id = "doingtasks")
    private WebElement doingtasks;

    @FindBy(id = "donetasks")
    private WebElement donetasks;

    public void assertTodoTasksSize(int size) {
        assertEquals(size, todotasks.findElements(By.cssSelector("li.task-item")).size());
    }

    public void assertDoingTasksSize(int size) {
        assertEquals(size, doingtasks.findElements(By.cssSelector("li.task-item")).size());
    }

    public void assertDoneTasksSize(int size) {
        assertEquals(size, donetasks.findElements(By.cssSelector("li.task-item")).size());
    }
}
