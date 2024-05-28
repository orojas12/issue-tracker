import { useContext } from "react";
import { ThemeContext } from "@/theme";
import { Switch, SwitchThumb } from "./switch";

import styles from "./styles/theme-toggle.module.css";
import { Moon, SunDim } from "lucide-react";

export function ThemeToggle() {
    const theme = useContext(ThemeContext);

    if (!theme) return null;

    return (
        <Switch
            onCheckedChange={theme.toggleDark}
            data-accent="neutral"
            className={styles.switch}
        >
            <SwitchThumb className={styles.thumb}>
                {theme.dark ? (
                    <Moon size="12px" className={styles.icon} />
                ) : (
                    <SunDim size="14px" className={styles.icon} />
                )}
            </SwitchThumb>
        </Switch>
    );
}
