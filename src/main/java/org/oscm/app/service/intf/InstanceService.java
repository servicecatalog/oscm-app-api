package org.oscm.app.service.intf;

import org.oscm.app.dto.InstanceDTO;

import java.util.List;
import java.util.Optional;

public interface InstanceService {

    InstanceDTO createInstance(InstanceDTO instanceDTO);

    Optional<InstanceDTO> getInstance(long id);

    List<InstanceDTO> getAllInstances();

    void deleteInstance(long id);

    void save(InstanceDTO instanceDTO);
}