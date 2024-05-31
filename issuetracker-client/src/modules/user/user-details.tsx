import { Container } from "@/components";
import { useNavigate, useParams } from "react-router-dom";
import { LocalDateFormatter } from "@/modules/util/date-format";

import styles from "./styles/user-details.module.css";
import { useUser } from "./use-user";

export function UserDetails() {
    const navigate = useNavigate();
    const { username } = useParams();
    if (!username) return null;
    const { user, isLoading, error } = useUser(username);

    const deleteUser = async (userId: string) => {
        const res = await fetch(`http://localhost:8080/users/${userId}`, {
            method: "DELETE",
        });
        if (res.ok) {
            navigate("/users", { replace: true });
        }
    };

    if (!user) return null;

    return (
        <>
            <Container fluid className={styles.header}></Container>
            <Container size="md">
                <div className={styles.avatarWrapper}>
                    <div className={styles.avatar}></div>
                    <div className={styles.heading}>
                        <h1>
                            {user.firstName} {user.lastName}
                        </h1>
                        <p>{user.username}</p>
                    </div>
                </div>
                <dl className={styles.properties}>
                    <div>
                        <dt>User ID</dt>
                        <dd>{user.id}</dd>
                    </div>
                    <div>
                        <dt>User since</dt>
                        <dd>{LocalDateFormatter.format(user.dateCreated)}</dd>
                    </div>
                </dl>
            </Container>
        </>
    );
}
