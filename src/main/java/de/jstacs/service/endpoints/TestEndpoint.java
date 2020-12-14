package de.jstacs.service.endpoints;

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.DataType;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.FileParameter.FileRepresentation;

@RestController
@RequestMapping("/test")
public class TestEndpoint {

    private ToolParameterSet toolParameterSet;

    @GetMapping
    public ToolParameterSet getParameterList() throws Exception {
        this.toolParameterSet = this.toolParameterSet != null ? this.toolParameterSet : this.createDefaultToolParameterSet();
        return this.toolParameterSet;
    }

    @PostMapping
    public ToolParameterSet setParameterList(@RequestBody ToolParameterSet toolParameterSet) {
        this.toolParameterSet = toolParameterSet;
        return this.toolParameterSet;
    }

    private ToolParameterSet createDefaultToolParameterSet() throws Exception {
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
        FileRepresentation fileRepresentation = new FileRepresentation("test.txt", "Hello World!");
        fileParameter.setDefault(fileRepresentation);
        return new ToolParameterSet("Simple Tool", charParameter, stringParameter, boolParameter, byteParameter,
                shortParameter, intParameter, longParameter, floatParameter, doubleParameter, fileParameter);
    }

}
