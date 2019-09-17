import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Principal 
{
    public static void main(String[] args) 
    {
        int puerto = 0; 
        ServerSocket socketServidor = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);  
        String datosEntrada = "";
        String datos = "";
        try {
            System.out.print("Escribe el puerto del servidor: ");
            puerto = Integer.parseInt(scanner.nextLine());
        } catch (Exception x) {
            System.out.print("Error al ingresar el puerto " + x.toString());
            System.exit(0);
        }
        
        try {
            socketServidor = new ServerSocket(puerto); //recibir informacion
            System.out.println("Esperando conexion con el cliente...");
            socket = socketServidor.accept();         //recibir informacion, el socket es como una tuberia
        } catch (Exception a){
            System.out.println("Error al asignar un puerto al servidor " + a.toString());
            System.exit(1);
        }finally
        {
            System.out.println("¡Conexion exitosa!");
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
        while(true)
        {
            try 
                {
                  datosEntrada = lector.readLine();
                  System.out.println(datosEntrada);
                } catch (Exception l) {
                    System.out.println("Error al recibir los datos " + l.toString());
                    System.exit(3);
                }
            PrintWriter escritor = null;
            try{
            escritor = new PrintWriter(socket.getOutputStream(),true);
            System.out.print("Escribe el mensaje: ");
            datos = scanner.nextLine();
            escritor.println(datos); //para leer lo que escribas en el cmd y sea como el mensaje
            }
            catch(IOException e)
            {
               System.out.println("Error al mandar info" + e.toString());
               System.exit(2);
            }
            
        }
    }
}
