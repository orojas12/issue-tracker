import { Box, Container, Flex, Heading, Separator, Text } from "@radix-ui/themes";
import { SearchField } from "@/components/search-field";
import { useEffect, useState } from "react";
import type { Issue } from "./types";

import styles from "./styles/issue-list.module.css";
import { IssueBadge } from "@/modules/issue/issue-badge";
import { Link } from "@/components/link";

export function IssueList() {
    const [issues, setIssues] = useState<Issue[]>([]);
    const [search, setSearch] = useState("");

    const getIssues = async () => {
        const res = await fetch(`http://localhost:8080/issues`);
        if (res.ok) {
            const data = await res.json();
            setIssues(data.map((obj) => ({
                ...obj,
                createdAt: new Date(obj.createdAt),
                dueDate: new Date(obj.dueDate)
            })));
        }
    };

    useEffect(() => {
        getIssues();
    }, []);

    const filteredIssues = issues.filter((issue) => issue.title.includes(search));

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
                </Flex>
                <Separator size="4" />
                <Box>
                    <Box className={styles["list-header"]} p="4">
                        blah
                    </Box>
                    <ul className={styles.list}>
                        {filteredIssues.map((issue) => <IssueItem key={issue.id} issue={issue} />)}
                    </ul>
                </Box>
            </Flex>
        </Container>
    );
}

function IssueItem({ issue }: { issue: Issue }) {
    return (
        <li className={styles["list-item"]}>
            <Link to={issue.id.toString()}>
                {issue.title}
            </Link>
            <Box flexGrow="1">
                <Text mr="1" size="1">#{issue.id} {"created on " + issue.createdAt.toLocaleDateString()}</Text>
                <IssueBadge closed={issue.closed} />
            </Box>
        </li>
    );
}