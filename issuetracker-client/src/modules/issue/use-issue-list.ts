import { useFetch } from "@/hooks/use-fetch";
import type { Issue, IssueData } from "./types";

export function useIssueList() {
    const { data, isLoading, error, refresh } = useFetch<IssueData[]>(
        `http://localhost:8080/issues`,
    );

    const issues: Issue[] = data
        ? data.map((issue) => ({
              ...issue,
              createdAt: new Date(issue.createdAt),
              dueDate: issue.dueDate ? new Date(issue.dueDate) : null,
          }))
        : [];

    return {
        issues,
        isLoading,
        error,
        refresh,
    };
}
