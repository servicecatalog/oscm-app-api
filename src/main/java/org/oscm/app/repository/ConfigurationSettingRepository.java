/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.repository;

import org.oscm.app.domain.Configuration;
import org.oscm.app.domain.ConfigurationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigurationSettingRepository
        extends JpaRepository<ConfigurationSetting, Long> {

    List<ConfigurationSetting> findByConfiguration(Configuration configuration);
}
