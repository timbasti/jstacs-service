package de.jstacs.service.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @Getter
    @Setter
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Getter
    @Setter
    private String name = "";

    @NonNull
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Tool tool;

    @NonNull
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String notes = "";

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String parameterValues = "{}";

    @Getter
    @Setter
    private double progress = -1;

    @Getter
    @Setter
    private ToolExecutionState state = ToolExecutionState.INITIALIZED;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String protocol = "";

    @Getter
    @Setter
    @Column(columnDefinition = "BLOB")
    private String[] results = {};

    @Getter
    private final Date createdAt = new Date();

}
