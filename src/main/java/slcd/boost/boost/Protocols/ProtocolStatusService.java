package slcd.boost.boost.Protocols;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slcd.boost.boost.Protocols.Entities.ProtocolStatusEntity;
import slcd.boost.boost.Protocols.Enums.EProtocolStatus;
import slcd.boost.boost.Protocols.RegularMeetings.Repos.ProtocolStatusRepository;

@Service
public class ProtocolStatusService {

    @Autowired
    private ProtocolStatusRepository protocolStatusRepository;

    public ProtocolStatusEntity getCreatedStatus(){
        return protocolStatusRepository.findByName(EProtocolStatus.CREATED);
    }

    public ProtocolStatusEntity getOnApprovalStatus(){
        return protocolStatusRepository.findByName(EProtocolStatus.ON_APPROVAL);
    }

    public ProtocolStatusEntity getApprovedStatus(){
        return protocolStatusRepository.findByName(EProtocolStatus.APPROVED);
    }
}
