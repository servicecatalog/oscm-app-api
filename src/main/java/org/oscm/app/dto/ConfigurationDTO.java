/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.oscm.app.domain.enumeration.Controller;
import org.oscm.app.validation.ControllerId;
import org.oscm.app.validation.ReadOnly;

import javax.validation.constraints.NotEmpty;

@ApiModel(value = "Configuration", description = "Simple configuration with organization and controller relation")
public class ConfigurationDTO {

    @ApiModelProperty(position = 1, notes = "Identifier of existing configuration", readOnly = true)
    private long id;

    @ApiModelProperty(position = 2, notes = "Id of the APP controller", allowableValues = "ess.aws, ess.openstack, ess.azure, ess.vmware", required = true)
    @NotEmpty
    @ControllerId
    private String controllerId;

    @ApiModelProperty(position = 3, notes = "Id of existing organization", required = true)
    @NotEmpty
    private String organizationId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
