package slcd.boost.boost.Certification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import slcd.boost.boost.Certification.DTOs.CreateCertProtocolRequest;
import slcd.boost.boost.General.DTOs.UUIDResponse;
import slcd.boost.boost.General.Exceptions.ResourceNotFoundException;
import slcd.boost.boost.General.Interfaces.IResponse;
import slcd.boost.boost.Protocols.Entities.ProtocolEntity;
import slcd.boost.boost.Protocols.Enums.EProtocolType;
import slcd.boost.boost.Protocols.Interfaces.IProtocolRequest;
import slcd.boost.boost.Protocols.Interfaces.IProtocolService;
import slcd.boost.boost.Protocols.ProtocolStatusService;
import slcd.boost.boost.Protocols.RegularMeetings.DTOs.ProtocolPageResponse;
import slcd.boost.boost.Protocols.RegularMeetings.Repos.ProtocolRepository;
import slcd.boost.boost.Users.Entities.UserEntity;
import slcd.boost.boost.Users.UserService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CertificationProtocolService implements IProtocolService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProtocolStatusService protocolStatusService;
    @Autowired
    private ProtocolRepository protocolRepository;

    @Override
    public IResponse createProtocol(IProtocolRequest protocol, List<MultipartFile> files){
        CreateCertProtocolRequest request = (CreateCertProtocolRequest) protocol;

        //Создание новой сущности протокола
        ProtocolEntity protocolEntity = new ProtocolEntity();

        //Генерация и заполнение uuid протокола
        protocolEntity.setUuid(
                UUID.randomUUID()
        );

        //Заполнение имени протокола, владельца и назначенного пользователя
        UserEntity owner = userService.findUserById(request.getOwnerId());
        protocolEntity.setName(request.getName());
        protocolEntity.setOwner(owner);
        protocolEntity.setAssignedUser(owner);

        //Установка статуса протокола CREATED и типа протокола CERTIFICATION
        protocolEntity.setStatus(
                protocolStatusService.getCreatedStatus()
        );
        protocolEntity.setType(EProtocolType.CERTIFICATION);

        //Заполнение даты создания и обновления протокола
        protocolEntity.setCreated(LocalDateTime.now());
        protocolEntity.setUpdated(LocalDateTime.now());


        //Сохранение протокола
        protocolRepository.save(protocolEntity);

        return new UUIDResponse(protocolEntity.getUuid().toString());
    }

    @Override
    public IResponse updateProtocol(String uuid, IProtocolRequest request, List<MultipartFile> files) throws AccessDeniedException {
        return null;
    }

    @Override
    public IResponse getProtocol(String uuid) throws AccessDeniedException {
        return null;
    }

    @Override
    public ProtocolPageResponse getProtocols(Long ownerId, Pageable pageable) {
        return null;
    }

    @Override
    public IResponse getAttachment(String uuid) throws IOException {
        return null;
    }

    @Override
    public ProtocolEntity getProtocolEntity(String uuid) {
        return protocolRepository.findByUuidAndType(
                UUID.fromString(uuid),
                EProtocolType.CERTIFICATION
        ).orElseThrow
                (() -> new ResourceNotFoundException(
                        String.format(CertificationConstants.CERT_PROTOCOL_NOT_FOUND_MESSAGE, uuid)
        ));
    }

    @Override
    public void setStatusOnApproval(String uuid) throws AccessDeniedException {

    }

    @Override
    public void setStatusCreated(String uuid) throws AccessDeniedException {

    }

    @Override
    public void setStatusApproved(String uuid) throws AccessDeniedException {

    }
}
