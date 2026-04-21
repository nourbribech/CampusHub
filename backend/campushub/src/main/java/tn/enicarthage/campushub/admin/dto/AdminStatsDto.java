package tn.enicarthage.campushub.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDto {
    private long totalUsers;
    private long totalEvents;
    private long pendingEvents;
    private long pendingReservations;
    private long pendingDemandes;
    private long totalClubs;
}
