package de.jstacs.service.endpoints;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.tools.JstacsTool;
import de.jstacs.tools.Protocol;
import de.jstacs.tools.ToolParameterSet;
import de.jstacs.tools.ToolResult;
import de.jstacs.tools.ui.cli.CLI.SysProtocol;

@RestController
@RequestMapping("/tools")
public class ToolsEndpoint {

    @GetMapping
    public ArrayList<JstacsTool> getTools() throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        ArrayList<JstacsTool> tools = new ArrayList<JstacsTool>();
        Reflections reflections = new Reflections("projects");
        Set<Class<? extends JstacsTool>> subTypes = reflections.getSubTypesOf(JstacsTool.class);
        subTypes.forEach(toolType -> {
            try {
                Class toolClass = classLoader.loadClass(toolType.getName());
                Constructor constructor = toolClass.getConstructor();
                JstacsTool jstacsTool = (JstacsTool) constructor.newInstance();
                tools.add(jstacsTool);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        return tools;
    }

    @RequestMapping(value = "/{tool}", method = RequestMethod.GET)
    public ToolParameterSet getToolParameterSet(@PathVariable String tool) throws Exception {
        System.out.println(tool);
        // Create a new JavaClassLoader
        ClassLoader classLoader = this.getClass().getClassLoader();

        // Load the target class using its binary name
        Class loadedMyClass = classLoader.loadClass(tool);

        System.out.println("Loaded class name: " + loadedMyClass.getName());

        // Create a new instance from the loaded class
        Constructor constructor = loadedMyClass.getConstructor();
        JstacsTool jstacsTool = (JstacsTool) constructor.newInstance();
        return jstacsTool.getToolParameters();
    }

    @RequestMapping(value = "/{tool}", method = RequestMethod.POST)
    public ToolResult getToolParameterSet(@PathVariable String tool,
            @RequestBody ToolParameterSet toolParameterSet) throws Exception {
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
