/*
 * Juego UNO - clase main principal.
 * 
 * Realiza la conexión de los sockets...
 * Es el server del juego.
 * 
 */

// Packete uno.
package uno;

// Importaciones.
import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Jason Barrantes Arce 2015048456
 *         David Vargas Rosales 2015128043
 *         Randy Morales Gamboa 2015085446
 * 
 */

// **************************************************************************** //
public class Uno {
    
    // Datos privados de la clase.
    private static boolean comerCarta = false;
    private static String efectoAnterior = " nada";
    private static String jugadorAnterior;
    private static String[] players;
        
    // Sockets
    private static ServerSocket serverUNO;
    private static final int puerto = 9000;        
    private static Socket[] conexionesSocket;
    private static ObjectInputStream[] entradas;
    private static ObjectOutputStream[] salidas;
        
    
    // Main principal de la clase.
    public static void main(String[] args) throws IOException {
        serverUNO = new ServerSocket(puerto);
        Partida juego;
        juego = Partida.getInstance();
        inicializarJuego(juego); 
        
        // Realice el proceso hasta que el juego determine un ganador.
        while(juego.ganador() == false){
            
            int pos = 0;
            for(String jugador: players){ 
                
                if(jugador.equals(juego.obtenerJugadorActual())){
                    
                    salidas[pos].writeObject(juego.reporteJuego(players[pos])+efectoAnterior); // es que es su turno.
                    movimientoJugador(juego,pos);   // El jugador realiza su movimiento.
                    
                    if(!juego.ganador()){
                        jugadorAnterior = juego.obtenerJugadorActual(); // Obtengo al jugador que puso
                                                                        // la carta.
                        juego.siguienteTurno(); // Pasamos de ronda.
                        
                        if (comerCarta == false){  // comi una carta. O realice un salto.
                             mensajeEfecto(juego,pos); // si no se comió carta significa que el efecto de la carta
                                                 // del descarte no debe aplicars
                        }
                        comerCarta = false; // Se puso una carta.
                    }
                }//.
                
                int posTemp = 0;
                for(String jugadorTemp : players){ // Actualice la información a los jugadores.
                    salidas[posTemp].writeObject(juego.reporteActualizacionJuego(players[posTemp])+efectoAnterior);
                    posTemp++;
                }
                pos++;
            }//*               
        }//*
        serverUNO.close();
        System.out.println(" El ganador del juego es el jugador actual. ");
    }//-
    
    
    // El jugador puso una carta y comprueba que el movimiento sea correcto.
    
    public static void movimientoJugador(Partida juego, int posicionSocket) throws IOException{
        
        int posCarta = 0;
        boolean movimientoCorrecto = false;
        
        /*
        / - Recibe una posicion de la carta.
         * Si es -1 : quiere tomar una nueva carta.
         * Si es -2 : quiere saltar el turno.
         * Si no : se seleccionó una carta y debo comprobar si el movimiento es válido.
        */
        
        while(!movimientoCorrecto){
            
            posCarta = verificadorSocket(juego,posicionSocket); // me decide que hacer.
            System.out.println("Carta: " + posCarta);
            
            if(posCarta == -1){ // Comer
                boolean solicitoCartaNueva = juego.nuevaCarta();
                comerCarta = true;  
                salidas[posicionSocket].writeObject(juego.reporteJuego(players[posicionSocket])+efectoAnterior);      
                System.out.println(juego.reporteJuego(players[posicionSocket])+efectoAnterior);
                System.out.println(" El jugador es: " + players[posicionSocket]);
                if (juego.ganador() == true){
                    return;
                }
                
            }else if(posCarta == -2){ // Saltar
                 comerCarta = true;
                 movimientoCorrecto = true;
                 
            }else{   // Seleccionar carta.
                try{
                    boolean move = juego.seleccionarCarta(posCarta); // Elimine la carta, comparela
                    if(move == true){                                // si es positiva valide el movimiento.
                        comerCarta = false;
                        salidas[posicionSocket].writeObject("1"); // Envía al programa que es válida.
                        movimientoCorrecto = true;
                    }else{
                        salidas[posicionSocket].writeObject("0"); // Envía al programa que no fue un movimiento válido.
                    }
                }catch(Exception e){
                    System.out.println(" No puede comer más cartas.");
                }
            }//.
        }//*
    }//-

    
    // Método que me recibe los datos que el usuario decida enviar, le contesto o recibo.
    
    public static int verificadorSocket(Partida juego, int posicion){
        try{
            String mensaje = (String) entradas[posicion].readObject(); // recibe movimiento.
            System.out.println(" Confirmación de lo digitado: ...." + mensaje);
            String[] partitura = mensaje.split(" ");          // divido.
            String accion = partitura[0];
            
            if (accion.equals("1")){   // Si la acción es 1. Se selecciono una carta.
                if (partitura[1].equals("")){
                    partitura[1] = "Undefined";
                }
                System.out.println(" Color: " + partitura[1] + "Carta: " + partitura[2]);
                return juego.escogerCarta(partitura[1],partitura[2]); // Busque la posicion de la carta, en la mano.
                
            }else if(accion.equals("0")){    // Tome una carta.
                return -1;
                
            // hay un error, porque se envían dos veces... para esto atrape el error y solicite los datos nuevamente.
            }else if (accion.equals("Rojo") || accion.equals("Verde") || accion.equals("Azul") || accion.equals("Amarillo")){
                salidas[posicion].writeObject(juego.reporteJuego(players[posicion])+efectoAnterior);
                return verificadorSocket(juego, posicion);
            }
        }catch(Exception e){
            System.out.println(" NO funciona al tomar la carta de seleccionar.");
        }
        return -2;// Si no es un salto.
    }//-
    
