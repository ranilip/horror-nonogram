package sk.tuke.kpi.kp.nonogram.core;

public enum CellState {
    CORRECT, CLOSE, FILLED;
    public String toSymbol() {
        return switch (this) {
            case CORRECT -> "⬜️";
            case CLOSE -> "⬛️";
            case FILLED -> "⚫️";
        };
    }
}
