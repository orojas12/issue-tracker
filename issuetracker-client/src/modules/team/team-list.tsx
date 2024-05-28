import { useEffect, useState } from "react";
import { Team } from "./types";

import styles from "./styles/team-list.module.css";
import { SearchField, Container, Link, Card, Button } from "@/components";

export function TeamsList() {
    const [teams, setTeams] = useState<Team[]>([]);
    const [search, setSearch] = useState("");

    const getAllTeams = async () => {
        const res = await fetch(`http://localhost:8080/teams`);
        const data: Team[] = await res.json();
        setTeams(data);
    };

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
        <Container className={styles.container} size="lg">
            <h1>Teams</h1>
            <div className={styles.controls}>
                <SearchField
                    value={search}
                    placeholder="Search teams..."
                    onChange={(e) => setSearch(e.target.value)}
                    onClear={() => setSearch("")}
                />
                <Button size="sm" color="primary">
                    New team
                </Button>
            </div>
            <div className={styles.grid}>
                {filteredTeams.map((team) => (
                    <Link
                        key={team.id}
                        href={`/teams/${team.id}`}
                        className={styles.link}
                    >
                        <Card className={styles.card}>
                            <div className={styles.cardImg}></div>
                            <p>{team.name}</p>
                        </Card>
                    </Link>
                ))}
            </div>
        </Container>
    );
}
