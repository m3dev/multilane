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

import com.m3.scalaflavor4j.*;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.m3.scalaflavor4j.ConcurrentOps.future;

public abstract class MultiLaneTemplate<A extends Action, R> implements MultiLane<A, R> {

    protected Map<String, F0<Either<Throwable, R>>> futures = new ConcurrentHashMap<String, F0<Either<Throwable, R>>>();

    protected Map<String, R> defaultValues = new CopyOnWriteHashMap<String, R>();

    protected Map<String, Long> spentTime = new ConcurrentHashMap<String, Long>();

    @Override
    public MultiLane<A, R> start(final String name, final A action) {
        return start(name, action, null);
    }

    @Override
    public MultiLane<A, R> start(final String name, final A action, R defaultValue) {
        defaultValues.put(name, defaultValue);
        F0<Either<Throwable, R>> f = future(new F0<Either<Throwable, R>>() {
            public Either<Throwable, R> _() {
                long before = currentMillis();
                Either<Throwable, R> result = action.apply();
                spentTime.put(name, currentMillis() - before);
                return result;
            }
        });
        futures.put(name, f);
        return this;
    }

    @Override
    public Map<String, Either<Throwable, R>> collect() {
        return SMap._(futures).map(new F1<Tuple2<String, F0<Either<Throwable, R>>>, Tuple2<String, Either<Throwable, R>>>() {
            public Tuple2<String, Either<Throwable, R>> _(Tuple2<String, F0<Either<Throwable, R>>> each) throws Exception {
                String name = each._1();
                Either<Throwable, R> result = each._2().apply();
                return Tuple2._(name, result);
            }
        }).toMap();
    }

    @Override
    public Map<String, R> collectValues() {
        return SMap._(collect()).map(new F1<Tuple2<String, Either<Throwable, R>>, Tuple2<String, R>>() {
            public Tuple2<String, R> _(Tuple2<String, Either<Throwable, R>> each) throws Exception {
                final String name = each._1();
                final Either<Throwable, R> result = each._2();
                return Tuple._(name, result.right().getOrElse(new F0<R>() {
                    public R _() {
                        return defaultValues.get(name);
                    }
                }));
            }
        }).toMap();
    }

    @Override
    public Map<String, Long> spentTime() {
        return spentTime;
    }

    private static long currentMillis() {
        return System.currentTimeMillis();
    }

}