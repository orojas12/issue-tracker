import React, { ComponentPropsWithRef } from "react";
import styles from "./styles/text-area.module.css";

type TextAreaProps = {
    label?: string;
    resize?: "vertical" | "horizontal" | "both" | "none";
} & ComponentPropsWithRef<"textarea">;

export const TextArea = React.forwardRef<HTMLTextAreaElement, TextAreaProps>(
    (props, ref) => {
        const { label: labelText, resize, className, ...inputProps } = props;

        const element = (
            <textarea
                data-accent="secondary"
                ref={ref}
                className={`${styles.textarea} ${styles["resize-" + (resize || "vertical")]}`}
                {...inputProps}
            />
        );

        return labelText ? (
            <label className={styles.label}>
                <span className={styles.labelText}>{labelText}</span>
                {element}
            </label>
        ) : (
            element
        );
    },
);
