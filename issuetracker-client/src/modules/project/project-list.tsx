import { Link } from "@/components";
import styles from "./styles/project-list.module.css";

import { useProjectList } from "./use-project-list";

export function ProjectList() {
    const { projects, isLoading, error } = useProjectList();

    if (!projects) return null;

    return (
        <ul className={styles.list}>
            {projects.map((project) => (
                <li key={project.id}>
                    <Link>{project.name}</Link>
                </li>
            ))}
        </ul>
    );
}
