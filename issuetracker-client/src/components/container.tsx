import styles from "./styles/container.module.css";

type ContainerProps = {
    size?: "sm" | "md" | "lg" | "xl";
    fluid?: boolean;
    children?: React.ReactNode;
} & React.HTMLAttributes<HTMLDivElement>;

export function Container(props: ContainerProps) {
    const { fluid, size, className, children, ...divProps } = props;
    return (
        <div
            className={`
                ${fluid ? styles.containerFluid : styles.container} 
                ${size ? styles["container--" + size] : ""}
                ${className}
            `}
            {...divProps}
        >
            {children}
        </div>
    );
}