    public static void inicializarJuego(Partida juego) throws IOException{
        
        //Se crean los jugadores indicados y su conexion
        solicitarJugadoresSockets();
        
        //Registrar jugadores en el sistema
        for (int i=0; i<players.length; i++) {
            juego.registrarJugador(players[i]);
        }//*
        jugadorAnterior = players[0];
        //Inicia la logica del juego
        juego.empezarJuego(conexionesSocket, entradas, salidas, players);
        
        //Los jugadores tienen sus cartas y el descarte
        mensajeEfecto(juego,1);
    }
    
    // Realiza el efecto de la carta en el descarte.
    public static void mensajeEfecto(Partida juego, int posSocket){
        int efecto;
        int pos = 0;
        String actual = juego.obtenerJugadorActual();
        
        for (int i=0; i < players.length; i++){
            if (players[i].equals(actual)){ // Obtenga la posicion del jugador en el arreglo de socket.
                posSocket = i;
            }
            if (players[i].equals(jugadorAnterior)){  // Obtenga la posición del jugador que puso la carta.
                pos = i;
            }
        }//*
        try{
            efecto = juego.ejecutarAccion();   // Recibo un mensaje que interpreto de la carta.
            String affect = "";
               switch(efecto){
                   
                    case -1: // Carta numérica.
                        break;
                        
                    case 1: // Carta de salto.
                        affect = juego.reporteActualizacionJuego(players[posSocket]);
                        affect += " salto";
                        salidas[posSocket].writeObject(affect);
                        break;
                        
                    case 2: // Carta reversa.
                        for(int i = 0; i < players.length; i++){ // Reporte a todos los jugadores.
                            affect = juego.reporteActualizacionJuego(players[i]);
                            affect += " reversa";
                            salidas[i].writeObject(affect);
                        }
                        System.out.println(" Se activo la reversa.");
                        break;
                        
                    case 3: // Carta Tome dos Cartas.
                        affect = juego.reporteActualizacionJuego(players[posSocket]);
                        affect += " TomeDos";
                        salidas[posSocket].writeObject(affect);
                        System.out.println(" Se puso un tome Dos, se salto al jugador.");
                        break;
                        
                    case 4: // Carta Tome cuatro cartas.
                        affect = juego.reporteActualizacionJuego(players[posSocket]);
                        affect += " TomeCuatro";
                        salidas[posSocket].writeObject(affect);
 
                    case 5: // Carta Seleccione el color.
                        affect = juego.reporteActualizacionJuego(players[pos]);
                        affect += " SeleccionaColor";
                        System.out.println(" Estoy enviando...");
                        salidas[pos].writeObject(affect);
                        System.out.println(" Estoy esperando... ");
                        String pColor = (String) entradas[pos].readObject();
                        juego.seleccionarColor(pColor);
                        break;

                    default:
                        System.out.println(" La carta no posee ningún formato de efecto."); 
               }
        }catch(Exception e){
            System.out.println(" Ocurre una excepción al tomar la carta.");
        }
    }//-
    
   
        
    // Solicito al usuario la cantidad de jugadores que van a jugar.
    public static Socket[] solicitarJugadoresSockets(){
        // Los jugadores deben ser minimo 2, máximo 4.
        int cantJugadores = 1;
        boolean verdad = false;
        Scanner numeroJugadores;
        while(!verdad){
            System.out.print(" Cuantos jugadores desea: ");
            // Lea de consola lo digitado por el usuario.
            numeroJugadores = new Scanner(System.in);
            cantJugadores = numeroJugadores.nextInt();
            players = new String[cantJugadores];
            try{
                
                Socket jugadorConectado;
                conexionesSocket = new Socket[cantJugadores];
                String nombreJugador;
                
                entradas = new ObjectInputStream[cantJugadores];
                salidas = new ObjectOutputStream[cantJugadores];
                
                System.out.println("Esperando la conexion de " + cantJugadores + " jugadores.");
                for (int i = 0; i < cantJugadores; i++) {
                    jugadorConectado = serverUNO.accept();
                    conexionesSocket[i] = jugadorConectado;
                    entradas[i] = new ObjectInputStream (conexionesSocket[i].getInputStream());
                    salidas[i] = new ObjectOutputStream (conexionesSocket[i].getOutputStream());
                                        
                    //Solicitar nombre jugador
                    System.out.println("Llego la conexion numero: " + i);
                    nombreJugador = (String) entradas[i].readObject();
                    salidas[i].writeObject("Hola " + nombreJugador + " !\n Espere a que se conecten los jugadores restantes...");
                    //System.out.println(entradas[i].readObject()); //para leer el null
                    players[i] = nombreJugador;
                    System.out.println("Jugador " + i + " conectado y se llama: " + players[i]);
                    
                }
                
            // Si no es formato String.
            }catch(Exception e){
                System.out.println("");
                System.out.println(" Hay un error con lo digitado");
            }
            System.out.println("");
            // Si la cantidad de jugadores es 2 a 4.
            if (cantJugadores > 1 && cantJugadores < 5){
                verdad = true;
            }else{
                System.out.println(" La cantidad no es válida ");
            }
        }//*
        return conexionesSocket; 
    }//- Socket
    
}//-
