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
import org.oscm.app.validation.ReadOnly;

import javax.validation.constraints.NotEmpty;

@ApiModel(value = "Setting", description = "Single configuration's setting")
public class ConfigurationSettingDTO {

    @ApiModelProperty(position = 1, notes = "Identifier of the setting", readOnly = true)
    private long id;

    @ApiModelProperty(position = 2, notes = "Unique setting's key", required = true)
    @NotEmpty
    private String key;

    @ApiModelProperty(position = 3, notes = "Setting's value", required = true)
    @NotEmpty
    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
