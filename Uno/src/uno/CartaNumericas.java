/*
 *****************************************************************************
 * Creo una carta numerica y sobreescribo el metodo ejecutarAccion.
 *
 *****************************************************************************
 */

// Importo el packete
package uno;

/**
 *
 * @author Jason Barrantes Arce 2015048456
 *         David Vargas Rosales 2015128043
 *         Randy Morales Gamboa 2015085446
 * 
 */

// Creo la carta.
public class CartaNumericas extends Cartas{
    // Constructor
    public CartaNumericas(String Color, int numero){
        setNombre(Integer.toString(numero));
        setNumero(numero);
        setColor(Color);
    }//-
    
    // Sobrecargo el método de ejecutarAccion
    @Override
    public String ejecutarAccion(){
        return "";
    }//-
    
}//-
