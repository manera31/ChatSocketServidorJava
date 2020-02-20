package com.joanmanera.chatsocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        Cliente c = new Cliente();
    }
    public Cliente(){
        try {
            Socket socket = new Socket("192.168.1.71", 9990);
            Mensaje mensaje = new Mensaje("yo", "tu", "hola mundo", false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(mensaje);

            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
