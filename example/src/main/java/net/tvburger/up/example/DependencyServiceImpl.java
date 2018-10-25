package net.tvburger.up.example;

import java.util.Date;

public class DependencyServiceImpl implements DependencyService {

    @Override
    public Date getTime(String input) {
        return new Date();
    }

}
