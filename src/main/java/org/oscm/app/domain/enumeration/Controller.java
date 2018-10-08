/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.domain.enumeration;

public enum Controller {

    OPENSTACK("ess.openstack", "Openstack controller"), VMWARE("ess.vmware",
            "Vmware controller"), AZURE("ess.azure",
                    "Azure controller"), AWS("ess.aws", "AWS controller");

    Controller(String controllerId, String description) {
        this.controllerId = controllerId;
        this.description = description;
    }

    String controllerId;

    String description;

    public String getControllerId() {
        return controllerId;
    }

    public String getDescription() {
        return description;
    }
}
