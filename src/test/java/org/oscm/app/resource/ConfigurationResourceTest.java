package org.oscm.app.resource;


import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.oscm.app.domain.Configuration;
import org.oscm.app.dto.ConfigurationDTO;
import org.oscm.app.dto.ConfigurationSettingDTO;
import org.oscm.app.dto.ControllerDTO;
import org.oscm.app.service.intf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.modelmapper.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ConfigurationResource.class)
public class ConfigurationResourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ConfigurationService configurationService;


    @Mock
    ConfigurationResource configurationResource;


    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );


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
                .thenReturn(Arrays.asList(configurationDTO1));

        //then
        mvc.perform(get("/configurations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    public void testGetConfigurationForOgranization()throws Exception{
        //given
        String organizationId = "aws";
        ConfigurationDTO configurationDTO1 = configurationDTO1();

        //when
        Mockito.when(configurationService.getConfigurationsForOrganization(organizationId))
                .thenReturn(Arrays.asList(configurationDTO1));


        //then
        mvc.perform(get("/configurations").param("organizationId", organizationId)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
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
        configurationDTO.setOrganizationId("aws");
        return configurationDTO;
    }

    private ConfigurationDTO configurationDTO2(){
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setId(2);
        configurationDTO.setControllerId("ess.aws");
        configurationDTO.setOrganizationId("azure org");
        return configurationDTO;
    }
}
