package com.metraplasa;

import java.util.UUID;

public class TestImpl implements TestInterface {
    @Override
    public Test makeTest(String test) {
        Test res = new Test();
        res.setName(UUID.randomUUID().toString() + " " + test);
        res.setAge(29);
        System.out.println("Test nih: " +test);
        return res;
    }
}
