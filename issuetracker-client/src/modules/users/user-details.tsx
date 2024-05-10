import { useEffect, useState } from "react";
import { User } from "../teams/types";
import { useParams } from "react-router-dom";
import { Flex, Heading } from "@radix-ui/themes";

export function UserDetails() {
    const { username } = useParams();
    const [user, setUser] = useState<User | null>(null);

    const getUser = async (username: string) => {
        const res = await fetch(`http://localhost:8080/users/${username}`);
        if (res.ok) {
            const data: User = await res.json();
            setUser(data);
        }
    };

    useEffect(() => {
        if (!username) return;
        getUser(username);
    }, [username]);

    if (!user) return null;

    return (
        <Flex>
            <Heading>
                {user.firstName} {user.lastName}
            </Heading>
        </Flex>
    );
}
