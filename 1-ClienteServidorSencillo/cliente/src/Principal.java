import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.imageio.IIOException;
import sun.net.util.IPAddressUtil;

public class Principal 
{
    public static void main(String[] args) 
    {
        String ip = ""; //se escribe por la consola la ip destino
        int puerto = 0; //se escribe por la consola el puerto con el que se comunicara al server
        String mensaje = ""; //y el mensaje que se le enviara
        Scanner scanner = new Scanner(System.in); //para detectar lo que se escriba en la consola
        String [] se;
        boolean sip;
        do
        {
          try {
            System.out.print("Ingresa la direccion ip del servidor: ");
            ip = scanner.nextLine();
            } catch (Exception y) {
                System.out.println("Error al ingresar la ip  " + y.toString());
                System.exit(4);
            }
            se = ip.split("\\.");
            sip  = IPAddressUtil.isIPv4LiteralAddress(ip);
        }while(ip.equalsIgnoreCase("") || se.length < 4 || se.length > 4 || !sip);
        
        
        try {
            System.out.print("Ingresa el puerto que usaras: ");
            puerto =  Integer.parseInt(scanner.nextLine());
        } catch (Exception n) {
            System.out.println("Error al ingresar el puerto: " + n.toString());
        }
        Socket socket = null;
        try {
            socket = new Socket(ip,puerto);
        } catch (IOException i) {
            System.out.println("Error al crear el socket " + i.toString());
            System.exit(1);
        }finally
        {
            System.out.println("¡Conexion exitosa!");
        }
        
        
        PrintWriter escritor = null;
        try{
        escritor = new PrintWriter(socket.getOutputStream(),true);
        System.out.print("Escribe el mensaje: ");
        mensaje = scanner.nextLine();
        escritor.println(mensaje); //para leer lo que escribas en el cmd y sea como el mensaje
        }
        catch(IOException e)
        {
           System.out.println("Error al mandar info" + e.toString());
           System.exit(2);
        }
        try 
        {
          socket.close();
        } catch (IOException f) {
            System.out.println("Error al cerrar el socket" + f.toString());
            System.exit(3);
        }
        
    }   
}
