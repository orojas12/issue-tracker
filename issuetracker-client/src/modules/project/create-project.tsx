import {
    Button,
    Dialog,
    DialogClose,
    DialogContent,
    DialogControls,
    DialogTrigger,
    TextField,
} from "@/components";
import React, { useState } from "react";

export function CreateProjectDialog({
    children,
    onCreate,
}: {
    children: React.ReactNode;
    onCreate: (data: { name: string }) => void;
}) {
    const [name, setName] = useState("");

    return (
        <Dialog>
            <DialogTrigger>{children}</DialogTrigger>
            <DialogContent title="Create new project">
                <TextField
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    label="Project Name"
                />
                <DialogControls>
                    <DialogClose>
                        <Button variant="soft">Cancel</Button>
                    </DialogClose>
                    <DialogClose>
                        <Button
                            color="primary"
                            onClick={() => onCreate({ name: name })}
                        >
                            Create
                        </Button>
                    </DialogClose>
                </DialogControls>
            </DialogContent>
        </Dialog>
    );
}
