import type { Issue } from "./types";
import { useFetch } from "@/hooks/use-fetch";

export type UpdateIssue = {
    title: string;
    description: string;
    dueDate: Date | null;
    closed: boolean;
};

type IssueData = {
    id: number;
    title: string;
    description: string;
    createdAt: string;
    dueDate: string | null;
    closed: boolean;
};

export function useIssue(id: string) {
    const { data, isLoading, error, refresh } = useFetch<IssueData>(
        `http://localhost:8080/issues/${id}`,
    );

    const update = async (issue: UpdateIssue) => {
        refresh({
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(issue),
        });
    };

    const issue: Issue | null = data
        ? {
              ...data,
              createdAt: new Date(data.createdAt),
              dueDate: data.dueDate ? new Date(data.dueDate) : null,
          }
        : null;

    return { issue, isLoading, error, update };
}
