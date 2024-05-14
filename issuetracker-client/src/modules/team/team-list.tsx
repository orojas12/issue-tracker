import {
    Button,
    Card,
    Container,
    Flex,
    Grid,
    Heading,
    Inset,
    Separator,
    Text,
} from "@radix-ui/themes";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { CreateTeamDialog } from "./create-team";
import { Team } from "./types";

import styles from "./styles/team-list.module.css";
import { SearchField } from "@/components/search-field";

export function TeamsList() {
    const [teams, setTeams] = useState<Team[]>([]);
    const [search, setSearch] = useState("");

    const getAllTeams = async () => {
        const res = await fetch(`http://localhost:8080/teams`);
        const data: Team[] = await res.json();
        setTeams(data);
    };

    // created team name returns null for some reason
    const createTeam = async (data: { name: string }) => {
        const res = await fetch(`http://localhost:8080/teams`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });
        if (res.ok) {
            getAllTeams();
        }
    };

    useEffect(() => {
        getAllTeams();
    }, []);

    const filteredTeams = teams.filter((team) =>
        team.name.toLowerCase().startsWith(search.toLowerCase()),
    );

    return (
        <Container size="2" p="6">
            <Flex direction="column" gap="5" align="stretch">
                <Heading>Teams</Heading>
                <Flex justify="between" gap="4">
                    <SearchField
                        value={search}
                        placeholder="Search teams..."
                        onChange={(e) => setSearch(e.target.value)}
                        onClear={() => setSearch("")}
                    />
                    <CreateTeamDialog onCreate={createTeam}>
                        <Button>Create new team...</Button>
                    </CreateTeamDialog>
                </Flex>
                <Separator size="4" />

                <Flex justify="center">
                    <Grid columns="4" gap="4" justify="center">
                        {filteredTeams.map((team) => (
                            <Link
                                key={team.id}
                                to={team.id}
                                className={styles.link}
                            >
                                <Card className={styles.card}>
                                    <Inset
                                        clip="padding-box"
                                        side="top"
                                        pb="current"
                                    >
                                        <img
                                            className={styles["card-image"]}
                                            src="https://images.unsplash.com/photo-1617050318658-a9a3175e34cb?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=600&q=80"
                                        />
                                    </Inset>
                                    <Flex justify="center" align="center">
                                        <Text size="2">{team.name}</Text>
                                    </Flex>
                                </Card>
                            </Link>
                        ))}
                    </Grid>
                </Flex>
            </Flex>
        </Container>
    );
}
