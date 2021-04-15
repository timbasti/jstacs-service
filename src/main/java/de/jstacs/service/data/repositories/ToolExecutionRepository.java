package de.jstacs.service.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.jstacs.service.data.entities.ToolExecution;

@Repository
public interface ToolExecutionRepository extends JpaRepository<ToolExecution, String> {
}
