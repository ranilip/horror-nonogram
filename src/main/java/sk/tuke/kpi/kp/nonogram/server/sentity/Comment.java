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
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String player;
    private String game;
    private String comment;
    private LocalDate commentedOn;

    // Stars (1–5) given together with this comment; null for comments without a rating.
    private Integer rating;

    // Synthetic demo rows (DemoData); shown with a Demo label on the site.
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean demo;

    public Comment(String player, String game, String comment, LocalDate commentedOn) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }
}
