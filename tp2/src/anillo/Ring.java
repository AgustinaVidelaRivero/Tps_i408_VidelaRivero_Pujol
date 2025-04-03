package anillo;
import java.util.ArrayList;
import java.util.List;

public class Ring {
    private List<Object> elements;
    private int currentIndex;

    public Ring next() {
        if (elements.isEmpty()) {
            throw new RuntimeException("El anillo está vacío."); 
        }
        currentIndex = (currentIndex + 1) % elements.size();  // Avanza de forma circular
        return this; 
    }

    public Object current() {
        if (elements.isEmpty() || currentIndex == -1) {     // Caso anillo vacio 
            throw new RuntimeException("No hay elemento actual"); 
        }
        return elements.get(currentIndex); 
    }

    public Ring add( Object cargo ) {
        elements.add(cargo); 
        if (currentIndex == -1) { // Si es el primer elemento, establece el índice actual a 0
            currentIndex = 0; 
        }
        return this;
    }

    public Ring remove() {
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
