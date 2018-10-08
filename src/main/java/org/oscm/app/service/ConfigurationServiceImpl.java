/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.service;

import org.modelmapper.ModelMapper;
import org.oscm.app.domain.Configuration;
import org.oscm.app.domain.ConfigurationSetting;
import org.oscm.app.domain.enumeration.Controller;
import org.oscm.app.dto.ConfigurationDTO;
import org.oscm.app.dto.ConfigurationSettingDTO;
import org.oscm.app.dto.ControllerDTO;
import org.oscm.app.repository.ConfigurationRepository;
import org.oscm.app.repository.ConfigurationSettingRepository;
import org.oscm.app.service.intf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ConfigurationSettingRepository settingRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<ControllerDTO> getAvailableControllers() {

        List<Controller> controllers = Arrays.asList(Controller.values());
        List<ControllerDTO> mappedControllers = controllers.stream()
                .map(controller -> mapper.map(controller, ControllerDTO.class))
                .collect(Collectors.toList());
        return mappedControllers;
    }

    @Override
    public ConfigurationDTO createConfiguration(
            ConfigurationDTO configuration) {

        Configuration organization = toConfiguration(configuration);
        Configuration createdOrganization = configurationRepository
                .save(organization);

        return toConfigurationDTO(createdOrganization);
    }

    @Override
    public ConfigurationDTO updateConfiguration(
            ConfigurationDTO configuration) {

        String orgId = configuration.getOrganizationId();
        String controllerId = configuration.getControllerId();

        Configuration storedConfiguration = configurationRepository
                .getOne(configuration.getId());
        storedConfiguration.setOrganizationId(orgId);
        storedConfiguration
                .setController(getControllerById(controllerId).get());

        Configuration createdOrganization = configurationRepository
                .save(storedConfiguration);

        return toConfigurationDTO(createdOrganization);
    }

    @Override
    public List<ConfigurationDTO> getAllConfigurations() {

        List<Configuration> configurations = configurationRepository.findAll();
        List<ConfigurationDTO> dtos = configurations.stream()
                .map(c -> toConfigurationDTO(c)).collect(Collectors.toList());

        return dtos;
    }

    @Override
    public boolean checkIfAlreadyExists(ConfigurationDTO configuration) {

        String controllerId = configuration.getControllerId();
        String orgId = configuration.getOrganizationId();
        Optional<Controller> controller = getControllerById(controllerId);

        Optional<Configuration> optional = configurationRepository
                .findByOrganizationIdAndController(orgId, controller.get());

        return optional.isPresent();
    }

    @Override
    public Optional<ConfigurationDTO> getConfigurationById(long id) {

        Optional<Configuration> configuration = configurationRepository
                .findById(id);
        if (configuration.isPresent()) {
            ConfigurationDTO configurationDTO = toConfigurationDTO(
                    configuration.get());
            return Optional.of(configurationDTO);
        }

        return Optional.empty();
    }

    @Override
    public void deleteConfiguration(long id) {

        configurationRepository.deleteById(id);
    }

    @Override
    public List<ConfigurationDTO> getConfigurationsForOrganization(
            String organizationId) {
        List<Configuration> configurations = configurationRepository
                .findByOrganizationId(organizationId);
        List<ConfigurationDTO> mappedConfigurations = configurations.stream()
                .map(c -> toConfigurationDTO(c)).collect(Collectors.toList());

        return mappedConfigurations;
    }

    @Override
    public List<ConfigurationSettingDTO> getConfigurationSettings(
            long configurationId) {

        Configuration configuration = configurationRepository
                .getOne(configurationId);
        List<ConfigurationSetting> settings = settingRepository
                .findByConfiguration(configuration);

        List<ConfigurationSettingDTO> mappedSettings = settings.stream().map(
                setting -> mapper.map(setting, ConfigurationSettingDTO.class))
                .collect(Collectors.toList());

        return mappedSettings;
    }

    @Override
    public ConfigurationSettingDTO createConfigurationSetting(
            long configurationId, ConfigurationSettingDTO settingDTO) {

        ConfigurationSetting setting = mapper.map(settingDTO,
                ConfigurationSetting.class);
        Configuration configuration = configurationRepository
                .getOne(configurationId);
        setting.setConfiguration(configuration);

        ConfigurationSetting createdSetting = settingRepository.save(setting);
        ConfigurationSettingDTO mappedSetting = mapper.map(createdSetting,
                ConfigurationSettingDTO.class);
        return mappedSetting;
    }

    @Override
    public Optional<ConfigurationSettingDTO> getConfigurationSettingById(
            long settingId) {

        Optional<ConfigurationSetting> setting = settingRepository
                .findById(settingId);
        if (setting.isPresent()) {
            ConfigurationSettingDTO mappedSetting = mapper.map(setting.get(),
                    ConfigurationSettingDTO.class);
            return Optional.of(mappedSetting);
        }
        return Optional.empty();
    }

    @Override
    public boolean checkIfSettingAlreadyExists(long configurationId,
            String key) {

        Configuration configuration = configurationRepository
                .getOne(configurationId);
        List<ConfigurationSetting> settings = settingRepository
                .findByConfiguration(configuration);

        Optional<ConfigurationSetting> matchingSetting = settings.stream()
                .filter(setting -> key.equals(setting.getKey())).findFirst();

        return matchingSetting.isPresent();
    }

    @Override
    public ConfigurationSettingDTO updateConfigurationSetting(
            ConfigurationSettingDTO setting) {

        ConfigurationSetting existingSetting = settingRepository
                .getOne(setting.getId());
        existingSetting.setKey(setting.getKey());
        existingSetting.setValue(setting.getValue());

        ConfigurationSetting updatedSetting = settingRepository
                .save(existingSetting);

        return mapper.map(updatedSetting, ConfigurationSettingDTO.class);
    }

    @Override
    public void deleteConfigurationSetting(long id) {

        settingRepository.deleteById(id);
    }

    private Configuration toConfiguration(ConfigurationDTO dto) {

        String controllerId = dto.getControllerId();
        Optional<Controller> controller = getControllerById(controllerId);

        Configuration configuration = new Configuration();
        configuration.setOrganizationId(dto.getOrganizationId());
        configuration.setController(controller.get());

        return configuration;
    }

    private ConfigurationDTO toConfigurationDTO(Configuration configuration) {

        ConfigurationDTO dto = new ConfigurationDTO();
        dto.setId(configuration.getId());
        dto.setOrganizationId(configuration.getOrganizationId());
        dto.setControllerId(configuration.getController().getControllerId());

        return dto;
    }

    private Optional<Controller> getControllerById(String controllerId) {

        Optional<Controller> controller = Arrays.stream(Controller.values())
                .filter(c -> c.getControllerId().equals(controllerId))
                .findFirst();

        return controller;
    }
}
