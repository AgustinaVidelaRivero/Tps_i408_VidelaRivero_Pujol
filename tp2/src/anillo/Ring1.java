package anillo;
import java.util.ArrayList;
import java.util.List;

public class Ring1 {
    private List<Object> elements;
    private int currentIndex;

    // Constructor
    public Ring1() {
        this.elements = new ArrayList<>(); // Inicializa la lista vacía con el currentIndex en -1
        this.currentIndex = -1;
    }

    public Ring1 next() {
        if (elements.isEmpty() || currentIndex == -1) {    // Caso anillo vacío
            throw new RuntimeException("El anillo está vacío, no hay elemento siguiente"); 
        } else {
            currentIndex = (currentIndex + 1) % elements.size();  // Avanza de forma circular
            return this; 
        }
    }

    public Object current() {
        if (elements.isEmpty() || currentIndex == -1) {     // Caso anillo vacio 
            throw new RuntimeException("El anillo está vacío, no hay elemento actual"); 
        }
        return elements.get(currentIndex); 
    }

    public Ring1 add(Object cargo) {
        if (currentIndex == -1) { // Si es el primer elemento, establece el índice actual a 0
            currentIndex = 0;
            elements.add(cargo);
        } else {         
            elements.add(currentIndex, cargo);    // Se agrega el elemento en la posición especificada
        }
        return this;
    }

    public Ring1 remove() {
        if (elements.isEmpty()) {
            throw new RuntimeException("No hay elementos para eliminar"); // Caso anillo vacío
        }
        elements.remove(currentIndex); 
        // Ajustamos el índice 
        if (elements.isEmpty()) {
            currentIndex = -1; // Caso anillo queda vacío
        } else {
            currentIndex = currentIndex % elements.size(); // índice dentro del rango del anillo
        }
        return this;
    }
}
