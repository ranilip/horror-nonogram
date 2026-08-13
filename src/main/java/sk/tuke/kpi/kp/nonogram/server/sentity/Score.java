package sk.tuke.kpi.kp.nonogram.sentity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Score implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String player;
    private String game;
    private int points;
    @Column(nullable = false)
    private LocalDate playedOn;

    public Score(String player, String game, int points, LocalDate playedOn) {
        this.player = player;
        this.game = game;
        this.points = points;
        this.playedOn = playedOn;
    }

    @PrePersist
    protected void onCreate() {
        if (playedOn == null) {
            playedOn = LocalDate.now();
        }
    }
}
