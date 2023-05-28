package slcd.boost.boost.Goals;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slcd.boost.boost.Certification.Enums.ECertificationStatus;
import slcd.boost.boost.General.DTOs.UUIDResponse;
import slcd.boost.boost.General.UserAccessCheckService;
import slcd.boost.boost.Goals.DTOs.*;
import slcd.boost.boost.Goals.Entities.GoalEntity;
import slcd.boost.boost.Goals.Entities.GoalNoteEntity;
import slcd.boost.boost.Goals.Entities.GoalStepEntity;
import slcd.boost.boost.Goals.Repos.GoalNoteRepository;
import slcd.boost.boost.Goals.Repos.GoalStepRepository;
import slcd.boost.boost.Goals.Enums.EGoalStatus;
import slcd.boost.boost.Goals.Exceptions.GoalCantChangeStatusException;
import slcd.boost.boost.Goals.Exceptions.GoalNotFoundException;
import slcd.boost.boost.Goals.Repos.GoalRepository;
import slcd.boost.boost.Certification.CertificationProtocolService;
import slcd.boost.boost.Certification.CertificationService;
import slcd.boost.boost.Certification.Entities.CertificationEntity;
import slcd.boost.boost.Users.Entities.UserEntity;
import slcd.boost.boost.Users.UserService;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private CertificationProtocolService certificationProtocolService;
    @Autowired
    private UserAccessCheckService userAccessCheckService;
    @Autowired
    private UserService userService;

    @Autowired
    private CertificationService certificationService;
    @Autowired
    private GoalStepRepository goalStepRepository;
    @Autowired
    private GoalNoteRepository goalNoteRepository;

    public GoalEntity findGoalByUuid(String uuid){
        return goalRepository.findByUuid(
                UUID.fromString(uuid)
        ).orElseThrow(()-> new GoalNotFoundException(
                String.format(GoalConstants.GOAL_NOT_FOUND_MESSAGE, uuid)
        ));
    }

    public UUIDResponse createGoal(CreateGoalRequest request) throws AccessDeniedException {

        //Получение текущего пользователя и владельца протокола
        UserEntity owner = userService.findUserById(request.getOwnerId());

        //Проверка, есть ли права у пользователя создавать цели для данного пользователя
        userAccessCheckService.checkTeamLeadAccess(
                request.getOwnerId()
        );

        CertificationEntity certification
                = certificationService.findCertificationByOwnerAndNotCompleted(owner);

        //Формирование новой сущности цели
        GoalEntity goalEntity = request.mapToEntity(certification);
        goalEntity = goalRepository.save(goalEntity);

        GoalNoteEntity noteEntity = new GoalNoteEntity(
                goalEntity,
                "",
                LocalDateTime.now()
        );
        goalNoteRepository.save(noteEntity);

        return new UUIDResponse(goalEntity.getUuid().toString());
    }

    public GoalResponse getGoalByUuid(String uuid) throws AccessDeniedException {

        GoalEntity goalEntity = findGoalByUuid(uuid);

        //Проверка, есть ли права у пользователя смотреть цели для данного пользователя
        userAccessCheckService.checkTeamLeadAccess(
                goalEntity.getCertification().getOwner().getId()
        );

        return GoalResponse.mapFromEntity(goalEntity);
    }

    public List<GoalShortResponse> getUserCurrentGoals(Long ownerId) throws AccessDeniedException{
        //Проверка, есть ли права у пользователя смотреть цели для данного пользователя
        userAccessCheckService.checkTeamLeadAccess(
                ownerId
        );

        UserEntity owner = userService.findUserById(ownerId);

        CertificationEntity certificationEntity
                = certificationService.findCertificationByOwnerAndNotCompleted(owner);

        return certificationEntity
                .getGoals()
                .stream()
                .map(GoalShortResponse::mapFromEntity)
                .toList();
    }

    public UUIDResponse updateGoalInfo(String uuid, UpdateGoalRequest request) throws AccessDeniedException {
        GoalEntity goalEntity = findGoalByUuid(uuid);

        //Проверка, есть ли права у пользователя смотреть цели для данного пользователя
        userAccessCheckService.checkTeamLeadAccess(
                goalEntity.getCertification().getOwner().getId()
        );

        //Создание маппера для обновления сущности
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        TypeMap<CreateGoalRequest, GoalEntity> typeMap = modelMapper.createTypeMap(CreateGoalRequest.class, GoalEntity.class);
        typeMap.addMappings(mapper -> {
            mapper.skip(GoalEntity::setId);
            mapper.skip(GoalEntity::setUuid);
        });

        //Обновление сущности
        modelMapper.map(request, goalEntity);
        goalEntity.setUpdated(LocalDateTime.now());
        goalRepository.save(goalEntity);

        return new UUIDResponse(uuid);
    }

    public void setGoalStatusCompleted(String uuid) throws AccessDeniedException {
        GoalEntity goalEntity = findGoalByUuid(uuid);

        //Проверка, есть ли права у пользователя смотреть цели для данного пользователя
        userAccessCheckService.checkTeamLeadAccess(
                goalEntity.getCertification().getOwner().getId()
        );
        //
        if(goalEntity.getCertification().getStatus().equals(ECertificationStatus.COMPLETED))
            throw new GoalCantChangeStatusException(GoalConstants.GOAL_CANT_CHANGE_STATUS_MESSAGE);

        goalEntity.setStatus(EGoalStatus.COMPLETED);
        goalRepository.save(goalEntity);
    }

    public void setGoalStatusInProgress(String uuid) throws AccessDeniedException {
        GoalEntity goalEntity = findGoalByUuid(uuid);

        //Проверка, есть ли права у пользователя смотреть цели для данного пользователя
        userAccessCheckService.checkTeamLeadAccess(
                goalEntity.getCertification().getOwner().getId()
        );
        if(goalEntity.getCertification().getStatus().equals(ECertificationStatus.COMPLETED))
            throw new GoalCantChangeStatusException(GoalConstants.GOAL_CANT_CHANGE_STATUS_MESSAGE);

        goalEntity.setStatus(EGoalStatus.IN_PROGRESS);
        goalRepository.save(goalEntity);
    }

    public GoalsStatisticsDTO getUserGoalStatistics(Long id) throws AccessDeniedException {
        //Проверка, есть ли права у пользователя смотреть цели для данного пользователя
        userAccessCheckService.checkTeamLeadAccess(id);

        UserEntity owner = userService.findUserById(id);

        CertificationEntity certificationEntity
                = certificationService.findCertificationByOwnerAndNotCompleted(owner);

        if(certificationEntity.getGoals().isEmpty())
            return new GoalsStatisticsDTO(GoalConstants.PERSONAL_STATISTICS_STRING, 0, 0, false);

        AtomicInteger completed = new AtomicInteger();
        AtomicInteger inProgress = new AtomicInteger();
        certificationEntity
                .getGoals()
                .forEach(goalEntity -> {
                    switch (goalEntity.getStatus()){
                        case IN_PROGRESS -> inProgress.getAndIncrement();
                        case COMPLETED -> completed.getAndIncrement();
                    }
                });

        int goalCount = certificationEntity.getGoals().size();
        int completedPercent;
        int inProgressPercent;
        if(completed.get() != 0) {
            completedPercent = getPercentRatio(completed.get(), goalCount);
            inProgressPercent = 100 - completedPercent;
        }
        else{
            inProgressPercent = getPercentRatio(inProgress.get(), goalCount);
            completedPercent = 100 - inProgressPercent;
        }

        return new GoalsStatisticsDTO(
                GoalConstants.PERSONAL_STATISTICS_STRING,
                inProgressPercent,
                completedPercent,
                true
        );
    }

    public List<GoalsStatisticsDTO> getTeamGoalsStatistics(Long id) throws AccessDeniedException {
        userAccessCheckService.checkTeamLeadAccess(id);

        UserEntity owner = userService.findUserById(id);

        List<GoalsStatisticsDTO> goalsStatisticsDTOList = new ArrayList<>();

        owner.getUserProducts().forEach(
                userProductEntity -> {

                    AtomicInteger goalCount = new AtomicInteger();
                    AtomicInteger completed = new AtomicInteger(0);
                    AtomicInteger inProgress = new AtomicInteger(0);

                    userProductEntity.getProduct().getUserProducts().forEach(
                            userProductEntity1 -> {

                                var certification = certificationService
                                        .findCurrentCertification(
                                                userProductEntity1.getUser()
                                        );

                                if(
                                        certification != null
                                                && !certification.getGoals().isEmpty()
                                ){
                                    certification
                                            .getGoals()
                                            .forEach(goalEntity -> {
                                                        switch (goalEntity.getStatus()){
                                                            case IN_PROGRESS -> inProgress.getAndIncrement();
                                                            case COMPLETED -> completed.getAndIncrement();
                                                        }
                                                    }
                                            );

                                    goalCount.set(goalCount.get() + certification.getGoals().size());
                                }
                            }
                    );

                    if(goalCount.get() == 0)
                        goalsStatisticsDTOList.add(new GoalsStatisticsDTO(
                                userProductEntity.getProduct().getName(),
                                0,
                                0,
                                false
                        ));
                    else {

                        int completedPercent;
                        int inProgressPercent;
                        if (completed.get() != 0) {
                            completedPercent = getPercentRatio(completed.get(), goalCount.get());
                            inProgressPercent = 100 - completedPercent;
                        } else {
                            inProgressPercent = getPercentRatio(inProgress.get(), goalCount.get());
                            completedPercent = 100 - inProgressPercent;
                        }

                        goalsStatisticsDTOList.add(new GoalsStatisticsDTO(
                                userProductEntity.getProduct().getName(),
                                inProgressPercent,
                                completedPercent,
                                true
                        ));

                    }
                }
        );

        return goalsStatisticsDTOList;
    }

    public void saveStepsByGoalUuid(String uuid, List<GoalStepDTO> request) {

        GoalEntity goal = findGoalByUuid(uuid);
        UserEntity currentUser = userService.getCurrentUserEntity();
        userAccessCheckService.isCurrentUser(
                goal.getCertification().getOwner(),
                currentUser
        );

        goalStepRepository.deleteAll(goal.getGoalSteps());

        List<GoalStepEntity> goalStepEntities
                = request.stream()
                .map(goalStepDTO -> goalStepDTO.mapToEntity(goal))
                .toList();

        goalStepRepository.saveAll(goalStepEntities);
    }

    private int getPercentRatio(int first, int second){
        Double firstDouble = (double) first;
        Double secondDouble = (double) second;
        double percentRatio = (firstDouble / secondDouble) * 100;

        return (int) Math.round(percentRatio);
    }

    public List<GoalStepDTO> getStepsByGoalUuid(String uuid) {
        GoalEntity goal = findGoalByUuid(uuid);
        UserEntity currentUser = userService.getCurrentUserEntity();
        userAccessCheckService.isCurrentUser(
                goal.getCertification().getOwner(),
                currentUser
        );

        List<GoalStepEntity> goalStepEntities = goalStepRepository.findByGoal(goal);
        return goalStepEntities.stream()
                .map(GoalStepDTO::mapFromEntity)
                .toList();
    }

    public StepStatisticsDTO getUserStepStatistics() {

        UserEntity userEntity = userService.getCurrentUserEntity();

        CertificationEntity certification
                = certificationService.findCurrentCertification(userEntity);

        List<GoalEntity> goals = certification.getGoals();

        AtomicInteger stepsCount = new AtomicInteger(0);
        AtomicInteger inProgress = new AtomicInteger(0);
        AtomicInteger completed = new AtomicInteger(0);

        goals.forEach(
                goalEntity -> {
                    goalEntity.getGoalSteps().forEach(
                            goalStepEntity -> {
                                if(goalStepEntity.isChecked())
                                    completed.getAndIncrement();
                                else
                                    inProgress.getAndIncrement();
                                stepsCount.getAndIncrement();
                            }
                    );
                }
        );

        int count = stepsCount.get();

        if(Objects.equals(count, 0))
            return new StepStatisticsDTO(
                    0,
                    0,
                    false
            );

        int completedPercent;
        int inProgressPercent;
        if(completed.get() != 0) {
            completedPercent = getPercentRatio(completed.get(), count);
            inProgressPercent = 100 - completedPercent;
        }
        else{
            inProgressPercent = getPercentRatio(inProgress.get(), count);
            completedPercent = 100 - inProgressPercent;
        }

        return new StepStatisticsDTO(
                inProgressPercent,
                completedPercent,
                true
        );
    }

    public void saveNote(String uuid, GoalNoteDTO request) {
        //Проверка, есть доступ у пользователя
        GoalEntity goalEntity = findGoalByUuid(uuid);
        UserEntity currentUser = userService.getCurrentUserEntity();
        userAccessCheckService.isCurrentUser(
                goalEntity.getCertification().getOwner(),
                currentUser
        );

        //Обновление сущности
        GoalNoteEntity noteEntity = goalEntity.getNote();
        noteEntity.setText(request.getText());
        noteEntity.setUpdated(LocalDateTime.now());
        goalNoteRepository.save(noteEntity);
    }

    public GoalNoteDTO getNoteByUuid(String uuid) {
        //Проверка, есть доступ у пользователя
        GoalEntity goalEntity = findGoalByUuid(uuid);
        UserEntity currentUser = userService.getCurrentUserEntity();
        userAccessCheckService.isCurrentUser(
                goalEntity.getCertification().getOwner(),
                currentUser
        );

        return GoalNoteDTO.mapFromEntity(
                goalEntity.getNote()
        );
    }

    public StepStatisticsDTO getGoalStepStatistics(String uuid) {
        //Проверка, есть доступ у пользователя
        GoalEntity goalEntity = findGoalByUuid(uuid);
        UserEntity currentUser = userService.getCurrentUserEntity();
        userAccessCheckService.isCurrentUser(
                goalEntity.getCertification().getOwner(),
                currentUser
        );

        //Инициализация переменных для получения статистики
        AtomicInteger inProgress = new AtomicInteger(0);
        AtomicInteger completed = new AtomicInteger(0);
        int stepsCount = goalEntity.getGoalSteps().size();

        //Если список шагов пустой, вернуть соответствующий ответ
        if(Objects.equals(stepsCount, 0))
            return new StepStatisticsDTO(
                    0,
                    0,
                    false
            );

        //Перебор всего шагов, чтобы получить статистику по статусам шагов
        goalEntity.getGoalSteps().forEach(
                goalStepEntity -> {
                    if(goalStepEntity.isChecked())
                        completed.getAndIncrement();
                    else
                        inProgress.getAndIncrement();
                }
        );

        //Формирование процентов статистики
        int completedPercent;
        int inProgressPercent;
        if(completed.get() != 0) {
            completedPercent = getPercentRatio(completed.get(), stepsCount);
            inProgressPercent = 100 - completedPercent;
        }
        else{
            inProgressPercent = getPercentRatio(inProgress.get(), stepsCount);
            completedPercent = 100 - inProgressPercent;
        }

        return new StepStatisticsDTO(
                inProgressPercent,
                completedPercent,
                true
        );
    }
}
