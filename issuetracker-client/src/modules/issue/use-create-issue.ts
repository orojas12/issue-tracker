import { useMutate } from "@/hooks/use-mutate";
import type { CreateIssue, Issue } from "./types";

export function useCreateIssue() {
    const { data, isLoading, error, mutate } = useMutate<Issue>(
        `http://localhost:8080/issues`,
    );

    const createIssue = (newIssue: CreateIssue) => {
        return mutate(newIssue);
    };

    return {
        newIssue: data
            ? {
                  ...data,
                  createdAt: new Date(data.createdAt),
                  dueDate: data.dueDate ? new Date(data.dueDate) : null,
              }
            : null,
        isLoading,
        error,
        createIssue,
    };
}
