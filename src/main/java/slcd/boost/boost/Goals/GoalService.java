package slcd.boost.boost.Goals;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slcd.boost.boost.Certification.CertificationProtocolService;
import slcd.boost.boost.Certification.CertificationService;
import slcd.boost.boost.Certification.Entities.CertificationEntity;
import slcd.boost.boost.Certification.Enums.ECertificationStatus;
import slcd.boost.boost.General.DTOs.UUIDResponse;
import slcd.boost.boost.General.UserAccessCheckService;
import slcd.boost.boost.Goals.DTOs.*;
import slcd.boost.boost.Goals.Entities.GoalEntity;
import slcd.boost.boost.Goals.Enums.EGoalStatus;
import slcd.boost.boost.Goals.Exceptions.GoalCantChangeStatusException;
import slcd.boost.boost.Goals.Exceptions.GoalNotFoundException;
import slcd.boost.boost.Goals.Repos.GoalRepository;
import slcd.boost.boost.Users.Entities.UserEntity;
import slcd.boost.boost.Users.UserService;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

        GoalEntity goalEntity = request.mapToEntity(certification);
        goalRepository.save(goalEntity);
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

    private int getPercentRatio(int first, int second){
        Double firstDouble = (double) first;
        Double secondDouble = (double) second;
        double percentRatio = (firstDouble / secondDouble) * 100;

        return (int) Math.round(percentRatio);
    }
}
