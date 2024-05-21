export type Issue = {
    id: number;
    title: string;
    description: string;
    createdAt: Date;
    dueDate?: Date;
    closed: boolean;
}

export type CreateIssue = {
    title: string;
    description: string;
    dueDate?: string;
    dueDateTimeZone?: string;
}
