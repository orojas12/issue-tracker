import { Container } from "@/components/container";
import { Box, Button, Dialog, Text } from "@radix-ui/themes";
import { SearchField } from "@/components/search-field";
import { FormEvent, useEffect, useRef, useState } from "react";
import type { CreateIssue, Issue } from "./types";

import styles from "./styles/issue-list.module.css";
import { IssueBadge } from "@/modules/issue/issue-badge";
import { Link } from "@/components/link";
import { useIssueList } from "./use-issue-list";
import { NewIssueForm } from "./new-issue-form";

const dateFormatter = Intl.DateTimeFormat(undefined, {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
});

export function IssueList() {
    const { issues, isLoading, error } = useIssueList();
    const [search, setSearch] = useState("");

    const filteredIssues = issues.filter((issue) =>
        issue.title.includes(search),
    );

    return (
        <Container size="lg">
            <div className={styles.wrapper}>
                <div className={styles.controls}>
                    <SearchField
                        value={search}
                        placeholder="Search issues..."
                        onChange={(e) => setSearch(e.target.value)}
                        onClear={() => setSearch("")}
                    />
                    <NewIssueForm />
                </div>
                <ul className={styles.list}>
                    <div data-accent="neutral" className={styles.listHeader}>
                        blah
                    </div>
                    {filteredIssues.map((issue) => (
                        <IssueItem key={issue.id} issue={issue} />
                    ))}
                </ul>
            </div>
        </Container>
    );
}

function IssueItem({ issue }: { issue: Issue }) {
    return (
        <li data-accent="neutral" className={styles.listItem}>
            <Link data-accent="secondary" href={issue.id.toString()}>
                {issue.title.trim() || "[no title]"}
            </Link>
            <div className={styles.listItemDetails}>
                <span className={styles.listItemDetailText}>
                    #{issue.id}{" "}
                    {"created on " + dateFormatter.format(issue.createdAt)}
                </span>
                <IssueBadge closed={issue.closed} />
            </div>
        </li>
    );
}
