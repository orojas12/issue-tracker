import { ComponentPropsWithRef, forwardRef } from "react";

import styles from "./styles/text-field.module.css";

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
            children,
            ...inputProps
        } = props;

        const inputElement = (
            <div data-accent="secondary" className={styles.inputWrapper}>
                <input
                    ref={ref}
                    type="text"
                    className={`${styles.input} ${styles[size || ""]} ${styles[variant || ""]} ${className}`}
                    {...inputProps}
                />
                {children}
            </div>
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

export function TextFieldSlot({
    side,
    children,
}: {
    side: "left" | "right";
    children: React.ReactNode;
}) {
    return <div className={styles["slot-" + side]}>{children}</div>;
}

TextField.displayName = "TextField";
