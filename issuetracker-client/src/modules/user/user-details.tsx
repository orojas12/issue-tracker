import { AlertDialog, Button, Container, DataList, DropdownMenu, Flex, Heading, Separator } from "@radix-ui/themes";
import { Ellipsis } from "lucide-react";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { User } from "@/modules/team/types";

export function UserDetails() {
    const navigate = useNavigate();
    const { username } = useParams();
    const [user, setUser] = useState<User | null>(null);

    const getUser = async (username: string) => {
        const res = await fetch(`http://localhost:8080/users/${username}`);
        if (res.ok) {
            const data: User = await res.json();
            setUser(data);
        }
    };

    const deleteUser = async (userId: string) => {
        const res = await fetch(`http://localhost:8080/users/${userId}`, {
            method: "DELETE"
        });
        if (res.ok) {
            navigate("/users", { replace: true });
        }
    };

    useEffect(() => {
        if (!username) return;
        getUser(username);
    }, [username]);

    if (!user) return null;

    return (
        <Container size="2" p="6">
            <Flex direction="column" gap="5">
                <Heading>User Details</Heading>
                <Separator size="4" />
                <DataList.Root>
                    <DataList.Item>
                        <DataList.Label>Full Name</DataList.Label>
                        <DataList.Value>
                            {user.firstName} {user.lastName}
                        </DataList.Value>
                    </DataList.Item>
                    <DataList.Item>
                        <DataList.Label>Username</DataList.Label>
                        <DataList.Value>{user.username}</DataList.Value>
                    </DataList.Item>

                    <DataList.Item>
                        <DataList.Label>ID</DataList.Label>
                        <DataList.Value>{user.id}</DataList.Value>
                    </DataList.Item>
                    <DataList.Item>
                        <DataList.Label>User Since</DataList.Label>
                        <DataList.Value>
                            {new Date(user.dateCreated).toLocaleDateString()}
                        </DataList.Value>
                    </DataList.Item>
                </DataList.Root>
                <Flex justify="end">
                    <UserOptions user={user} onDelete={deleteUser} />
                </Flex>
            </Flex>
        </Container>
    );
}

export function UserOptions({
                                user,
                                onDelete
                            }: {
    user: User;
    onDelete: (userId: string) => void;
}) {
    return (
        <AlertDialog.Root>
            <DropdownMenu.Root>
                <DropdownMenu.Trigger>
                    <Button color="gray" variant="soft" size="2">
                        <Ellipsis />
                    </Button>
                </DropdownMenu.Trigger>
                <DropdownMenu.Content size="2">
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
