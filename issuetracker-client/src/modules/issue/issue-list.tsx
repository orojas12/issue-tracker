import { Container } from "@/components/container";
import {
    Box,
    Button,
    Card,
    Dialog,
    Flex,
    Heading,
    Separator,
    Text,
} from "@radix-ui/themes";
import { SearchField } from "@/components/search-field";
import { FormEvent, useEffect, useRef, useState } from "react";
import type { CreateIssue, Issue } from "./types";

import styles from "./styles/issue-list.module.css";
import { IssueBadge } from "@/modules/issue/issue-badge";
import { Link } from "@/components/link";
import { CreateIssueForm } from "@/modules/issue/create-issue.tsx";
import { useIssueList } from "./use-issue-list";

const dateFormatter = Intl.DateTimeFormat(undefined, {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
});

export function IssueList() {
    const { issues, isLoading, error } = useIssueList();
    const [search, setSearch] = useState("");
    const [dialogOpen, setDialogOpen] = useState(false);

    const filteredIssues = issues.filter((issue) =>
        issue.title.includes(search),
    );

    const closeFormDialog = () => {
        setDialogOpen(false);
    };

    return (
        <Container size="lg">
            <div className={styles.wrapper}>
                <h1>Issues</h1>
                <div className="controls">
                    <SearchField
                        value={search}
                        placeholder="Search issues..."
                        onChange={(e) => setSearch(e.target.value)}
                        onClear={() => setSearch("")}
                    />
                    <NewIssueDialog open={dialogOpen} setOpen={setDialogOpen}>
                        <CreateIssueForm
                            onSubmit={closeFormDialog}
                            onCancel={() => setDialogOpen(false)}
                        />
                    </NewIssueDialog>
                </div>
                <Card className={styles["list-wrapper"]}>
                    <Box className={styles["list-header"]} p="4">
                        blah
                    </Box>
                    <ul className={styles.list}>
                        {filteredIssues.map((issue) => (
                            <IssueItem key={issue.id} issue={issue} />
                        ))}
                    </ul>
                </Card>
            </div>
        </Container>
    );
}

interface NewIssueDialogProps {
    open: boolean;
    setOpen: (open: boolean) => void;
    children: React.ReactNode;
}
function NewIssueDialog({ open, setOpen, children }: NewIssueDialogProps) {
    return (
        <Dialog.Root open={open} onOpenChange={setOpen}>
            <Dialog.Trigger>
                <Button>New Issue</Button>
            </Dialog.Trigger>
            <Dialog.Content>
                <Dialog.Title mb="6">Create new issue</Dialog.Title>
                {children}
            </Dialog.Content>
        </Dialog.Root>
    );
}

function IssueItem({ issue }: { issue: Issue }) {
    return (
        <li className={styles["list-item"]}>
            <Link to={issue.id.toString()}>
                {issue.title.trim() || "[no title]"}
            </Link>
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
