/* 
 *****************************************************************************
 * Clase que realiza la lógica del juego.
 *
 * - Realiza toda la lógica del juego, comunica con la interfaz de consola.
 * 
 *****************************************************************************
 */

// Importo el paquete
package uno;

// Importo los modulos
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


/**
 *
 * @author Jason Barrantes Arce 2015048456
 *         David Vargas Rosales 2015128043
 *         Randy Morales Gamboa 2015085446
 * 
 */

// Clase Partida
public class Partida {
    
    // Datos 
    private static Partida instance = null;
    private ArrayList<Jugador> jugadores;
    private ArrayList<Cartas> pilaDescarte;
    private HashMap<String,String> imagenes;
    private GeneradorCartas Mazo;
    private boolean reversa;
    private String colorActivo;
    private String nombreJugador;
    private int tamanoMano;
    private int posJugadorActual;
    private String ultimaCarta;
    private Jugador jugadorGanador;
    private Jugador jugadorActual;
    
    // Estos sockets no deberían en la Partida ....
    private static Socket[] jugadoresSocket;
    private static ObjectInputStream[] entradas;
    private static ObjectOutputStream[] salidas;
    private static String[] players;
        
    private Partida(){
        jugadores = new ArrayList();
        pilaDescarte = new ArrayList();
        Mazo = new GeneradorCartas();
        imagenes = new HashMap<>();
        reversa = false;
        colorActivo = "Undefined";
        nombreJugador = "Undefined";
        tamanoMano = 0;
        posJugadorActual = 0;
        ultimaCarta = "Undefined";
        jugadorGanador = null;
    }
   
    
    private void pierdeTurno() throws IOException{
        siguienteTurno();     
        nombreJugador = jugadorActual.getNombre();
    }
    
    private void cambioSentido() throws IOException{
        reversa = !reversa;
        pierdeTurno();
    }
    
    private void escogerColor(String pColor){
        colorActivo = pColor;
    }
    
    private void darDosCartas(){
        for (int i = 0; i < 2; i++){
            darCartaJugador();
        }
    }
    
    // Verifica si el mazo tiene cartas./
    private boolean verificarMazo(){
        if (Mazo.numeroCartas() == 0){
            return false;
        }else{
            return true;
        }
    }
    
    private void llenarMazo(){
        int pos;
        Cartas Carta;
        Cartas CartaGuardar;
        
        pos = pilaDescarte.size()-1;
        CartaGuardar = pilaDescarte.remove(pos);
        
        // Revuelvo la pila.
        Collections.shuffle(pilaDescarte);
        
        //Elimine las cartas de la pila hasta que quede vacía.
        while(!pilaDescarte.isEmpty()){
            pos = pilaDescarte.size()-1;
            Carta = pilaDescarte.remove(pos);
            Mazo.agregarCarta(Carta);
        }  
        
        // Guarde el nombre de la ultima carta de la pila.
        ultimaCarta = CartaGuardar.getNombre();
        pilaDescarte.add(CartaGuardar);
    }
    
    // Se le entregan las cartas a un jugador.
    private void darCartaJugador(){
        boolean hayCartas;
        Cartas Carta;
        hayCartas = verificarMazo();
        if (!hayCartas){
            llenarMazo();
        }
        Carta = Mazo.siguienteCarta();
        jugadorActual.agregarCarta(Carta);
        tamanoMano++;
    }
        
    private Cartas verTop(){
        int numeroCartas;
        numeroCartas = pilaDescarte.size()-1;
        return pilaDescarte.get(numeroCartas);
    }
    
    private void repartirCartas() throws IOException{
        /*
        Reparte 7 cartas a todos los jugadores al empezar el juego.
        */
        int canal = 0;
        for(Jugador player : jugadores){
            jugadorActual = player;
            for (int i=0; i < 7; i++){
                darCartaJugador();
            }
            //Enviamos por el socket correspondiente las cartas a cada jugador
            String infoActual = jugadorActual.cartasJugador();
            infoActual += this.infoJugadores();
            salidas[canal].writeObject(infoActual);
            canal++;
        }
    }
        
    private void ponerCartaEnDescarte(Cartas pCarta){
        pilaDescarte.add(pCarta);
        ultimaCarta = pCarta.getNombre();
    }
    
    private boolean verificarCarta(String pColor, String pNombre){
        
        if (colorActivo.equals(pColor) || pColor.equals("") || pColor.equals("Undefined")){
            return true;
        }else if (ultimaCarta.equals(pNombre) || ultimaCarta.equals("")){
            return true;
        }else{
            return false;
        }
    }
    
