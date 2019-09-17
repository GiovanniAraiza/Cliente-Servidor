import java.net.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;
public class mainServidorDir 
{
    public static void main(String[] args) 
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
            
            //recibe el directorio
            try {
                entrada = lector.readLine();
                //System.out.println(entrada);
            } catch (Exception b) {
                System.out.println("Error al recibir los datos " + b.toString());
                System.exit(4);
            }
            //crea el directorio 
            File carpeta =  null;
            String[] contenido;
            carpeta = new File(entrada);
            //verifica si existe el directorio
            if(carpeta.exists())
            {
                try {
                       escritor.println("true");
                    } catch (Exception g) {
                        System.out.println("Error al enviar los datos " + g.toString());
                        System.exit(5);
                    }
                contenido = carpeta.list();
                int num = 0;
                while(num < contenido.length)
                {
                    try {
                       escritor.println("true");
                    } catch (Exception g) {
                        System.out.println("Error al enviar los datos " + g.toString());
                        System.exit(6);
                    }
                    try {
                       escritor.println(contenido[num]);
                    } catch (Exception c) {
                        System.out.println("Error al enviar contenido de carpeta" + c.toString());
                        System.exit(7);
                    }
                    num++;
                }
                try {
                       escritor.println("false");
                    } catch (Exception g) {
                        System.out.println("Error al enviar los datos " + g.toString());
                        System.exit(8);
                    }
            }
            else
            {
                try {
                       escritor.println("El directorio no existe");
                    } catch (Exception g) {
                        System.out.println("Error al enviar los datos " + g.toString());
                        System.exit(9);
                    }
            }
            
            try {
                socket.close();
                socketServidor.close();
            } catch (Exception k) {
                System.out.println("Error al cerrar los sockets " + k.toString());
                System.exit(10);
            }
        }
    }
    
}
