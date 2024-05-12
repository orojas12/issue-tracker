import "@radix-ui/themes/styles.css";
import styles from "./App.module.css";
import "./theme.css";

import { Flex, Section, Text, Theme } from "@radix-ui/themes";
import { Link, Outlet } from "react-router-dom";

function App() {
    return (
        <Theme>
            <Section p="3" className={styles.navbar}>
                <Flex gap="6" justify="start">
                    <Text>Issue Tracker</Text>
                    <Link to="/users">Users</Link>
                    <Link to="/teams">Teams</Link>
                </Flex>
            </Section>
            <Outlet />
        </Theme>
    );
}

export default App;
