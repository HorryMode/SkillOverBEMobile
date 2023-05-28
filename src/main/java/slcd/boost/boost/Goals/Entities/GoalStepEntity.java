package slcd.boost.boost.Goals.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "f_goal_steps")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoalStepEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "text", nullable = false, length = 2047)
    private String text;

    @Column(name = "isChecked", nullable = false)
    private boolean isChecked;

    @ManyToOne
    @JoinColumn(name = "goal_id", referencedColumnName = "id", nullable = false)
    private GoalEntity goal;
}
