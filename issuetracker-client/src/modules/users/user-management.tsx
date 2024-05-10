import { useEffect, useState } from "react";
import { CreateUserRequest, User } from "../teams/types";
import {
    AlertDialog,
    Button,
    DropdownMenu,
    Flex,
    Heading,
    Table,
    TextField,
} from "@radix-ui/themes";
import { Link } from "react-router-dom";
import { CreateUserDialog } from "./create-user";
import { Ellipsis } from "lucide-react";

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

    const deleteUser = async (userId: string) => {
        const res = await fetch(`http://localhost:8080/users/${userId}`, {
            method: "DELETE",
        });
        if (res.ok) {
            setUsers((users) => users.filter((user) => user.id !== userId));
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
                                <Link to={`${user.username}`}>
                                    {user.username}
                                </Link>
                            </Table.RowHeaderCell>
                            <Table.Cell>
                                <UserOptions
                                    user={user}
                                    onDelete={deleteUser}
                                />
                            </Table.Cell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table.Root>
        </Flex>
    );
}

function UserOptions({
    user,
    onDelete,
}: {
    user: User;
    onDelete: (userId: string) => void;
}) {
    return (
        <AlertDialog.Root>
            <DropdownMenu.Root>
                <DropdownMenu.Trigger>
                    <Button color="gray" variant="soft" size="1">
                        <Ellipsis />
                    </Button>
                </DropdownMenu.Trigger>
                <DropdownMenu.Content size="1">
                    <AlertDialog.Trigger>
                        <DropdownMenu.Item color="red">
                            Delete
                        </DropdownMenu.Item>
                    </AlertDialog.Trigger>
                </DropdownMenu.Content>
            </DropdownMenu.Root>
            <AlertDialog.Content maxWidth="450px">
                <AlertDialog.Title>Delete user?</AlertDialog.Title>
                <AlertDialog.Description size="2">
                    Are you sure you want to delete {user.username}? This action
                    cannot be reversed.
                </AlertDialog.Description>
                <Flex gap="3" mt="4" justify="end">
                    <AlertDialog.Cancel>
                        <Button variant="soft" color="gray">
                            Cancel
                        </Button>
                    </AlertDialog.Cancel>
                    <AlertDialog.Action>
                        <Button onClick={() => onDelete(user.id)} color="red">
                            Delete user
                        </Button>
                    </AlertDialog.Action>
                </Flex>
            </AlertDialog.Content>
        </AlertDialog.Root>
    );
}
