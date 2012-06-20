package com.m3.multilane;


import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServer {

    public static void main(String[] args) throws Exception {
        new TestServer(8888).start();
    }

    public class GetMethodHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest req, HttpServletResponse res) {
            try {
                String uri = req.getRequestURI();
                if (uri.equals("/404")) {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } else if (uri.equals("/500")) {
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                } else {
                    // blocking simulation
                    if (uri.equals("/500ms")) {
                        Thread.sleep(500L);
                    } else if (uri.equals("/1s")) {
                        Thread.sleep(1000L);
                    } else if (uri.equals("/2s")) {
                        Thread.sleep(2000L);
                    } else if (uri.equals("/3s")) {
                        Thread.sleep(3000L);
                    } else {
                        Thread.sleep(100L);
                    }
                    String v = req.getParameter("v");
                    String charset = req.getParameter("charset");

                    res.setStatus(HttpServletResponse.SC_OK);
                    res.setContentType("text/plain");

                    if (charset != null) {
                        res.setCharacterEncoding(charset);
                    }

                    if (v != null) {
                        String body = new String(v.getBytes(res.getCharacterEncoding()), res.getCharacterEncoding());
                        res.getWriter().print(body);
                    } else {
                        res.getWriter().print("Nothing");
                    }
                }
                baseRequest.setHandled(true);
            } catch (InterruptedException ie) {
                Thread.interrupted();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    private Server server;

    public TestServer(int port) {
        server = new Server(port);
        server.setHandler(new GetMethodHandler());
    }

    public void start() throws Exception {
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }


}
