package net.tvburger.up.example.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependencyServiceImpl implements DependencyService {

    private static final Logger logger = LoggerFactory.getLogger(DependencyServiceImpl.class);

    @Override
    public String getPrompt(boolean superUser) {
        logger.info("We need root prompt? " + superUser);
        return superUser ? "#" : "$";
    }

}
