import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import App from "./App.tsx";
import { TeamDetails } from "@/modules/team/team-details.tsx";
import { TeamsList } from "@/modules/team/team-list.tsx";
import { UserDetails } from "@/modules/user/user-details.tsx";
import { UserManagement } from "@/modules/user/user-list.tsx";
import { IssueList } from "@/modules/issue/issue-list.tsx";
import { IssueDetails } from "@/modules/issue/issue-details.tsx";
import { Playground } from "./playground.tsx";

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
                path: "teams/:teamId",
                element: <TeamDetails />,
            },
            {
                path: "users",
                element: <UserManagement />,
            },
            {
                path: "users/:username",
                element: <UserDetails />,
            },
            {
                path: "issues",
                element: <IssueList />,
            },
            {
                path: "issues/:issueId",
                element: <IssueDetails />,
            },
            {
                path: "test",
                element: <Playground />,
            },
        ],
    },
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <RouterProvider router={router} />
    </React.StrictMode>,
);
