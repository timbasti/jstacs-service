package de.jstacs.service.tasks;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import de.jstacs.DataType;
import de.jstacs.parameters.AbstractSelectionParameter;
import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSet;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;
import de.jstacs.service.data.entities.ToolExecution;
import de.jstacs.service.data.repositories.ToolExecutionRepository;
import de.jstacs.service.services.ToolLoader;
import de.jstacs.service.storage.StorageProperties;
import de.jstacs.service.storage.StorageService;
import de.jstacs.service.utils.toolexecution.ToolExecutionProgressUpdater;
import de.jstacs.service.utils.toolexecution.ToolExecutionProtocol;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ProgressUpdater;
import de.jstacs.tools.Protocol;
import de.jstacs.tools.ToolParameterSet;

import lombok.RequiredArgsConstructor;

@SuppressWarnings({ "unchecked" })
@Service
@RequiredArgsConstructor
public class ToolExecutionTaskFactory {

    private final StorageProperties storageProperties;

    private final StorageService storageService;

    private final ObjectMapper objectMapper;

    private final ToolExecutionRepository toolExecutionRepository;

    private final ToolLoader toolLoader;

    public ToolExecutionTask create(String toolExecutionId, String parameterValues) throws Exception {
        Optional<ToolExecution> optionalToolExecution = this.toolExecutionRepository.findById(toolExecutionId);

        ToolExecution toolExecution = optionalToolExecution.get();
        toolExecution.setParameterValues(parameterValues);

        TypeFactory typeFactory = this.objectMapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        Map<String, Object> deserializedValues = this.objectMapper.readValue(parameterValues, mapType);

        String toolType = toolExecution.getTool().getType();
        JstacsTool jstacsTool = this.toolLoader.load(toolType);
        ToolParameterSet parameterSet = jstacsTool.getToolParameters();
        this.updateParameterSet(parameterSet, deserializedValues);

        Protocol protocol = new ToolExecutionProtocol(toolExecution, this.toolExecutionRepository);
        ProgressUpdater progressUpdater = new ToolExecutionProgressUpdater(toolExecution, this.toolExecutionRepository);

        String rootLocation = this.storageProperties.getRootLocation();
        String resultsLocation = this.storageProperties.getResultsLocation();
        Path resultDirectoryPath = Paths.get(rootLocation, toolExecution.getUser().getId(), toolExecutionId,
                resultsLocation);

        this.toolExecutionRepository.save(toolExecution);
        ToolExecutionTask toolTask = new ToolExecutionTask(jstacsTool, parameterSet, protocol, progressUpdater,
                resultDirectoryPath, storageService);
        return toolTask;
    }

    private void updateParameterSet(ParameterSet parameterSet, Map<String, Object> values) throws Exception {

        int numberOfParameters = parameterSet.getNumberOfParameters();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter parameter = parameterSet.getParameterAt(i);
            String parameterName = parameter.getName();
            Object newValue = values.get(parameterName);

            if (newValue == null) {
                if (parameter.isRequired() && parameter.getValue() == null) {
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

    private void updateParameterValue(Parameter parameter, Object newValue) throws IllegalValueException, IOException {
        if (parameter instanceof AbstractSelectionParameter) {
            Map<String, Object> selectionParameterValues = (Map<String, Object>) newValue;
            String selectedName = (String) selectionParameterValues.get("selectedName");
            parameter.setValue(selectedName);
        } else if (parameter instanceof FileParameter) {
            Map<String, Object> fileParameterValues = (Map<String, Object>) newValue;
            String fileName = (String) fileParameterValues.get("name");
            Resource file = storageService.loadAsResource(fileName);
            FileRepresentation fileRepresentation = new FileRepresentation(file.getFile().getAbsolutePath());
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
