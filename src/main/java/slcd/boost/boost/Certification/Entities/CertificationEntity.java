package slcd.boost.boost.Certification.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import slcd.boost.boost.Certification.Enums.ECertificationStatus;
import slcd.boost.boost.Goals.Entities.GoalEntity;
import slcd.boost.boost.Users.Entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "f_certifications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class CertificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID uuid;

    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private UserEntity owner;

    @Column(name = "certification_date", nullable = false)
    private String certificationDateString;

    @Column(name = "duration")
    private String duration;

    @Column(name = "venue")
    private String venue;

    @JoinColumn(name = "test_skills_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CertificationTestSkillsEntity testSkills;

    @JoinColumn(name = "education_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CertificationEducationEntity education;

    @OneToMany(mappedBy = "certification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationKeyQuestionEntity> keyQuestions;

    @OneToMany(mappedBy = "certification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalEntity> goals;

    @OneToMany(mappedBy = "certification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificationOpenQuestionEntity> openQuestions;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ECertificationStatus status;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    public CertificationEntity
            (
                    UUID uuid,
                    UserEntity owner,
                    String certificationDateString,
                    String duration, String venue,
                    CertificationTestSkillsEntity testSkills,
                    CertificationEducationEntity education,
                    List<CertificationKeyQuestionEntity> keyQuestions,
                    List<GoalEntity> goals,
                    List<CertificationOpenQuestionEntity> openQuestions,
                    ECertificationStatus status,
                    LocalDateTime created,
                    LocalDateTime updated
            )
    {
        this.uuid = uuid;
        this.owner = owner;
        this.certificationDateString = certificationDateString;
        this.duration = duration;
        this.venue = venue;
        this.testSkills = testSkills;
        this.education = education;
        this.keyQuestions = keyQuestions;
        this.goals = goals;
        this.openQuestions = openQuestions;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }
}
