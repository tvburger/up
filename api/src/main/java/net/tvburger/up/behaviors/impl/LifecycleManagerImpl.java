package net.tvburger.up.behaviors.impl;

import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.LifecycleManager;

public class LifecycleManagerImpl implements LifecycleManager {

    private State state = State.NEW;

    @Override
    public void init() throws LifecycleException {
        transitionState(State.NEW, State.READY);
    }

    @Override
    public void start() throws LifecycleException {
        transitionState(State.READY, State.ACTIVE);
    }

    @Override
    public void stop() throws LifecycleException {
        transitionState(State.ACTIVE, State.READY);
    }

    @Override
    public void destroy() throws LifecycleException {
        transitionState(State.READY, State.RETIRED);
    }

    protected synchronized void fail() {
        state = State.FAILED;
    }

    @Override
    public synchronized State getState() {
        return state;
    }

    private synchronized void transitionState(State from, State to) throws LifecycleException {
        if (state != from) {
            throw new LifecycleException(String.format("We should be in state %s but are in %s!", from, state));
        }
        state = to;
    }

}
