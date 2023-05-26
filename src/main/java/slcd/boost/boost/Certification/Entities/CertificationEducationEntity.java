package slcd.boost.boost.Certification.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "f_certification_educations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class CertificationEducationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "completed_education", nullable = false)
    private String completedEducation;

    @Column(name = "managed_do_best", nullable = false)
    private String managedDoBest;

    @Column(name = "wanted_do_but_failed", nullable = false)
    private String wantedDoButFailed;

    @Column(name = "interest_in_tasks", nullable = false)
    private String interestInTasks;

    @Column(name = "got_skills", nullable = false)
    private String gotSkills;

    @Column(name = "education_plans", nullable = false)
    private String educationPlans;

    public CertificationEducationEntity
            (
                    String completedEducation,
                    String managedDoBest,
                    String wantedDoButFailed,
                    String interestInTasks,
                    String gotSkills,
                    String educationPlans
            )
    {
        this.completedEducation = completedEducation;
        this.managedDoBest = managedDoBest;
        this.wantedDoButFailed = wantedDoButFailed;
        this.interestInTasks = interestInTasks;
        this.gotSkills = gotSkills;
        this.educationPlans = educationPlans;
    }
}
