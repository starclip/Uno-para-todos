/*
 ****************************************************************************
 * Creo la carta Comodin y sobreescribo el método ejecutarAccion.
 *
 ****************************************************************************
 */

// Importo el packete.
package uno;

/**
 *
 * @author Jason Barrantes Arce 2015048456
 *         David Vargas Rosales 2015128043
 *         Randy Morales Gamboa 2015085446
 * 
 */

// Creo la carta.
public class CartaComodin extends Cartas{
    // Constructor
    public CartaComodin(String Nombre){
        setNombre(Nombre);
        setColor("Undefined");
    }//-
    
    // Sobreescribo el método ejecutarAccion
    @Override
    public String ejecutarAccion(){
        return "seleccionarcolor";
    }//-
}//-
