import styles from "./styles/card.module.css";

type CardProps = React.HTMLAttributes<HTMLDivElement>;

export function Card(props: CardProps) {
    return <div className={styles.card} {...props} />;
}
