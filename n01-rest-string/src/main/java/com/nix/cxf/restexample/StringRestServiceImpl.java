package com.nix.cxf.restexample;

public class StringRestServiceImpl implements StringRestService {
    @Override
    public String getString() throws Exception {

        return "test string - " + System.currentTimeMillis();
    }
}
