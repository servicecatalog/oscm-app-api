package org.oscm.app.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.oscm.app.service.intf.InstanceService;
import org.oscm.app.domain.enumeration.ProvisioningStatus;
import org.oscm.app.dto.InstanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InstanceResource.class)
public class InstanceResourceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InstanceService instanceService;

    @Test
    public void testGetInstanceResponse() throws Exception {

        // given
        InstanceDTO instanceDTO = new InstanceDTO();
        instanceDTO.setId(1);
        instanceDTO.setOrganizationId("sampleOrgId");
        instanceDTO.setReferenceId("13de453w");
        instanceDTO.setControllerId("ess.vmware");
//        instanceDTO.setProvisioningStatus(ProvisioningStatus.COMPLETED);
        instanceDTO.setSubscriptionId("sub_3451245");

        Mockito.when(instanceService.getInstance(1)).thenReturn(Optional.of(instanceDTO));

        // when and then
        mvc.perform(get("/instances/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationId").value("sampleOrgId"));

    }

    @Test
    public void testGetInstancesResponse() throws Exception {

        // given
        InstanceDTO instanceDTO = new InstanceDTO();
        instanceDTO.setId(1);
        instanceDTO.setOrganizationId("sampleOrgId1");
        instanceDTO.setReferenceId("4667666");
        instanceDTO.setControllerId("ess.vmware");
//        instanceDTO.setProvisioningStatus(ProvisioningStatus.WAITING_FOR_SYSTEM_CREATION);
        instanceDTO.setSubscriptionId("sub_1244565");

        InstanceDTO instanceDTO2 = new InstanceDTO();
        instanceDTO2.setId(2);
        instanceDTO2.setOrganizationId("sampleOrgId");
        instanceDTO2.setReferenceId("13de453w");
        instanceDTO2.setControllerId("ess.openstack");
//        instanceDTO2.setProvisioningStatus(ProvisioningStatus.COMPLETED);
        instanceDTO2.setSubscriptionId("sub_3451245");

        Mockito.when(instanceService.getAllInstances())
                .thenReturn(Arrays.asList(instanceDTO, instanceDTO2));

        // when and then
        mvc.perform(get("/instances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }
}
