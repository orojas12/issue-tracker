import { ComponentPropsWithRef, forwardRef } from "react";
import styles from "./styles/text-field.module.css";
import { TextField as RTextField } from "@radix-ui/themes";

type TextFieldProps = {
    variant?: "normal" | "soft" | "inset" | "shadow";
    label?: string;
    size?: "sm" | "md";
} & Omit<ComponentPropsWithRef<"input">, "size">;

export const TextField = forwardRef<HTMLInputElement, TextFieldProps>(
    (props, ref) => {
        const {
            variant,
            label: labelText,
            size,
            className,
            ...inputProps
        } = props;

        const inputElement = (
            <input
                ref={ref}
                type="text"
                className={`${styles["text-field"]} ${styles[size || ""]} ${styles[variant || ""]} ${className}`}
                data-accent="secondary"
                {...inputProps}
            />
        );

        return labelText ? (
            <label className={styles.label}>
                <span className={styles.labelText}>{labelText}</span>
                {inputElement}
            </label>
        ) : (
            inputElement
        );
    },
);

TextField.displayName = "TextField";
