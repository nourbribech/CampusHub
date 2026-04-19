package tn.enicarthage.campushub.request;

import tn.enicarthage.campushub.model.enums.RequestStatus;
import tn.enicarthage.campushub.student.Student;
import tn.enicarthage.campushub.student.StudentRepository;
import tn.enicarthage.campushub.request.AdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRequestService {

    private final AdminRequestRepository requestRepository;
    private final StudentRepository studentRepository;

    private Student getCurrentStudent() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // Submit a new request
    public AdminRequest submitRequest(AdminRequest request) {
        Student student = getCurrentStudent();
        request.setStudentId(student.getId());
        request.setStatus(RequestStatus.PENDING);
        request.setSubmittedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }

    // Track my requests
    public List<AdminRequest> getMyRequests() {
        Student student = getCurrentStudent();
        return requestRepository.findByStudentId(student.getId());
    }

    // Get a single request (must belong to current student)
    public AdminRequest getMyRequest(Long requestId) {
        Student student = getCurrentStudent();
        AdminRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getStudentId().equals(student.getId())) {
            throw new RuntimeException("Access denied");
        }

        return request;
    }
}