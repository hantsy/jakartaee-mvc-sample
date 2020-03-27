/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.it;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author hantsy
 */
@Location("index.html")
public class IndexPage {

    @FindBy(css = "a.btn")
    private WebElement startButton;


    public void clickStartButton() {
        Graphene.guardHttp(startButton).click();
    }
   
}
