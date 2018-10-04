/*
 ****************************************************************************
 * Crea la carta Tome Cuatro cartas y sobreescribe el metodo ejecutarAccion.
 *
 ****************************************************************************
 */

// Importar packete.
package uno;

/**
 *
 * @author Jason Barrantes Arce 2015048456
 *         David Vargas Rosales 2015128043
 *         Randy Morales Gamboa 2015085446
 * 
 */

// Crea la clase tome cuatro.
public class CartaTomeCuatro extends Cartas{
    // Constructor.
    public CartaTomeCuatro(String Nombre){
        setNombre(Nombre);
        setColor("Undefined");
    }//-
    
    // Sobreescribe el método ejecutarAccion de la clase.
    @Override
    public String ejecutarAccion(){
        return "tomecuatro";
    }//-
}//-
