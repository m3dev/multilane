/*
 * Copyright 2012 M3, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.m3.multilane.action;

import com.m3.scalaflavor4j.Either;
import com.m3.scalaflavor4j.F0;
import com.m3.scalaflavor4j.Left;
import com.m3.scalaflavor4j.Right;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.m3.scalaflavor4j.ConcurrentOps.future;

/**
 * Action with input
 *
 * @param <INPUT>  input
 * @param <OUTPUT> output
 */
public abstract class InputAction<INPUT, OUTPUT> implements Action<INPUT, OUTPUT> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private INPUT input;
    private Integer timeoutMillis;

    public InputAction(INPUT input, Integer timeoutMillis) {
        setInput(input);
        setTimeoutMillis(timeoutMillis);
    }

    @Override
    public INPUT getInput() {
        return this.input;
    }

    @Override
    public void setInput(INPUT input) {
        this.input = input;
    }

    @Override
    public Integer getTimeoutMillis() {
        return timeoutMillis;
    }

    @Override
    public void setTimeoutMillis(Integer millis) {
        this.timeoutMillis = millis;
    }

    @Override
    public Either<Throwable, OUTPUT> apply() {
        try {
            Future<OUTPUT> future = future(new F0<OUTPUT>() {
                public OUTPUT _() throws Exception {
                    return process(getInput());
                }
            }).toJucFuture();
            return Right._(future.get(getTimeoutMillis(), TimeUnit.MILLISECONDS));
        } catch (Throwable e) {
            onFailure(e);
            return Left._(e);
        }
    }

    /**
     * Callback when some Exception is thrown
     *
     * @param t exception
     */
    protected void onFailure(Throwable t) {
        if (log.isDebugEnabled()) {
            log.debug("Processing thrown " + t.getClass().getCanonicalName(), t);
        }
    }

    /**
     * Processes a blocking operation
     *
     * @param input input
     * @return result
     */
    protected abstract OUTPUT process(INPUT input) throws Exception;

}
