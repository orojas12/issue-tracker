import React from "react";
import { Link as RouterLink } from "react-router-dom";

import styles from "./styles/link.module.css";

export function Link({ to, children }: { to: string, children: React.ReactNode }) {
    return (
        <RouterLink className={styles.link} to={to}>
            {children}
        </RouterLink>
    );
}