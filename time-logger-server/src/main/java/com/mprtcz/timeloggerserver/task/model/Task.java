package com.mprtcz.timeloggerserver.task.model;

import com.mprtcz.timeloggerserver.record.model.Record;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by mprtcz on 2017-01-23.
 */
@Getter
@Setter
@ToString
@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COLOR", nullable = false)
    private String color;

    @Column(name = "LAST_MODIFIED", nullable = false)
    private LocalDateTime lastModified;

    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "task")
    Collection<Record> activityRecords;

    public Task() {}

    public Task(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
