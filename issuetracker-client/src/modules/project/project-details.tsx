import { Button, Container } from "@/components";
import { useParams } from "react-router-dom";
import { useProject } from "./use-project";
import { UserList } from "../user/user-list";

import styles from "./styles/project-details.module.css";
import { IssueList } from "../issue/issue-list";

export function ProjectDetails() {
    const { teamId } = useParams();

    if (!teamId) return null;

    const { team, isLoading, error } = useProject(teamId);

    if (!team) return null;

    return (
        <>
            <Container fluid className={styles.headerWrapper}>
                <Container size="md" className={styles.header}>
                    <h1>{team.name}</h1>
                    <h2>
                        Lorem ipsum dolor sit amet, officia excepteur ex fugiat
                        reprehenderit enim labore culpa sint ad nisi Lorem
                        pariatur mollit ex esse exercitation amet. Nisi anim
                        cupidatat excepteur officia. Reprehenderit nostrud
                        nostrud ipsum.
                    </h2>
                    <Button>Go to issues</Button>
                </Container>
            </Container>
        </>
    );
}
