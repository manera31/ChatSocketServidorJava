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

    // ArrayList para guardas las IP de todos los clientes que se conecten.
    private ArrayList<String> conexiones;

    public void run() {
        conexiones = new ArrayList<String>();
        ServerSocket serverSocket = null;

        Socket socket = null;
        try {
            // Indicamos el puerto de escucha del servidor.
            serverSocket = new ServerSocket(9990);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                // El servidor se queda a la espera en esta línea de código hasta que un cliente haga una petición.
                socket = serverSocket.accept();

                // Cogemos el mensaje del cliente.
                // La siguiente linea puede lanzar una excepción. Indica que se ha conectado un nuevo cliente.
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Mensaje mensaje = (Mensaje) objectInputStream.readObject();

                // Mostramos por consola los datos del mensaje para poder ver un registro.
                System.out.println("De " + mensaje.getDe() + ", para " + mensaje.getPara());
                System.out.println(" - " + mensaje.getMensaje());

                // Comprobamos si el mensaje se trata de uno privado.
                if (mensaje.isPrivate()){
                    // Si es privado se lo envia a la IP del cliente de destino.
                    Socket destino = new Socket(mensaje.getPara(), 9090);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(destino.getOutputStream());
                    objectOutputStream.writeObject(mensaje);
                    destino.close();
                } else {
                    // Si no es privado recorre todas las IP que estén conectadas y reenvia el mensaje a cada una.
                    for (String conexion : conexiones) {
                        Socket destino = new  Socket(conexion, 9090);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(destino.getOutputStream());
                        objectOutputStream.writeObject(mensaje);
                        destino.close();
                    }
                }

                // Se cierra el socket para eviatar posibles fallos.
                socket.close();

            } catch (EOFException eofe) {
                // Entrará esta excepcion si la línea 36 no recibe nada en el InputStream. (Hecho a propósito para ver
                // si se trata de un cliente que se acaba de conectar.

                boolean esta = false;

                // Comprueba si la IP ya esta en el array.
                for (String s: conexiones){
                    if(s.equals(socket.getInetAddress().getHostAddress())){
                        esta = true;
                    }
                }

                // Si no está en el array, la añade y muestra un mensaje de conexión.
                if (!esta){
                    conexiones.add(socket.getInetAddress().getHostAddress());
                    System.out.println("Se ha conectado : "+ socket.getInetAddress().getHostAddress());
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
