import { useEffect, useState } from "react";
import { Team } from "./types";
import { Link } from "react-router-dom";
import { Button, Flex, Heading, Table } from "@radix-ui/themes";
import { CreateTeamDialog } from "./create-team";

export function TeamsList() {
    const [teams, setTeams] = useState<Team[]>([]);

    const getAllTeams = async () => {
        const res = await fetch(`http://localhost:8080/teams`);
        const data: Team[] = await res.json();
        setTeams(data);
    };

    useEffect(() => {
        getAllTeams();
    }, []);

    return (
        <Flex direction="column" gap="4">
            <Heading>Team Management</Heading>
            <CreateTeamDialog>
                <Button>Create new team...</Button>
            </CreateTeamDialog>
            <Table.Root>
                <Table.Header>
                    <Table.Row>
                        <Table.ColumnHeaderCell>Name</Table.ColumnHeaderCell>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {teams.map((team) => (
                        <Table.Row key={team.id}>
                            <Table.RowHeaderCell>
                                <Link to={team.id}>{team.name}</Link>
                            </Table.RowHeaderCell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table.Root>
        </Flex>
    );
}
