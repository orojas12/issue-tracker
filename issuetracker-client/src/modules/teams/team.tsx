import { useEffect, useState } from "react";
import type { Team, TeamMember, User } from "./types";
import { Ellipsis } from "lucide-react";
import {
    Box,
    Button,
    Dialog,
    DropdownMenu,
    Flex,
    Heading,
    Table,
    Text,
} from "@radix-ui/themes";
import { Command } from "cmdk";

export function TeamPage() {
    const [team, setTeam] = useState<Team | null>(null);

    const getTeam = async (teamId: string) => {
        const res = await fetch(`http://localhost:8080/teams/${teamId}`);
        const team: Team = await res.json();
        setTeam(team);
    };

    const addUserToTeam = async (username: string) => {
        if (!team) return;
        const res = await fetch(
            `http://localhost:8080/teams/${team.id}/members?username=${username}`,
            {
                method: "POST",
            },
        );
        if (res.ok) {
            getTeam("team1");
        }
    };

    useEffect(() => {
        getTeam("team1");
    }, []);

    if (!team) return null;

    return (
        <Flex direction="column" gap="4">
            <Heading>{team.name}</Heading>
            <AddUserDialog
                onSubmit={addUserToTeam}
                teamMembers={team.teamMembers.map((member) => member.username)}
            />
            <Table.Root>
                <Table.Header>
                    <Table.Row>
                        <Table.ColumnHeaderCell>
                            Username
                        </Table.ColumnHeaderCell>
                        <Table.ColumnHeaderCell></Table.ColumnHeaderCell>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {team?.teamMembers.map((member) => (
                        <TeamMemberRow
                            key={member.username}
                            teamMember={member}
                        />
                    ))}
                </Table.Body>
            </Table.Root>
        </Flex>
    );
}

function TeamMemberRow({ teamMember }: { teamMember: TeamMember }) {
    return (
        <Table.Row id={teamMember.username} align="center">
            <Table.RowHeaderCell>
                <Text>{teamMember.username}</Text>
            </Table.RowHeaderCell>
            <Table.Cell width="50px">
                <TeamMemberOptions />
            </Table.Cell>
        </Table.Row>
    );
}

function TeamMemberOptions() {
    return (
        <DropdownMenu.Root>
            <DropdownMenu.Trigger>
                <Button color="gray" variant="soft" size="1">
                    <Ellipsis />
                </Button>
            </DropdownMenu.Trigger>
            <DropdownMenu.Content size="1">
                <DropdownMenu.Item color="red">Remove</DropdownMenu.Item>
            </DropdownMenu.Content>
        </DropdownMenu.Root>
    );
}

function AddUserDialog({
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
            <Dialog.Content>
                <Dialog.Title>Add user to team</Dialog.Title>
                <Dialog.Description>
                    Select a user to add to this team
                </Dialog.Description>
                <UserSelect
                    users={users.filter(
                        (user) => !teamMembers.includes(user.username),
                    )}
                    selection={username}
                    onSelect={setUsername}
                />
                <Flex justify="end">
                    <Dialog.Close>
                        <Button onClick={() => onSubmit(username)}>Add</Button>
                    </Dialog.Close>
                </Flex>
            </Dialog.Content>
        </Dialog.Root>
    );
}

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

    return (
        <Command label="Users" value={value} onValueChange={setValue}>
            <Command.Input value={input} onValueChange={setInput} />
            <Command.List>
                <Command.Empty>No users found.</Command.Empty>
                {users
                    .filter((user) => user.username.includes(input))
                    .map((user) => (
                        <Command.Item
                            key={user.username}
                            value={user.username}
                            onSelect={onSelect}
                            style={{
                                backgroundColor:
                                    selection === user.username
                                        ? "lightblue"
                                        : "",
                            }}
                        >
                            {user.username}
                        </Command.Item>
                    ))}
            </Command.List>
        </Command>
    );
}
