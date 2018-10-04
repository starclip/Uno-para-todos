/*
 *
 * Clase cliente que permite al programa comunicarse con la interfaz.
 * Es el socket de recibo - comunica al programa uno.
 *
 */

/// Packete Cliente.
package cliente;

/// Datos importados del paquete Interfaz.

import interfaz.BienvenidoUno;
import interfaz.ComerActivado;
import interfaz.ComerCuatroActivado;
import interfaz.Ganador;
import interfaz.InterfazGrafica;
import interfaz.MensajeBienvenida;
import interfaz.OpcionesColores;
import interfaz.ReversaActivada;
import interfaz.SaltoActivado;
import java.applet.AudioClip;


/// Comunicadores de la interfaz - Cliente.

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/// Datos importados de Sockets.

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/// Importación de Arreglo

import java.util.ArrayList;


/**
 *
 * @authors
 *          Jason Barrantes Arce - 2015048456
 *          Randy Morales Gamboa - 2015085446
 *          David Vargas Rosales - 2015128043
 * 
 */



public class Cliente {

    // Nombran las variables.
    
    // Ventanas:
    
    private static InterfazGrafica interfaz;
    private static MensajeBienvenida ventanaInicial;
    private static OpcionesColores ventanaColores;
    private static ReversaActivada ventanaReversa;
    private static SaltoActivado ventanaSaltar;
    private static ComerActivado ventanaComer;
    private static ComerCuatroActivado ventanaComeCuatro;
    private static ActionListener listener;
    private static BienvenidoUno ventanaBienvenida;
    private static Ganador ventanaFinal;
    // ventana ganador....
    
    // Datos:
    
    private static String[] palabra;
    private static int posPalabra = 0;
    private static String jugadorPrincipal;
    private static int cantidadCartasActual;
    private static int cantidadOponentes = 0;
    private static boolean error = false;
    
    
    // Sockets...
    private static ObjectInputStream entrada;
    private static ObjectOutputStream salida;
    static OutputStream SocketStream;
    static InputStreamReader SocketInputStream;
    static InputStream normal;
    private static Socket clienteSocket; //socket 
    private static final int puerto = 9000; 
    private static String IPServer;
    
    //********************************************************************************************//
    
    /// Realizo la lógica del juego.
   
    // Método que me informa que el juego ha finalizado.
    public static void ventanaGanador(){
        ventanaFinal = new Ganador();
        ventanaFinal.setVisible(true);
        ventanaFinal.setResizable(false);
        ventanaFinal.setSize(800,800);
        ventanaFinal.pack();
        ventanaFinal.setLocationRelativeTo(null);
        encriptacionFinal();
        ventanaFinal.actualizarPantalla();
    }
    
    
    // Método que me informa que se activo la reversa.
    public static void activarReversa(){
        ventanaReversa = new ReversaActivada(listener);
        ventanaReversa.setVisible(true);
        ventanaReversa.setResizable(false);
        ventanaReversa.setSize(500,800);
        ventanaReversa.pack();
        ventanaReversa.setLocationRelativeTo(null);
        ventanaReversa.actualizarPantalla();
    }//-
    
    // Función que me informa que se activo la carta salto en mi contra.
    public static void activarSalto(){
        ventanaSaltar = new SaltoActivado(listener);
        ventanaSaltar.setVisible(true);
        ventanaSaltar.setResizable(false);
        ventanaSaltar.setSize(630,357);
        ventanaSaltar.pack();
        ventanaSaltar.setLocationRelativeTo(null);
        ventanaSaltar.actualizarPantalla();
    }//-
    
    // Función que me informa que escoja un color.
    public static void seleccionarColor(){
        ventanaColores = new OpcionesColores(listener);
        ventanaColores.setVisible(true);
        ventanaColores.setSize(626,411);
        ventanaColores.setResizable(false);
        ventanaColores.pack();
        ventanaColores.setLocationRelativeTo(null);
        ventanaColores.actualizarPantalla();
    }//-
    
