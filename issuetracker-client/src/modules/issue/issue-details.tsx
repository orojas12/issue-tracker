import { useNavigate, useParams } from "react-router-dom";
import { useState } from "react";
import { Issue } from "./types.ts";
import {
    AlertDialog,
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

const dateTimeFormatter = Intl.DateTimeFormat(undefined, {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
});

export function IssueDetails() {
    const { issueId } = useParams();
    const navigate = useNavigate();
    const { issue, isLoading, error, updateIssue, deleteIssue } = useIssue(
        issueId as string,
    );

    const onTitleChange = (title: string) => {
        if (!issue) return;
        updateIssue({
            title,
            description: issue.description,
            dueDate: issue.dueDate,
            closed: issue.closed,
        });
    };

    const onDescChange = (description: string) => {
        if (!issue) return;
        updateIssue({
            title: issue.title,
            description,
            dueDate: issue.dueDate,
            closed: issue.closed,
        });
    };

    const onDelete = async () => {
        if (!issue) return;
        await deleteIssue();
        navigate("/issues", { replace: true });
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
                    <Card size="3" className={styles["description-card"]}>
                        <IssueDescription
                            issue={issue}
                            onChange={onDescChange}
                        />
                    </Card>
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
                                        {dateTimeFormatter.format(
                                            issue.createdAt,
                                        )}
                                    </DataList.Value>
                                </DataList.Item>
                                <DataList.Item>
                                    <DataList.Label>Due Date</DataList.Label>
                                    <DataList.Value>
                                        {issue.dueDate
                                            ? dateTimeFormatter.format(
                                                  issue.dueDate,
                                              )
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
                                <AlertDialog.Root>
                                    <AlertDialog.Trigger>
                                        <Button variant="ghost" color="red">
                                            Delete issue
                                        </Button>
                                    </AlertDialog.Trigger>
                                    <AlertDialog.Content>
                                        <AlertDialog.Title>
                                            Delete this issue?
                                        </AlertDialog.Title>
                                        <AlertDialog.Description>
                                            This issue and any related data will
                                            be deleted forever.
                                        </AlertDialog.Description>
                                        <Flex gap="3" mt="4" justify="end">
                                            <AlertDialog.Cancel>
                                                <Button
                                                    variant="soft"
                                                    color="gray"
                                                >
                                                    Cancel
                                                </Button>
                                            </AlertDialog.Cancel>
                                            <AlertDialog.Action>
                                                <Button
                                                    variant="solid"
                                                    color="red"
                                                    onClick={onDelete}
                                                >
                                                    Delete issue
                                                </Button>
                                            </AlertDialog.Action>
                                        </Flex>
                                    </AlertDialog.Content>
                                </AlertDialog.Root>
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
        <Box>
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
