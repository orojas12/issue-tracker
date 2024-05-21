import { Button, Flex, TextArea, TextField } from "@radix-ui/themes";

import styles from "./styles/create-issue.module.css";
import { FormEvent, useRef } from "react";

function formatLocalDate(date: Date) {
    const offsetMillis = date.getTimezoneOffset() * 60000;
    const dateMillis = date.getTime();
    return new Date(dateMillis - offsetMillis).toISOString().substring(0, 16);
}

type CreateIssueFormProps = {
    onSubmit: (e: FormEvent) => void;
    onCancel: () => void;
};

export function CreateIssueForm({ onSubmit, onCancel }: CreateIssueFormProps) {
    const datePicker = useRef<HTMLInputElement | null>(null);

    const resetDatePicker = () => {
        if (datePicker.current) datePicker.current.value = "";
    };

    const localDateToday: string = formatLocalDate(new Date());

    return (
        <form className={styles.form} onSubmit={onSubmit}>
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
            <Flex justify="end" gap="4">
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

