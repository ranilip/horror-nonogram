package sk.tuke.kpi.kp.nonogram.consoleUI;

import lombok.Setter;
import sk.tuke.kpi.kp.nonogram.core.Generate;
import sk.tuke.kpi.kp.nonogram.core.mode.Game;
import sk.tuke.kpi.kp.nonogram.core.mode.GameFactory;
import sk.tuke.kpi.kp.nonogram.core.mode.Mode;
import java.util.Scanner;

import static sk.tuke.kpi.kp.nonogram.consoleUI.PrintUI.printStart;

@Setter
public class ConsoleUI {
    private Game game;
    private final Scanner scanner = new Scanner(System.in);
    private final PlayerSessionService sessionService;

    public ConsoleUI(PlayerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void play() {
        Mode mode = chooseMode();

        if (mode == Mode.STORYMODE) {
            printStart();
            game = GameFactory.createGame(Mode.STORYMODE, null);
            new GameLoopUI(game, scanner, sessionService).run();
            return;
        }

        Generate.Nonogram nonogram = Generate.convert(1, 2, 1, 2);
        game = GameFactory.createGame(mode, nonogram);
        new GameLoopUI(game, scanner, sessionService).run();
    }

    private Mode chooseMode() {
        System.out.println("Choose game mode:\n1 - Score Mode\n2 - Story Mode");
        System.out.print("Your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return (choice == 2) ? Mode.STORYMODE : Mode.SCOREMODE;
    }
}
