import { Container } from "@/components";

import styles from "./styles/team-details.module.css";
import { useParams } from "react-router-dom";
import { useTeam } from "./use-team";
import { UserList } from "../user/user-list";
import { User } from "./types";

export function TeamDetails() {
    const { teamId } = useParams();

    if (!teamId) return null;

    const { team, isLoading, error } = useTeam(teamId);

    if (!team) return null;

    return (
        <Container size="lg">
            <h1 className={styles.heading}>{team.name}</h1>
            <UserList users={team.teamMembers} />
        </Container>
    );
}
