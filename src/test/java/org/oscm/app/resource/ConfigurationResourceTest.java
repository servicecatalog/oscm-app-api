package org.oscm.app.resource;


import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import org.mockito.Mockito;
import org.oscm.app.dto.ConfigurationDTO;
import org.oscm.app.dto.ControllerDTO;
import org.oscm.app.service.intf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ConfigurationResource.class)
public class ConfigurationResourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ConfigurationService configurationService;


    @Test
    public void testGetAllControllers() throws Exception{

        //given
        ControllerDTO controllerDTO1 = controllerDTO1();
        ControllerDTO controllerDTO2 = controllerDTO2();
        //when
        Mockito.when(configurationService.getAvailableControllers())
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
        Mockito.when(configurationService.getAllConfigurations())
                .thenReturn(Arrays.asList(configurationDTO1, configurationDTO2));

        //then
        mvc.perform(get("/configurations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    public void testGetConfigurationForOgranization()throws Exception{

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
        configurationDTO.setControllerId("1234");
        configurationDTO.setOrganizationId("aws org");
        return configurationDTO;
    }

    private ConfigurationDTO configurationDTO2(){
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setId(2);
        configurationDTO.setControllerId("4321");
        configurationDTO.setOrganizationId("azure org");
        return configurationDTO;
    }
}
