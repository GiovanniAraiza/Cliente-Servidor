import java.net.*;
import java.io.*;
import java.util.Scanner;
import sun.net.util.IPAddressUtil;
public class mainClienteDir 
{

    public static void main(String[] args) 
    {
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;
        
        String datos = "";
        String datosEntrada = "";
        String ip = ""; //se escribe por la consola la ip destino
        int puerto = 0; //se escribe por la consola el puerto con el que se comunicara al server
        Scanner scanner = new Scanner(System.in);
        String [] se;
        boolean sip;
        
        do
        {
          try {
            System.out.print("Ingresa la direccion ip del servidor: ");
            ip = scanner.nextLine();
            } catch (Exception y) {
                System.out.println("Error al ingresar la ip  " + y.toString());
                System.exit(1);
            }
            se = ip.split("\\.");
            sip  = IPAddressUtil.isIPv4LiteralAddress(ip);
        }while(ip.equalsIgnoreCase("") || se.length < 4 || se.length > 4 || !sip);
        
        try {
            System.out.print("Ingresa el puerto que usaras: ");
            puerto =  Integer.parseInt(scanner.nextLine());
        } catch (Exception n) {
            System.out.println("Error al ingresar el puerto: " + n.toString());
            System.exit(2);
        }
        ////creacion del socket
        try {
            socket = new Socket(ip,puerto);
        } catch (IOException i) {
            System.out.println("Error al crear el socket " + i.toString());
            System.exit(3);
        }finally
        {
            System.out.println("¡Conexion exitosa!");
        }
        
        //recibir la informacion
        try {
             lector = new BufferedReader(
            new InputStreamReader(socket.getInputStream()) //Esto es como el flujo de informacion
        );//esperar a que se llene informacion
        } catch (IOException e) {
            System.out.println("Error al recibir la informacion " + e.toString());
            System.exit(4);
        }
        //para enviar el directorio solicitado
            try {
                escritor = new PrintWriter(socket.getOutputStream(), true);
                System.out.print("Escribe la ruta del directorio a mostrar su contenido: ");
                datos = scanner.nextLine();
                escritor.println(datos); //para leer lo que escribas en el cmd y sea como el mensaje
            } catch (IOException e) {
                System.out.println("Error al mandar info " + e.toString());
                System.exit(5);
            }
            //lector para recibir la bandera
            try {
                datosEntrada = lector.readLine();
            } catch (Exception l) {
                System.out.println("Error al recibir los datos " + l.toString());
                System.exit(6);
            }
            //bandera de existencia de carpeta
            if (datosEntrada.equalsIgnoreCase("true"))
            {
                //ciclo para recibir el contenido mientras la bandera sea true
                try {
                    while((datosEntrada = lector.readLine()).equalsIgnoreCase("true"))
                    {
                       try 
                        {
                          datosEntrada = lector.readLine();
                            System.out.println(datosEntrada);
                        } catch (Exception l) {
                            System.out.println("Error al recibir los datos " + l.toString());
                            System.exit(7);
                        } 
                    }
                } catch (Exception e) {
                    System.out.println("Error al recibir los datos " + e.toString());
                    System.exit(8);
                }
            }
            else
            {
                System.out.print("El directorio no existe");
            }
            //cerrar socket del cliente
            try {
            socket.close();
            } catch (Exception t) {
                System.out.println("Error al cerrar socket " + t.toString());
                System.exit(9);
            }
    }
    
}
