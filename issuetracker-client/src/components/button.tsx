import { forwardRef } from "react";
import styles from "./styles/button.module.css";

type ButtonProps = {
    variant?: "solid" | "outline" | "soft" | "transparent";
    color?: "primary" | "secondary" | "neutral" | "destructive";
    size?: "sm" | "md";
} & React.HTMLAttributes<HTMLButtonElement>;

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
    (props, ref) => {
        const { variant, color, size, ...btnProps } = props;

        return (
            <button
                ref={ref}
                data-accent={color || "neutral"}
                className={`${styles.btn} ${styles[size || "md"]} ${styles[variant || "solid"]}`}
                {...btnProps}
            />
        );
    },
);

Button.displayName = "Button";
