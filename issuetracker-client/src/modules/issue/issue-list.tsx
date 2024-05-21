import {
    Box,
    Button,
    Container,
    Dialog,
    Flex,
    Heading,
    Separator,
    Text,
} from "@radix-ui/themes";
import { SearchField } from "@/components/search-field";
import { FormEvent, useEffect, useState } from "react";
import type { Issue } from "./types";

import styles from "./styles/issue-list.module.css";
import { IssueBadge } from "@/modules/issue/issue-badge";
import { Link } from "@/components/link";
import { CreateIssueForm } from "@/modules/issue/create-issue.tsx";

const dateFormatter = Intl.DateTimeFormat(undefined, {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
});

const timeFormatter = Intl.DateTimeFormat(undefined, {
    minute: "2-digit",
    second: "2-digit",
});

export function IssueList() {
    const [issues, setIssues] = useState<Issue[]>([]);
    const [search, setSearch] = useState("");

    const getIssues = async () => {
        const res = await fetch(`http://localhost:8080/issues`);
        if (res.ok) {
            const data: Issue[] = await res.json();
            setIssues(
                data.map((issue) => ({
                    ...issue,
                    createdAt: new Date(issue.createdAt),
                    dueDate: issue.dueDate
                        ? new Date(issue.dueDate)
                        : undefined,
                })),
            );
        }
    };

    const onCreateIssue = (newIssue: Issue) => {
        setIssues([
            ...issues,
            {
                ...newIssue,
                createdAt: new Date(newIssue.createdAt),
                dueDate: newIssue.dueDate
                    ? new Date(newIssue.dueDate)
                    : undefined,
            },
        ]);
    };

    useEffect(() => {
        getIssues();
    }, []);

    const filteredIssues = issues.filter((issue) =>
        issue.title.includes(search),
    );

    return (
        <Container size="3" p="6">
            <Flex direction="column" gap="5" align="stretch">
                <Heading>Issues</Heading>
                <Flex justify="between" gap="4">
                    <SearchField
                        value={search}
                        placeholder="Search issues..."
                        onChange={(e) => setSearch(e.target.value)}
                        onClear={() => setSearch("")}
                    />
                    <NewIssueDialog onCreateIssue={onCreateIssue} />
                </Flex>
                <Separator size="4" />
                <Box>
                    <Box className={styles["list-header"]} p="4">
                        blah
                    </Box>
                    <ul className={styles.list}>
                        {filteredIssues.map((issue) => (
                            <IssueItem key={issue.id} issue={issue} />
                        ))}
                    </ul>
                </Box>
            </Flex>
        </Container>
    );
}

interface NewIssueDialogProps {
    onCreateIssue: (newIssue: Issue) => void;
}
function NewIssueDialog({ onCreateIssue }: NewIssueDialogProps) {
    const [open, setOpen] = useState(false);

    const onSubmit = async (e: FormEvent) => {
        e.preventDefault();
        const localTimeZone = new Intl.DateTimeFormat().resolvedOptions()
            .timeZone;
        const formData = new FormData(e.target);
        const dueDate = formData.get("dueDate") as string;
        const data = {
            title: formData.get("title"),
            description: formData.get("description"),
            dueDate: dueDate,
            dueDateTimeZone: dueDate && localTimeZone,
        };
        const res = await fetch(`http://localhost:8080/issues`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });
        if (res.ok) {
            const newIssue: Issue = await res.json();
            onCreateIssue(newIssue);
            setOpen(false);
        }
    };

    return (
        <Dialog.Root open={open} onOpenChange={setOpen}>
            <Dialog.Trigger>
                <Button>New Issue</Button>
            </Dialog.Trigger>
            <Dialog.Content>
                <Dialog.Title mb="6">Create new issue</Dialog.Title>
                <CreateIssueForm
                    onSubmit={onSubmit}
                    onCancel={() => setOpen(false)}
                />
            </Dialog.Content>
        </Dialog.Root>
    );
}

function IssueItem({ issue }: { issue: Issue }) {
    return (
        <li className={styles["list-item"]}>
            <Link to={issue.id.toString()}>{issue.title}</Link>
            <Box flexGrow="1">
                <Text mr="1" size="1">
                    #{issue.id}{" "}
                    {"created on " + dateFormatter.format(issue.createdAt)}
                </Text>
                <IssueBadge closed={issue.closed} />
            </Box>
        </li>
    );
}
