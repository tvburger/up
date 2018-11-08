package net.tvburger.up.example.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DependencyServiceImpl implements DependencyService {

    private static final Logger logger = LoggerFactory.getLogger(DependencyServiceImpl.class);

    @Override
    public Date getTime(String input) {
        logger.info("My custom log message!");
        return new Date();
    }

}
