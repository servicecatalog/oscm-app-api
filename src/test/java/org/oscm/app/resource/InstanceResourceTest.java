package org.oscm.app.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.oscm.app.service.intf.InstanceService;
import org.oscm.app.dto.InstanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(InstanceResource.class)
public class InstanceResourceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InstanceService instanceService;


    @Autowired
    private ObjectMapper mapper;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Test
    public void testGetInstanceResponse() throws Exception {

        // given
        InstanceDTO instanceDTO = newInstance();

        Mockito.when(instanceService.getInstance(1)).thenReturn(Optional.of(instanceDTO));

        // when and then
        mvc.perform(get("/instances/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId").value("sampleOrgId1"));

    }

    @Test
    public void testGetInstancesResponse() throws Exception {

        // given
        InstanceDTO instanceDTO = newInstance();

        InstanceDTO instanceDTO2 = new InstanceDTO();
        instanceDTO2.setId(2);
        instanceDTO2.setOrganizationId("sampleOrgId");
        instanceDTO2.setReferenceId("13de453w");
        instanceDTO2.setControllerId("ess.openstack");
        instanceDTO2.setSubscriptionId("sub_3451245");

        Mockito.when(instanceService.getAllInstances())
                .thenReturn(Arrays.asList(instanceDTO, instanceDTO2));

        // when and then
        mvc.perform(get("/instances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testWhetherInstanceWasCreated() throws Exception {

        //given
        InstanceDTO instanceDTO = newInstance();


        //when
        Mockito.when(instanceService.createInstance(any(InstanceDTO.class)))
        .thenReturn(instanceDTO);

        String instance = mapper.writeValueAsString(instanceDTO);


        //then
        mvc.perform(post("/instances").contentType(APPLICATION_JSON_UTF8).content(instance))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.organizationId").value("sampleOrgId1"))
                .andExpect(jsonPath("$.referenceId").value("4667666"))
                .andExpect(jsonPath("$.subscriptionId").value("sub_1244565"));


    }


    @Test
    public void testDeletingInstance()  throws Exception {

        //given
        InstanceDTO instanceDTO = newInstance();
        Optional<InstanceDTO> instanceDTO1 = Optional.of(instanceDTO);

        //when
        when(instanceService.getInstance(1)).thenReturn(instanceDTO1);
        doNothing().when(instanceService).deleteInstance(1);


        //then
        mvc.perform(delete("/instances/{id}", 1).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

    }

    private InstanceDTO newInstance(){
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
