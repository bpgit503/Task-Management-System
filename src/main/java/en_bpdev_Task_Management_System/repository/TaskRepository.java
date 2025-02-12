package en_bpdev_Task_Management_System.repository;

import en_bpdev_Task_Management_System.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {
}
