/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.domain;

import org.oscm.app.domain.enumeration.Controller;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "controller",
        "organizationId" }))
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Controller controller;

    @Column(nullable = false)
    private String organizationId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "configuration", fetch = FetchType.LAZY)
    private List<ConfigurationSetting> settings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public List<ConfigurationSetting> getSettings() {
        return settings;
    }

    public void setSettings(List<ConfigurationSetting> settings) {
        this.settings = settings;
    }
}
