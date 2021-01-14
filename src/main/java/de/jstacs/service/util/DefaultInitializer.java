package de.jstacs.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import de.jstacs.DataType;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameterSet;
import de.jstacs.parameters.FileParameter.FileRepresentation;
import de.jstacs.parameters.validation.NumberValidator;
import de.jstacs.parameters.validation.RegExpValidator;
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
                true, new RegExpValidator("Hello.*"), "Hello parameter");
        Parameter charParameter = new SimpleParameter(DataType.CHAR, "Char Parameter", "Parameter to set a character",
                true, Character.valueOf('T'));
        Parameter byteParameter = new SimpleParameter(DataType.BYTE, "Byte Parameter", "Parameter to set a byte", true,
                new NumberValidator<Byte>((byte) -4, (byte) 24), (byte) 20);
        Parameter shortParameter = new SimpleParameter(DataType.SHORT, "Short Parameter", "Parameter to set a short",
                true, new NumberValidator<Short>((short) -2350, (short) 2350), (short) 2345);
        Parameter intParameter = new SimpleParameter(DataType.INT, "Int Parameter", "Parameter to set an int", true,
                new NumberValidator<Integer>(-324423560, 324423560), 324423555);
        Parameter longParameter = new SimpleParameter(DataType.LONG, "Long Parameter", "Parameter to set a long", true,
                new NumberValidator<Long>(Long.parseLong("-2345462565436537"), Long.parseLong("2345462565436540")),
                Long.parseLong("2345462565436537"));
        Parameter floatParameter = new SimpleParameter(DataType.FLOAT, "Float Parameter", "Parameter to set a float",
                true, new NumberValidator<Float>((float) -1.2432, (float) 1.2440), (float) 1.2432);
        Parameter doubleParameter = new SimpleParameter(DataType.DOUBLE, "Double Parameter",
                "Parameter to set a double", true,
                new NumberValidator<Double>(-2.23435436256547546765487687659, 2.23435436256547546765487687659),
                1.23435436256547546765487687659);
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

        SimpleParameterSet ps1 = new SimpleParameterSet(
                new SimpleParameter(DataType.INT, "int parameter", "some int parameter", true),
                new SimpleParameter(DataType.STRING, "string parameter", "some string parameter", true));
        SimpleParameterSet ps2 = new SimpleParameterSet(
                new SimpleParameter(DataType.DOUBLE, "double parameter", "some double parameter", true));
        SelectionParameter sp = new SelectionParameter(DataType.PARAMETERSET, new String[] { "option 1", "option 2" },
                new Object[] { ps1, ps2 }, "selection", "select something", true);
        return new ToolParameterSet("Simple Tool", charParameter, stringParameter, byteParameter, shortParameter,
                intParameter, longParameter, floatParameter, doubleParameter, boolParameter, sp);
    }

}
