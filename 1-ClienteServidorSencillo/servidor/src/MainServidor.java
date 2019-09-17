import java.net.*;
import java.io.*;
import java.util.Scanner;

public class MainServidor 
{

    public static void main(String[] args) 
    {
        int escritor = 0; //la primera palabra sera el puerto
        ServerSocket socketServidor = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);  
        
        try {
            System.out.print("Escribe el puerto del servidor: ");
            escritor = Integer.parseInt(scanner.nextLine());
        } catch (Exception x) {
            System.out.print("Error al ingresar el puerto " + x.toString());
            System.exit(0);
        }
        while(true)
        {
        try {
            socketServidor = new ServerSocket(escritor); //recibir informacion
            System.out.println("Esperando conexion con el cliente...");
            socket = socketServidor.accept();                //recibir informacion el socket es como una tuberia
        } catch (Exception a){
            System.out.println("Error al asignar un puerto al servidor " + a.toString());
            System.exit(1);
        }finally
        {
            System.out.println("¡Conexion exitosa!");
        }
        
        System.out.println("Esperando mensaje...");
        
        BufferedReader lector = null;
        try {
             lector = new BufferedReader(
            new InputStreamReader(socket.getInputStream()) //Esto es como el flujo de informacion
        );//esperar a que se llene informacion
        } catch (Exception e) {
            System.out.println("Error al crear el espacio para recibir la infomacion " + e.toString());
            System.exit(2);
        }
        String entrada = "";
        try {
            while ((entrada = lector.readLine()) != null) 
            {
                System.out.println("me dijeron: " + entrada);
            }
        } catch (IOException e) {
            System.out.println("Error al recibir la informacion " + e.toString());
            System.exit(3);
        }
        
        
        try {
            socket.close();
            socketServidor.close();
        } catch (Exception f) {
            System.out.println("Error al cerrar el socket " + f.toString());
            System.exit(4);
        }
        }
    }
    
}
