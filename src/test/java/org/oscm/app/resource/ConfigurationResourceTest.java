/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oscm.app.dto.ConfigurationDTO;
import org.oscm.app.dto.ControllerDTO;
import org.oscm.app.service.intf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ConfigurationResource.class)
public class ConfigurationResourceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ConfigurationService configurationService;

    @Test
    public void testGetAllControllers() throws Exception{

        //given
        ControllerDTO controllerDTO1 = controllerDTO1();
        ControllerDTO controllerDTO2 = controllerDTO2();

        //when
        when(configurationService.getAvailableControllers())
                .thenReturn(Arrays.asList(controllerDTO1, controllerDTO2));

        //then
        mvc.perform(get("/controllers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetAllConfigurations() throws Exception{

        //given
        ConfigurationDTO configurationDTO1 = configurationDTO1();
        ConfigurationDTO configurationDTO2 = configurationDTO2();

        //when
        when(configurationService.getAllConfigurations())
                .thenReturn(Arrays.asList(configurationDTO1, configurationDTO2));

        //then
        mvc.perform(get("/configurations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetConfigurationForOrganization()throws Exception{

        //given
        ConfigurationDTO configurationDTO1 = configurationDTO1();
        String organizationId = configurationDTO1.getOrganizationId();

        //when
        when(configurationService.getConfigurationsForOrganization(anyString()))
                .thenReturn(Arrays.asList(configurationDTO1));

        //then
        mvc.perform(get("/configurations").param("organizationId", organizationId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testCreateConfiguration() throws Exception{

        //given
        ConfigurationDTO configurationDTO = configurationDTO1();
        String organizationId = configurationDTO.getOrganizationId();

        //when
        when(configurationService.checkIfAlreadyExists(any(ConfigurationDTO.class)))
                .thenReturn(false);

        when(configurationService.createConfiguration(any(ConfigurationDTO.class)))
                .thenReturn(configurationDTO);

        String configuration = mapper.writeValueAsString(configurationDTO);

        //then
        mvc.perform(post("/configurations/").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(configuration))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.organizationId").value(organizationId));
    }

    @Test
    public void testUpdateConfiguration() throws Exception{

        //given
        ConfigurationDTO configurationDTO = configurationDTO1();
        long id = configurationDTO.getId();

        //when
        when(configurationService.getConfigurationById(id)).thenReturn(Optional.of(configurationDTO));

        when(configurationService.checkIfAlreadyExists(configurationDTO)).thenReturn(false);

        configurationDTO.setOrganizationId("changedOrg");

        when(configurationService.updateConfiguration(any(ConfigurationDTO.class)))
                .thenReturn(configurationDTO);

        String configuration = mapper.writeValueAsString(configurationDTO);

        //then
        mvc.perform(put("/configurations/{configurationId}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(configuration))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId").value("changedOrg"));
    }

    @Test
    public void testDeleteConfiguration() throws Exception{

        //given
        Optional<ConfigurationDTO> configurationDTO1 = Optional.of(configurationDTO1());
        long id = configurationDTO1.get().getId();

        //when
        when(configurationService.getConfigurationById(id)).thenReturn(configurationDTO1);
        doNothing().when(configurationService).deleteConfiguration(id);

        //then
        mvc.perform(delete("/configurations/{configurationId}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    private ControllerDTO controllerDTO1(){
        ControllerDTO controllerDTO = new ControllerDTO();
        controllerDTO.setControllerId("1");
        controllerDTO.setDescription("lorem ipsum");
        return controllerDTO;
    }

    private ControllerDTO controllerDTO2(){
        ControllerDTO controllerDTO = new ControllerDTO();
        controllerDTO.setControllerId("2");
        controllerDTO.setDescription("lorem ipsum lorem ipsum...");
        return controllerDTO;
    }

    private ConfigurationDTO configurationDTO1(){
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setId(1);
        configurationDTO.setControllerId("ess.aws");
        configurationDTO.setOrganizationId("ui71wws3");
        return configurationDTO;
    }

    private ConfigurationDTO configurationDTO2(){
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setId(2);
        configurationDTO.setControllerId("ess.aws");
        configurationDTO.setOrganizationId("hgt541q7");
        return configurationDTO;
    }
}
