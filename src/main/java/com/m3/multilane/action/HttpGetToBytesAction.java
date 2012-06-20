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

import com.m3.multilane.util.JerseyClientUtil;
import com.m3.scalaflavor4j.Either;
import com.m3.scalaflavor4j.Left;
import com.m3.scalaflavor4j.Right;
import com.sun.jersey.api.client.Client;

/**
 * HTTP GET request action
 */
public class HttpGetToBytesAction implements Action<String, byte[]> {

    private String url;

    private Integer readTimeoutMillis = 10000;

    public HttpGetToBytesAction(String url, Integer timeoutMillis) {
        setInput(url);
        setTimeoutMillis(timeoutMillis);
    }

    @Override
    public Either<Throwable, byte[]> apply() {
        try {
            Client client = JerseyClientUtil.createClient(readTimeoutMillis);
            return Right._(client.resource(url).get(byte[].class));
        } catch (Throwable e) {
            return Left._(e);
        }
    }

    @Override
    public String getInput() {
        return this.url;
    }

    @Override
    public void setInput(String input) {
        this.url = input;
    }

    @Override
    public Integer getTimeoutMillis() {
        return this.readTimeoutMillis;
    }

    @Override
    public void setTimeoutMillis(Integer millis) {
        this.readTimeoutMillis = millis;
    }

}
