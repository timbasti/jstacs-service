package de.jstacs.service.endpoints;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.service.data.entities.Tool;
import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.entities.User;
import de.jstacs.service.data.repositories.ToolExecutionRepository;
import de.jstacs.service.data.repositories.ToolRepository;
import de.jstacs.service.data.repositories.UserRepository;
import de.jstacs.service.data.requestmappings.ExecutionValues;
import de.jstacs.service.data.responsemappings.ToolValues;
import de.jstacs.service.services.ToolLoader;
import de.jstacs.service.services.ToolExecuter;
import de.jstacs.service.tasks.ToolExecutionTaskFactory;
import de.jstacs.service.tasks.ToolExecutionTask;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ToolParameterSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5000", "https://jstacs-online.herokuapp.com" })
@RestController
@RequestMapping("tools")
@RequiredArgsConstructor
@Slf4j
public class ToolsEndpoint {

    private final ToolExecuter toolService;
    private final UserRepository userRepository;
    private final ToolRepository toolRepository;
    private final ToolExecutionRepository toolExecutionRepository;
    private final ToolLoader toolLoader;
    private final ToolExecutionTaskFactory toolExecutionTaskFactory;

    @GetMapping
    public List<Tool> getTools() {
        return this.toolRepository.findAll();
    }

    @GetMapping("{toolId}")
    public ToolValues getTool(@PathVariable Long toolId)
            throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Tool tool = this.toolRepository.findById(toolId).get();
        JstacsTool runnableTool = this.toolLoader.load(tool.getType());
        ToolParameterSet parameterSet = runnableTool.getToolParameters();
        ToolValues toolValues = new ToolValues(tool, parameterSet);
        return toolValues;
    }

    @GetMapping("parameters")
    public ToolParameterSet getToolParameterSet(@RequestParam(name = "type") String toolType) throws Exception {
        JstacsTool jstacsTool = this.toolLoader.load(toolType);
        ToolParameterSet toolParameterSet = jstacsTool.getToolParameters();
        return toolParameterSet;
    }

    @PostMapping("executions")
    public Map<String, String> createToolExecution(@RequestBody ExecutionValues values,
            @RequestHeader("user-id") String userId) throws Exception {
        log.info(values.getToolType() + ", " + userId);
        Optional<User> optionalUser = this.userRepository.findById(userId);
        Optional<Tool> optionalTool = this.toolRepository.findByType(values.getToolType());
        ToolExecution toolExecution = new ToolExecution(optionalTool.get(), optionalUser.get());
        this.toolExecutionRepository.save(toolExecution);
        Map<String, String> responseData = new HashMap<String, String>();
        responseData.put("toolId", toolExecution.getId());
        log.info("Created: " + toolExecution.getId());
        return responseData;
    }

    @PutMapping("executions/{toolExecutionId}")
    private ToolParameterSet updateToolExecution(@PathVariable String toolExecutionId, @RequestBody String values,
            @RequestHeader("user-id") String userId) throws Exception {
        ToolExecutionTask toolTask = this.toolExecutionTaskFactory.create(toolExecutionId, values);
        ToolParameterSet toolParameterSet = toolTask.getToolParameterSet();
        this.toolService.execute(toolTask);
        return toolParameterSet;
    }

    @GetMapping("executions/{toolExecutionId}")
    private ToolExecution getToolExecution(@PathVariable String toolExecutionId,
            @RequestHeader("user-id") String userId) throws Exception {
        Optional<ToolExecution> optionalToolExecution = this.toolExecutionRepository.findById(toolExecutionId);
        return optionalToolExecution.get();
    }

    @DeleteMapping("executions/{toolExecutionId}")
    private void deleteToolExecution(@PathVariable String toolExecutionId, @RequestHeader("user-id") String userId)
            throws Exception {
        this.toolExecutionRepository.deleteById(toolExecutionId);
    }

}
