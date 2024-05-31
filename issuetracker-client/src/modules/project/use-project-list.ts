import { useFetch } from "@/hooks/use-fetch";
import { Team } from "./types";

export function useProjectList() {
    const { data, isLoading, error, refresh } = useFetch<Team[]>(
        `http://localhost:8080/teams`,
    );

    return {
        projects: data && data.map((team: Team) => ({ ...team })),
        isLoading,
        error,
    };
}
