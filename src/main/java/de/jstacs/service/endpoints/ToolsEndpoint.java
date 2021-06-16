package de.jstacs.service.endpoints;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.service.data.entities.Application;
import de.jstacs.service.data.entities.Tool;
import de.jstacs.service.data.repositories.ApplicationRepository;
import de.jstacs.service.data.repositories.ToolRepository;
import de.jstacs.service.data.responsemappings.ToolOverview;
import de.jstacs.service.data.responsemappings.ToolValues;
import de.jstacs.service.services.ToolLoader;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ToolParameterSet;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5000",
        "https://jstacs-online.herokuapp.com" }, methods = { RequestMethod.OPTIONS, RequestMethod.GET,
                RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.HEAD })
@RestController
@RequestMapping("tools")
@RequiredArgsConstructor
public class ToolsEndpoint {

    private final ToolRepository toolRepository;
    private final ToolLoader toolLoader;
    private final ApplicationRepository applicationRepository;

    @GetMapping
    public List<ToolOverview> listTools() {
        List<ToolOverview> availableTools = new ArrayList<ToolOverview>();
        List<Tool> tools = this.toolRepository.findAll();
        tools.forEach((tool) -> {
            ToolOverview toolOverview = new ToolOverview(tool);
            availableTools.add(toolOverview);
        });
        return availableTools;
    }

    @GetMapping("{toolId}")
    public ToolValues loadTool(@PathVariable Long toolId)
            throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Tool tool = this.toolRepository.findById(toolId).get();
        JstacsTool runnableTool = this.toolLoader.load(tool.getType());
        ToolParameterSet parameterSet = runnableTool.getToolParameters();
        ToolValues toolValues = new ToolValues(tool, parameterSet);
        return toolValues;
    }

    @GetMapping("{applicationId}/{toolId}")
    public ToolValues loadTool(@PathVariable Long applicationId, @PathVariable Long toolId)
            throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Application application = this.applicationRepository.findById(applicationId).get();
        Tool tool = this.toolRepository.findById(toolId).get();
        List<Tool> assignedTools = application.getTools();
        ToolValues toolValues = new ToolValues();
        for (Tool assignedTool : assignedTools) {
            if (assignedTool.getId().equals(tool.getId())) {
                JstacsTool runnableTool = this.toolLoader.load(assignedTool.getType());
                ToolParameterSet parameterSet = runnableTool.getToolParameters();
                toolValues.setTool(assignedTool);
                toolValues.setParameters(parameterSet);
            }
        }
        return toolValues;
    }

}
