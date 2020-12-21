package de.jstacs.service.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.jstacs.tools.ToolParameterSet;
import de.jstacs.service.util.DefaultInitializer;

@RestController
@RequestMapping("/test")
public class TestEndpoint {

    private ToolParameterSet toolParameterSet;
    private final DefaultInitializer defaultInitializer;

    @Autowired
    public TestEndpoint(DefaultInitializer defaultInitializer) throws Exception {
        this.defaultInitializer = defaultInitializer;
    }

    @GetMapping
    public ToolParameterSet getParameterList() throws Exception {
        if (this.toolParameterSet == null) {
            this.toolParameterSet = defaultInitializer.initializeDefaultToolParameterSet();
        }
        return this.toolParameterSet;
    }

    @PostMapping
    public void setParameterList(@RequestBody ToolParameterSet toolParameterSet) {
        this.toolParameterSet = toolParameterSet;
    }

}
