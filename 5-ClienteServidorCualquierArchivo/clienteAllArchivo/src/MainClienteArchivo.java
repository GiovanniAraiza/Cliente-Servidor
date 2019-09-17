import java.net.*;
import java.io.*;
import java.util.Scanner;
import sun.net.util.IPAddressUtil;
public class MainClienteArchivo 
{
    public String ruta()
    {
        URL link = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        return link.toString();
    }
    
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
        //para enviar el archivo solicitado solicitado
            try {
                escritor = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("Error al mandar info " + e.toString());
                System.exit(5);
            }
            try {
                
                System.out.print("Escribe la ruta con el nombre del archivo a pasar: ");
                datos = scanner.nextLine();
                escritor.println(datos); //para leer lo que escribas en el cmd y sea como el mensaje
            } catch (Exception e) {
                System.out.println("Error al mandar info " + e.toString());
                System.exit(5);
            }
            
            try 
                {
                  datosEntrada = lector.readLine();
                } catch (Exception ll) {
                    System.out.println("Error al recibir los datos " + ll.toString());
                    System.exit(6);
                }
                if(datosEntrada.equalsIgnoreCase("true"))
                {
                    //calcular ruta del proyecto
                    MainClienteArchivo camino = new MainClienteArchivo();
                    String ruta = camino.ruta();
                    int discoDuro = ruta.indexOf(":") + 2;
                    int nombreCarpeta = ruta.indexOf("clienteAllArchivo") + 18;
                    String rutaFinal = ruta.substring(discoDuro, nombreCarpeta);
                    DataInputStream dis = null;
                    try {
                        dis = new DataInputStream(socket.getInputStream());
                    } catch (IOException e) {
                        System.out.println("Error al crear el flujo de data " + e.toString());
                        System.exit(7);
                    }
                    String nombreArchivo = ""; 
                    try 
                    {
                        nombreArchivo = dis.readUTF().toString();
                    } catch (Exception l) {
                        System.out.println("Error al recibir los datos del dis " + l.toString());
                        System.exit(8);
                    }
                    long tamaño = 0;
                    try 
                    {
                      tamaño = dis.readLong();
                    } catch (Exception l) {
                        System.out.println("Error al recibir los datos " + l.toString());
                        System.exit(9);
                    }
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream( rutaFinal + nombreArchivo );
                    } catch (Exception fo) {
                        System.out.println("Error al crear el flujo de salida al archivo " + fo.toString());
                        System.exit(10);
                    }
                    BufferedOutputStream out = null;
                    try {
                        out = new BufferedOutputStream(fos);
                    } catch (Exception ou) {
                        System.out.println("Error al crear el tamaño de salida hacia el archivo " + ou.toString());
                        System.exit(11);
                    }
                    BufferedInputStream in =  null;
                    try {
                        in = new BufferedInputStream(socket.getInputStream() );
                    } catch (Exception inn) {
                        System.out.println("Error al crear el tamaño de de entrada del socket " + inn.toString());
                        System.exit(12);
                    }
                    //si pesa menos de 10000 bytes recibe mas pequeños los paquetes
                    if(tamaño < 10000)
                    {
                        //dividir el tamaño del archivo en 100
                        double c1 = (double)tamaño/100;
                        //multiplicar por 100 para tomar 2 decimales
                        double c2 = c1*100;
                        //tomar la parte entera ya con 2 decimales
                        int c3 = (int)c2;
                        //volver a dividir la parte entera para tomar solo los 2 decimales
                        double redondear = (double)c3/100;
                        //float para que la resta sea exacta
                        float decimal = (float)(redondear - ((int)tamaño/100));
                        //calcula cuantos Bytes faltan 
                        double byFaltantes = decimal * 100;
                        //calcula solo la parte entera para tomarla en el buffer
                        int repartir = (int)tamaño/100;

                        int control = 0;
                        byte[] buffer = null;
                        try {
                            int llenado = 0;
                            System.out.print("Descargando archivo... 0%");
                            while(llenado < tamaño)
                            {
                                if(control == 0)
                                {
                                    buffer = new byte[repartir + (int)byFaltantes];
                                }
                                else
                                {
                                    buffer = new byte[repartir];
                                }
                                for(int i = 0; i < buffer.length;i++)
                                {
                                    buffer [i] = (byte)in.read();
                                }
                                try {
                                    out.write( buffer ); 
                                } catch (IOException e) {
                                }
                                llenado = llenado + buffer.length;
                                control++;
                                System.out.print("\r");
                                System.out.print("Descargando archivo... "+control+"%");
                            }
                        } catch (IOException e) {
                            System.out.println("Error al descargar el archivo " + e.toString());
                            System.exit(13);
                        }
                    }
                    else
                    {
                        int repartir = (int)(tamaño/10000);
                        String c = String.valueOf(tamaño);
                        int bytesFaltantes =Integer.parseInt(c.substring(c.length() - 4));
                        
                        int control = 0;
                        int porcentaje = 0;
                        int llenar = 0;
                        byte[] buffer = null;
                        try {
                            long llenado = 0;
                            System.out.print("Descargando archivo... 0%");
                            while(llenado < tamaño )
                            {
                                if(control == 0)
                                {
                                    buffer = new byte[repartir + (int)bytesFaltantes];
                                    control++;
                                }
                                else
                                {
                                    buffer = new byte[repartir];
                                }
                                for (int i = 0; i < buffer.length; i++) 
                                {
                                    buffer[i] = (byte) in.read();
                                }

                                try {
                                    out.write( buffer );
                                } catch (IOException el) {
                                    System.out.println("Error al grabar el archivo " + el.toString());
                                    System.exit(13);
                                }


                                llenado = llenado + buffer.length;
                                llenar++;
                                if(llenar == 100)
                                {
                                    porcentaje++;
                                    System.out.print("\r");
                                    System.out.print("Descargando archivo... "+porcentaje+"%");
                                    llenar = 0;
                                }
                                System.out.print("\r");
                                String llegaron = "llegaron";
                               try {
                                    escritor.println(llegaron); 
                                } catch (Exception em) {
                                    System.out.println("Error al mandar info " + em.toString());
                                    System.exit(14);
                                }
                               out.flush();


                            }
                        } catch (IOException e) {
                            System.out.println("Error al descargar el archivo " + e.toString());
                            System.exit(15);
                        }
                    }
                    System.out.print("\r");
                    System.out.println("Descargando archivo... "+100+"%");
                    System.out.print("Archivo Descargado");
                    try {
                        
                        in.close();
                        out.close();
                    } catch (Exception em) {
                        System.out.println("Error al cerrar los flujos " + em.toString());
                        System.exit(16);
                    }
                    
                }
                else
                {
                    System.out.println("El archivo no existe");
                }
            
            try {
            socket.close();
            } catch (Exception t) {
                System.out.println("Error al cerrar socket " + t.toString());
                System.exit(17);
            }
    }
    
}
