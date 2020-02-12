package com.joanmanera.chatsocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable{
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9990);

            while (true){
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Mensaje mensaje = (Mensaje)objectInputStream.readObject();
                System.out.println("De " + mensaje.getDe() + ", para " + mensaje.getPara());
                System.out.println(" - " + mensaje.getMensaje());

                Socket destino = new Socket(mensaje.getPara(), 9090);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(destino.getOutputStream());
                objectOutputStream.writeObject(mensaje);

                destino.close();
                socket.close();
            }
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
