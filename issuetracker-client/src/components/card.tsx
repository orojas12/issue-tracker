import styles from "./styles/card.module.css";

type CardProps = React.HTMLAttributes<HTMLDivElement>;

export function Card(props: CardProps) {
    const { className, ...cardProps } = props;
    return <div className={styles.card + " " + className} {...cardProps} />;
}
