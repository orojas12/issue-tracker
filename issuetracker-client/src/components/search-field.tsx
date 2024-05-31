import { Button } from "./button";
import { TextField, TextFieldSlot } from "./text-field";
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
            size="sm"
            className={styles.textField}
            placeholder={placeholder}
            value={value}
            onChange={onChange}
        >
            <TextFieldSlot side="left">
                <Search size="1em" className={styles.searchIcon} />
            </TextFieldSlot>
            {value ? (
                <TextFieldSlot side="right">
                    <Button
                        onClick={onClear}
                        variant="link"
                        className={styles.clearButton}
                    >
                        <X size="1em" />
                    </Button>
                </TextFieldSlot>
            ) : null}
        </TextField>
    );
}
