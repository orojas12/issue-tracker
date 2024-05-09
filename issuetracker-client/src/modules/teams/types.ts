export type Team = {
    id: string;
    name: string;
    teamMembers: TeamMember[];
};

export type TeamMember = {
    username: string;
    teamId: string;
};

export type User = {
    id: string;
    username: string;
    firstName: string;
    lastName: string;
    dateCreated: Date;
};
