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
    username: string;
};
