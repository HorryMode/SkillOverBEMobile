package slcd.boost.boost.Goals.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import slcd.boost.boost.Certification.Entities.CertificationEntity;
import slcd.boost.boost.Goals.Enums.EGoalStatus;
import slcd.boost.boost.Goals.Enums.EGradeStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "f_goals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class GoalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "certification_id", referencedColumnName = "id",nullable = false)
    @ManyToOne
    private CertificationEntity certification;

    @Column(name = "description", length = 65_535)
    private String description;

    @Column(name = "grade_three", nullable = false, length = 65_535)
    private String gradeThree;

    @Column(name = "status_three", nullable = false, length = 35)
    @Enumerated(EnumType.STRING)
    private EGradeStatus statusThree;

    @Column(name = "grade_four", nullable = false, length = 65_535)
    private String gradeFour;

    @Column(name = "status_four", nullable = false, length = 35)
    @Enumerated(EnumType.STRING)
    private EGradeStatus statusFour;

    @Column(name = "grade_five", nullable = false, length = 65_535)
    private String gradeFive;

    @Column(name = "status_five", nullable = false, length = 35)
    @Enumerated(EnumType.STRING)
    private EGradeStatus statusFive;

    @Column(name = "result", length = 65_535)
    private String result;

    @Column(name = "status", nullable = false, length = 35)
    @Enumerated(EnumType.STRING)
    private EGoalStatus status;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;
}
