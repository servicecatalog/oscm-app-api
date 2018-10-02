package org.oscm.app.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.oscm.app.domain.Instance;
import org.oscm.app.domain.enumeration.ProvisioningStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InstanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InstanceRepository repository;

    @Test
    public void testFindAll() {

        //given
        Instance instance = new Instance();
        instance.setOrganizationId("sampleOrgId");
        instance.setReferenceId("13de453w");
        instance.setControllerId("ess.vmware");
        instance.setProvisioningStatus(ProvisioningStatus.WAITING_FOR_SYSTEM_CREATION);
        instance.setSubscriptionId("sub_3451245");
        instance.setInstanceId("inst_456334");
        instance.setRequestTime(LocalDateTime.now());

        Instance instance2 = new Instance();
        instance2.setOrganizationId("sampleOrgId");
        instance2.setReferenceId("13de453w");
        instance2.setControllerId("ess.vmware");
        instance2.setProvisioningStatus(ProvisioningStatus.WAITING_FOR_SYSTEM_CREATION);
        instance2.setSubscriptionId("sub_3451245");
        instance2.setInstanceId("inst_456334");
        instance2.setRequestTime(LocalDateTime.now());

        entityManager.persist(instance);
        entityManager.persist(instance2);

        // when
        List<Instance> instances = repository.findAll();

        //then
        assertEquals(10, instances.size());
    }

}
