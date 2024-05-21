import { useParams } from "react-router-dom";
import { useState } from "react";
import { Issue } from "./types.ts";
import {
    Badge,
    Box,
    Card,
    Container,
    DataList,
    Flex,
    Heading,
    Separator,
    Text,
    TextArea,
    TextField,
} from "@radix-ui/themes";
import { Button } from "@/components/button.tsx";
import styles from "./styles/issue-details.module.css";
import { useIssue } from "./use-issue.ts";

export function IssueDetails() {
    const { issueId } = useParams();
    const { issue, isLoading, error, update } = useIssue(issueId as string);

    const onTitleChange = (title: string) => {
        if (!issue) return;
        update({
            title,
            description: issue.description,
            dueDate: issue.dueDate,
            closed: issue.closed,
        });
    };

    const onDescChange = (description: string) => {
        if (!issue) return;
        update({
            title: issue.title,
            description,
            dueDate: issue.dueDate,
            closed: issue.closed,
        });
    };

    const status = issue?.closed ? "Closed" : "Open";
    const statusColor = issue?.closed ? "blue" : "green";

    return !issue ? null : (
        <Container size="3" p="6">
            <Flex direction="column" gap="5">
                <Heading color="gray" size="3">
                    Issue #{issue.id}
                </Heading>
                <IssueTitle issue={issue} onChange={onTitleChange} />
                <Separator size="4" />
                <Flex gap="6">
                    <IssueDescription issue={issue} onChange={onDescChange} />
                    <Flex
                        direction="column"
                        gap="4"
                        flexShrink="0"
                        flexBasis="300px"
                    >
                        <Card size="3">
                            <DataList.Root className={styles.datalist}>
                                <DataList.Item>
                                    <DataList.Label>Status</DataList.Label>
                                    <DataList.Value>
                                        <Badge color={statusColor}>
                                            {status}
                                        </Badge>
                                    </DataList.Value>
                                </DataList.Item>
                                <DataList.Item>
                                    <DataList.Label>Created At</DataList.Label>
                                    <DataList.Value>
                                        {issue.createdAt.toLocaleString()}
                                    </DataList.Value>
                                </DataList.Item>
                                <DataList.Item>
                                    <DataList.Label>Due Date</DataList.Label>
                                    <DataList.Value>
                                        {issue.dueDate
                                            ? issue.dueDate.toLocaleDateString()
                                            : "-"}
                                    </DataList.Value>
                                </DataList.Item>
                            </DataList.Root>
                        </Card>
                        <Card size="3">
                            <Flex direction="column" align="start" gap="4">
                                <Button variant="ghost" color="gray">
                                    Close issue
                                </Button>
                                <Button variant="ghost" color="red">
                                    Delete issue
                                </Button>
                            </Flex>
                        </Card>
                    </Flex>
                </Flex>
            </Flex>
        </Container>
    );
}

function IssueTitle({
    issue,
    onChange,
}: {
    issue: Issue;
    onChange: (title: string) => void;
}) {
    const [editable, setEditable] = useState(false);
    const [input, setInput] = useState(issue.title);

    const onSave = () => {
        onChange(input);
        toggleEditable();
    };

    const toggleEditable = () => {
        setEditable(!editable);
        setInput(issue.title);
    };

    return (
        <Flex justify="between">
            {editable ? (
                <TextField.Root
                    className={styles["input-title"]}
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                />
            ) : (
                <Heading wrap="wrap" weight="medium" size="5">
                    {issue.title}
                </Heading>
            )}

            {editable ? (
                <Flex gap="2">
                    <Button variant="soft" onClick={onSave}>
                        Save
                    </Button>
                    <Button
                        variant="transparent"
                        color="gray"
                        onClick={toggleEditable}
                    >
                        Cancel
                    </Button>
                </Flex>
            ) : (
                <Button
                    variant="transparent"
                    color="gray"
                    onClick={toggleEditable}
                >
                    Edit
                </Button>
            )}
        </Flex>
    );
}

function IssueDescription({
    issue,
    onChange,
}: {
    issue: Issue;
    onChange: (description: string) => void;
}) {
    const [editable, setEditable] = useState(false);
    const [input, setInput] = useState(issue.description);
    const onSave = () => {
        onChange(input);
        toggleEditable();
    };

    const toggleEditable = () => {
        setEditable(!editable);
        setInput(issue.description);
    };

    return (
        <Box flexGrow="1">
            <Flex justify="between" mb="2">
                <Heading size="3" my="auto">
                    Description
                </Heading>
                <Flex gap="2">
                    {!editable && (
                        <Button
                            variant="transparent"
                            color="gray"
                            onClick={toggleEditable}
                        >
                            Edit
                        </Button>
                    )}
                </Flex>
            </Flex>

            {editable ? (
                <TextArea
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    resize="vertical"
                    className={styles["input-desc"]}
                />
            ) : (
                <Text>{issue.description}</Text>
            )}

            {editable && (
                <Flex direction="row-reverse" gap="2" my="3">
                    <Button variant="soft" onClick={onSave}>
                        Save
                    </Button>
                    <Button
                        variant="transparent"
                        color="gray"
                        onClick={toggleEditable}
                    >
                        Cancel
                    </Button>
                </Flex>
            )}
        </Box>
    );
}
