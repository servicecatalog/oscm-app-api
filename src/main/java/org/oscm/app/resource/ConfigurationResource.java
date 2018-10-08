/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.resource;

import io.swagger.annotations.*;
import org.oscm.app.dto.ConfigurationDTO;
import org.oscm.app.dto.ConfigurationSettingDTO;
import org.oscm.app.dto.ControllerDTO;
import org.oscm.app.exception.ExceptionResponse;
import org.oscm.app.exception.ObjectNotFoundException;
import org.oscm.app.exception.ValidationException;
import org.oscm.app.service.intf.ConfigurationService;
import org.oscm.app.util.ApiParamValue;
import org.oscm.app.util.HttpStatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Api("Operations related to configuration resource")
@RestController
public class ConfigurationResource {

    @Autowired
    private ConfigurationService configurationService;

    @ApiOperation("Retrieves the list of available controllers")
    @GetMapping("/controllers")
    public ResponseEntity<List<ControllerDTO>> getAllControllers() {

        List<ControllerDTO> controllers = configurationService
                .getAvailableControllers();
        return ResponseEntity.ok(controllers);
    }

    @ApiOperation("Retrieves the list of all configurations")
    @GetMapping("/configurations")
    public ResponseEntity<List<ConfigurationDTO>> getAllConfigurations() {

        List<ConfigurationDTO> configurations = configurationService
                .getAllConfigurations();
        return ResponseEntity.ok(configurations);
    }

    @ApiOperation("Retrieves the list of configurations for specific organization")
    @GetMapping(value = "/configurations", params = "organizationId")
    public ResponseEntity<List<ConfigurationDTO>> getConfigurationsForOrganization(
            @ApiParam(value = ApiParamValue.ORGANIZATION_ID, required = true) @RequestParam String organizationId) {

        List<ConfigurationDTO> configurations = configurationService
                .getConfigurationsForOrganization(organizationId);

        return ResponseEntity.ok(configurations);
    }

