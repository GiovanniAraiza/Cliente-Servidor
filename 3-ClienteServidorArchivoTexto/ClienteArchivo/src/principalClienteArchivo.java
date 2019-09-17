import java.net.*;
import java.io.*;
import java.util.Scanner;
import sun.net.util.IPAddressUtil;

public class principalClienteArchivo 
{
    public String ruta()
    {
        URL link = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        return link.toString();
    }
    
    public static void main(String[] args) throws Exception
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
        
            try {
                escritor = new PrintWriter(socket.getOutputStream(), true);
                System.out.print("Escribe la ruta con el nombre del archivo: ");
                datos = scanner.nextLine();
                escritor.println(datos); //para leer lo que escribas en el cmd y sea como el mensaje
            } catch (IOException e) {
                System.out.println("Error al mandar info " + e.toString());
                System.exit(5);
            }
            
            int arch = datos.length() - (datos.indexOf(".") + 1);
            if((arch == 3 || arch == 4) && datos.contains("."))
            {   
                try 
                {
                  datosEntrada = lector.readLine();
                } catch (Exception l) {
                    System.out.println("Error al recibir los datos " + l.toString());
                    System.exit(6);
                }
                if(datosEntrada.equalsIgnoreCase("true"))
                {
                    //calcular ruta del proyecto
                    principalClienteArchivo camino = new principalClienteArchivo();
                    String ruta = camino.ruta();
                    int discoDuro = ruta.indexOf(":") + 2;
                    int nombreCarpeta = ruta.indexOf("ClienteArchivo") + 15;
                    String rutaFinal = ruta.substring(discoDuro, nombreCarpeta);
                    
                    try 
                    {
                      datosEntrada = lector.readLine();
                    } catch (Exception l) {
                        System.out.println("Error al recibir los datos " + l.toString());
                        System.exit(7);
                    }
                    //creacion de archivo y escritura de datos
                    FileWriter file = new FileWriter(rutaFinal + datosEntrada);
                    PrintWriter pw = new PrintWriter(file);
                    try {
                           
                           while((datosEntrada = lector.readLine()).equalsIgnoreCase("true"))
                            {
                                datosEntrada = lector.readLine();
                                pw.println(datosEntrada);
                            }   
                    } catch (Exception e) {
                        file.close();
                        System.out.println("Error en la creacion del archivo" +e.toString());
                        System.exit(8);
                    }
                        file.close();
                        socket.close();
                        System.exit(9);
                        
                }
            }
    }
}

