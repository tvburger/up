package net.tvburger.up.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DependencyServiceImpl implements DependencyService {

    private static final Logger logger = LoggerFactory.getLogger(DependencyServiceImpl.class);

    @Override
    public Date getTime(String input) {
        logger.info("my test!", new IllegalArgumentException("hahaha!"));
        return new Date();
    }

}
