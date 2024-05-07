import { Button, Dialog, Flex, Text, TextField } from "@radix-ui/themes";
import React from "react";

export function CreateTeamDialog({ children }: { children: React.ReactNode }) {
    return (
        <Dialog.Root>
            <Dialog.Trigger>{children}</Dialog.Trigger>
            <Dialog.Content maxWidth="450px">
                <Dialog.Title>Create new team</Dialog.Title>
                <Dialog.Description size="2">
                    Create a new team for this project
                </Dialog.Description>
                <Flex direction="column" gap="4" mt="4">
                    <label>
                        <Text>Name</Text>
                        <TextField.Root placeholder="Enter team name" />
                    </label>
                </Flex>
                <Flex gap="3" mt="4" justify="end">
                    <Dialog.Close>
                        <Button variant="soft" color="gray">
                            Cancel
                        </Button>
                    </Dialog.Close>
                    <Dialog.Close>
                        <Button>Save</Button>
                    </Dialog.Close>
                </Flex>
            </Dialog.Content>
        </Dialog.Root>
    );
}
