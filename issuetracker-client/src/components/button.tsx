import styles from "./styles/button.module.css";

type ButtonProps = {
    variant?: "solid" | "outline" | "soft" | "transparent";
    color?: "primary" | "secondary" | "neutral" | "destructive";
    size?: "sm" | "md";
} & React.HTMLAttributes<HTMLButtonElement>;

export function Button(props: ButtonProps) {
    const { variant, color, size, ...btnProps } = props;

    return (
        <button
            data-accent={color || "neutral"}
            className={`${styles.btn} ${styles[size || "md"]} ${styles[variant || "solid"]}`}
            {...btnProps}
        />
    );
}
