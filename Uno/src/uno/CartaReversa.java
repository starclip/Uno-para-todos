/*
 ***************************************************************************
 * Crea la carta de reversa del juego y sobreescribe el método ejecutarAccion.
 *
 ***************************************************************************
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

// Crea la carta.
public class CartaReversa extends Cartas{
    // Constructor
    public CartaReversa(String Color, String Nombre){
        setNombre(Nombre);
        setColor(Color);
    }//-
    
    // Sobreescribo el método ejecutarAccion.
    @Override
    public String ejecutarAccion(){
        return "reversa";
    }//-
}//-
