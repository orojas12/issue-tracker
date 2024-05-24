import React, { createContext } from "react";
import { useState } from "react";

export type ThemeProps = {
    dark: boolean;
    toggleDark: () => void;
};

export const ThemeContext = createContext<ThemeProps | null>(null);

export function Theme({ children }: { children: React.ReactNode }) {
    const [dark, setDark] = useState(false);

    const toggleDark = () => setDark(!dark);

    return (
        <ThemeContext.Provider value={{ dark, toggleDark }}>
            <div className={dark ? "dark" : "light"}>{children}</div>
        </ThemeContext.Provider>
    );
}
