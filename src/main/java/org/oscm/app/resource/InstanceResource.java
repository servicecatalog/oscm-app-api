package org.oscm.app.resource;

import io.swagger.annotations.*;
import org.oscm.app.dto.InstanceAttributeDTO;
import org.oscm.app.dto.InstanceDTO;
import org.oscm.app.dto.InstanceParameterDTO;
import org.oscm.app.exception.ExceptionResponse;
import org.oscm.app.exception.ObjectNotFoundException;
import org.oscm.app.service.intf.InstanceService;
import org.oscm.app.util.ApiParamValue;
import org.oscm.app.util.HttpStatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Api("Operations related to instance resource")
@RestController
public class InstanceResource {

    @Autowired
    private InstanceService instanceService;

    @ApiOperation("Retrieves the list of all instances")
    @GetMapping("/instances")
    public ResponseEntity<List<InstanceDTO>> getInstances() {

        List<InstanceDTO> instances = instanceService.getAllInstances();
        return ResponseEntity.ok(instances);
    }

    @ApiOperation("Retrieves the details of instance")
    @ApiResponses({
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class)
    })
    @GetMapping("/instances/{id}")
    public ResponseEntity<InstanceDTO> getInstance(
            @ApiParam(value = ApiParamValue.INSTANCE_ID, required = true)
            @PathVariable Long id) {

        InstanceDTO instance = checkIfInstanceIdExists(id);
        return ResponseEntity.ok(instance);
    }

    @ApiOperation("Creates new instance")
    @ApiResponses({
            @ApiResponse(code = 400, message = HttpStatusMessage.MSG_400, response = ExceptionResponse.class)
    })
    @PostMapping("/instances")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InstanceDTO> createInstance(
            @ApiParam(value = ApiParamValue.INSTANCE, name = "instance", required = true)
            @Valid @RequestBody InstanceDTO instanceDTO) {

        InstanceDTO createdInstance = instanceService.createInstance(instanceDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdInstance.getId()).toUri();

        return ResponseEntity.created(location).body(createdInstance);
    }

    @ApiOperation("Removes existing instance")
    @ApiResponses({
            @ApiResponse(code = 404, message = HttpStatusMessage.MSG_404, response = ExceptionResponse.class)
    })
    @DeleteMapping("/instances/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteInstance(
            @ApiParam(value = ApiParamValue.INSTANCE_ID, required = true)
            @PathVariable Long id) {

        checkIfInstanceIdExists(id);
        instanceService.deleteInstance(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/instances")
    public ResponseEntity<InstanceDTO> updateInstance(@RequestBody InstanceDTO instanceDTO){


        Optional<InstanceDTO> result = instanceService.getInstance(instanceDTO.getId());
        if (result.isPresent()){
            instanceService.updateInstance(instanceDTO, result);
            return ResponseEntity.ok(instanceDTO);
        }
        else return ResponseEntity.badRequest().body(instanceDTO);

    }

    private InstanceDTO checkIfInstanceIdExists(Long id) {

        Optional<InstanceDTO> instance = instanceService.getInstance(id);
        if(!instance.isPresent()) {
            throw new ObjectNotFoundException("Instance [id=" + id + "] has not been found");
        }
        return instance.get();
    }


}
