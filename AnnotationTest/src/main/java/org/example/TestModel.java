package org.example;

public class TestModel {
    String testString = "Test string has not been changed yet.";

    @TestAnnotation
    public void setTestString(String testString) {
        this.testString = testString;
    }

    public String getTestString() {
        return testString;
    }
}
