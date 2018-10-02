package org.oscm.app.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.oscm.app.service.intf.InstanceService;
import org.oscm.app.domain.Instance;
import org.oscm.app.domain.enumeration.ProvisioningStatus;
import org.oscm.app.dto.InstanceDTO;
import org.oscm.app.repository.InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class InstanceServiceTest {

    @TestConfiguration
    public static class InstanceDAOServiceDBTestConfiguration {

        @Bean
        public InstanceService instanceDAOService() {
            return new InstanceServiceImpl();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Autowired
    private InstanceService service;

    @MockBean
    private InstanceRepository repository;

    @Test
    public void testFindById() {

        //given
        Instance instance = new Instance();
        instance.setId(1);
        instance.setOrganizationId("sampleOrgId");
        instance.setReferenceId("13de453w");
        instance.setControllerId("ess.vmware");
        instance.setProvisioningStatus(ProvisioningStatus.COMPLETED);
        instance.setSubscriptionId("sub_3451245");
        long id = 1;
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(instance));

        //when
        InstanceDTO instanceDTO = service.getInstance(1).get();

        //then
        assertEquals(instanceDTO.getOrganizationId(), instance.getOrganizationId());
        assertEquals(instanceDTO.getReferenceId(), instance.getReferenceId());
        assertEquals(instanceDTO.getControllerId(), instance.getControllerId());
        assertEquals(instanceDTO.getProvisioningStatus(), instance.getProvisioningStatus().toString());
        assertEquals(instanceDTO.getSubscriptionId(), instance.getSubscriptionId());
    }


}
