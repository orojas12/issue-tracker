import { useNavigate } from "react-router-dom";
import { useCreateIssue } from "./use-create-issue";
import { useEffect, useRef, useState, type FormEvent } from "react";
import { TextField } from "@/components/text-field";
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogControls,
    DialogTrigger,
} from "@/components/dialog";
import { Button } from "@/components/button";
import { TextArea } from "@/components/text-area";

import styles from "./styles/new-issue-form.module.css";
import { CreateIssue } from "./types";

function formatLocalDate(date: Date) {
    const offsetMillis = date.getTimezoneOffset() * 60000;
    const dateMillis = date.getTime();
    return new Date(dateMillis - offsetMillis).toISOString().substring(0, 16);
}

export function NewIssueForm() {
    const { createIssue, isLoading, error } = useCreateIssue();
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const datePicker = useRef<HTMLInputElement | null>(null);

    useEffect(() => {
        if (error) {
            alert(error);
        }
    }, [error]);

    const localDateToday: string = formatLocalDate(new Date());

    const resetDatePicker = () => {
        if (datePicker.current) datePicker.current.value = "";
    };

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const localTimeZone = new Intl.DateTimeFormat().resolvedOptions()
            .timeZone;
        const formData = new FormData(e.currentTarget);
        const newIssue: CreateIssue = {
            title: formData.get("title") as string,
            description: formData.get("description") as string | null,
            dueDate: formData.get("dueDate") as string | null,
            dueDateTimeZone: formData.get("dueDate") && localTimeZone,
        };
        const result = await createIssue(newIssue);
        if (result) {
            navigate("/issues/" + result.id);
        }
    };

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger>
                <Button color="primary" size="sm">
                    New Issue
                </Button>
            </DialogTrigger>
            <DialogContent
                title="Create new issue"
                description="Create a new issue for your project."
                className={styles.dialog}
            >
                <form onSubmit={onSubmit} className={styles.form}>
                    <TextField required name="title" label="Title" />
                    <TextArea name="description" label="Description" />
                    <label className={styles.dateLabel}>
                        Due Date
                        <div className={styles.date}>
                            <input
                                id="create-issue-date-picker"
                                ref={datePicker}
                                name="dueDate"
                                className={styles.datePicker}
                                type="datetime-local"
                                min={localDateToday}
                            />
                            <Button
                                size="sm"
                                variant="soft"
                                type="button"
                                onClick={resetDatePicker}
                            >
                                Reset
                            </Button>
                        </div>
                    </label>
                    <DialogControls>
                        <DialogClose>
                            <Button type="reset" variant="outline">
                                Cancel
                            </Button>
                        </DialogClose>
                        <Button type="submit" color="primary">
                            Submit
                        </Button>
                    </DialogControls>
                </form>
            </DialogContent>
        </Dialog>
    );
}
