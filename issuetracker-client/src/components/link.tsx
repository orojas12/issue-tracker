import type { ComponentPropsWithoutRef } from "react";
import { Link as RouterLink } from "react-router-dom";

import styles from "./styles/link.module.css";

type LinkProps = ComponentPropsWithoutRef<"a">;
export function Link({ children, href, className, ...props }: LinkProps) {
    return (
        <RouterLink
            className={styles.link + " " + className}
            to={href || ""}
            {...props}
        >
            {children}
        </RouterLink>
    );
}
