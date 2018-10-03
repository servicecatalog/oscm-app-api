package org.oscm.app.service;

import org.modelmapper.ModelMapper;
import org.oscm.app.dto.InstanceAttributeDTO;
import org.oscm.app.dto.InstanceParameterDTO;
import org.oscm.app.service.intf.InstanceService;
import org.oscm.app.domain.Instance;
import org.oscm.app.domain.enumeration.ProvisioningStatus;
import org.oscm.app.dto.InstanceDTO;
import org.oscm.app.repository.InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InstanceServiceImpl implements InstanceService {

    @Autowired
    private InstanceRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public InstanceDTO createInstance(InstanceDTO instanceDTO) {

        Instance instance = mapper.map(instanceDTO, Instance.class);

        instance.getInstanceParameters().forEach(param->param.setInstance(instance));
        instance.getInstanceAttributes().forEach(att->att.setInstance(instance));

        instance.setRequestTime(LocalDateTime.now(ZoneId.systemDefault()));
        instance.setProvisioningStatus(ProvisioningStatus.WAITING_FOR_SYSTEM_CREATION);

        //TODO: generating unique instance id
        instance.setInstanceId("some_generated_id");

        Instance createdInstance = repository.save(instance);
        InstanceDTO mappedInstance = mapper.map(createdInstance, InstanceDTO.class);

        return mappedInstance;
    }

    @Override
    public Optional<InstanceDTO> getInstance(long id) {

        Optional<Instance> instance = repository.findById(id);
        if(instance.isPresent()){
            InstanceDTO instanceDTO = mapper.map(instance.get(), InstanceDTO.class);
            return Optional.of(instanceDTO);
        }

        return Optional.empty();
    }

    @Override
    public List<InstanceDTO> getAllInstances() {

        List<Instance> instances = repository.findAll();
        List<InstanceDTO> mappedInstances = instances.stream().map(i -> mapper.map(i, InstanceDTO.class)).collect(Collectors.toList());

        return mappedInstances;
    }

    @Override
    public void deleteInstance(long id) {
        repository.deleteById(id);
    }


    @Override
    public InstanceDTO updateInstance(InstanceDTO instanceDTO,  Optional<InstanceDTO> result) {

            Map<String, String> keyToValue = instanceDTO.getAttributes().stream().collect(Collectors.toMap(
                    InstanceAttributeDTO::getKey,
                    InstanceAttributeDTO::getValue
            ));
            for (InstanceAttributeDTO instanceAttributeDTO : result.get().getAttributes()) {
                String newValue = keyToValue.get(instanceAttributeDTO.getKey());
                if (newValue != null) {
                    instanceAttributeDTO.setValue(newValue);
                    keyToValue.remove(instanceAttributeDTO.getKey());
                }
            }
            for (InstanceAttributeDTO instanceAttributeDTO : instanceDTO.getAttributes()) {
                if (keyToValue.containsKey(instanceAttributeDTO.getKey())) {
                    result.get().getAttributes().add(instanceAttributeDTO);
                }
            }

            Map<String, String> keyToValueParam = instanceDTO.getParameters().stream().collect(Collectors.toMap(
                    InstanceParameterDTO::getKey,
                    InstanceParameterDTO::getValue
            ));
            for (InstanceParameterDTO instanceParameterDTO : result.get().getParameters()) {
                String newValue = keyToValueParam.get(instanceParameterDTO.getKey());
                if (newValue != null) {
                    instanceParameterDTO.setValue(newValue);
                    keyToValueParam.remove(instanceParameterDTO.getKey());
                }
            }
            for (InstanceAttributeDTO instanceAttributeDTO : instanceDTO.getAttributes()) {
                if (keyToValueParam.containsKey(instanceAttributeDTO.getKey())) {
                    result.get().getAttributes().add(instanceAttributeDTO);
                }
            }
            save(instanceDTO);
            return instanceDTO;
    }


    @Override
    public void save(InstanceDTO instanceDTO) {

        Instance instance = mapper.map(instanceDTO, Instance.class);

        instance.getInstanceParameters().forEach(param -> param.setInstance(instance));
        instance.getInstanceAttributes().forEach(att -> att.setInstance(instance));
        repository.save(instance);
    }
}
