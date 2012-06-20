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
package com.m3.multilane.util;

import com.sun.jersey.api.client.Client;

/**
 * Jersey Client Utility
 */
public class JerseyClientUtil {

    private JerseyClientUtil() {
    }

    /**
     * Default connect timeout ms
     */
    private static final Integer DEFAULT_CONNECT_TIMEOUT_MILLIS = 1000;

    /**
     * Default read timeout ms
     */
    private static final Integer DEFAULT_READ_TIMEOUT_MILLIS = 10000;

    /**
     * Creates a new {@link Client} instance with default connect/read timeout values.
     *
     * @return client instance
     */
    public static Client createClient() {
        return createClient(DEFAULT_CONNECT_TIMEOUT_MILLIS, DEFAULT_READ_TIMEOUT_MILLIS);
    }

    /**
     * Creates a new {@link Client} instance with default connect timeout value and specified read timeout value.
     *
     * @param readTimeoutMillis
     * @return client instance
     */
    public static Client createClient(Integer readTimeoutMillis) {
        return createClient(DEFAULT_CONNECT_TIMEOUT_MILLIS, readTimeoutMillis);
    }

    /**
     * Creates a new {@link Client} instance with specified connect/read timeout values.
     *
     * @param connectTimeoutMillis
     * @param readTimeoutMillis
     * @return client instance
     */
    public static Client createClient(Integer connectTimeoutMillis, Integer readTimeoutMillis) {
        Client client = Client.create();
        client.setConnectTimeout(connectTimeoutMillis.intValue());
        client.setReadTimeout(readTimeoutMillis.intValue());
        return client;
    }


}
