/*
 *****************************************************************************
 * Clase que fabrica las cartas del sistema.
 *
 * - Funciona de una manera similar a un mazo de cartas.
 * - Genera las cartas y las selecciona al azar.
 * - Almacena las cartas.
 * - Tome la siguiente carta.
 * 
 *****************************************************************************
 */

// Importar packetes.
package uno;

// Se importan los módulos.
import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * @author Jason Barrantes Arce 2015048456
 *         David Vargas Rosales 2015128043
 *         Randy Morales Gamboa 2015085446
 * 
 */

// Clase Factory que genera las cartas.
public class GeneradorCartas {
    
    // Arraylist de cartas y un arreglo de colores.
    private static ArrayList<Cartas> listaCartas = new ArrayList(108);
    private final String[] Color = {"Rojo","Verde","Azul","Amarillo"};
    private int numeroCartas;
    
    public GeneradorCartas(){
        /*
        * Las cartas son generadas en orden segun sea respectivo.
        * Se crean 108 cartas, y se dividen en ciertos grupos.
        */
        
        // Declaro las variables.
        String ColorCin;
        int numeros;
        
        // Se crean las cuatro cartas "0", "Comodin Selecciona el color", y "Tome Cuatro".
        for(int i=0; i < 4; i++){
            ColorCin = Color[i]; // El color cambia según la posicion de i.
            listaCartas.add(new CartaNumericas(ColorCin,0));
            listaCartas.add(new CartaComodin("SeleccionaColor"));
            listaCartas.add(new CartaTomeCuatro("TomeCuatro"));
        } //*
        
        numeros = 1;
        // Se crean las cartas de los numeros del "0" al "9".
        while(numeros < 10){
            for(int i=0; i < 4; i++){ // Realice cuatro veces, según los colores.
                ColorCin = Color[i]; // El color cambia según la posicion de i.
                for(int j = 0; j < 2; j++){ // Cree una carta repetida.
                    listaCartas.add(new CartaNumericas(ColorCin, numeros));
                }//*
            }//*
            numeros++;
        } //*
        
        // Creo las cartas de salto, reversa y el tome dos.
        for (int i=0; i < 4; i++){
            for(int a = 0; a < 2; a++){
                ColorCin = Color[i];
                listaCartas.add(new CartaSalto(ColorCin, "Salto"));
                listaCartas.add(new CartaReversa(ColorCin, "Reversa"));
                listaCartas.add(new CartaTomeDos(ColorCin, "TomeDos"));
            }//*
        } //*
        
        // Revuelvo las cartas.
        Collections.shuffle(listaCartas);
        this.numeroCartas = 108;
    }//-
    
    // Método para obtener la siguiente carta del mazo.
    public Cartas siguienteCarta(){
        this.numeroCartas--;
        return (Cartas) listaCartas.remove(this.numeroCartas);
    }//-
    
    // Método para devolver una carta al mazo.
    public void agregarCarta(Cartas Carta){
        listaCartas.add(Carta);
        this.numeroCartas++;
    }//-
    
    // Metodo para obtener el numero de cartas en el mazo.
    public int numeroCartas(){
        return this.numeroCartas;
    }//-
    
}//-
