import "./styles/variables.css";
import "./styles/reset.css";
import "./styles/themes.css";
import "./styles/base.css";
import styles from "./app.module.css";

import { Theme } from "@/theme";
import { Outlet, NavLink } from "react-router-dom";
import { ThemeToggle } from "./components/theme-toggle";
import { useState } from "react";
import { AppContext } from "./context";
import { SideDrawer } from "@/components";
import { ProjectList, useProjectList } from "@/modules/project";

function App() {
    const [appRootElement, setAppRootElement] = useState<HTMLDivElement | null>(
        null,
    );

    const toggleScroll = (open: boolean) => {
        if (!appRootElement) return;
        if (open) {
            appRootElement.classList.add("noscroll");
        } else {
            appRootElement.classList.remove("noscroll");
        }
    };

    return (
        <AppContext.Provider value={{ appRootElement }}>
            <Theme>
                <div id="app" ref={setAppRootElement}>
                    <nav className={styles.navbar}>
                        <SideDrawer onOpenChange={toggleScroll}>
                            <ul className={styles.drawerList}>
                                <li className={styles.drawerListGroup}>
                                    <div
                                        className={styles.drawerListGroupTitle}
                                    >
                                        Projects
                                    </div>
                                    <ProjectList />
                                </li>
                            </ul>
                        </SideDrawer>
                        <span className={styles.projectName}>Murphy Inc</span>
                        <div className={styles.navbarEnd}>
                            <NavLink to="/projects/28JBVIJP" end>
                                Project
                            </NavLink>
                            <NavLink to="/projects/28JBVIJP/issues">
                                Issues
                            </NavLink>
                            <ThemeToggle />
                        </div>
                    </nav>
                    <Outlet />
                </div>
            </Theme>
        </AppContext.Provider>
    );
}

export default App;
