import { ComponentPropsWithRef, forwardRef } from "react";
import styles from "./styles/button.module.css";

type ButtonProps = {
    variant?: "solid" | "outline" | "soft" | "transparent" | "link";
    color?: "primary" | "secondary" | "neutral" | "destructive";
    size?: "sm" | "md";
} & ComponentPropsWithRef<"button">;

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
    (props, ref) => {
        const { variant, color, size, className, ...btnProps } = props;

        return (
            <button
                ref={ref}
                data-accent={color || "neutral"}
                className={`${styles.btn} ${styles[size || "md"]} ${styles[variant || "solid"]} ${className}`}
                {...btnProps}
            />
        );
    },
);

Button.displayName = "Button";
