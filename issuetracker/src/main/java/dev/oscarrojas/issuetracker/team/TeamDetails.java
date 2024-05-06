package dev.oscarrojas.issuetracker.team;

import java.util.List;

public record TeamDetails(String id, String name, List<TeamMember> teamMembers) {
}
