import {
    Button,
    TextField,
    Dialog,
    DialogTrigger,
    DialogContent,
    DialogClose,
    DialogControls,
} from "@/components";
import { useState } from "react";
import type { CreateUserRequest } from "@/modules/team/types";

import styles from "./styles/create-user.module.css";

export function CreateUserDialog({
    children,
    onCreate,
}: {
    children: React.ReactNode;
    onCreate: (user: CreateUserRequest) => void;
}) {
    const [username, setUsername] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");

    return (
        <Dialog>
            <DialogTrigger>{children}</DialogTrigger>
            <DialogContent
                title="Create new user"
                description="Create a new user account."
                className={styles.content}
            >
                <TextField
                    placeholder="Enter username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    label="Username"
                />
                <TextField
                    placeholder="Enter first name"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    label="First Name"
                />
                <TextField
                    placeholder="Enter last name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    label="Last Name"
                />
                <DialogControls className={styles.controls}>
                    <DialogClose>
                        <Button variant="soft">Cancel</Button>
                    </DialogClose>
                    <DialogClose>
                        <Button
                            onClick={() =>
                                onCreate({ username, firstName, lastName })
                            }
                            color="primary"
                        >
                            Save
                        </Button>
                    </DialogClose>
                </DialogControls>
            </DialogContent>
        </Dialog>
    );
}
