package com.metraplasa;

public class Test2Impl implements Test2Interface {
    @Override
    public Test buildTest(String name, Integer age) {
        Test res = new Test();
        res.setName(name);
        res.setAge(age);
        System.out.println("doing build : " +res);
        return res;
    }

    @Override
    public Test doTest(String name, Integer age) {
        Test res = new Test();
        res.setName(name);
        res.setAge(age);
        System.out.println("doing test : " +res);
        return res;
    }
}
