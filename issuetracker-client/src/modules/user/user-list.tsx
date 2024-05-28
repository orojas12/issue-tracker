import { useEffect, useState } from "react";
import { CreateUserRequest, User } from "@/modules/team/types";
import { CreateUserDialog } from "./create-user";

import { Link, SearchField, Container, Button, Card } from "@/components";
import styles from "./styles/user-list.module.css";

export function UserManagement() {
    const [users, setUsers] = useState<User[]>([]);
    const [search, setSearch] = useState("");

    const getUsers = async () => {
        const res = await fetch(`http://localhost:8080/users`);
        if (res.ok) {
            const data: User[] = await res.json();
            setUsers(data);
        }
    };

    const createUser = async (user: CreateUserRequest) => {
        const res = await fetch(`http://localhost:8080/users`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(user),
        });
        if (res.ok) {
            const result: User = await res.json();
            setUsers((users) => [...users, result]);
        }
    };

    const filteredUsers = users.filter((user) =>
        user.username.startsWith(search),
    );

    useEffect(() => {
        getUsers();
    }, []);

    return (
        <Container size="lg" className={styles.container}>
            <h1>Users</h1>
            <div className={styles.controls}>
                <SearchField
                    value={search}
                    placeholder="Search users..."
                    onChange={(e) => setSearch(e.target.value)}
                    onClear={() => setSearch("")}
                />
                <CreateUserDialog onCreate={createUser}>
                    <Button size="sm" color="primary">
                        New User
                    </Button>
                </CreateUserDialog>
            </div>
            <UserList users={filteredUsers} />
        </Container>
    );
}

export function UserList({ users }: { users: { username: string }[] }) {
    return (
        <ul className={styles.list}>
            {users.map((user) => (
                <li key={user.username} className={styles.listItem}>
                    <Link href={user.username}>
                        <Card className={styles.card}>
                            <div className={styles.avatar}></div>
                            <p>{user.username}</p>
                        </Card>
                    </Link>
                </li>
            ))}
        </ul>
    );
}
