import { useEffect, useState } from "react";
import { User } from "../teams/types";
import { Button, Flex, Heading, Table, TextField } from "@radix-ui/themes";
import { Link } from "react-router-dom";
import { CreateUserDialog } from "./create-user";

export function UserManagement() {
    const [users, setUsers] = useState<User[]>([]);
    const [searchInput, setSearchInput] = useState("");

    const getUsers = async () => {
        const res = await fetch(`http://localhost:8080/users`);
        if (res.ok) {
            const data: User[] = await res.json();
            setUsers(data);
        }
    };

    const createUser = async (user: { username: string }) => {
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
        user.username.startsWith(searchInput),
    );

    useEffect(() => {
        getUsers();
    }, []);

    return (
        <Flex direction="column" gap="4">
            <Heading>User Management</Heading>
            <Flex justify="between">
                <TextField.Root
                    size="2"
                    placeholder="Search users..."
                    value={searchInput}
                    onChange={(e) => setSearchInput(e.target.value)}
                />
                <CreateUserDialog onCreate={createUser}>
                    <Button>Create user...</Button>
                </CreateUserDialog>
            </Flex>
            <Table.Root>
                <Table.Header>
                    <Table.Row>
                        <Table.ColumnHeaderCell>Name</Table.ColumnHeaderCell>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {filteredUsers.map((user) => (
                        <Table.Row key={user.id}>
                            <Table.RowHeaderCell>
                                <Link to="">{user.username}</Link>
                            </Table.RowHeaderCell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table.Root>
        </Flex>
    );
}