    // Función que me informa que debo comer dos cartas.
    public static void comerCarta(){
        ventanaComer = new ComerActivado(listener);
        ventanaComer.setVisible(true);
        ventanaComer.setResizable(false);
        ventanaComer.setSize(450,450);
        ventanaComer.pack();
        ventanaComer.setLocationRelativeTo(null);
        ventanaComer.actualizarPantalla();
        
    }//-
    
    // Función que me informa que debo comer cuatro cartas.
    public static void comerCuatroCartas(){
        ventanaComeCuatro = new ComerCuatroActivado(listener);
        ventanaComeCuatro.setVisible(true);
        ventanaComeCuatro.setResizable(false);
        ventanaComeCuatro.setSize(450,450);
        ventanaComeCuatro.pack();
        ventanaComeCuatro.setLocationRelativeTo(null);
        ventanaComeCuatro.actualizarPantalla();
    }//-
            
    // Función que crea las cartas del juego.
    public static void crearCartas(InterfazGrafica interfaceGrafica){
        
        String palabraConvertirEntero = palabra[posPalabra++];
        int cantidadCartas = Integer.parseInt(palabraConvertirEntero);
        cantidadCartasActual = cantidadCartas;
        System.out.println(" Cantidad de Cartas: "+cantidadCartas + " ");
        String color;
        String nombre;
        for(int i = 0; i < cantidadCartas; i++){ // Cree todas las cartas que le enviaron.
           color = palabra[posPalabra++];
           if(color.equals("Undefined")){
               color = "";
           }
           nombre = palabra[posPalabra++]; 
            System.out.print(" Carta: " + nombre + " " + color);
           interfaceGrafica.crearCartas(color, nombre);
        }
    }//-
    
    // Función que me indica que debo escoger un efecto de la carta aplicada a mi.
    public static void seleccionarEfecto(){
        
        String efecto = palabra[palabra.length-1];
        System.out.println("*************");
        switch(efecto){
            
            case "salto": 
                // Invocó la ventana de salto.
                System.out.println(" Te saltaron");
                activarSalto();
                break;
                
            case "reversa":
                // Invocó la ventana de reversa.
                System.out.println(" Se aplico la reversa para todos");
                activarReversa();
                break;
                
            case "TomeDos":
                // Invoco la ventana de TomeDos.
                System.out.println(" Come dos cartas");
                comerCarta();
                break;
                
            case "TomeCuatro":
                // Invoco la ventana de TomeCuatro.
                System.out.println(" Come cuatro cartas.");
                comerCuatroCartas();
                break;
                
            case "SeleccionaColor":
                // Invoco la ventana de Seleccione Color.
                seleccionarColor();
                System.out.println(" Seleccione el color.");
                return;
                
            default:
                System.out.println(" No se registra carta que afecte su instancia.");
                finalizarJuego();
                error = true;
        }
        actualizarJuego();
    }//-
    
    // Función que me crea los jugadores del juego.
    public static void crearJugadores(InterfazGrafica interfaceGrafica){
        interfaceGrafica.configurarNombre(jugadorPrincipal);// Jugador Principal.
        String cantidadNombres = palabra[posPalabra++];
        int numero = Integer.parseInt(cantidadNombres);
        
        for (int i = 0; i < numero; i++){       // Cree los rivales.
            String nombreJugador = palabra[posPalabra++];
            String imagen = palabra[posPalabra++];
            if (nombreJugador.equals(jugadorPrincipal)){
                //nothing
            }else{
                interfaceGrafica.crearRivales(nombreJugador,imagen);
                cantidadOponentes++;
            }
        }
    }//-
    
    
    public static void encriptacionFinal(){
        String oponente;
        String cartasOponente;
        String ganador;
        int posPalabra = 2;
        if (error == true){
            ventanaFinal.setGanador(jugadorPrincipal);
            ventanaFinal.setJugador(jugadorPrincipal,"0");
            return;
        }
        ganador = palabra[posPalabra++];
        for(int i=0; i < cantidadOponentes; i++){
            oponente = palabra[posPalabra++];
            cartasOponente = palabra[posPalabra++];
            if (i== 0){
                ventanaFinal.setJugadorUno(oponente, cartasOponente);
            }else if(i == 1){
                ventanaFinal.setJugadorDos(oponente, cartasOponente);
            }else{
                ventanaFinal.setJugadorTres(oponente, cartasOponente);
            }
        }
        cartasOponente = Integer.toString(cantidadCartasActual);
        ventanaFinal.setGanador(ganador);
        ventanaFinal.setJugador(jugadorPrincipal, cartasOponente);
    }
    
