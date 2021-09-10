package de.jstacs.service.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.vladmihalcea.hibernate.type.array.StringArrayType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import de.jstacs.tools.JstacsTool;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@TypeDefs({
    @TypeDef(
        name = "string-array",
        typeClass = StringArrayType.class
    )
})
@Entity
@NoArgsConstructor
public class Tool {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    @Column(name = "tool_id")
    private Long id;

    @NonNull
    @Getter
    @Setter
    @Column(name= "tool_type", unique = true)
    private String type;

    @NonNull
    @Getter
    @Setter
    @Column(name = "tool_name")
    private String name;


    @Getter
    @Setter
    private String shortName = "";

    @Getter
    @Setter
    @Column(name = "tool_version")
    private String version = "";

    @Getter
    @Setter
    @Column(name = "tool_description", columnDefinition = "text")
    private String description = "";

    @Getter
    @Setter
    @Column(columnDefinition = "text")
    private String helpText = "";

    @Getter
    @Setter
    @Type(type = "string-array")
    @Column(name = "tool_references", columnDefinition = "text[]")
    private String[] references = {};

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
