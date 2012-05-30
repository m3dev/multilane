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
package com.m3.multilane;

import com.m3.multilane.action.Action;
import com.m3.scalaflavor4j.Either;

import java.util.Map;

/**
 * MultiLane
 *
 * @param <A> action type
 * @param <R> result type
 */
public interface MultiLane<A extends Action, R> {

    /**
     * Starts the specified action asynchronously
     *
     * @param name   name
     * @param action action
     * @return self
     */
    MultiLane<A, R> start(String name, A action);

    /**
     * Starts the specified action with the default value asynchronously
     *
     * @param name         name
     * @param action       action
     * @param defaultValue default value
     * @return self
     */
    MultiLane<A, R> start(String name, A action, R defaultValue);

    /**
     * Returns the actions' results as Either values
     *
     * @return results
     */
    Map<String, Either<Throwable, R>> collect();

    /**
     * Returns the actions' result values
     *
     * @return
     */
    Map<String, R> collectValues();

    /**
     * Returns all the spent time in millis
     *
     * @return spent time map
     */
    Map<String, Long> spentTime();

}