    // Función que me finaliza el juego.
    public static void finalizarJuego(){
        try{
           clienteSocket.close(); 
        }catch(Exception e){
            System.out.println(" Me es imposible cerrar el socket.");
        }
        interfaz.autoDestruccion();
        ventanaGanador();
    }//-
    
    // Función que me divide lo que me envío el socket.
    public static void encriptacion(){
        String oponente;
        String cartasOponente;
        String cartaDescarte;
        String colorJuego;
        System.out.print("  #Rivales : " + cantidadOponentes + " ");
        for(int i=0; i < cantidadOponentes; i++){
            oponente = palabra[posPalabra++];
            cartasOponente = palabra[posPalabra++];
            System.out.print(" Oponente: " + oponente + " " + cartasOponente);
            interfaz.cambiarNumeroCartas(oponente, cartasOponente);
        }
        colorJuego = palabra[posPalabra++];
        cartaDescarte = palabra[posPalabra++];
        System.out.print(" Carta en el descarte: " + colorJuego + " " + cartaDescarte + " ");
        interfaz.clear();
        interfaz.seleccionarColor(colorJuego);
        interfaz.cambiarImagenDescarte(colorJuego, cartaDescarte);
        crearCartas(interfaz);
    }//-
    
    
    // Función que me actualiza los datos. Es un mensaje informativo, por la actividad del turno.
    public static void actualizarDatos(){
      
        posPalabra = 0;
        interfaz.actualizarPantalla();
        if (palabra[posPalabra++].equals("1")){
            finalizarJuego();
            return;
        }
        posPalabra++;
        String jugadorActual = palabra[posPalabra++];
        interfaz.turnoJugador(jugadorActual);
        System.out.print(" Jugador Actual : " + jugadorActual);
        encriptacion();
    }//-
    
    
    // Función que recibe los datos del server.
    public static void actualizarJuego(){
        String Datos = "";  
        try{
            posPalabra = 0;
            interfaz.actualizarPantalla();
            Datos = (String) entrada.readObject(); // recibe.
        }catch(Exception e){
            System.out.println(" Hay un fallo en la comunicación.");
        }
        
        palabra = Datos.split(" "); // Divide los datos en un arreglo.
        
        if(palabra[posPalabra++].equals("1")){ // Si se finalizo el juego.
            finalizarJuego();
            return;
        }
        
        if (palabra[palabra.length-1].equals("nada")){ // Si no hay efecto de una carta.
            System.out.println(" No hay efecto.");
        }else{
            System.out.println(" Si hay efecto.");
            System.out.println(" Efecto : " + palabra[palabra.length-1]);
            seleccionarEfecto();
            return;
        }
        
        if (palabra[posPalabra++].equals("1")){ // Si el mensaje es de actualizacion de informacion.
            actualizarDatos();
            actualizarJuego();
            return;
        }
        
        String jugadorTurno = palabra[posPalabra++];
        interfaz.turnoJugador(jugadorTurno);
        // Si soy el jugador actual.
        if (jugadorTurno.equals(jugadorPrincipal)){
            encriptacion();
            interfaz.enable(true);
            interfaz.actualizarPantalla();

        }else{
            encriptacion();
            actualizarJuego();
        }
        
    }
    
