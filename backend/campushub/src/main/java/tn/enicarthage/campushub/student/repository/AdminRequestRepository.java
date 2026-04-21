package tn.enicarthage.campushub.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enicarthage.campushub.student.model.AdminRequest;
import tn.enicarthage.campushub.student.model.RequestStatus;

import java.util.List;

public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long> {
    List<AdminRequest> findByStudentId(Long studentId);
    List<AdminRequest> findByStatus(RequestStatus status);
}