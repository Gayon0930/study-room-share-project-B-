package me.docherri.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "todoList")
    private User user;

    @ElementCollection
    @CollectionTable(name = "todo_tasks", joinColumns = @JoinColumn(name = "todo_list_id"))
    @Column(name = "task")
    private List<String> tasks = new ArrayList<>();

    public void addTasks(String task) {
        if (tasks.size() < 5) {
            tasks.add(task);
        } else {
            throw new RuntimeException("항목은 최대 5개까지");
        }
    }

    public void deleteTasks(int idx) {
        if (idx >= 0 && idx < tasks.size()) {
            tasks.remove(idx);
        } else {
            throw new RuntimeException("인덱스 오류");
        }
    }
}