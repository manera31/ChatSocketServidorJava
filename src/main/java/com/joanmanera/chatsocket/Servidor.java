package com.joanmanera.chatsocket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Servidor implements Runnable{
    private ArrayList<String> conexiones;
    public void run() {
        conexiones = new ArrayList<String>();
        ServerSocket serverSocket = null;

        Socket socket = null;
        try {
            serverSocket = new ServerSocket(9990);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {



                socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Mensaje mensaje = (Mensaje) objectInputStream.readObject();
                System.out.println("De " + mensaje.getDe() + ", para " + mensaje.getPara());
                System.out.println(" - " + mensaje.getMensaje());

                if (mensaje.isPrivate()){
                    Socket destino = new Socket(mensaje.getPara(), 9090);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(destino.getOutputStream());
                    objectOutputStream.writeObject(mensaje);
                    destino.close();
                } else {
                    for (String conexion : conexiones) {
                        Socket destino = new  Socket(conexion, 9090);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(destino.getOutputStream());
                        objectOutputStream.writeObject(mensaje);
                        destino.close();
                    }
                }

                socket.close();

            } catch (EOFException eofe) {
                boolean esta = false;
                for (String s: conexiones){
                    if(s.equals(socket.getInetAddress().getHostAddress())){
                        esta = true;
                    }
                }
                if (!esta){
                    conexiones.add(socket.getInetAddress().getHostAddress());
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
