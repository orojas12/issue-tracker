import {
    AlertDialog,
    Box,
    Button,
    Container,
    DropdownMenu,
    Flex,
    Heading,
    Section,
    Separator,
} from "@radix-ui/themes";
import { Ellipsis } from "lucide-react";
import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import type { Team } from "./types";

import { SearchField } from "@/components/search-field";
import { AddUserDialog } from "./add-user-dialog";
import styles from "./styles/team-details.module.css";

export function TeamDetails() {
    const navigate = useNavigate();
    const { teamId } = useParams();
    const [team, setTeam] = useState<Team | null>(null);
    const [search, setSearch] = useState("");

    const getTeam = async (teamId: string) => {
        if (!teamId) return;
        const res = await fetch(`http://localhost:8080/teams/${teamId}`);
        const team: Team = await res.json();
        setTeam(team);
    };

    const deleteTeam = async (teamId: string) => {
        if (!teamId) return;
        const res = await fetch(`http://localhost:8080/teams/${teamId}`, {
            method: "DELETE",
        });
        if (res.ok) {
            navigate("/teams", { replace: true });
        }
    };

    const addUserToTeam = async (username: string) => {
        if (!team) return;
        const res = await fetch(
            `http://localhost:8080/teams/${team.id}/members?username=${username}`,
            { method: "POST" },
        );
        if (res.ok) {
            const data: Team = await res.json();
            setTeam(data);
        }
    };

    const removeUserFromTeam = async (username: string) => {
        if (!team) return;
        const res = await fetch(
            `http://localhost:8080/teams/${team.id}/members?username=${username}`,
            { method: "DELETE" },
        );
        if (res.ok) {
            getTeam(team.id);
        }
    };

    useEffect(() => {
        if (!teamId) return;
        getTeam(teamId);
    }, [teamId]);

    if (!team) return null;

    const filteredMembers = team.teamMembers.filter((member) =>
        member.username.startsWith(search),
    );

    return (
        <>
            <Section size="4" className={styles.header}></Section>
            <Container size="2" p="6">
                <Flex direction="column" gap="5">
                    <Heading>{team.name}</Heading>
                    <Flex justify="between">
                        <SearchField
                            value={search}
                            placeholder="Search members..."
                            onChange={(e) => setSearch(e.target.value)}
                            onClear={() => setSearch("")}
                        />
                        <Flex gap="2">
                            <AddUserDialog
                                onSubmit={addUserToTeam}
                                teamMembers={team.teamMembers.map(
                                    (member) => member.username,
                                )}
                            />
                            <TeamOptions team={team} onDelete={deleteTeam} />
                        </Flex>
                    </Flex>
                    <Separator size="4" />
                    <Box>
                        <Heading as="h2" size="4" mb="4">
                            Members
                        </Heading>
                        <ul className={styles["member-list"]}>
                            {filteredMembers.map((member) => (
                                <li key={member.teamId}>
                                    <Link to={`/users/${member.username}`}>
                                        {member.username}
                                    </Link>
                                    <TeamMemberOptions
                                        username={member.username}
                                        onRemove={removeUserFromTeam}
                                    />
                                </li>
                            ))}
                        </ul>
                    </Box>
                </Flex>
            </Container>
        </>
    );
}

function TeamMemberOptions({
    username,
    onRemove,
}: {
    username: string;
    onRemove: (username: string) => void;
}) {
    return (
        <AlertDialog.Root>
            <DropdownMenu.Root>
                <DropdownMenu.Trigger>
                    <Button color="gray" variant="ghost" size="2">
                        <Ellipsis />
                    </Button>
                </DropdownMenu.Trigger>
                <DropdownMenu.Content size="2">
                    <AlertDialog.Trigger>
                        <DropdownMenu.Item color="red">
                            Remove
                        </DropdownMenu.Item>
                    </AlertDialog.Trigger>
                </DropdownMenu.Content>
            </DropdownMenu.Root>
            <AlertDialog.Content maxWidth="450px">
                <AlertDialog.Title>Remove user?</AlertDialog.Title>
                <AlertDialog.Description size="2">
                    Are you sure you want to remove {username} from this team?
                </AlertDialog.Description>
                <Flex gap="3" mt="4" justify="end">
                    <AlertDialog.Cancel>
                        <Button variant="soft" color="gray">
                            Cancel
                        </Button>
                    </AlertDialog.Cancel>
                    <AlertDialog.Action>
                        <Button onClick={() => onRemove(username)} color="red">
                            Remove user
                        </Button>
                    </AlertDialog.Action>
                </Flex>
            </AlertDialog.Content>
        </AlertDialog.Root>
    );
}

function TeamOptions({
    team,
    onDelete,
}: {
    team: Team;
    onDelete: (teamId: string) => void;
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
                <AlertDialog.Title>Delete team?</AlertDialog.Title>
                <AlertDialog.Description size="2">
                    Are you sure you want to delete {team.name}? This action
                    cannot be reversed.
                </AlertDialog.Description>
                <Flex gap="3" mt="4" justify="end">
                    <AlertDialog.Cancel>
                        <Button variant="soft" color="gray">
                            Cancel
                        </Button>
                    </AlertDialog.Cancel>
                    <AlertDialog.Action>
                        <Button onClick={() => onDelete(team.id)} color="red">
                            Delete team
                        </Button>
                    </AlertDialog.Action>
                </Flex>
            </AlertDialog.Content>
        </AlertDialog.Root>
    );
}
