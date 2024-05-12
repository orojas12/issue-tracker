import { Button, TextField } from "@radix-ui/themes";

import { Search, X } from "lucide-react";
import styles from "./styles/search.module.css";

export function SearchField({
    value,
    onChange,
    onClear,
    placeholder,
}: {
    value?: string;
    onChange?: React.ChangeEventHandler<HTMLInputElement>;
    onClear?: React.MouseEventHandler<HTMLButtonElement>;
    placeholder?: string;
}) {
    return (
        <TextField.Root
            className={styles["text-field"]}
            placeholder={placeholder}
            value={value}
            onChange={onChange}
        >
            <TextField.Slot px="1">
                <Search size="1em" />
            </TextField.Slot>
            <TextField.Slot>
                <Button
                    disabled={!value}
                    variant="ghost"
                    color="gray"
                    size="1"
                    onClick={onClear}
                >
                    <X opacity={!value ? "0" : "100"} size="1em" />
                </Button>
            </TextField.Slot>
        </TextField.Root>
    );
}
