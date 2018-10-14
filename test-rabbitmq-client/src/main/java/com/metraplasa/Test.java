package com.metraplasa;

import java.io.Serializable;

public class Test implements Serializable {
    private static final long serialVersionUID = 2L;

    private String name;
    private Integer age;
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Test{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", test='" + test + '\'' +
                '}';
    }
}
