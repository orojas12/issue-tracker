import { Button, Container, Flex, Heading, Separator } from "@radix-ui/themes";
import { useEffect, useState } from "react";
import { CreateUserRequest, User } from "@/modules/team/types";
import { CreateUserDialog } from "./create-user";

import { SearchField } from "@/components/search-field";
import { Link } from "react-router-dom";
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
        <Container size="2" p="6">
            <Flex direction="column" gap="5" align="stretch">
                <Heading>Users</Heading>
                <Flex justify="between">
                    <SearchField
                        value={search}
                        placeholder="Search users..."
                        onChange={(e) => setSearch(e.target.value)}
                        onClear={() => setSearch("")}
                    />
                    <CreateUserDialog onCreate={createUser}>
                        <Button>Create user...</Button>
                    </CreateUserDialog>
                </Flex>
                <Separator size="4" />
                <ul className={styles.list}>
                    {filteredUsers.map((user) => (
                        <li key={user.id} className={styles["list-item"]}>
                            <Link to={user.username}>{user.username}</Link>
                        </li>
                    ))}
                </ul>
            </Flex>
        </Container>
    );
}
