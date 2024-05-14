import { Badge } from "@radix-ui/themes";

export function IssueBadge({ closed }: { closed: boolean }) {
    return (
        <Badge color={closed ? "blue" : "green"}>
            {closed ? "Closed" : "Open"}
        </Badge>
    );
}