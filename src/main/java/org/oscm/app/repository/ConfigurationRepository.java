/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.repository;

import org.oscm.app.domain.Configuration;
import org.oscm.app.domain.enumeration.Controller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfigurationRepository
        extends JpaRepository<Configuration, Long> {

    Optional<Configuration> findByOrganizationIdAndController(
            String organizationId, Controller controller);

    List<Configuration> findByOrganizationId(String organizationId);
}
