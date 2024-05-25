import { forwardRef } from "react";
import styles from "./styles/text-field.module.css";

type TextFieldProps = {
    variant?: "normal" | "soft" | "inset" | "shadow";
    size?: "sm" | "md";
} & React.InputHTMLAttributes<HTMLInputElement>;

export const TextField = forwardRef<HTMLInputElement, TextFieldProps>(
    (props, ref) => {
        const { variant, size, className, ...inputProps } = props;
        return (
            <input
                ref={ref}
                type="text"
                className={`${styles["text-field"]} ${styles[size || ""]} ${styles[variant || ""]} ${className}`}
                data-accent="secondary"
                {...inputProps}
            />
        );
    },
);

TextField.displayName = "TextField";
