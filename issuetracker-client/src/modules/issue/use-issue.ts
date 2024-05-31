import type { Issue, IssueData, UpdateIssue } from "./types";
import { useFetch } from "@/hooks/use-fetch";

export function useIssue(id: string) {
    const { data, isLoading, error, refresh } = useFetch<IssueData>(
        `http://localhost:8080/issues/${id}`,
    );

    const updateIssue = async (issue: UpdateIssue) => {
        refresh({
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(issue),
        });
    };

    const deleteIssue = async () => {
        refresh({
            method: "DELETE",
        });
    };

    const issue: Issue | null = data
        ? {
              ...data,
              createdAt: new Date(data.createdAt),
              dueDate: data.dueDate ? new Date(data.dueDate) : null,
          }
        : null;

    return { issue, isLoading, error, updateIssue, deleteIssue };
}
