import { Button as RadixButton } from "@radix-ui/themes";
import type { ButtonProps as RadixButtonProps } from "@radix-ui/themes";

import styles from "./styles/button.module.css";

interface ButtonProps extends Omit<RadixButtonProps, "variant"> {
    variant: RadixButtonProps["variant"] | "transparent";
}

export function Button(props: ButtonProps) {
    const { variant, ...btnProps } = props;

    return (
        <RadixButton
            {...btnProps}
            variant={variant === "transparent" ? "soft" : variant}
            className={
                variant === "transparent" ? styles["button--transparent"] : ""
            }
        />
    );
}
