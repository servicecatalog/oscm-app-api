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
import org.oscm.app.dto.InstanceDTO;
import org.oscm.app.service.intf.InstanceService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InstanceResource.class)
public class InstanceResourceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private InstanceService instanceService;

    @Test
    public void testGetInstance() throws Exception {

        // given
        InstanceDTO instanceDTO = newInstance();
        long id = instanceDTO.getId();

        // when
        when(instanceService.getInstance(id)).thenReturn(Optional.of(instanceDTO));

        // then
        mvc.perform(get("/instances/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId").value(instanceDTO.getOrganizationId()));
    }

    @Test
    public void testGetInstances() throws Exception {

        // given
        InstanceDTO instanceDTO = newInstance();
        InstanceDTO instanceDTO2 = newInstance();

        // when
        when(instanceService.getAllInstances())
                .thenReturn(Arrays.asList(instanceDTO, instanceDTO2));

        // when then
        mvc.perform(get("/instances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testWhetherInstanceWasCreated() throws Exception {

        //given
        InstanceDTO instanceDTO = newInstance();

        //when
        when(instanceService.createInstance(any(InstanceDTO.class)))
                .thenReturn(instanceDTO);

        String instance = mapper.writeValueAsString(instanceDTO);

        //then
        mvc.perform(post("/instances").contentType(MediaType.APPLICATION_JSON_UTF8).content(instance))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.organizationId").value(instanceDTO.getOrganizationId()))
                .andExpect(jsonPath("$.referenceId").value(instanceDTO.getReferenceId()))
                .andExpect(jsonPath("$.subscriptionId").value(instanceDTO.getSubscriptionId()));
    }

    @Test
    public void testDeleteInstance() throws Exception {

        //given
        InstanceDTO instanceDTO = newInstance();
        long id = instanceDTO.getId();
        Optional<InstanceDTO> instanceDTO1 = Optional.of(instanceDTO);

        //when
        when(instanceService.getInstance(id)).thenReturn(instanceDTO1);
        doNothing().when(instanceService).deleteInstance(id);

        //then
        mvc.perform(delete("/instances/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    private InstanceDTO newInstance() {
        InstanceDTO instanceDTO = new InstanceDTO();
        instanceDTO.setId(1);
        instanceDTO.setOrganizationId("sampleOrgId1");
        instanceDTO.setReferenceId("4667666");
        instanceDTO.setControllerId("ess.vmware");
        instanceDTO.setSubscriptionId("sub_1244565");
        instanceDTO.setOrganizationName("sampleOrganization");
        return instanceDTO;
    }

}
