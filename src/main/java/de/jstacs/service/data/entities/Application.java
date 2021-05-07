package de.jstacs.service.data.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Application {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @NonNull
    @Getter
    @Setter
    @Column(unique = true)
    private String name;

    @ManyToMany
    @JoinTable(name = "Application_Tools", joinColumns = @JoinColumn(name = "application_id"), inverseJoinColumns = @JoinColumn(name = "tool_id"))
    @Getter
    @Setter
    @OrderColumn
    private List<Tool> tools = new ArrayList<Tool>();

}
