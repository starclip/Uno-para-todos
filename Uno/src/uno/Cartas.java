/*
 *************************************************************************
 * Clase Abstracta que sirve para que las clases subhijas reescriban sus 
 * métodos y hereden su estructura.
 *
 *************************************************************************
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
public abstract class Cartas {
    
    // Todas las cartas comparten algunos de sus valores.
    // Poseen: Nombre, Numero y Color.
    private String nombre;
    private int numero;
    private String color;

    // Obtener el nombre de la carta.
    public String getNombre() {
        return nombre;
    }//-

    // Cambiar el nombre de la carta.
    public void setNombre(String Nombre) {
        this.nombre = Nombre;
    }//-

    // Obtener el numero de la carta (si es necesario).
    public int getNumero() {
        return numero;
    }//-

    // Cambiar el numero de la carta.
    public void setNumero(int Numero) {
        this.numero = Numero;
    }//-

    // Obtener el Color de la carta.
    public String getColor() {
        return color;
    }//-

    // Cambiar el color de la carta.
    public void setColor(String Color) {
        this.color = Color;
    }//-
    
    // Método abstracto que es heredado.
    public abstract String ejecutarAccion();
    
}//-
