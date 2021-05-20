package de.jstacs.service.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import de.jstacs.tools.JstacsTool;

@SuppressWarnings({ "unchecked" })
@Service
public class ToolLoader {

    private final ClassLoader classLoader;

    public ToolLoader() {
        this.classLoader = this.getClass().getClassLoader();
    }

    public JstacsTool load(String type) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<? extends JstacsTool> loadedMyClass = (Class<? extends JstacsTool>) this.classLoader.loadClass(type);
        Constructor<? extends JstacsTool> constructor = loadedMyClass.getConstructor();
        JstacsTool loadedTool = (JstacsTool) constructor.newInstance();
        return loadedTool;
    }

    public List<JstacsTool> loadAll() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Reflections reflections = new Reflections("projects", "de.jstacs.service.projects");
        Set<Class<? extends JstacsTool>> subTypes = reflections.getSubTypesOf(JstacsTool.class);
        List<JstacsTool> loadedTools = new ArrayList<JstacsTool>();
        for (Class<? extends JstacsTool> toolType : subTypes) {
            Class<? extends JstacsTool> toolClass = (Class<? extends JstacsTool>) classLoader
                    .loadClass(toolType.getName());
            Constructor<? extends JstacsTool> constructor = toolClass.getConstructor();
            JstacsTool jstacsTool = (JstacsTool) constructor.newInstance();
            loadedTools.add(jstacsTool);
        }
        return loadedTools;
    }

}
