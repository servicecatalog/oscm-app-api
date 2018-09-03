package org.oscm.app.repository;

import org.oscm.app.domain.ControllerOrganization;
import org.oscm.app.domain.enumeration.Controller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ControllerOrganizationRepository extends JpaRepository<ControllerOrganization, Long> {

    List<ControllerOrganization> findByController(Controller controller);
}