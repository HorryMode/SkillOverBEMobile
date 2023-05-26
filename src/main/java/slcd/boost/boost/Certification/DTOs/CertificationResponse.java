package slcd.boost.boost.Certification.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Certification.Entities.CertificationEntity;
import slcd.boost.boost.Goals.DTOs.GoalResponse;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CertificationResponse {

    private String uuid;
    private long ownerId;
    private String certificationDateString;
    private String duration;
    private String venue;
    private CertificationTestSkillsDTO testSkills;
    private CertificationEducationDTO education;
    private List<CertificationKeyQuestionDTO> keyQuestions;
    private List<GoalResponse> goals;
    private List<CertificationOpenQuestionResponse> openQuestions;
    private String status;
    private LocalDateTime created;
    private LocalDateTime updated;

    public static CertificationResponse mapFromEntity(CertificationEntity certification){
        return new CertificationResponse(
                certification.getUuid().toString(),
                certification.getOwner().getId(),
                certification.getCertificationDateString(),
                certification.getDuration(),
                certification.getVenue(),
                CertificationTestSkillsDTO.mapFromEntity(
                        certification.getTestSkills()
                ),
                CertificationEducationDTO.mapFromEntity(
                        certification.getEducation()
                ),
                certification.getKeyQuestions()
                        .stream()
                        .map(CertificationKeyQuestionDTO::mapFromEntity)
                        .toList(),
                certification.getGoals()
                        .stream()
                        .map(GoalResponse::mapFromEntity)
                        .toList(),
                certification.getOpenQuestions()
                        .stream()
                        .map(CertificationOpenQuestionResponse::mapFromEntity)
                        .toList(),
                certification.getStatus().toString(),
                certification.getCreated(),
                certification.getUpdated()
        );
    }
}
