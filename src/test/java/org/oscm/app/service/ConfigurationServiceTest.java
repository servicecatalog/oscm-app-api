package org.oscm.app.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.oscm.app.domain.Configuration;
import org.oscm.app.domain.ConfigurationSetting;
import org.oscm.app.domain.enumeration.Controller;
import org.oscm.app.dto.ConfigurationDTO;
import org.oscm.app.dto.ConfigurationSettingDTO;
import org.oscm.app.repository.ConfigurationRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
public class ConfigurationServiceTest {

    public static final String TEST_ORGANIZATION_ID = "org";
    @Mock
    ConfigurationRepository configurationRepository;

    @InjectMocks
    ConfigurationServiceImpl configurationService;

    @Before
    public void setup() {
        doReturn(Optional.of(new Configuration())).when(configurationRepository).findByOrganizationIdAndController(TEST_ORGANIZATION_ID, Controller.AWS);
    }


    @Test
    public void givenExistingConfigurationShouldFind() {
        //given
        ConfigurationDTO configuration = new ConfigurationDTO();
        configuration.setControllerId(Controller.AWS.getControllerId());
        configuration.setOrganizationId(TEST_ORGANIZATION_ID);

        //when
        boolean exists = configurationService.checkIfAlreadyExists(configuration);

        //then
        assertTrue(exists);
    }

    @Test
    public void givenExistingConfigurationShouldntFind() {
        //given
        ConfigurationDTO configuration = new ConfigurationDTO();
        configuration.setControllerId(Controller.AWS.getControllerId());
        configuration.setOrganizationId("not existing organization");

        //when
        boolean exists = configurationService.checkIfAlreadyExists(configuration);

        //then
        assertFalse(exists);
    }







}
