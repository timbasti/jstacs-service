package de.jstacs.service.endpoints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.jstacs.service.data.entities.Tool;
import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.entities.User;
import de.jstacs.service.data.repositories.ToolExecutionRepository;
import de.jstacs.service.data.repositories.ToolRepository;
import de.jstacs.service.data.repositories.UserRepository;
import de.jstacs.service.data.requestmappings.ExecutionValues;
import de.jstacs.service.services.ToolExecuter;
import de.jstacs.service.tasks.ToolExecutionTaskFactory;
import de.jstacs.service.tasks.TaskAlreadyRunningException;
import de.jstacs.service.tasks.ToolExecutionTask;
import de.jstacs.tools.ToolParameterSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5000",
        "https://jstacs-online.herokuapp.com" }, methods = { RequestMethod.OPTIONS, RequestMethod.GET,
                RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.HEAD })
@RestController
@RequestMapping("tool-executions")
@RequiredArgsConstructor
@Slf4j
public class ToolExecutionsEndpoint {
    private final ToolExecuter toolService;
    private final UserRepository userRepository;
    private final ToolRepository toolRepository;
    private final ToolExecutionRepository toolExecutionRepository;
    private final ToolExecutionTaskFactory toolExecutionTaskFactory;

    @PostMapping
    public Map<String, String> createToolExecution(@RequestBody ExecutionValues executionValues,
            @RequestHeader("user-id") String userId) throws Exception {
        Long toolId = executionValues.getToolId();
        Optional<User> optionalUser = this.userRepository.findById(userId);
        Optional<Tool> optionalTool = this.toolRepository.findById(toolId);
        ToolExecution newToolExecution = new ToolExecution(optionalTool.get(), optionalUser.get());
        String executionName = executionValues.getExecutionName();
        String executionNotes = executionValues.getExecutionNotes();
        newToolExecution.setName(executionName);
        newToolExecution.setNotes(executionNotes);
        newToolExecution = this.toolExecutionRepository.saveAndFlush(newToolExecution);
        Map<String, String> responseData = new HashMap<String, String>();
        responseData.put("toolExecutionId", newToolExecution.getId());
        log.info("Created: " + newToolExecution.getId());
        return responseData;
    }

    @GetMapping
    public List<ToolExecution> listToolExecutions(@RequestParam("tool-id") Long toolId,
            @RequestHeader("user-id") String userId) throws Exception {
        Tool tool = this.toolRepository.findById(toolId).get();
        User user = this.userRepository.findById(userId).get();
        List<ToolExecution> toolExecutions = this.toolExecutionRepository.findAllByUserAndToolOrderByCreatedAtDesc(user, tool);
        return toolExecutions;
    }

    @GetMapping("{toolExecutionId}")
    public ToolExecution loadToolExecution(@PathVariable String toolExecutionId,
            @RequestHeader("user-id") String userId) throws Exception {
        Optional<ToolExecution> optionalToolExecution = this.toolExecutionRepository.findById(toolExecutionId);
        return optionalToolExecution.get();
    }

    @PutMapping("{toolExecutionId}")
    public ToolParameterSet updateToolExecution(@PathVariable String toolExecutionId, @RequestBody String values,
            @RequestHeader("user-id") String userId) throws Exception {
        try {
            ToolExecutionTask toolTask = this.toolExecutionTaskFactory.create(toolExecutionId, values);
            ToolParameterSet toolParameterSet = toolTask.getToolParameterSet();
            this.toolService.execute(toolTask);
            return toolParameterSet;
        } catch (TaskAlreadyRunningException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Update of ToolExecution not possible. Task already created.", e);
        }
    }

    @DeleteMapping("{toolExecutionId}")
    public void deleteToolExecution(@PathVariable String toolExecutionId, @RequestHeader("user-id") String userId)
            throws Exception {
        this.toolExecutionRepository.deleteById(toolExecutionId);
    }

}
