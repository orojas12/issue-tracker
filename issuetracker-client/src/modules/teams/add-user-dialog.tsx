import { Button, Dialog, Flex, Text, TextField } from "@radix-ui/themes";
import { Command } from "cmdk";
import { useEffect, useState } from "react";
import { User } from "./types";

import styles from "./styles/add-user-dialog.module.css";

export function AddUserDialog({
    onSubmit,
    teamMembers,
}: {
    onSubmit: (username: string) => void;
    teamMembers: string[];
}) {
    const [users, setUsers] = useState<User[]>([]);
    const [username, setUsername] = useState<string>("");

    const getAllUsers = async () => {
        const res = await fetch("http://localhost:8080/users");
        const users: User[] = await res.json();
        setUsers(users);
    };

    useEffect(() => {
        getAllUsers();
    }, []);

    return (
        <Dialog.Root>
            <Dialog.Trigger>
                <Button>Add user...</Button>
            </Dialog.Trigger>
            <Dialog.Content className={styles["dialog"]}>
                <Dialog.Title>Add user to team</Dialog.Title>
                <Dialog.Description mb="4">
                    Select a user to add to this team
                </Dialog.Description>
                <UserSelect
                    users={users.filter(
                        (user) => !teamMembers.includes(user.username),
                    )}
                    selection={username}
                    onSelect={setUsername}
                />
                <Flex justify="end" gap="2">
                    <Dialog.Close>
                        <Button color="gray" variant="outline">
                            Cancel
                        </Button>
                    </Dialog.Close>
                    <Dialog.Close>
                        <Button onClick={() => onSubmit(username)}>
                            Add user
                        </Button>
                    </Dialog.Close>
                </Flex>
            </Dialog.Content>
        </Dialog.Root>
    );
}

// TODO: fix add-user-dialog sizing

function UserSelect({
    users,
    selection,
    onSelect,
}: {
    users: User[];
    selection: string;
    onSelect: (value: string) => void;
}) {
    const [value, setValue] = useState("");
    const [input, setInput] = useState("");

    const getUserItemStyles = (username: string) => {
        const userItemStyles = [styles["list-item"]];
        if (value === username) userItemStyles.push(styles.active);
        if (selection === username) userItemStyles.push(styles.selected);

        return userItemStyles.join(" ");
    };

    return (
        <Command label="Users" value={value} onValueChange={setValue}>
            <Flex direction="column" gap="4">
                <TextField.Root
                    placeholder="Search user..."
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                />
                <Command.List className={styles["list"]}>
                    <Command.Empty>No users found.</Command.Empty>
                    {users
                        .filter((user) => user.username.startsWith(input))
                        .map((user) => (
                            <Command.Item
                                key={user.username}
                                value={user.username}
                                onSelect={onSelect}
                                className={getUserItemStyles(user.username)}
                            >
                                <Text>{user.username}</Text>
                            </Command.Item>
                        ))}
                </Command.List>
            </Flex>
        </Command>
    );
}
