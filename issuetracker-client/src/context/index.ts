import { createContext } from "react";

type AppContextValue = {
    appRootElement: HTMLElement | null;
} | null;

export const AppContext = createContext<AppContextValue>(null);
