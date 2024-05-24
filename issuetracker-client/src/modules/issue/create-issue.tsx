import { Button, Flex, TextArea, TextField } from "@radix-ui/themes";

import styles from "./styles/create-issue.module.css";
import { FormEvent, MutableRefObject, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { CreateIssue } from "./types";
import { useCreateIssue } from "./use-create-issue";

function formatLocalDate(date: Date) {
    const offsetMillis = date.getTimezoneOffset() * 60000;
    const dateMillis = date.getTime();
    return new Date(dateMillis - offsetMillis).toISOString().substring(0, 16);
}

type CreateIssueFormProps = {
    onSubmit: () => void;
    onCancel: () => void;
};

export function CreateIssueForm({ onSubmit, onCancel }: CreateIssueFormProps) {
    const datePicker = useRef<HTMLInputElement | null>(null);
    const form = useRef<HTMLFormElement | null>(null);
    const navigate = useNavigate();

    const { newIssue, isLoading, error, createIssue } = useCreateIssue();

    const onCreateIssueSubmit = (e: FormEvent<HTMLFormElement>) => {
        if (!form.current) return;
        e.preventDefault();
        const localTimeZone = new Intl.DateTimeFormat().resolvedOptions()
            .timeZone;
        const formData = new FormData(form.current);
        const dueDate = formData.get("dueDate") as string;
        const data: CreateIssue = {
            title: formData.get("title"),
            description: formData.get("description"),
            dueDate: dueDate,
            dueDateTimeZone: dueDate && localTimeZone,
        };
        createIssue(data);
    };

    useEffect(() => {
        if (!isLoading && newIssue) {
            navigate(newIssue.id.toString());
        }
    }, [isLoading, newIssue]);

    useEffect(() => {
        if (error) {
            alert(error);
        }
    }, [error]);

    const resetDatePicker = () => {
        if (datePicker.current) datePicker.current.value = "";
    };

    const localDateToday: string = formatLocalDate(new Date());

    return (
        <form ref={form} className={styles.form} onSubmit={onCreateIssueSubmit}>
            <label>
                Title
                <TextField.Root
                    name="title"
                    placeholder="Enter title..."
                    className={styles.input}
                    required
                />
            </label>
            <label className={styles["date-label"]}>
                Due Date
                <div className={styles.date}>
                    <input
                        id="create-issue-date-picker"
                        ref={datePicker}
                        name="dueDate"
                        className={styles["date-picker"]}
                        type="datetime-local"
                        min={localDateToday}
                    />
                    <Button
                        size="1"
                        color="gray"
                        variant="soft"
                        type="button"
                        onClick={resetDatePicker}
                    >
                        Reset
                    </Button>
                </div>
            </label>
            <label>
                Description
                <TextArea
                    name="description"
                    placeholder="Enter description..."
                    resize="vertical"
                    className={styles.input}
                />
            </label>
            <Flex justify="end" gap="2">
                <Button
                    type="reset"
                    variant="outline"
                    color="gray"
                    onClick={onCancel}
                >
                    Cancel
                </Button>
                <Button type="submit">Submit</Button>
            </Flex>
        </form>
    );
}
