export type Issue = {
    id: number;
    title: string;
    description: string;
    createdAt: Date;
    dueDate?: Date;
    closed: boolean;
}
