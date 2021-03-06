package de.jstacs.service.endpoints;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.service.data.entities.Application;
import de.jstacs.service.data.entities.Tool;
import de.jstacs.service.data.repositories.ApplicationRepository;
import de.jstacs.service.data.repositories.ToolRepository;
import de.jstacs.service.data.requestmappings.ApplicationValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5000",
        "https://jstacs-online.herokuapp.com" }, methods = { RequestMethod.OPTIONS, RequestMethod.GET,
                RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.HEAD })
@RestController
@RequestMapping("applications")
@RequiredArgsConstructor
@Slf4j
public class ApplicationsEndpoint {

    private final ToolRepository toolRepository;
    private final ApplicationRepository applicationRepository;

    @GetMapping
    public List<Application> listApplications() {
        return this.applicationRepository.findAll();
    }

    @PostMapping
    public List<Application> createApplication(@RequestBody ApplicationValues creationValues) {
        Application application = new Application(creationValues.getName());
        creationValues.getToolIds().forEach((toolId) -> {
            Tool tool = this.toolRepository.findById(toolId).get();
            application.getTools().add(tool);
        });
        this.applicationRepository.saveAndFlush(application);
        log.debug("Created: " + application.getName());
        return this.applicationRepository.findAll();
    }

    @PutMapping("{applicationId}")
    public List<Application> updateApplication(@PathVariable Long applicationId,
            @RequestBody ApplicationValues updateValues) {
        Application application = this.applicationRepository.findById(applicationId).get();
        List<Tool> tools = new ArrayList<Tool>();
        updateValues.getToolIds().forEach((toolId) -> {
            Tool tool = this.toolRepository.findById(toolId).get();
            tools.add(tool);
        });
        application.setTools(tools);
        application.setName(updateValues.getName());
        this.applicationRepository.saveAndFlush(application);
        log.debug("Updated: " + application.getName());
        return this.applicationRepository.findAll();
    }

    @DeleteMapping("{applicationId}")
    public List<Application> deleteApplication(@PathVariable Long applicationId) {
        Application application = this.applicationRepository.findById(applicationId).get();
        this.applicationRepository.delete(application);
        log.debug("Deleted: " + application.getName());
        return this.applicationRepository.findAll();
    }

}
