package slcd.boost.boost.Certification.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "s_certification_key_questions")
public class SCertificationKeyQuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "question", nullable = false, length = 2047)
    private String question;

    @Column(name = "is_teamleader_field", nullable = false)
    private boolean isTeamleaderField;

    public SCertificationKeyQuestionEntity(String question, boolean isTeamleaderField) {
        this.question = question;
        this.isTeamleaderField = isTeamleaderField;
    }
}
