import styles from "./styles/container.module.css";

type ContainerProps = {
    size?: "sm" | "md" | "lg" | "xl";
    fluid?: boolean;
    children?: React.ReactNode;
} & React.HTMLAttributes<HTMLDivElement>;

export function Container(props: ContainerProps) {
    const { fluid, size, children, ...divProps } = props;
    return (
        <div
            className={`
                ${fluid ? styles["container-fluid"] : styles["container"]} 
                ${size ? styles["container--" + size] : ""}
            `}
            {...divProps}
        >
            {children}
        </div>
    );
}