    private boolean verificarPilaDescarte(){
        if (pilaDescarte.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    public static Partida getInstance(){
        if (instance == null){
            instance = new Partida();
        }
        return instance;
    }
    
    public void registrarJugador(String pNombre){
        Jugador jugador = new Jugador (pNombre);
        int imagenConvertir = jugador.crearImagen();
        String imagen = Integer.toString(imagenConvertir);
        imagenes.put(pNombre,imagen);
        System.out.println(" Nombre : " + pNombre);
        System.out.println(" Imagen : " + imagen);
        jugadores.add(jugador);
    }
    
    
    public void empezarJuego(Socket[] pJugadoresSocket, ObjectInputStream[] pEntradas, 
                            ObjectOutputStream[] pSalidas, String[] pPlayers) throws IOException{
        
        jugadoresSocket = pJugadoresSocket;
        entradas = pEntradas;
        salidas = pSalidas;
        players = pPlayers;
        
        repartirCartas(); // Se envian las cartas por los sockets
        siguienteTurno();
        Cartas primeraCarta = Mazo.siguienteCarta();
        ultimaCarta = primeraCarta.getNombre();
        colorActivo = primeraCarta.getColor();
        pilaDescarte.add(primeraCarta);
        
        String cartaDescarteEnviar = pilaDescarte.get(0).getColor() + " " + pilaDescarte.get(0).getNombre() + " ";
        for (ObjectOutputStream salida: salidas){
            salida.writeObject(cartaDescarteEnviar);
        }
    }
    
    
    public String infoJugadores(){
        String informacion = "";
        informacion += jugadores.size() + " ";
        for (Jugador i: jugadores){
            String imagen = imagenes.get(i.getNombre());
            informacion += i.getNombre() + " " + imagen + " ";
        }
        return informacion;
    }
    
    public boolean ganador(){
        if(tamanoMano == 0 || jugadorGanador != null){
            return true;
        }//.
        return false;
    }//- 
    
    public boolean comerCarta(){
        darCartaJugador();
        Cartas cartaTemp;
        boolean cartaFunciona;
        try{
            cartaTemp = jugadorActual.retornaCarta(jugadorActual.cantidadCartas()-1);
            cartaFunciona = verificarCarta(cartaTemp.getColor(), cartaTemp.getNombre());
            jugadorActual.agregarCarta(cartaTemp);
            if(cartaFunciona == false){
                return false;
            }else{
                return true;
            }
        }catch(Exception e){
            return false;
        }
    }
    
    public String obtenerJugadorActual(){
        return nombreJugador;
    }
    
    public int escogerCarta(String pColor, String pNombre){
        return jugadorActual.verificarCarta(pColor,pNombre);
    }
   
    public boolean seleccionarCarta(int pos) throws Exception{
        Cartas cartaSeleccionada;
        boolean cartaValida;
        // Si la carta existe.
        try{
            // Guardo la carta en una variable. Verifico si se puede poner.
            cartaSeleccionada = jugadorActual.retornaCarta(pos);
            cartaValida = verificarCarta(cartaSeleccionada.getColor(), cartaSeleccionada.getNombre());
            // Si se puede poner la agrego a la pila de Descarte y retorno que fue un exito.
            if(cartaValida){
                ponerCartaEnDescarte(cartaSeleccionada);
                tamanoMano--;
                return true;
            // Si no se puede poner la vuelvo a agregar al final de mi mano.
            }else{
                jugadorActual.agregarCarta(cartaSeleccionada);
                return false;
            }//.
        // Si llega aqui la carta no existe.
        }catch(Exception e){
            throw new Exception(" La carta no existe");
        }
    }//-
    
    // Selecciono una nuevaCarta.
    public boolean nuevaCarta(){
        if (verificarPilaDescarte() == false && verificarMazo()== false){
            for(Jugador jugadorScan : jugadores){
                if (jugadorScan.cantidadCartas() < tamanoMano){
                    jugadorActual = jugadorScan;
                    tamanoMano = jugadorScan.cantidadCartas();
                    nombreJugador = jugadorActual.getNombre();
                }
            }
            jugadorGanador = jugadorActual;
            return false;
        }
        boolean temp = comerCarta();
        if (temp){
            return true;
        }else{
            return false;
        }
        
    }//-
    
    // Se verifica de quien es el siguiente Turno.
    public void siguienteTurno(){
        int siguienteJugador;
        int numeroJugadores;
        
        // Si la reversa no esta activa.
        if (!reversa){
            siguienteJugador = 1;
        }else{
            if (posJugadorActual == 0){
                posJugadorActual = jugadores.size();
            }
            siguienteJugador = -1;
            
        }
        // Obtengo la cantidad de jugadores.
        numeroJugadores = jugadores.size();
        // Siguiente jugador - depende de si derecha o izquierda.
        siguienteJugador = (posJugadorActual + siguienteJugador) % numeroJugadores;
        // Actualizo el jugador actual por el siguiente.
        posJugadorActual = siguienteJugador;
        jugadorActual = jugadores.get(siguienteJugador);
        nombreJugador = jugadorActual.getNombre();
        tamanoMano = jugadorActual.cantidadCartas();
        
    }//-
    
    // Se ejecuta la accion de las cartas que se digiten.
    public int ejecutarAccion()throws Exception{
        String efecto;
        Cartas cartaEfecto;
        
        // Si no hay nada en el descarte, no se puede realizar accion.
        if (pilaDescarte.isEmpty()){
            throw new Exception("Esta vacia");
        }//.
        
        // Tome la carta de la pila, revise el efecto y ponga el color segun la carta.
        // (si la carta no tiene color, se escogera después.)
        cartaEfecto = verTop();
        efecto = cartaEfecto.ejecutarAccion();
        escogerColor(cartaEfecto.getColor());
        
        // Revise la carta y su respectivo efecto.
        switch(efecto){ 
            
            case "salto":
                pierdeTurno();
                return 1; // La interfaz interpreta un mensaje con respecto al valor de retorno.
                
            case "reversa":
                cambioSentido();
                pierdeTurno();
                return 2; // La interfaz interpreta un mensaje con respecto al valor de retorno.
                
            case "tomedos":
                darDosCartas();
                pierdeTurno();
                return 3; // La interfaz interpreta un mensaje con respecto al valor de retorno.
                
            case "tomecuatro":
                darDosCartas();
                darDosCartas();
                pierdeTurno();
                return 4; // La interfaz interpreta un mensaje con respecto al valor de retorno.
                
            case "seleccionarcolor":
                return 5; // La interfaz interpreta un mensaje con respecto al valor de retorno.
                
            case "":
                return -1; // La interfaz interpreta un mensaje con respecto al valor de retorno.
                
            default:
                return 0; // La interfaz interpreta un mensaje con respecto al valor de retorno.
        }//.
    
    }//-
    
    public String reporteActualizacionJuego(String player){
        String reporteJugadores = "";
        String reporteJugadorActual = "";
        String ganador = "0 ";
        String actualizo = "1 ";
        String reporte="";
        
        if(this.ganador() == true){
            ganador = "1 "; 
        }
        
        for (Jugador jugadorTemp: jugadores){
            if (jugadorTemp.getNombre().equals(player)){
                reporteJugadorActual = jugadorTemp.cartasJugador();
            }else{
                reporteJugadores += jugadorTemp.getNombre() + " " + jugadorTemp.cantidadCartas() + " ";
            }
        }
        
        reporte += ganador + actualizo + nombreJugador + " ";
        
        // El reporte de la información del jugador Actual.
        reporte += reporteJugadores + colorActivo + " " + ultimaCarta + " ";
         
        reporte += reporteJugadorActual;
        
        System.out.println(reporte);
        
        return reporte;
    }
    
    // Selecciono el color del juego activo.
    public void seleccionarColor(String newColor){
        escogerColor(newColor);
    }//-
   
    
    // Funcion que me muestra el estado de juego de los jugadores.
    public String reporteJuego(String player) throws IOException{
        String reporteJugadores = "";
        String reporteJugadorActual = "";
        String ganador = "0 ";
        String actualizo = "0 ";
        String reporte="";
        
        if(this.ganador() == true){
            ganador = "1 "; 
        }
        
        System.out.println("player : " + player);
        for (Jugador jugadorTemp: jugadores){
            if (jugadorTemp.getNombre().equals(player)){
                reporteJugadorActual = jugadorTemp.cartasJugador();
            }else{
                reporteJugadores += jugadorTemp.getNombre() + " " + jugadorTemp.cantidadCartas() + " ";
            }
        }
        
        reporte += ganador + actualizo + nombreJugador + " ";
        
        // El reporte de la información del jugador Actual.
        reporte += reporteJugadores + colorActivo + " " + ultimaCarta + " ";
         
        reporte += reporteJugadorActual;
        
        System.out.println(reporte);
        
        return reporte;
    }//-
    
}//-
