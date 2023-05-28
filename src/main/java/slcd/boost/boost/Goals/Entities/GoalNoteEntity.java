package slcd.boost.boost.Goals.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity(name = "f_goal_note")
@Getter
@Setter
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class GoalNoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "goal_id", referencedColumnName = "id", nullable = false)
    private GoalEntity goal;

    @Column(name = "text", nullable = false, columnDefinition = "text")
    private String text;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    public GoalNoteEntity(GoalEntity goal, String text, LocalDateTime updated) {
        this.goal = goal;
        this.text = text;
        this.updated = updated;
    }
}
