import { useFetch } from "@/hooks/use-fetch";
import type { Team } from "./types";

export function useProject(teamId: string) {
    const { data, isLoading, error, refresh } = useFetch<Team>(
        `http://localhost:8080/teams/${teamId}`,
    );

    return {
        team: data,
        isLoading,
        error,
        refresh,
    };
}
