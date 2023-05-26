package slcd.boost.boost.Certification.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "f_certification_test_skills")
@DynamicUpdate
public class CertificationTestSkillsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "hard_skills", nullable = false)
    private String hardSkills;

    @Column(name = "competencies", nullable = false)
    private String competencies;

    @Column(name = "test_documentation_work", nullable = false)
    private String testDocumentationWork;

    @Column(name = "test_experience", nullable = false)
    private String testExperience;

    @Column(name = "product_support", nullable = false)
    private String productSupport;

    @Column(name = "soft_skills", nullable = false)
    private String softSkills;

    public CertificationTestSkillsEntity(
            String hardSkills,
            String competencies,
            String testDocumentationWork,
            String testExperience,
            String productSupport,
            String softSkills
    ) {
        this.hardSkills = hardSkills;
        this.competencies = competencies;
        this.testDocumentationWork = testDocumentationWork;
        this.testExperience = testExperience;
        this.productSupport = productSupport;
        this.softSkills = softSkills;
    }
}
