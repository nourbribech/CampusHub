package tn.enicarthage.campushub.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.campushub.student.model.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
        Optional<Student> findByEmail(String email);
    }

