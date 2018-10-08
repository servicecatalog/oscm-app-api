/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2018
 *                                                                                                                                 
 *  Creation Date: 08.10.20178                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.app.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "Error", description = "Response returned in case of error")
public class ExceptionResponse {

    public ExceptionResponse(LocalDateTime timestamp, String errorMessage) {
        this.timestamp = timestamp;
        this.errorMessage = errorMessage;
    }

    @ApiModelProperty("Timestamp of the appeared error")
    private LocalDateTime timestamp;

    @ApiModelProperty("Message describing the error")
    private String errorMessage;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
