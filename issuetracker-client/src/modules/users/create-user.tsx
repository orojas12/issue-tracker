import { Button, Dialog, Flex, Text, TextField } from "@radix-ui/themes";
import { useState } from "react";
import { CreateUserRequest } from "../teams/types";

export function CreateUserDialog({
    children,
    onCreate,
}: {
    children: React.ReactNode;
    onCreate: (user: CreateUserRequest) => void;
}) {
    const [username, setUsername] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");

    return (
        <Dialog.Root>
            <Dialog.Trigger>{children}</Dialog.Trigger>
            <Dialog.Content maxWidth="450px">
                <Dialog.Title>Create new user</Dialog.Title>
                <Dialog.Description size="2">
                    Create a new user account
                </Dialog.Description>
                <Flex direction="column" gap="4" mt="4">
                    <label>
                        <Text>Username</Text>
                        <TextField.Root
                            placeholder="Enter username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </label>
                    <label>
                        <Text>First Name</Text>
                        <TextField.Root
                            placeholder="Enter first name"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                        />
                    </label>
                    <label>
                        <Text>Last Name</Text>
                        <TextField.Root
                            placeholder="Enter last name"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                        />
                    </label>
                </Flex>
                <Flex gap="3" mt="4" justify="end">
                    <Dialog.Close>
                        <Button variant="soft" color="gray">
                            Cancel
                        </Button>
                    </Dialog.Close>
                    <Dialog.Close>
                        <Button
                            onClick={() =>
                                onCreate({ username, firstName, lastName })
                            }
                        >
                            Save
                        </Button>
                    </Dialog.Close>
                </Flex>
            </Dialog.Content>
        </Dialog.Root>
    );
}
