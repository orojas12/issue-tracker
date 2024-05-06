import "@radix-ui/themes/styles.css";

import { Container, Theme } from "@radix-ui/themes";
import { Outlet } from "react-router-dom";

function App() {
    return (
        <Theme>
            <Container size="1">
                <Outlet />
            </Container>
        </Theme>
    );
}

export default App;
