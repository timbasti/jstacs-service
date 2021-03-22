package de.jstacs.service.endpoints;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.tools.ToolResult;
import de.jstacs.tools.ui.cli.CLI.SysProtocol;

@RestController
@RequestMapping("/tools")
public class ToolsEndpoint {

    private final List<String> corruptProjects = Arrays.asList(new String[] {
            /* "projects.xanthogenomes.tools.PredictAndIntersectTargetsTool", */"projects.motifComp.FindPWMsAndClusters" });

    @GetMapping
    public ArrayList<JstacsTool> getTools() throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        ArrayList<JstacsTool> tools = new ArrayList<JstacsTool>();
        Reflections reflections = new Reflections("projects");
        Set<Class<? extends JstacsTool>> subTypes = reflections.getSubTypesOf(JstacsTool.class);
        for (Class<? extends JstacsTool> toolType : subTypes) {
            if (corruptProjects.contains(toolType.getName())) {
                continue;
            }
            Class toolClass = classLoader.loadClass(toolType.getName());
            Constructor constructor = toolClass.getConstructor();
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
        Class loadedMyClass = classLoader.loadClass(toolType);

        System.out.println("Loaded class name: " + loadedMyClass.getName());

        // Create a new instance from the loaded class
        Constructor constructor = loadedMyClass.getConstructor();
        JstacsTool jstacsTool = (JstacsTool) constructor.newInstance();
        return jstacsTool.getToolParameters();
    }

    @RequestMapping(value = "/{tool}", method = RequestMethod.POST)
    public ToolResult getToolParameterSet(@PathVariable String tool, @RequestBody ToolParameterSet toolParameterSet)
            throws Exception {
        System.out.println(tool);
        // Create a new JavaClassLoader
        ClassLoader classLoader = this.getClass().getClassLoader();

        // Load the target class using its binary name
        Class loadedMyClass = classLoader.loadClass(tool);

        System.out.println("Loaded class name: " + loadedMyClass.getName());

        // Create a new instance from the loaded class
        Constructor constructor = loadedMyClass.getConstructor();
        JstacsTool jstacsTool = (JstacsTool) constructor.newInstance();
        SysProtocol protocol = new SysProtocol();
        return jstacsTool.run(toolParameterSet, protocol, null, 1);
    }

}
