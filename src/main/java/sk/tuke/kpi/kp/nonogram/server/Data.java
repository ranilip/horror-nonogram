package sk.tuke.kpi.kp.nonogram.server;

import lombok.Getter;
import lombok.Setter;
import sk.tuke.kpi.kp.nonogram.core.Field;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Data {
    private int wight;
    private int height;
    private List<List<Integer>> rowHints;
    private List<List<Integer>> colHints;
    private int[][] nonogram;
    private boolean win;

    public Data(Field field) {
        this.wight = field.getCols();
        this.height = field.getRows();
        this.rowHints = field.getRowHints();
        this.colHints = field.getColHints();
        this.nonogram = Arrays.stream(field.getUserGrid())
                .map(row -> Arrays.stream(row)
                        .mapToInt(cell -> switch (cell) {
                            case CORRECT -> 1;
                            case CLOSE -> 2;
                            case FILLED -> 3;
                        })
                        .toArray()
                )
                .toArray(int[][]::new);
    }
}
