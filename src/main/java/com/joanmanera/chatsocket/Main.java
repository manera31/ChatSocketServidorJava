package com.joanmanera.chatsocket;

public class Main {
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        Thread t = new Thread(servidor);
        t.start();
    }
}
