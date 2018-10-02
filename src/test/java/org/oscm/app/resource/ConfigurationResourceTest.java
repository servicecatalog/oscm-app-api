package org.oscm.app.resource;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import org.mockito.Mockito;
import org.oscm.app.dto.ConfigurationDTO;
import org.oscm.app.dto.ConfigurationSettingDTO;
import org.oscm.app.dto.ControllerDTO;
import org.oscm.app.dto.InstanceDTO;
import org.oscm.app.service.intf.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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


    @Autowired
    private ObjectMapper mapper;


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
        when(configurationService.getConfigurationsForOrganization(organizationId))
                .thenReturn(Arrays.asList(configurationDTO1));


        //then
        mvc.perform(get("/configurations").param("organizationId", organizationId)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }


    @Test
    public void testCreatingCofiguration() throws Exception{
        ConfigurationDTO configurationDTO = configurationDTO1();

        //when
        when(configurationService.checkIfAlreadyExists(configurationDTO))
                .thenReturn(true);

        when(configurationService.createConfiguration(any(ConfigurationDTO.class)))
                .thenReturn(configurationDTO);


        String configuration = mapper.writeValueAsString(configurationDTO);

        //then
        mvc.perform(post("/configurations/").contentType(MediaType.APPLICATION_JSON_UTF8).content(configuration))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.organizationId").value("aws"));
    }

    @MockBean
    ConfigurationResource configurationResource;

    @Test
    public void testUpdatingConfiguration() throws Exception{
        //given
        ConfigurationDTO configurationDTO = configurationDTO1();


        //when
        when(configurationService.getConfigurationById(1)).
                thenReturn(Optional.of(configurationDTO));

        when(configurationService.checkIfAlreadyExists(configurationDTO))
                .thenReturn(false);

        configurationDTO.setOrganizationId("changedOrg");

        when(configurationService.updateConfiguration(configurationDTO))
                .thenReturn(configurationDTO);

        String configuration = mapper.writeValueAsString(configurationDTO);
        String longId = String.valueOf(1);

        //then
        mvc.perform(put("/configurations/{configurationId}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(configuration))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId").value("changedOrg"));
    }



    @Test
    public void testDeletingConfiguration() throws Exception{

        ConfigurationDTO configurationDTO = configurationDTO1();
        Optional<ConfigurationDTO> configurationDTO1 = Optional.of(configurationDTO);

        when(configurationService.getConfigurationById(1)).thenReturn(configurationDTO1);
        doNothing().when(configurationService).deleteConfiguration(1);

        String longId = String.valueOf(1);

        //then
        mvc.perform(delete("/configurations/{configurationId}", 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

    }


    @Test
    public void testGettingConfigurationSettings() throws Exception{
        //given



    }


    @Test
    public void testGettingConfiguration() throws Exception{
        long confId = 1;
        ConfigurationSettingDTO configurationSettingDTO = configurationSettingDTO();

        //when
        Mockito.when(configurationService.getConfigurationSettings(1)).thenReturn(Arrays.asList(configurationSettingDTO));

        //then

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

    private ConfigurationSettingDTO configurationSettingDTO(){
        ConfigurationSettingDTO confSettDTO = new ConfigurationSettingDTO();
        confSettDTO.setId(1);
        confSettDTO.setKey("firstKey");
        confSettDTO.setValue("firstVal");
        return confSettDTO;
    }
}
