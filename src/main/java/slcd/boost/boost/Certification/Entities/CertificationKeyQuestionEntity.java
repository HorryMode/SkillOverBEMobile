package slcd.boost.boost.Certification.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "f_certification_key_questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificationKeyQuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "certification_id", referencedColumnName = "id", nullable = false)
    private CertificationEntity certification;

    @ManyToOne
    @JoinColumn(name = "s_key_question_id", referencedColumnName = "id")
    private SCertificationKeyQuestionEntity sCertificationKeyQuestion;

    @Column(name = "answer", length = 65_355)
    private String answer;

    public CertificationKeyQuestionEntity
            (
                    CertificationEntity certification,
                    SCertificationKeyQuestionEntity sCertificationKeyQuestion,
                    String answer
            )
    {
        this.certification = certification;
        this.sCertificationKeyQuestion = sCertificationKeyQuestion;
        this.answer = answer;
    }
}
