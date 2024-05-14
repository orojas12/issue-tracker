import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { Issue } from "./types.ts";
import { Badge, Box, Container, DataList, Flex, Heading, Separator, Text } from "@radix-ui/themes";

export function IssueDetails() {
    const { issueId } = useParams();
    const [issue, setIssue] = useState<Issue | null>(null);

    const getIssue = async (issueId: string) => {
        const res = await fetch(`http://localhost:8080/issues/${issueId}`);
        if (res.ok) {
            const data: Issue = await res.json();
            setIssue({
                ...data,
                createdAt: new Date(data.createdAt),
                dueDate: data.dueDate ? new Date(data.dueDate) : undefined
            });
        }
    };

    useEffect(() => {
        if (!issueId) return;
        getIssue(issueId);
    }, []);

    if (!issue) return null;

    const status = issue.closed ? "Closed" : "Open";
    const statusColor = issue.closed ? "blue" : "green";

    return (
        <Container size="3" p="6">
            <Flex direction="column" gap="5">
                <Heading color="gray" size="3">Issue #{issue.id}</Heading>
                <Heading wrap="wrap" weight="medium" size="5">{issue.title}</Heading>
                <Separator size="4" />
                <Flex gap="4">
                    <Box flexShrink="0" flexBasis="300px">
                        <DataList.Root>
                            <DataList.Item>
                                <DataList.Label>
                                    Status
                                </DataList.Label>
                                <DataList.Value>
                                    <Badge color={statusColor}>{status}</Badge>
                                </DataList.Value>
                            </DataList.Item>
                            <DataList.Item>
                                <DataList.Label>Created At</DataList.Label>
                                <DataList.Value>{issue.createdAt.toLocaleString()}</DataList.Value>
                            </DataList.Item>
                            <DataList.Item>
                                <DataList.Label>Due Date</DataList.Label>
                                <DataList.Value>{issue.dueDate ? issue.dueDate.toLocaleDateString() : "-"}</DataList.Value>
                            </DataList.Item>
                        </DataList.Root>
                    </Box>
                    <Box>
                        <Heading size="3" mb="4">Description</Heading>
                        <Text>{issue.description}</Text>
                    </Box>
                </Flex>
            </Flex>
        </Container>
    );
}