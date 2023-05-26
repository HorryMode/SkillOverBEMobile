package slcd.boost.boost.Certification;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slcd.boost.boost.Certification.DTOs.*;
import slcd.boost.boost.Certification.Entities.*;
import slcd.boost.boost.Certification.Enums.ECertificationStatus;
import slcd.boost.boost.Certification.Exceptions.CertificationAlreadyExistException;
import slcd.boost.boost.Certification.Exceptions.CertificationKeyQuestionNotFoundException;
import slcd.boost.boost.Certification.Exceptions.NotCompletedCertificationNotFoundException;
import slcd.boost.boost.Certification.Repos.CertificationRepository;
import slcd.boost.boost.Certification.Repos.SCertificationKeyQuestionRepository;
import slcd.boost.boost.General.Constants;
import slcd.boost.boost.General.DTOs.UUIDResponse;
import slcd.boost.boost.General.Exceptions.OnlyTeamLeaderHaveAccessException;
import slcd.boost.boost.General.UserAccessCheckService;
import slcd.boost.boost.Goals.Entities.GoalEntity;
import slcd.boost.boost.Goals.Enums.EGoalStatus;
import slcd.boost.boost.Goals.Enums.EGradeStatus;
import slcd.boost.boost.Users.Entities.UserEntity;
import slcd.boost.boost.Users.UserService;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificationService {

    @Autowired
    private SCertificationKeyQuestionRepository sCertificationKeyQuestionRepository;
    @Autowired
    private CertificationRepository certificationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CertificationEducationRepository certificationEducationRepository;
    @Autowired
    private CertificationTestSkillsRepository certificationTestSkillsRepository;
    @Autowired
    private UserAccessCheckService userAccessCheckService;

    public SCertificationKeyQuestionEntity findSCertificationKeyQuestionById(Long id){
        return sCertificationKeyQuestionRepository
                .findById(id)
                .orElseThrow(()-> new CertificationKeyQuestionNotFoundException(
                        String.format(CertificationConstants.CERTIFICATION_KEY_QUESTION_NOT_FOUND_MESSAGE, id)
                ));
    }

    public CertificationEntity findCertificationByUuidAndNotCompleted(String uuid){
        return certificationRepository
                .findByUuidAndStatusNot(
                        UUID.fromString(uuid),
                        ECertificationStatus.COMPLETED
                )
                .orElseThrow(()-> new NotCompletedCertificationNotFoundException(
                        CertificationConstants.CERTIFICATION_NOT_COMPLETED_NOT_FOUND_MESSAGE
                ));
    }

    public CertificationEntity findCertificationByOwnerAndNotCompleted(UserEntity owner){


        return certificationRepository.findByOwnerAndStatusNot(owner, ECertificationStatus.COMPLETED)
                .orElseThrow(()-> new NotCompletedCertificationNotFoundException(
                        CertificationConstants.CERTIFICATION_NOT_COMPLETED_NOT_FOUND_MESSAGE
                ));
    }

    public CertificationEntity findCurrentCertification(UserEntity owner){
        Optional<CertificationEntity> certificationEntity= certificationRepository
                .findByOwnerAndStatusNot(owner, ECertificationStatus.COMPLETED);

        return certificationEntity.orElse(null);
    }

    public CertificationEntity findCertificationByUuid(String uuid){
        return certificationRepository
                .findByUuid(
                        UUID.fromString(uuid)
                )
                .orElseThrow(()-> new CertificationKeyQuestionNotFoundException(
                        String.format(CertificationConstants.CERTIFICATION_NOT_FOUND_MESSAGE, uuid)
                ));
    }

    public CertificationKeyQuestionEntity mapCertificationKeyQuestionToEntity
            (
                    CertificationEntity certification,
                    CertificationKeyQuestionDTO keyQuestion
            )
    {
        return new CertificationKeyQuestionEntity(
                certification,
                findSCertificationKeyQuestionById(keyQuestion.getKeyQuestionId()),
                keyQuestion.getAnswer()
        );
    }

    public GoalEntity mapCertificationGoalToEntity
            (
                    CertificationEntity certification,
                    CreateCertificationGoalDTO goal
            )
    {
        GoalEntity goalEntity = new GoalEntity();

        goalEntity.setUuid(UUID.randomUUID());
        goalEntity.setCertification(certification);
        goalEntity.setName(goal.getName());
        goalEntity.setDescription(goal.getDescription());
        goalEntity.setGradeThree(goal.getGradeThree());
        goalEntity.setStatusThree(EGradeStatus.IN_PROGRESS);
        goalEntity.setGradeFour(goal.getGradeFour());
        goalEntity.setStatusFour(EGradeStatus.IN_PROGRESS);
        goalEntity.setGradeFive(goal.getGradeFive());
        goalEntity.setStatusFive(EGradeStatus.IN_PROGRESS);
        goalEntity.setStatus(EGoalStatus.IN_PROGRESS);
        goalEntity.setCreated(LocalDateTime.now());
        goalEntity.setUpdated(LocalDateTime.now());

        return goalEntity;
    }

    public CertificationOpenQuestionEntity mapCertificationOpenQuestionToEntity
            (
                    CertificationEntity certification,
                    CreateCertificationOpenQuestionDTO openQuestion
            )
    {
        return new CertificationOpenQuestionEntity(
                certification,
                openQuestion.getQuestion(),
                openQuestion.getResponsible(),
                openQuestion.getTerm(),
                openQuestion.getResult(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public CertificationOpenQuestionEntity mapCertificationOpenQuestionToEntity
            (
                    CertificationEntity certification,
                    UpdateCertificationOpenQuestionDTO openQuestion
            )
    {
        return new CertificationOpenQuestionEntity(
                certification,
                openQuestion.getQuestion(),
                openQuestion.getResponsible(),
                openQuestion.getTerm(),
                openQuestion.getResult(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public boolean existCertificationByOwnerAndNotCompleted(UserEntity owner){
        return certificationRepository.existsByOwnerAndStatusNot(
                owner,
                ECertificationStatus.COMPLETED
        );
    }

    public List<CertificationEntity> findCertificationsByOwnerId(Long ownerId){
        UserEntity userEntity = userService.findUserById(ownerId);
        return certificationRepository.findByOwner(userEntity);
    }

    public UUIDResponse createCertification(CreateCertificationRequest request){

        CertificationEntity certificationEntity = request.mapToEntity();

        UserEntity owner = userService.findUserById(request.getOwnerId());
        UserEntity currentUser = userService.getCurrentUserEntity();
        //Проверка, является ли пользователь тимлидом или руководителем отдела
        if(!(
            userAccessCheckService.isTeamLeader(owner, currentUser)
            || userAccessCheckService.isSubdivisionHead(owner.getId())
        )) throw new OnlyTeamLeaderHaveAccessException(Constants.ONLY_TEAM_LEADER_HAVE_ACCESS_MESSAGE);

        //Проверка, есть ли у owner уже протокол. Если да, то ошибка
        if(existCertificationByOwnerAndNotCompleted(owner))
            throw new CertificationAlreadyExistException(CertificationConstants.CERTIFICATION_ALREADY_EXIST_MESSAGE);

        certificationEntity.setOwner(owner);
        certificationEntity.setKeyQuestions(
                request.getKeyQuestions()
                        .stream()
                        .map(keyQuestion -> mapCertificationKeyQuestionToEntity(certificationEntity, keyQuestion))
                        .toList()
        );
        certificationEntity.setGoals(
                request.getGoals()
                        .stream()
                        .map(goal -> mapCertificationGoalToEntity(certificationEntity, goal))
                        .toList()
        );
        certificationEntity.setOpenQuestions(
                request.getOpenQuestions()
                        .stream()
                        .map(openQuestion -> mapCertificationOpenQuestionToEntity(certificationEntity, openQuestion))
                        .toList()
        );

        CertificationEducationEntity educationEntity = request.getEducation().mapToEntity();
        educationEntity = certificationEducationRepository.save(educationEntity);
        certificationEntity.setEducation(educationEntity);

        CertificationTestSkillsEntity testSkills = request.getTestSkills().mapToEntity();
        testSkills = certificationTestSkillsRepository.save(testSkills);
        certificationEntity.setTestSkills(testSkills);

        certificationRepository.save(certificationEntity);

        return new UUIDResponse(
                certificationEntity.getUuid().toString()
        );
    }

    public void setStatusCompleted(String uuid) {
        CertificationEntity certificationEntity = findCertificationByUuid(uuid);
        UserEntity owner = certificationEntity.getOwner();
        UserEntity currentUser = userService.getCurrentUserEntity();

        if(!(
                userAccessCheckService.isTeamLeader(owner, currentUser)
                        || userAccessCheckService.isSubdivisionHead(owner.getId())
        )) throw new OnlyTeamLeaderHaveAccessException(Constants.ONLY_TEAM_LEADER_HAVE_ACCESS_MESSAGE);

        certificationEntity.setStatus(ECertificationStatus.COMPLETED);
        certificationEntity.setUpdated(LocalDateTime.now());
        certificationRepository.save(certificationEntity);
    }

    public void setStatusCreated(String uuid) {
        CertificationEntity certificationEntity = findCertificationByUuid(uuid);
        UserEntity owner = certificationEntity.getOwner();
        UserEntity currentUser = userService.getCurrentUserEntity();

        //Проверка, является ли пользователь тимлидом или руководителем отдела
        if(!(
                userAccessCheckService.isTeamLeader(owner, currentUser)
                || userAccessCheckService.isSubdivisionHead(owner.getId())
        )) throw new OnlyTeamLeaderHaveAccessException(Constants.ONLY_TEAM_LEADER_HAVE_ACCESS_MESSAGE);

        //Проверка, есть ли у owner уже протокол. Если да, то ошибка
        if(existCertificationByOwnerAndNotCompleted(owner))
            throw new CertificationAlreadyExistException(CertificationConstants.CERTIFICATION_ALREADY_EXIST_MESSAGE);

        certificationEntity.setStatus(ECertificationStatus.CREATED);
        certificationEntity.setUpdated(LocalDateTime.now());
        certificationRepository.save(certificationEntity);
    }

    public CertificationResponse findCertificationByUuidForController(String uuid) throws AccessDeniedException {
        CertificationEntity certificationEntity = findCertificationByUuid(uuid);

        UserEntity owner = certificationEntity.getOwner();

        //Проверка, является ли пользователь тимлидом или руководителем отдела
        userAccessCheckService.checkTeamLeadAccess(owner.getId());

        return CertificationResponse.mapFromEntity(certificationEntity);
    }

    public List<CertificationShortResponse> findCertificationsByOwner(Long ownerId) {
        return findCertificationsByOwnerId(ownerId).stream()
                .map(CertificationShortResponse::mapFromEntity)
                .sorted(Comparator.comparing(CertificationShortResponse::getCreated).reversed())
                .toList();
    }

    public UUIDResponse updateCertification(String uuid, UpdateCertificationRequest request) {
        CertificationEntity certificationEntity = findCertificationByUuid(uuid);
        UserEntity owner = certificationEntity.getOwner();
        UserEntity currentUser = userService.getCurrentUserEntity();

        //Проверка, является ли пользователь тимлидом или руководителем отдела
        if(!(
                userAccessCheckService.isTeamLeader(owner, currentUser)
                || userAccessCheckService.isSubdivisionHead(owner.getId())
        )) throw new OnlyTeamLeaderHaveAccessException(Constants.ONLY_TEAM_LEADER_HAVE_ACCESS_MESSAGE);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull())
                .setMatchingStrategy(MatchingStrategies.STRICT);
        TypeMap<UpdateCertificationRequest, CertificationEntity> typeMap = modelMapper.createTypeMap(
                UpdateCertificationRequest.class,
                CertificationEntity.class
        );
        typeMap.addMappings(mapper -> {
            mapper.skip(CertificationEntity::setId);
            mapper.skip(CertificationEntity::setKeyQuestions);
            mapper.skip(CertificationEntity::setOpenQuestions);
        });

        modelMapper.map(request, certificationEntity);
        certificationEntity.getKeyQuestions().clear();
        certificationEntity.getKeyQuestions().addAll(
                request.getKeyQuestions()
                        .stream()
                        .map(keyQuestion -> mapCertificationKeyQuestionToEntity(certificationEntity, keyQuestion))
                        .toList()
                );
        certificationEntity.getOpenQuestions().clear();
        certificationEntity.getOpenQuestions().addAll(
                request.getOpenQuestions()
                        .stream()
                        .map(openQuestion -> mapCertificationOpenQuestionToEntity(certificationEntity, openQuestion))
                        .toList()
        );

        certificationRepository.save(certificationEntity);

        return new UUIDResponse(uuid);
    }
}
