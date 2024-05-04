package dev.oscarrojas.issuetracker.team;

import java.util.Set;

public record TeamMember(String username, String teamId, Set<String> roles) {
}
