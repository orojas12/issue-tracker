export type Project = {
    id: string;
    name: string;
};

export type Team = {
    id: string;
    name: string;
    teamMembers: TeamMember[];
};

export type TeamMember = {
    username: string;
    teamId: string;
};

export type CreateUserRequest = {
    username: string;
    firstName: string;
    lastName: string;
};
