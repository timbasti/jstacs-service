package de.jstacs.service.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.jstacs.service.data.entities.Tool;
import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.entities.User;

@Repository
public interface ToolExecutionRepository extends JpaRepository<ToolExecution, String> {

    List<ToolExecution> findAllByUserAndTool(User user, Tool tool);

}
