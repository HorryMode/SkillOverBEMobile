package slcd.boost.boost.Certification.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "f_certification_open_questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificationOpenQuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "certification_id", referencedColumnName = "id", nullable = false)
    private CertificationEntity certification;

    @Column(name = "question", nullable = false, length = 65_535)
    private String question;

    @Column(name = "responsible", length = 2047)
    private String responsible;

    @Column(name = "term", length = 2047)
    private String term;

    @Column(name = "result", length = 65_535)
    private String result;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    public CertificationOpenQuestionEntity(CertificationEntity certification, String question, String responsible, String term, String result, LocalDateTime created, LocalDateTime updated) {
        this.certification = certification;
        this.question = question;
        this.responsible = responsible;
        this.term = term;
        this.result = result;
        this.created = created;
        this.updated = updated;
    }
}
