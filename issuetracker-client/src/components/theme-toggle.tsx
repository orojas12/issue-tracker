import { useContext } from "react";
import { ThemeContext } from "@/theme";
import { Button } from "./button";

export function ThemeToggle() {
    const theme = useContext(ThemeContext);

    return <Button onClick={theme?.toggleDark}>Mode</Button>;
}
