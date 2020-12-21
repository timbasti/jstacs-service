package de.jstacs.service.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import de.jstacs.DataType;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.service.storage.FileSystemStorageService;

@Component
public class DefaultInitializer {

    private final FileSystemStorageService storageService;

    @Autowired
    public DefaultInitializer(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    public ToolParameterSet initializeDefaultToolParameterSet() throws Exception {
        Parameter stringParameter = new SimpleParameter(DataType.STRING, "String Parameter", "Parameter to set a text",
                true, "Hello parameter");
        Parameter charParameter = new SimpleParameter(DataType.CHAR, "Char Parameter", "Parameter to set a character",
                true, 'T');
        Parameter byteParameter = new SimpleParameter(DataType.BYTE, "Byte Parameter", "Parameter to set a byte", true,
                Byte.parseByte("20"));
        Parameter shortParameter = new SimpleParameter(DataType.SHORT, "Short Parameter", "Parameter to set a short",
                true, Short.parseShort("2345"));
        Parameter intParameter = new SimpleParameter(DataType.INT, "Int Parameter", "Parameter to set an int", true,
                Integer.parseInt("324423555"));
        Parameter longParameter = new SimpleParameter(DataType.LONG, "Long Parameter", "Parameter to set a long", true,
                Long.parseLong("2345462565436537"));
        Parameter floatParameter = new SimpleParameter(DataType.FLOAT, "Float Parameter", "Parameter to set a float",
                true, Float.parseFloat("1.2432"));
        Parameter doubleParameter = new SimpleParameter(DataType.DOUBLE, "Double Parameter",
                "Parameter to set a double", true, Double.parseDouble("1.23435436256547546765487687659"));
        Parameter boolParameter = new SimpleParameter(DataType.BOOLEAN, "Boolean Parameter", "Parameter to set a bool",
                true, false);
        Parameter fileParameter = new FileParameter("File Parameter", "Parameter to set a file",
                MimeTypeUtils.TEXT_PLAIN_VALUE, true, null, true);
        String fileName = storageService.create("test.txt", "Hello World!");
        String absoluteFilePath = storageService.resolveFilePath(fileName);
        FileRepresentation fileRepresentation = new FileRepresentation(absoluteFilePath);
        fileParameter.setDefault(fileRepresentation);
        Parameter nextFileParameter = new FileParameter("Next File Parameter", "Another Parameter to set a file",
                MimeTypeUtils.TEXT_PLAIN_VALUE, true, null, true);
        String nextFileName = storageService.create("foo.txt", "Bar!");
        String nextAbsoluteFilePath = storageService.resolveFilePath(nextFileName);
        FileRepresentation nextFileRepresentation = new FileRepresentation(nextAbsoluteFilePath);
        nextFileParameter.setDefault(nextFileRepresentation);
        return new ToolParameterSet("Simple Tool", fileParameter, nextFileParameter);
    }

}
