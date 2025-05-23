package juegoUnoAgus;

public abstract class ControladorTurnos {
    protected ControladorTurnos opuesto;

    public void setOpuesto(ControladorTurnos opuesto) {
        this.opuesto = opuesto;
    }

    public ControladorTurnos opuesto() {
        return opuesto;
    }

    public abstract Jugador siguiente(Jugador actual);
}

