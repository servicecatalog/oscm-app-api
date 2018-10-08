/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.domain;

import org.oscm.app.domain.enumeration.ProvisioningStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Instance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String organizationId;

    private String organizationName;

    @Column(nullable = false)
    private String subscriptionId;

    private String referenceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProvisioningStatus provisioningStatus;

    private String provisioningMsg;

    @Column(nullable = false)
    private LocalDateTime requestTime;

    @Column(nullable = false)
    private String instanceId;

    @Column(nullable = false)
    private String controllerId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instance", fetch = FetchType.LAZY)
    private List<InstanceParameter> instanceParameters = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instance", fetch = FetchType.LAZY)
    private List<InstanceAttribute> instanceAttributes = new ArrayList<>();

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

    public ProvisioningStatus getProvisioningStatus() {
        return provisioningStatus;
    }

    public void setProvisioningStatus(ProvisioningStatus provisioningStatus) {
        this.provisioningStatus = provisioningStatus;
    }

    public String getProvisioningMsg() {
        return provisioningMsg;
    }

    public void setProvisioningMsg(String provisioningMsg) {
        this.provisioningMsg = provisioningMsg;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
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

    public List<InstanceParameter> getInstanceParameters() {
        return instanceParameters;
    }

    public void setInstanceParameters(
            List<InstanceParameter> instanceParameters) {
        this.instanceParameters = instanceParameters;
    }

    public List<InstanceAttribute> getInstanceAttributes() {
        return instanceAttributes;
    }

    public void setInstanceAttributes(
            List<InstanceAttribute> instanceAttributes) {
        this.instanceAttributes = instanceAttributes;
    }
}
