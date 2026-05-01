package tn.enicarthage.campushub.student.service;

import tn.enicarthage.campushub.admin.model.DemandeAdmin;
import tn.enicarthage.campushub.admin.repository.DemandeAdminRepository;
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

    private final DemandeAdminRepository demandeAdminRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public DemandeAdmin submitRequest(DemandeAdmin.Type type, String detail) {
        User user = getCurrentUser();

        DemandeAdmin demande = new DemandeAdmin();
        demande.setStudentId(user.getId());
        demande.setType(type);
        demande.setDetail(detail);
        demande.setStatut(DemandeAdmin.Statut.EN_ATTENTE);
        demande.setSubmittedAt(LocalDateTime.now());

        return demandeAdminRepository.save(demande);
    }

    public List<DemandeAdmin> getMyRequests() {
        User user = getCurrentUser();
        return demandeAdminRepository.findByStudentId(user.getId());
    }

    public DemandeAdmin getMyRequest(Long requestId) {
        User user = getCurrentUser();
        DemandeAdmin demande = demandeAdminRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!demande.getStudentId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return demande;
    }
}