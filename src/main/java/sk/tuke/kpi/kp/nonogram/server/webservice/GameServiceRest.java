package sk.tuke.kpi.kp.nonogram.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.nonogram.core.Field;
import sk.tuke.kpi.kp.nonogram.core.Generate;
import sk.tuke.kpi.kp.nonogram.server.Data;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameServiceRest {

    private final Map<String, Field> sessions = new HashMap<>();

    @GetMapping("/new")
    public Data startNewGame(@RequestParam String username) {
        Field field = new Field(Generate.convert(5, 10, 5, 10));
        sessions.put(username, field);
        return new Data(field);
    }

    @PostMapping("/play")
    public Data play(@RequestParam String username, @RequestParam int row, @RequestParam int col) {
        Field field = sessions.get(username);
        return new Data(field);
    }

    @GetMapping("/state")
    public Data getGameState(@RequestParam String username) {
        Field field = sessions.get(username);
        return new Data(field);
    }
}
