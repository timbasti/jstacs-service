package de.jstacs.service.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "jstacs_user")
public class User {

    @Id
    @Getter
    @Setter
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_id")
    private String id;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Column(name = "user_executions")
    private List<ToolExecution> executions = new ArrayList<ToolExecution>();
    
}
