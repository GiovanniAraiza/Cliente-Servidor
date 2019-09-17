import java.net.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

public class principalServidorArchivo 
{
    
    public static void main(String[] args) throws Exception
    {
        String entrada = "";
        Scanner scanner = new Scanner(System.in);
        Scanner x;
        String salida = "";
        int puerto = 0;
        ServerSocket socketServidor = null; //recibir informacion
        Socket socket = null;
        
        try {
            System.out.print("Escribe el puerto del servidor: ");
            puerto = Integer.parseInt(scanner.nextLine());
        } catch (Exception h) {
            System.out.print("Error al ingresar el puerto " + h.toString());
            System.exit(0);
        }
        while(true)
        {
            try {
                socketServidor = new ServerSocket(puerto); //recibir informacion
                socket = socketServidor.accept();         //recibir informacion, el socket es como una tuberia
            } catch (Exception a) {
                System.out.println("Error al asignar un puerto al servidor " + a.toString());
                System.exit(1);
            }
            BufferedReader lector = null;
            try {
                lector = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()) //Esto es como el flujo de informacion
                );//esperar a que se llene informacion
            } catch (Exception e) {
                System.out.println("Error al recibir la informacion " + e.toString());
                System.exit(2);
            }
            PrintWriter escritor = null;
            try {
                escritor = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("Error al mandar info" + e.toString());
                System.exit(3);
            }
            
            try {
                entrada = lector.readLine();
                //System.out.println(entrada);
            } catch (Exception b) {
                System.out.println("Error al recibir los datos " + b.toString());
                System.exit(4);
            }
            int arch = entrada.length() - (entrada.indexOf(".") + 1);
            if ((arch == 3 || arch == 4) && entrada.contains(".")) {

                File archivo = new File(entrada);
                if (archivo.exists()) {
                    
                    x = new Scanner(archivo);
                    try {
                        salida = "true";
                        try {
                            escritor.println(salida);
                        } catch (Exception e) {
                            System.out.println("Error al enviar los datos " + e.toString());
                            System.exit(5);
                        }
                        
                        salida = archivo.getName();
                        try {
                            escritor.println(salida);
                        } catch (Exception q) {
                            System.out.println("Error al enviar los datos " + q.toString());
                            System.exit(6);
                        }
                        
                        while (x.hasNext()) {
                            salida = "true";
                            escritor.println(salida);
                            String texto = x.nextLine();
                            salida = texto;
                            escritor.println(salida);
                        }
                    } catch (Exception l) {
                       System.out.println("Error mientras se envia el archivo " + l.toString());
                       System.exit(7);
                    }
                    salida = "false";
                    try {
                        escritor.println(salida);
                    } catch (Exception j) {
                        System.out.println("Error al enviar los datos " + j.toString());
                        System.exit(8);
                    }
                    try {
                        socket.close();
                        socketServidor.close();
                    } catch (Exception c) {
                        System.out.println("Error al cerrar los sockets " + c.toString());
                        System.exit(9);
                    }
                    //System.out.println("Archivo enviado con exito");
                } else {
                    System.out.println("no existe");
                    salida = "false";
                    try {
                        escritor.println(salida);
                    } catch (Exception g) {
                        System.out.println("Error al enviar los datos " + g.toString());
                        System.exit(10);
                    }
                    try {
                        socket.close();
                        socketServidor.close();
                    } catch (Exception k) {
                        System.out.println("Error al cerrar los sockets " + k.toString());
                        System.exit(11);
                    }
                }

            }
        }
    }
}