    @ApiOperation("Creates new configuration")
    @ApiResponses({
            @ApiResponse(code = 400, message = HttpStatusMessage.MSG_400, response = ExceptionResponse.class) })
    @PostMapping("/configurations")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ConfigurationDTO> createConfiguration(
            @ApiParam(value = ApiParamValue.CONFIGURATION, required = true) @Valid @RequestBody ConfigurationDTO configuration) {

        checkIfConfigurationAlreadyExists(configuration);
        ConfigurationDTO createdConfiguration = configurationService
                .createConfiguration(configuration);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdConfiguration.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdConfiguration);
    }

    @ApiOperation("Retrieves existing configuration")
    @ApiResponses({
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class) })
    @GetMapping("/configurations/{configurationId}")
    public ResponseEntity<ConfigurationDTO> getConfiguration(
            @ApiParam(value = ApiParamValue.CONFIGURATION_ID, required = true) @PathVariable long configurationId) {

        ConfigurationDTO configuration = checkIfIdOfConfigurationIsValid(
                configurationId);

        return ResponseEntity.ok(configuration);
    }

    @ApiOperation("Updates existing configuration")
    @ApiResponses({
            @ApiResponse(code = 400, message = HttpStatusMessage.MSG_400, response = ExceptionResponse.class),
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class) })
    @PutMapping("/configurations/{configurationId}")
    public ResponseEntity<ConfigurationDTO> updateConfiguration(
            @ApiParam(value = ApiParamValue.CONFIGURATION_ID, required = true) @PathVariable long configurationId,
            @ApiParam(value = ApiParamValue.CONFIGURATION, required = true) @Valid @RequestBody ConfigurationDTO configuration) {

        checkIfIdOfConfigurationIsValid(configurationId);
        configuration.setId(configurationId);
        checkIfConfigurationAlreadyExists(configuration);
        ConfigurationDTO updatedConfiguration = configurationService
                .updateConfiguration(configuration);

        return ResponseEntity.ok(updatedConfiguration);
    }

    @ApiOperation("Deletes configuration")
    @ApiResponses({
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class) })
    @DeleteMapping("/configurations/{configurationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteConfiguration(
            @ApiParam(value = ApiParamValue.CONFIGURATION_ID, required = true) @PathVariable long configurationId) {

        checkIfIdOfConfigurationIsValid(configurationId);
        configurationService.deleteConfiguration(configurationId);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Retrieves configuration settings")
    @ApiResponses({
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class) })
    @GetMapping("/configurations/{configurationId}/settings")
    public ResponseEntity<List<ConfigurationSettingDTO>> getConfigurationSettings(
            @ApiParam(value = ApiParamValue.CONFIGURATION_ID, required = true) @PathVariable long configurationId) {

        checkIfIdOfConfigurationIsValid(configurationId);
        List<ConfigurationSettingDTO> settings = configurationService
                .getConfigurationSettings(configurationId);

        return ResponseEntity.ok(settings);
    }

    @ApiOperation("Creates new configuration's setting")
    @ApiResponses({
            @ApiResponse(code = 400, message = HttpStatusMessage.MSG_400, response = ExceptionResponse.class),
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class) })
    @PostMapping("/configurations/{configurationId}/settings")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ConfigurationSettingDTO> createConfigurationSettings(
            @ApiParam(value = ApiParamValue.CONFIGURATION_ID, required = true) @PathVariable long configurationId,
            @ApiParam(value = ApiParamValue.SETTING, required = true) @Valid @RequestBody ConfigurationSettingDTO setting) {

        checkIfIdOfConfigurationIsValid(configurationId);
        checkIfSettingAlreadyExists(configurationId, setting);

        ConfigurationSettingDTO createdSetting = configurationService
                .createConfigurationSetting(configurationId, setting);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{settingId}").buildAndExpand(createdSetting.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdSetting);
    }

    @ApiOperation("Retrieves existing configuration's setting")
    @ApiResponses({
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class) })
    @GetMapping("/configurations/{configurationId}/settings/{settingId}")
    public ResponseEntity<ConfigurationSettingDTO> getConfigurationSetting(
            @ApiParam(value = ApiParamValue.CONFIGURATION_ID, required = true) @PathVariable long configurationId,
            @ApiParam(value = ApiParamValue.SETTING_ID, required = true) @PathVariable long settingId) {

        checkIfIdOfConfigurationIsValid(configurationId);
        ConfigurationSettingDTO setting = checkIfIdOfSettingIsValid(settingId);

        return ResponseEntity.ok(setting);
    }

    @ApiOperation("Updates existing configuration's setting")
    @ApiResponses({
            @ApiResponse(code = 400, message = HttpStatusMessage.MSG_400, response = ExceptionResponse.class),
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class) })
    @PutMapping("/configurations/{configurationId}/settings/{settingId}")
    public ResponseEntity<ConfigurationSettingDTO> updateConfigurationSetting(
            @ApiParam(value = ApiParamValue.CONFIGURATION_ID, required = true) @PathVariable long configurationId,
            @ApiParam(value = ApiParamValue.SETTING_ID, required = true) @PathVariable long settingId,
            @ApiParam(value = ApiParamValue.SETTING, required = true) @RequestBody @Valid ConfigurationSettingDTO setting) {

        checkIfIdOfConfigurationIsValid(configurationId);
        checkIfIdOfSettingIsValid(settingId);

        setting.setId(settingId);
        ConfigurationSettingDTO updatedSetting = configurationService
                .updateConfigurationSetting(setting);

        return ResponseEntity.ok(updatedSetting);
    }

    @ApiOperation("Deletes configuration's setting")
    @ApiResponses({
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class) })
    @DeleteMapping("/configurations/{configurationId}/settings/{settingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteConfigurationSetting(
            @ApiParam(value = ApiParamValue.CONFIGURATION_ID, required = true) @PathVariable long configurationId,
            @ApiParam(value = ApiParamValue.SETTING_ID, required = true) @PathVariable long settingId) {

        checkIfIdOfConfigurationIsValid(configurationId);
        checkIfIdOfSettingIsValid(settingId);

        configurationService.deleteConfigurationSetting(settingId);

        return ResponseEntity.noContent().build();
    }

    private void checkIfConfigurationAlreadyExists(
            ConfigurationDTO configuration) {

        boolean exists = configurationService
                .checkIfAlreadyExists(configuration);
        if (exists) {
            String controllerId = configuration.getControllerId();
            String orgId = configuration.getOrganizationId();
            throw new ValidationException(
                    "Configuration [controllerId=" + controllerId + ", "
                            + "organizationId=" + orgId + "] already exists");
        }
    }

    private void checkIfSettingAlreadyExists(long configurationId,
            ConfigurationSettingDTO setting) {

        String settingKey = setting.getKey();
        boolean exists = configurationService
                .checkIfSettingAlreadyExists(configurationId, settingKey);
        if (exists) {
            throw new ValidationException("Setting [key=" + settingKey
                    + "] already exists in " + "configuration [configurationId="
                    + configurationId + "]");
        }
    }

    private ConfigurationDTO checkIfIdOfConfigurationIsValid(long id) {

        Optional<ConfigurationDTO> configuration = configurationService
                .getConfigurationById(id);
        if (!configuration.isPresent()) {
            throw new ObjectNotFoundException(
                    "Configuration [id=" + id + "] has not been found");
        }
        return configuration.get();
    }

    private ConfigurationSettingDTO checkIfIdOfSettingIsValid(long settingId) {

        Optional<ConfigurationSettingDTO> setting = configurationService
                .getConfigurationSettingById(settingId);
        if (!setting.isPresent()) {
            throw new ObjectNotFoundException(
                    "Setting [id=" + settingId + "] has not been found");
        }
        return setting.get();
    }
}