    public static void cartaDescarte(){
        // Inicializo la imagen del descarte.
        
        try{
            String Datos;
            Datos = (String) entrada.readObject(); // lea
            System.out.println(Datos);
            String[] descarte = Datos.split(" ");
            if(descarte[0].equals("Undefined")){ // Es un color no registrado, no tendrá nombre.
                descarte[0] = "";
            }
            interfaz.cambiarImagenDescarte(descarte[0], descarte[1]); // Ponga la imagen del descarte.
            
        }catch(Exception e){
            System.out.println(" Hay error");
        }
    }//-
    
    
    public static void iniciarJuego(){
        // Inicializo la interfaz principal.
        
        interfaz = new InterfazGrafica(listener);
        interfaz.setLocationRelativeTo(null); //Se coloca en el medio de la pantalla.
        interfaz.setVisible(true);
        interfaz.setSize(1200,720);
        interfaz.setResizable(false);  
        crearCartas(interfaz); // Reparto las cartas.
        crearJugadores(interfaz); // Creo a los jugadores.
        cartaDescarte();          // Recibo la carta del descarte.
        
        interfaz.enable(false);
        actualizarJuego();      // Invoco el llamado de iniciar juego.
        interfaz.ActualTurno();
        interfaz.enable(true);
        interfaz.actualizarPantalla();
    }//-
    
    
    
    public static void crearSocket(){
        /// Creo los sockets, recibo el mensaje de Bienvenida.

        try{
            clienteSocket = new Socket(IPServer,puerto); 
            SocketStream = clienteSocket.getOutputStream();
            normal = clienteSocket.getInputStream();
            salida = new ObjectOutputStream(SocketStream); // Con este envio mensajes.
            entrada = new ObjectInputStream(normal);       // Con este recibo mensajes.
            String message;
            salida.writeObject(jugadorPrincipal);          // Envío al jugador Principal.
            message = (String) entrada.readObject();
            System.out.println(message);                   // Recibo el mensaje de bienvenida.
            String cartasRecibidas = (String) entrada.readObject(); // Recibo las cartas.
            palabra = cartasRecibidas.split(" ");  
            
            
        }catch(Exception e){
            System.out.println(" El socket no fue creado correspondientemente.");
        } 
        iniciarJuego();
    }//-
    
    
    
    // Solicito digitar la IP y el nombre.
    public static void digitarIP(){
        ventanaInicial = new MensajeBienvenida(listener);
        ventanaInicial.setVisible(true);
        ventanaInicial.setResizable(false);
        ventanaInicial.setSize(400,400);
        ventanaInicial.pack();
        ventanaInicial.setLocationRelativeTo(null); 
    }//-
    
    
    public static void mensajeBienvenida(){
        ventanaBienvenida = new BienvenidoUno(listener);
        ventanaBienvenida.setVisible(true);
        ventanaBienvenida.setResizable(false);
        ventanaBienvenida.setSize(1000,800);
        ventanaBienvenida.pack();
        ventanaBienvenida.setLocationRelativeTo(null);
    }
    
    /**
     * Main del programa
     * 
     */
    
    public static void main(String[] args) {
        listener = new listener();
        mensajeBienvenida();
    }
    
    //******************************************************************************************************************//
    
    // ActionListener ... recibe acciones de los botones de la interfaz.
    
    public static class listener implements ActionListener{
        
        private AudioClip sonidoTriunfal = java.applet.Applet.newAudioClip(getClass().getResource("/Musica/Crash-good.wav"));
        private AudioClip sonidoFracaso = java.applet.Applet.newAudioClip(getClass().getResource("/Musica/Crash-moveInc.wav"));;

