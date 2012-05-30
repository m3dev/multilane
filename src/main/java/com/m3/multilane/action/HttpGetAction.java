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
import com.m3.scalaflavor4j.Left;
import com.m3.scalaflavor4j.Right;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * HTTP GET request action
 */
public class HttpGetAction implements Action<String, String> {

    private String url;

    private String charset = "UTF-8";

    private Integer connectTimeoutMillis = 1000;

    private Integer readTimeoutMillis = 10000;

    public HttpGetAction(String url, String charset, Integer timeoutMillis) {
        setInput(url);
        setCharset(charset);
        setTimeoutMillis(timeoutMillis);
    }

    private Client getJerseyClient() {
        Client client = Client.create();
        client.setConnectTimeout(connectTimeoutMillis.intValue());
        client.setReadTimeout(readTimeoutMillis.intValue());
        return client;
    }

    @Override
    public Either<Throwable, String> apply() {
        try {
            WebResource resource = getJerseyClient().resource(url);
            // TODO check
            resource.setProperty("Accept-Charset", charset);
            String result = resource.get(String.class);
            return Right._(result);
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

    public void setCharset(String charset) {
        this.charset = charset;
    }


}
