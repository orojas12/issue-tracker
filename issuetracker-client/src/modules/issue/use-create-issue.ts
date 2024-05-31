import { useMutate } from "@/hooks/use-mutate";
import type { CreateIssue, Issue } from "./types";

export function useCreateIssue() {
    const { isLoading, error, mutate } = useMutate<CreateIssue, Issue>(
        `http://localhost:8080/issues`,
    );

    const createIssue = (newIssue: CreateIssue) => {
        return mutate(newIssue);
    };

    return {
        isLoading,
        error,
        createIssue,
    };
}
