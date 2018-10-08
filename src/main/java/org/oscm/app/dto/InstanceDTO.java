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
import org.oscm.app.validation.ControllerId;
import org.oscm.app.validation.ReadOnly;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "Instance", description = "Instance to be provisioned by the system")
public class InstanceDTO {

    @ApiModelProperty(position = 1, notes = "Identifier of existing instance", readOnly = true)
    private long id;

    @ApiModelProperty(position = 2, notes = "Identifier of existing organization", required = true)
    @NotEmpty
    private String organizationId;

    @ApiModelProperty(position = 3, notes = "Name of existing organization")
    private String organizationName;

    @ApiModelProperty(position = 4, notes = "Identifier of related subscription", required = true)
    @NotEmpty
    private String subscriptionId;

    @ApiModelProperty(position = 5, notes = "Unique identifier of related subscription", required = true)
    private String referenceId;

    @ApiModelProperty(position = 6, notes = "Status of instance provisioning process", readOnly = true)
    @ReadOnly
    private String provisioningStatus;

    @ApiModelProperty(position = 7, notes = "Latest message related to instance provisioning process", readOnly = true)
    @ReadOnly
    private String provisioningMsg;

    @ApiModelProperty(position = 8, notes = "Timestamp of the instance creation", readOnly = true)
    @ReadOnly
    private String requestTime;

    @ApiModelProperty(position = 9, notes = "Unique, automatically generated instance's identifier", readOnly = true)
    @ReadOnly
    private String instanceId;

    @ControllerId
    @ApiModelProperty(position = 10, notes = "Id of the APP controller", allowableValues = "ess.aws, ess.openstack, ess.azure, ess.vmware", required = true)
    @NotEmpty
    private String controllerId;

    @ApiModelProperty(position = 11)
    private List<InstanceParameterDTO> parameters = new ArrayList<>();

    @ApiModelProperty(position = 12)
    private List<InstanceAttributeDTO> attributes = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getProvisioningStatus() {
        return provisioningStatus;
    }

    public void setProvisioningStatus(String provisioningStatus) {
        this.provisioningStatus = provisioningStatus;
    }

    public String getProvisioningMsg() {
        return provisioningMsg;
    }

    public void setProvisioningMsg(String provisioningMsg) {
        this.provisioningMsg = provisioningMsg;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public List<InstanceParameterDTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<InstanceParameterDTO> parameters) {
        this.parameters = parameters;
    }

    public List<InstanceAttributeDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<InstanceAttributeDTO> attributes) {
        this.attributes = attributes;
    }
}
