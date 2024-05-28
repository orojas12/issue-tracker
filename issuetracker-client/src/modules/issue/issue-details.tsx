import { useNavigate, useParams } from "react-router-dom";
import { useState } from "react";
import { Issue } from "./types.ts";
import {
    Button,
    Container,
    Dialog,
    DialogTrigger,
    DialogClose,
    DialogContent,
    DialogControls,
    TextField,
    TextArea,
} from "@/components";
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

    const toggleClose = () => {
        if (!issue) return;
        updateIssue({
            ...issue,
            closed: !issue.closed,
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
        <Container size="lg" className={styles.container}>
            <h1 className={styles.issueNumber}>Issue #{issue.id}</h1>
            <IssueTitle issue={issue} onChange={onTitleChange} />
            <div className={styles.wrapper}>
                <IssueDescription issue={issue} onChange={onDescChange} />
                <IssuePropertyList issue={issue} />
                <div className={styles.actions}>
                    <Button
                        size="sm"
                        variant="transparent"
                        onClick={toggleClose}
                    >
                        {issue.closed ? "Reopen issue" : "Close issue"}
                    </Button>
                    <Dialog>
                        <DialogTrigger>
                            <Button size="sm" variant="transparent">
                                Delete issue
                            </Button>
                        </DialogTrigger>
                        <DialogContent
                            title="Delete this issue?"
                            description="This issue and any related data will be deleted forever."
                            className={styles.deleteDialogContent}
                        >
                            <DialogControls>
                                <DialogClose>
                                    <Button size="sm" variant="soft">
                                        Cancel
                                    </Button>
                                </DialogClose>
                                <Button
                                    size="sm"
                                    color="destructive"
                                    onClick={onDelete}
                                >
                                    Delete issue
                                </Button>
                            </DialogControls>
                        </DialogContent>
                    </Dialog>
                </div>
            </div>
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
        <div className={styles.title}>
            {editable ? (
                <TextField
                    className={styles.titleInput}
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                />
            ) : (
                <h1 className={styles.titleText}>{issue.title}</h1>
            )}

            <div className={styles.titleControls}>
                {editable ? (
                    <>
                        <Button size="sm" color="primary" onClick={onSave}>
                            Save
                        </Button>
                        <Button
                            size="sm"
                            variant="transparent"
                            onClick={toggleEditable}
                        >
                            Cancel
                        </Button>
                    </>
                ) : (
                    <Button
                        size="sm"
                        variant="link"
                        color="secondary"
                        onClick={toggleEditable}
                        className={styles.editButton}
                    >
                        Edit
                    </Button>
                )}
            </div>
        </div>
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
        <div className={styles.description}>
            <div className={styles.descriptionHeading}>
                <h2 className={styles.descriptionHeadingText}>Description</h2>
                {!editable && (
                    <Button
                        color="secondary"
                        size="sm"
                        variant="link"
                        onClick={toggleEditable}
                        className={styles.editButton}
                    >
                        Edit
                    </Button>
                )}
            </div>

            {editable ? (
                <TextArea
                    defaultValue={issue.description}
                    onChange={(e) => setInput(e.target.value)}
                    resize="vertical"
                    className={styles.descriptionInput}
                />
            ) : (
                <p className={styles.descriptionText}>{issue.description}</p>
            )}

            {editable && (
                <div className={styles.descriptionEditControls}>
                    <Button size="sm" color="primary" onClick={onSave}>
                        Save
                    </Button>
                    <Button
                        size="sm"
                        variant="transparent"
                        onClick={toggleEditable}
                    >
                        Cancel
                    </Button>
                </div>
            )}
        </div>
    );
}

function IssuePropertyList({ issue }: { issue: Issue }) {
    return (
        <dl className={styles.propertyList}>
            <dt>Status</dt>
            <dd>{issue.closed ? "Closed" : "Open"}</dd>
            <dt>Due Date</dt>
            <dd>{issue.dueDate && dateTimeFormatter.format(issue.dueDate)}</dd>
        </dl>
    );
}
