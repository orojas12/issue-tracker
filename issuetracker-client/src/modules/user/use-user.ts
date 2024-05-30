import { useFetch } from "@/hooks/use-fetch";
import type { User } from "./types";

export function useUser(username: string) {
    const { data, isLoading, error, refresh } = useFetch<User>(
        `http://localhost:8080/users/${username}`,
    );

    return {
        user: data && {
            ...data,
            dateCreated: new Date(data.dateCreated),
        },
        isLoading,
        error,
        refresh,
    };
}
