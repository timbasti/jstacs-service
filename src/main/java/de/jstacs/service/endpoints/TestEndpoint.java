package de.jstacs.service.endpoints;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.tools.ToolParameterSet;
import de.jstacs.DataType;
import de.jstacs.parameters.AbstractSelectionParameter;
import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterSet;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.parameters.SimpleParameter.IllegalValueException;
import de.jstacs.service.storage.FileSystemStorageService;
import de.jstacs.service.util.DefaultInitializer;

@RestController
@RequestMapping("/test")
public class TestEndpoint {

    private ToolParameterSet toolParameterSet;
    private final DefaultInitializer defaultInitializer;
    private final FileSystemStorageService storageService;

    @Autowired
    public TestEndpoint(DefaultInitializer defaultInitializer, FileSystemStorageService storageService)
            throws Exception {
        this.defaultInitializer = defaultInitializer;
        this.storageService = storageService;
    }

    @GetMapping
    public ToolParameterSet getParameterList() throws Exception {
        if (this.toolParameterSet == null) {
            this.toolParameterSet = defaultInitializer.initializeDefaultToolParameterSet();
        }
        return this.toolParameterSet;
    }

    @PostMapping
    public ToolParameterSet setParameterList(@RequestBody Map<String, Object> values) throws Exception {
        this.updateParameterSet(this.toolParameterSet, values);
        return this.toolParameterSet;
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    private void updateParameterSet(ParameterSet parameterSet, Map<String, Object> values)
            throws Exception {
        
        int numberOfParameters = parameterSet.getNumberOfParameters();
        for (int i = 0; i < numberOfParameters; i++) {
            Parameter parameter = parameterSet.getParameterAt(i);
            String parameterName = parameter.getName();
            Object newValue = values.get(parameterName);

            if (newValue == null) {
                if (parameter.isRequired()) {
                    throw new Exception("Missing value for required parameter: " + parameterName    );
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

}
