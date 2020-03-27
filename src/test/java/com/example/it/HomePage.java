/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author hantsy
 */
public class HomePage {

    @FindBy(id = "todotasks")
    private WebElement todotasks;

    @FindBy(id = "doingtasks")
    private WebElement doingtasks;

    @FindBy(id = "donetasks")
    private WebElement donetasks;

    @FindBy(css = "h1.page-header")
    private WebElement title;

    public void assertOnHomePage() {
        assertEquals("TASK LIST", title.getText());
    }

    public void assertTodoTasksSize(int size) {
        assertTrue(todotasks.findElements(By.cssSelector("li.list-group-item")).size() == size);
    }

    public void assertDoingTasksSize(int size) {
        assertTrue(doingtasks.findElements(By.cssSelector("li.list-group-item")).size() == size);
    }

    public void assertDoneTasksSize(int size) {
        assertTrue(donetasks.findElements(By.cssSelector("li.list-group-item")).size() == size);
    }

}
