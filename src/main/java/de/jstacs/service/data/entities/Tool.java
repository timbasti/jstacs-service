package de.jstacs.service.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import de.jstacs.tools.JstacsTool;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Tool {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @NonNull
    @Column(unique = true)
    @Getter
    @Setter
    private String type;

    @NonNull
    @Getter
    @Setter
    private String name;


    @Getter
    @Setter
    private String shortName = "";

    @Getter
    @Setter
    private String version = "";

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String description = "";

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String helpText = "";

    @Getter
    @Setter
    @Column(columnDefinition = "BLOB")
    private String[] references = {};

    @Getter
    @Setter
    @OneToMany(mappedBy="tool")
    @OrderColumn
    private List<ToolExecution> executions = new ArrayList<ToolExecution>();

    @Getter
    @Setter
    @ManyToMany(mappedBy = "tools")
    private List<Application> applications = new ArrayList<Application>();

    public Tool(JstacsTool tool) {
        this.type = tool.getClass().getName();
        this.name = tool.getToolName();
        this.shortName = tool.getShortName();
        this.version = tool.getToolVersion();
        this.description = tool.getDescription();
        this.helpText = tool.getHelpText();
        String[] references = tool.getReferences();
        if (references != null) {
            this.references = references;
        }
    }

}
