import "@radix-ui/themes/styles.css";

import { Container, Flex, Theme } from "@radix-ui/themes";
import { Link, Outlet } from "react-router-dom";

function App() {
    return (
        <Theme>
            <Container size="1">
                <Flex gap="4">
                    <Link to="/users">Users</Link>
                    <Link to="/teams">Teams</Link>
                </Flex>
                <Outlet />
            </Container>
        </Theme>
    );
}

export default App;
