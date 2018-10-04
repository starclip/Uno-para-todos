/*
 **************************************************************************
 * Crea la carta TomeDos, hereda de Cartas y sobreescribe el método ejecutarAccion.
 *
 **************************************************************************
 */

// Importar paquetes
package uno;

/**
 *
 * @author Jason Barrantes Arce 2015048456
 *         David Vargas Rosales 2015128043
 *         Randy Morales Gamboa 2015085446
 * 
 */

// Crea la carta.
public class CartaTomeDos extends Cartas{
    // Constructor.
    public CartaTomeDos(String Color, String Nombre){
        setNombre(Nombre);
        setColor(Color);
    }//-
    
    // Sobreescribe el método ejecutarAccion.
    @Override
    public String ejecutarAccion(){
        return "tomedos";
    }//-
}//-
