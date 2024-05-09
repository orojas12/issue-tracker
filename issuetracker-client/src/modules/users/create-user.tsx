import { Button, Dialog, Flex, TextField, Text } from "@radix-ui/themes";
import { useState } from "react";

export function CreateUserDialog({
    children,
    onCreate,
}: {
    children: React.ReactNode;
    onCreate: (user: { username: string }) => void;
}) {
    const [username, setUsername] = useState("");

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
                            placeholder="Enter team name"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
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
                            onClick={() => onCreate({ username: username })}
                        >
                            Save
                        </Button>
                    </Dialog.Close>
                </Flex>
            </Dialog.Content>
        </Dialog.Root>
    );
}