        // Realiza la acción de todos los eventos de los botones.
        @Override
        public void actionPerformed(ActionEvent e) {
            Object objeto = e.getActionCommand(); // El evento fue realizado por el botón.
            
            if(objeto.equals("Seleccionar")){ // Si el botón fue seleccionar.
                try{
                    interfaz.bloquearTodo(); // Bloquee todo el juego.
                    String color = interfaz.getColorSelect();
                    String carta = interfaz.getCartaSelect();
                    String nombre = "1 " + color + " " + carta;
                    salida.writeObject(nombre); // Envie la carta al servidor.
                    
                    nombre = (String) entrada.readObject(); // Reciba si fue correcta o no.
                    if (!nombre.equals("1")){                // Si no es correcto. Agregue las cartas a la mano.   
                        // Si no almacene la carta y vuelva a intentarlo.
                        sonidoFracaso.play();
                        interfaz.crearCartas(color, carta);
                        interfaz.ActualTurno();
                        interfaz.enable(true);
                        interfaz.actualizarPantalla();
                        return;
                    }else{
                        sonidoTriunfal.play();
                    }
                }catch(Exception o){
                    System.out.println(" Al seleccionar cartas, no funca."); 
                }   
            }//*|*//
            
            if(objeto.equals("NuevaCarta")){ // Si el botón fue nuevaCarta.
                interfaz.setBloqueoTurno(); // Desbloqueo el botón de paso.
                interfaz.setBloqueoCarta();  // Bloqueo el de pedir más cartas.
                try{
                    salida.writeObject("0");  // Solicito una nueva carta.
                }catch(Exception w){
                    System.out.println(" Hay un error al solicitar una nueva carta.");
                }   
                interfaz.actualizarPantalla();
                interfaz.enable(true);
                actualizarJuego();
                return;            
            }//*|*//
            
            if(objeto.equals("Paso")){   // Si me salto el turno.
                System.out.println(" Estoy haciendo un salto.");
                try{
                    salida.writeObject("55");
                }catch(Exception y){
                    System.out.println("sintax Error");
                }  
            }//*|*//
            
            if(objeto.equals("Start")){ // Inicie el juego.
                String nombre;
                nombre = ventanaInicial.obtenerNombre();
                String[] lista = nombre.split(" ");
                nombre = "";
                for (String i: lista){
                    nombre += i;
                }
                jugadorPrincipal = nombre;
                IPServer = ventanaInicial.obtenerIP();
                ventanaInicial.dispose();
                crearSocket();
                return;
            }//*|*//
            
            // Si el boton es Amarillo.
            if(objeto.equals("Amarillo")){
                try{
                   salida.writeObject("Amarillo"); // Seleccione el color amarillo.
                }catch(Exception mil){
                    System.out.println(" No se puede realizar la acción de selección de color.");
                }
                ventanaColores.dispose();
                actualizarJuego();
                return;
            }//*|*//
            
            // Si el boton es Azul.
            if(objeto.equals("Azul")){
                try{
                   salida.writeObject("Azul"); // Seleccione el color azul.
                }catch(Exception mil){
                    System.out.println(" Soy un inutil color");
                }
                ventanaColores.dispose();
                actualizarJuego();
                return;
            }//*|*//
            
            // Si el boton es Rojo.
            if(objeto.equals("Rojo")){
                try{
                   salida.writeObject("Rojo");
                }catch(Exception mil){
                    System.out.println(" Soy un inutil color");
                }
                ventanaColores.dispose();
                actualizarJuego();
                return;    
            }//*|*//
            
            // Si el boton es Verde.
            if(objeto.equals("Verde")){
                try{
                   salida.writeObject("Verde");
                }catch(Exception mil){
                    System.out.println(" Soy un inutil color");
                }
                ventanaColores.dispose();
                actualizarJuego();
                return;
            }//*|*//
            
            if (objeto.equals("REVERSA")){
                ventanaReversa.dispose();
                return;
            }//*|*//
            
            if (objeto.equals("Confirmar")){
                ventanaSaltar.dispose();
                return;     
            }//*|*//
            
            if (objeto.equals("comer")){
                ventanaComer.dispose();
                return;
            }//*|*//
            
            if (objeto.equals("...")){
                ventanaComeCuatro.dispose();
                return;
            }//*|*//
            
            if (objeto.equals("Acepto")){
                ventanaBienvenida.dispose();
                digitarIP();
                return;
            }
            
            // Permita la interfaz desactivarse y solicitar jugador.
            interfaz.enable(false);
            interfaz.bloquearTodo();
            actualizarJuego();
            interfaz.ActualTurno();
            interfaz.enable(true);
        }
    }//-
    
}//-
