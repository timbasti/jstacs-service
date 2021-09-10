package de.jstacs.service.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.vladmihalcea.hibernate.type.array.StringArrayType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import de.jstacs.service.utils.toolexecution.ToolExecutionState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@TypeDefs({
    @TypeDef(
        name = "string-array",
        typeClass = StringArrayType.class
    )
})
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class ToolExecution {

    @Id
    @Getter
    @Setter
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "execution_id")
    private String id;

    @Getter
    @Setter
    @Column(name = "execution_name")
    private String name = "";

    @NonNull
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    private Tool tool;

    @NonNull
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Getter
    @Setter
    @Column(columnDefinition = "text")
    private String notes = "";

    @Getter
    @Setter
    @Column(columnDefinition = "text")
    private String parameterValues = "{}";

    @Getter
    @Setter
    private double progress = -1;

    @Getter
    @Setter
    private ToolExecutionState state = ToolExecutionState.INITIALIZED;

    @Getter
    @Setter
    @Column(columnDefinition = "text")
    private String protocol = "";

    @Getter
    @Setter
    @Type(type = "string-array")
    @Column(columnDefinition = "text[]")
    private String[] results = {};

    @Getter
    private final Date createdAt = new Date();

}
