package de.jstacs.service.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import de.jstacs.service.utils.toolexecution.ToolExecutionState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class ToolExecution {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name = "";

    @ManyToOne
    @NonNull
    @Getter
    @Setter
    private Tool tool;

    @ManyToOne
    @NonNull
    @Getter
    @Setter
    private User user;

    @Column(columnDefinition = "TEXT")
    @Getter
    @Setter
    private String parameterValues = "{}";

    @Getter
    @Setter
    private double progress = -1;

    @Getter
    @Setter
    private ToolExecutionState state = ToolExecutionState.INITIALIZED;

    @Column(columnDefinition = "TEXT")
    @Getter
    @Setter
    private String protocol = "";

    @Column(columnDefinition = "BLOB")
    @Getter
    @Setter
    private String[] results = {};

    @Getter
    private final Date createdAt = new Date();

}
