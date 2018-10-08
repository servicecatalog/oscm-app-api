/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.service.intf;

import org.oscm.app.dto.ConfigurationDTO;
import org.oscm.app.dto.ConfigurationSettingDTO;
import org.oscm.app.dto.ControllerDTO;

import java.util.List;
import java.util.Optional;

public interface ConfigurationService {

    List<ControllerDTO> getAvailableControllers();

    List<ConfigurationDTO> getAllConfigurations();

    ConfigurationDTO createConfiguration(ConfigurationDTO configuration);

    ConfigurationDTO updateConfiguration(ConfigurationDTO configuration);

    boolean checkIfAlreadyExists(ConfigurationDTO configuration);

    Optional<ConfigurationDTO> getConfigurationById(long id);

    void deleteConfiguration(long id);

    List<ConfigurationDTO> getConfigurationsForOrganization(
            String organizationId);

    List<ConfigurationSettingDTO> getConfigurationSettings(
            long configurationId);

    ConfigurationSettingDTO createConfigurationSetting(long configurationId,
            ConfigurationSettingDTO setting);

    Optional<ConfigurationSettingDTO> getConfigurationSettingById(
            long settingId);

    boolean checkIfSettingAlreadyExists(long configurationId, String key);

    ConfigurationSettingDTO updateConfigurationSetting(
            ConfigurationSettingDTO setting);

    void deleteConfigurationSetting(long id);

}
