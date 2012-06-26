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

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.m3.scalaflavor4j.ConcurrentOps.future;

/**
 * Action without input
 *
 * @param <OUTPUT> output
 */
public abstract class NoInputAction<OUTPUT> implements Action<NoInput, OUTPUT> {

    private Integer timeoutMillis;

    public NoInputAction(Integer timeoutMillis) {
        setTimeoutMillis(timeoutMillis);
    }

    @Override
    public NoInput getInput() {
        throw new UnsupportedOperationException("This is a NoInputAction.");
    }

    @Override
    public void setInput(NoInput s) {
        throw new UnsupportedOperationException("This is a NoInputAction.");
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
                    return process();
                }
            }).toJucFuture();
            return Right._(future.get(getTimeoutMillis(), TimeUnit.MILLISECONDS));
        } catch (Throwable e) {
            return Left._(e);
        }
    }

    /**
     * Processes a blocking operation
     *
     * @return result
     */
    protected abstract OUTPUT process() throws Exception;

}
