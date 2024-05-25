import "./styles/variables.css";
import "./styles/reset.css";
import "./styles/themes.css";
import "./styles/base.css";

import { Theme } from "@/theme";
import { Outlet } from "react-router-dom";
import { Link } from "@/components/link";
import { ThemeToggle } from "./components/theme-toggle";
import { useState } from "react";
import { AppContext } from "./context";

function App() {
    const [appRootElement, setAppRootElement] = useState<HTMLDivElement | null>(
        null,
    );

    return (
        <AppContext.Provider value={{ appRootElement }}>
            <Theme>
                <div id="app" ref={setAppRootElement}>
                    <nav className="navbar">
                        <Link to="/users">Users</Link>
                        <Link to="/teams">Teams</Link>
                        <Link to="/issues">Issues</Link>
                        <ThemeToggle />
                    </nav>
                    <Outlet />
                </div>
            </Theme>
        </AppContext.Provider>
    );
}

export default App;
