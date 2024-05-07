import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import "./index.css";
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import { TeamDetails } from "./modules/teams/team.tsx";
import { TeamsList } from "./modules/teams/teams-list.tsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        children: [
            {
                path: "teams",
                element: <TeamsList />,
            },
            {
                path: "/teams/:teamId",
                element: <TeamDetails />,
            },
        ],
    },
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <RouterProvider router={router} />
    </React.StrictMode>,
);
