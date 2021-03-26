package de.jstacs.service.endpoints;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.DataType;
import de.jstacs.parameters.AbstractSelectionParameter;
import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSet;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;
import de.jstacs.service.data.ToolTaskFileResult;
import de.jstacs.service.services.ToolService;
import de.jstacs.service.storage.FileSystemStorageService;
import de.jstacs.service.tasks.ToolTask;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ToolParameterSet;

@RestController
@RequestMapping("/tools")
@SuppressWarnings({ "unchecked" })
public class ToolsEndpoint {

    private final FileSystemStorageService storageService;
    private final ToolService toolService;

    private JstacsTool currentTool;

    private ToolParameterSet currentParameterSet;

    @Autowired
    public ToolsEndpoint(FileSystemStorageService storageService, ToolService toolService) {
        this.storageService = storageService;
        this.toolService = toolService;
    }

    @GetMapping
    public ArrayList<JstacsTool> getTools() throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        ArrayList<JstacsTool> tools = new ArrayList<JstacsTool>();
        Reflections reflections = new Reflections("projects");
        Set<Class<? extends JstacsTool>> subTypes = reflections.getSubTypesOf(JstacsTool.class);
        for (Class<? extends JstacsTool> toolType : subTypes) {
            Class<? extends JstacsTool> toolClass = (Class<? extends JstacsTool>) classLoader
                    .loadClass(toolType.getName());
            Constructor<? extends JstacsTool> constructor = toolClass.getConstructor();
            JstacsTool jstacsTool = (JstacsTool) constructor.newInstance();
            tools.add(jstacsTool);
        }
        return tools;
    }

    @RequestMapping(value = "/{toolType}", method = RequestMethod.GET)
    public ToolParameterSet getToolParameterSet(@PathVariable String toolType) throws Exception {
        System.out.println(toolType);
        // Create a new JavaClassLoader
        ClassLoader classLoader = this.getClass().getClassLoader();

        // Load the target class using its binary name
        Class<? extends JstacsTool> loadedMyClass = (Class<? extends JstacsTool>) classLoader.loadClass(toolType);

        System.out.println("Loaded class name: " + loadedMyClass.getName());

        // Create a new instance from the loaded class
        Constructor<? extends JstacsTool> constructor = loadedMyClass.getConstructor();
        this.currentTool = (JstacsTool) constructor.newInstance();
        this.currentParameterSet = this.currentTool.getToolParameters();
        return this.currentParameterSet;
    }

    @RequestMapping(value = "/{toolType}", method = RequestMethod.POST)
    private ToolParameterSet updateToolParameterSet(@PathVariable String toolType,
    @RequestBody Map<String, Object> values) throws Exception {
        this.updateParameterSet(this.currentParameterSet, values);
        ToolTask toolTask = new ToolTask(this.currentTool, this.currentParameterSet, this.storageService);
        toolService.execute(toolTask);
        return this.currentParameterSet;
    }

    @RequestMapping(value = "/{toolType}/results", method = RequestMethod.GET)
    private ResponseEntity<ToolTaskFileResult> getToolResults(@PathVariable String toolType) {
        return toolService.getResult();
    }

    private void updateParameterSet(ParameterSet parameterSet, Map<String, Object> values) throws Exception {

        int numberOfParameters = parameterSet.getNumberOfParameters();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter parameter = parameterSet.getParameterAt(i);
            String parameterName = parameter.getName();
            Object newValue = values.get(parameterName);

            if (newValue == null) {
                if (parameter.isRequired()) {
                    throw new Exception("Missing value for required parameter: " + parameterName);
                } else {
                    continue;
                }
            }

            this.updateParameterValue(parameter, newValue);
            if (parameter instanceof AbstractSelectionParameter && parameter.getDatatype() == DataType.PARAMETERSET) {
                Map<String, Object> selectionParameterValues = (Map<String, Object>) newValue;
                Map<String, Object> parameterSetValues = (Map<String, Object>) selectionParameterValues
                        .get("parameterSetValues");
                ParameterSet selectedParameterSet = (ParameterSet) parameter.getValue();
                this.updateParameterSet(selectedParameterSet, parameterSetValues);
            }
        }
    }

    private void updateParameterValue(Parameter parameter, Object newValue) throws IllegalValueException {
        if (parameter instanceof AbstractSelectionParameter) {
            Map<String, Object> selectionParameterValues = (Map<String, Object>) newValue;
            String selectedName = (String) selectionParameterValues.get("selectedName");
            parameter.setValue(selectedName);
        } else if (parameter instanceof FileParameter) {
            Map<String, Object> fileParameterValues = (Map<String, Object>) newValue;
            String fileName = (String) fileParameterValues.get("name");
            String absoluteFilePath = storageService.resolveFilePath(fileName);
            FileRepresentation fileRepresentation = new FileRepresentation(absoluteFilePath);
            parameter.setValue(fileRepresentation);
        } else {
            this.updatePrimitiveParameterValue(parameter, newValue);
        }
    }

    private void updatePrimitiveParameterValue(Parameter parameter, Object newValue) throws IllegalValueException {
        DataType dataType = parameter.getDatatype();
        switch (dataType) {
        case CHAR:
        case STRING:
            parameter.setValue((String) newValue);
            break;
        case BOOLEAN:
            parameter.setValue((Boolean) newValue);
            break;
        case BYTE: {
            Number numberValue = (Number) newValue;
            parameter.setValue(numberValue.byteValue());
            break;
        }
        case SHORT: {
            Number numberValue = (Number) newValue;
            parameter.setValue(numberValue.shortValue());
            break;
        }
        case INT: {
            Number numberValue = (Number) newValue;
            parameter.setValue(numberValue.intValue());
            break;
        }
        case LONG: {
            Number numberValue = (Number) newValue;
            parameter.setValue(numberValue.longValue());
            break;
        }
        case FLOAT: {
            Number numberValue = (Number) newValue;
            parameter.setValue(numberValue.floatValue());
            break;
        }
        case DOUBLE: {
            Number numberValue = (Number) newValue;
            parameter.setValue(numberValue.doubleValue());
            break;
        }
        default:
            parameter.setValue(newValue);
        }
    }

}
