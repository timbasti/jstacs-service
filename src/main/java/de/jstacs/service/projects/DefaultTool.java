package de.jstacs.service.projects;

import org.springframework.util.MimeTypeUtils;

import de.jstacs.DataType;
import de.jstacs.classifiers.differentiableSequenceScoreBased.OptimizableFunction.KindOfParameter;
import de.jstacs.tools.DataColumnParameter;
import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ProgressUpdater;
import de.jstacs.tools.Protocol;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.tools.ToolResult;
import de.jstacs.parameters.EnumParameter;
import de.jstacs.parameters.FileParameter;
import de.jstacs.parameters.Parameter;
import de.jstacs.parameters.ParameterException;
import de.jstacs.parameters.SelectionParameter;
import de.jstacs.parameters.SimpleParameter;
import de.jstacs.parameters.SimpleParameterSet;
import de.jstacs.parameters.validation.NumberValidator;
import de.jstacs.parameters.validation.RegExpValidator;

public class DefaultTool implements JstacsTool {

        @Override
        public void clear() {
                // TODO Auto-generated method stub
        }

        @Override
        public ResultEntry[] getDefaultResultInfos() {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public String getDescription() {
                return "A tool just to show all available parameters";
        }

        @Override
        public String getHelpText() {
                return "The tool does nothing. It only initializes all available parameters";
        }

        @Override
        public String[] getReferences() {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public String getShortName() {
                return "dtool";
        }

        @Override
        public ToolResult[] getTestCases(String arg0) {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public String getToolName() {
                return "Default Tool";
        }

        @Override
        public ToolParameterSet getToolParameters() {
                try {
                        Parameter stringParameter = new SimpleParameter(DataType.STRING, "String Parameter",
                                        "Parameter to set a text", true, new RegExpValidator("Hello.*"),
                                        "Hello parameter");
                        Parameter charParameter = new SimpleParameter(DataType.CHAR, "Char Parameter",
                                        "Parameter to set a character", true, Character.valueOf('T'));
                        Parameter byteParameter = new SimpleParameter(DataType.BYTE, "Byte Parameter",
                                        "Parameter to set a byte", true,
                                        new NumberValidator<Byte>((byte) -4, (byte) 24), (byte) 20);
                        Parameter shortParameter = new SimpleParameter(DataType.SHORT, "Short Parameter",
                                        "Parameter to set a short", true,
                                        new NumberValidator<Short>((short) -2350, (short) 2350), (short) 2345);
                        Parameter intParameter = new SimpleParameter(DataType.INT, "Int Parameter",
                                        "Parameter to set an int", true,
                                        new NumberValidator<Integer>(-324423560, 324423560), 324423555);
                        Parameter longParameter = new SimpleParameter(DataType.LONG, "Long Parameter",
                                        "Parameter to set a long", true,
                                        new NumberValidator<Long>(Long.parseLong("-2345462565436537"),
                                                        Long.parseLong("2345462565436540")),
                                        Long.parseLong("2345462565436537"));
                        Parameter floatParameter = new SimpleParameter(DataType.FLOAT, "Float Parameter",
                                        "Parameter to set a float", true,
                                        new NumberValidator<Float>((float) -1.2432, (float) 1.2440), (float) 1.2432);
                        Parameter doubleParameter = new SimpleParameter(DataType.DOUBLE, "Double Parameter",
                                        "Parameter to set a double", true,
                                        new NumberValidator<Double>(-2.23435436256547546765487687659,
                                                        2.23435436256547546765487687659),
                                        1.23435436256547546765487687659);
                        Parameter boolParameter = new SimpleParameter(DataType.BOOLEAN, "Boolean Parameter",
                                        "Parameter to set a bool", true, false);
                        Parameter fileParameter = new FileParameter("File Parameter", "Parameter to set a file", "tsv,tabular",
                                        true, null, true);
                        Parameter nextFileParameter = new FileParameter("Next File Parameter",
                                        "Another Parameter to set a file", "", true, null, true);
                        DataColumnParameter dataColumnParameter = new DataColumnParameter(nextFileParameter.getName(),
                                        "Data Column Parameter", "This is a nice data column parameter", true, 1);
                        SelectionParameter sp1 = new SelectionParameter(DataType.INT,
                                        new String[] { "selection 1", "selection 2" }, new Object[] { 12345, 67890 },
                                        "simple selection", "select something", true);
                        SimpleParameterSet ps1 = new SimpleParameterSet(
                                        new SimpleParameter(DataType.INT, "Int Parameter", "some int parameter", true),
                                        new SimpleParameter(DataType.STRING, "string parameter",
                                                        "some string parameter", true));
                        SimpleParameterSet ps2 = new SimpleParameterSet(new SimpleParameter(DataType.DOUBLE,
                                        "double parameter", "some double parameter", true));
                        SelectionParameter sp2 = new SelectionParameter(DataType.PARAMETERSET,
                                        new String[] { "selection 1", "selection 2" }, new Object[] { ps1, ps2 },
                                        "complex selection", "select something", true);
                        EnumParameter ep = new EnumParameter(KindOfParameter.class,
                                        "Indicates whether special plugIn parameters or the zero vector should be used as start parameters. For non-concave problems it is highly recommended to use plugIn parameters.",
                                        true, KindOfParameter.PLUGIN.name());
                        return new ToolParameterSet("Simple Tool", charParameter, stringParameter, byteParameter,
                                        shortParameter, intParameter, longParameter, floatParameter, doubleParameter,
                                        boolParameter, fileParameter, nextFileParameter, dataColumnParameter, sp1, sp2,
                                        ep);
                } catch (ParameterException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                return null;
        }

        @Override
        public String getToolVersion() {
                return "0.1";
        }

        @Override
        public ToolResult run(ToolParameterSet arg0, Protocol arg1, ProgressUpdater arg2, int arg3) throws Exception {
                // TODO Auto-generated method stub
                return null;
        }

}
