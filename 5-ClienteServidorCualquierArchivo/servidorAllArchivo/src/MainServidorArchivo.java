import java.net.*;
import java.io.*;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Scanner;
public class MainServidorArchivo 
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
            File archivo = new File(entrada);
            if (archivo.exists()) 
            {
                try {
                    salida = "true";
                        try {
                            escritor.println(salida);
                        } catch (Exception e) {
                            System.out.println("Error al enviar los datos " + e.toString());
                            System.exit(5);
                        }
                    long tamaño = (long)archivo.length();
                    DataOutputStream dos = null;
                    try {
                        dos = new DataOutputStream(socket.getOutputStream());
                    } catch (Exception b) {
                         System.out.println("Error al crear el flujo de salida de datos" + b.toString());
                         System.exit(6);
                    }
                    try {
                        dos.writeUTF(archivo.getName());
                    } catch (Exception bb) {
                        System.out.println("Error al enviar el nombre del archivo" + bb.toString());
                         System.exit(7);
                    }
                    try {
                        dos.writeLong(tamaño);
                    } catch (Exception ee) {
                        System.out.println("Error al enviar el tamaño del archivo" + ee.toString());
                        System.exit(8);
                    }
                    
                    FileInputStream fis = null;
                    try {
                          fis = new FileInputStream(archivo);
                    } catch (Exception fi) {
                        System.out.println("Error al crear el flujo del archivo" + fi.toString());
                        System.exit(9);
                    }
                    BufferedInputStream bis = null;
                    try {
                          bis = new BufferedInputStream(fis);
                    } catch (Exception bi) {
                        System.out.println("Error al crear el tamaño del flujo de entrada" + bi.toString());
                        System.exit(10);
                    }
                    BufferedOutputStream bos = null;
                    try {
                         bos = new BufferedOutputStream(socket.getOutputStream());
                    } catch (Exception bo) {
                        System.out.println("Error al crear el tamaño del flujo de envio de bytes" + bo.toString());
                        System.exit(11);
                    }
                    //si pesa menos de 10000 bytes manda mas pequeños los paquetes
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

                        byte[] buffer;
                        int llenado = 0;
                        int control = 0;
                        try {
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
                                bis.read(buffer);
                                for(int i = 0; i < buffer.length; i++)
                                {            
                                    bos.write(buffer[i]);
                                }
                                llenado = llenado + buffer.length;
                                control++;
                            }
                        } catch (Exception e) {
                            System.out.println("Error mientras se envia el archivo " + e.toString());
                            System.exit(12);
                        }
                    }
                    else
                    {
                        int repartir = (int)(tamaño/10000);
                        String c = String.valueOf(tamaño);
                        int bytesFaltantes =Integer.parseInt(c.substring(c.length() - 4));
                        byte[] buffer;
                        long llenado = 0;
                        int control = 0;
                        String checar = "llegaron";
                        try {
                            while(llenado < tamaño)
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
                                bis.read(buffer);
                                for(int i = 0; i < buffer.length; i++)
                                {            
                                    bos.write(buffer[i]);
                                }
                                bos.flush();
                                llenado = llenado + buffer.length;

                                try {
                                    entrada = lector.readLine();
                                    checar = entrada;
                                } catch (Exception b) {
                                    System.out.println("Error al recibir los datos " + b.toString());
                                    System.exit(20);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Error mientras se envia el archivo " + e.toString());
                            System.exit(12);
                        }
                    }
                    
                    bis.close();
                    bos.close();
 
                } catch (Exception l) {
                    System.out.println("Error al cerrar el flujo de datos" + l.toString());
                    System.exit(13);
                }
            }
            else
            {
                salida = "false";
                try {
                    escritor.println(salida);
                } catch (Exception e) {
                    System.out.println("Error al enviar los datos " + e.toString());
                    System.exit(14);
                }
            }
            try {
                socket.close();
                socketServidor.close();
            } catch (Exception k) {
                System.out.println("Error al cerrar los sockets " + k.toString());
                System.exit(15);
            }
        }
    }
}
