import { Button } from "./button";
import { TextField } from "./text-field";
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
        <TextField
            className={styles["text-field"]}
            placeholder={placeholder}
            value={value}
            onChange={onChange}
        />
    );
}
