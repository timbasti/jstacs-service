package de.jstacs.service.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.jstacs.service.data.entities.Tool;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {

    Optional<Tool> findByType(String type);

}
