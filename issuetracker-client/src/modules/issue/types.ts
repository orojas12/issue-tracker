export type Issue = {
    id: number;
    title: string;
    description: string;
    createdAt: Date;
    dueDate: Date | null;
    closed: boolean;
};

export type CreateIssue = {
    title: string;
    description: string | null;
    dueDate: string | null;
    dueDateTimeZone: string | null;
};

export type UpdateIssue = {
    title: string;
    description: string;
    dueDate: Date | null;
    closed: boolean;
};

export type IssueData = {
    id: number;
    title: string;
    description: string;
    createdAt: string;
    dueDate: string | null;
    closed: boolean;
};
