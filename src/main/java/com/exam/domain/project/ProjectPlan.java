package com.exam.domain.project;

import com.exam.domain.task.Task;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ProjectPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Task> tasks;
}

