package com.m3.multilane;

public class RendezvousFactory {

    private RendezvousFactory() {
    }

    public static Rendezvous create() {
        return new DefaultRendezvous();
    }

}
