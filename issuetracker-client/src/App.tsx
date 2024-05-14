import "@radix-ui/themes/styles.css";
import styles from "./App.module.css";
import "./theme.css";

import { Flex, Section, Text, Theme } from "@radix-ui/themes";
import { Outlet } from "react-router-dom";
import { Link } from "@/components/link";

function App() {
    return (
        <Theme appearance="light">
            <Section p="3" className={styles.navbar}>
                <Flex gap="6" justify="start">
                    <Text>Issue Tracker</Text>
                    <Link to="/users">Users</Link>
                    <Link to="/teams">Teams</Link>
                    <Link to="/issues">Issues</Link>
                </Flex>
            </Section>
            <Outlet />
        </Theme>
    );
}

export default App;
