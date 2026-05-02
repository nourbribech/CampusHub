package tn.enicarthage.campushub.student.service;

import tn.enicarthage.campushub.student.repository.AdminRequestRepository;
import tn.enicarthage.campushub.student.model.AdminRequest;
import tn.enicarthage.campushub.student.model.RequestStatus;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminRequestService {

    private final AdminRequestRepository requestRepository;
    private final UserRepository userRepository;  // ← remplace StudentRepository

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AdminRequest submitRequest(AdminRequest request) {
        User user = getCurrentUser();
        request.setStudentId(user.getId());  // réutilise studentId pour stocker user.id
        request.setStatus(RequestStatus.PENDING);
        request.setSubmittedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }

    public List<AdminRequest> getMyRequests() {
        User user = getCurrentUser();
        return requestRepository.findByStudentId(user.getId());
    }

    public AdminRequest getMyRequest(Long requestId) {
        User user = getCurrentUser();
        AdminRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (!request.getStudentId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        return request;
    }
}