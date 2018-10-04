/*
 *************************************************************************
 * Clase que crea los jugadores del juego.
 *
 * - Crea los jugadores en el juego con un nombre.
 * - Cada jugador posee un pequeño mazo de cartas.
 *
 *************************************************************************
 */

// Importo el packete.
package uno;

// Importa los modulos.
import java.util.ArrayList;

/**
 *
 * @author Jason Barrantes Arce 2015048456
 *         David Vargas Rosales 2015128043
 *         Randy Morales Gamboa 2015085446
 * 
 */

// Clase que me crea los jugadores del juego.
public class Jugador {
    
    // Nombre del jugador y un arreglo de cartas.
    private String nombre;
    private ArrayList<Cartas> mano;
    private int cantidadCartas;
    
    // Creo el jugador con un nombre e inicializo el arreglo de cartas.
    public Jugador(String pNombre){
        nombre = pNombre;
        mano = new ArrayList();
        this.cantidadCartas = 0;
    }//-

    // Obtener el nombre del jugador.
    public String getNombre() {
        return nombre;
    }//-

    public ArrayList<Cartas> getMano() {
        return mano;
    }

    // Cambiar el nombre al jugador.
    public void setNombre(String Nombre) {
        this.nombre = Nombre;
    }//-
    
    // Agregar cartas a la mano del jugador.
    public void agregarCarta(Cartas nuevaCarta){
        mano.add(nuevaCarta);
        this.cantidadCartas++;
    }//-
    
    // Obtener una carta del jugador.
    public Cartas retornaCarta(int pos) throws Exception{
        // Si el jugador no tiene cartas.
        if (mano.isEmpty()){
            throw new Exception(" El jugador no tiene cartas.");
        }//.
        this.cantidadCartas--;
        return (Cartas) mano.remove(pos);
    }//-
    
    public int crearImagen(){
        int numero;
        numero = (int)Math.floor(Math.random()*35+1);
        return numero;
    }
    
    public int verificarCarta(String pColor, String pNombre){
        int pos = 0;
        for (Cartas subcarta: mano){
            String nombre = subcarta.getNombre();
            String color = subcarta.getColor();
            if(nombre.equals(pNombre) && color.equals(pColor)){
                return pos;
            }
            pos++;
        }
        return pos;
    }
    
    // Obtener la cantidad de cartas de la mano del jugador.
    public int cantidadCartas(){
        return this.cantidadCartas;
    }//-
    
    public String cartasJugador(){
        String cartas = "";
        cartas += Integer.toString(cantidadCartas) + " ";
        for(Cartas carta: mano){
            cartas +=carta.getColor() +  " " + carta.getNombre() + " ";
        }
        return cartas;
    }
    
}//-
