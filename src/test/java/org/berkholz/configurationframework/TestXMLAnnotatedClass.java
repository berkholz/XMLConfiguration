/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berkholz.configurationframework;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Marcel Berkholz
 */
@XmlRootElement(name = "PabamoTestConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestXMLAnnotatedClass {

    @XmlElement(name = "testvar")
    private String testVariable;

    public TestXMLAnnotatedClass() {
        testVariable = "Hallo";
    }

    public String getTestVariable() {
        return testVariable;
    }

    public void setTestVariable(String testVariable) {
        this.testVariable = testVariable;
    }
}
