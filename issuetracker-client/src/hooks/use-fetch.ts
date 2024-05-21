import { useEffect, useState } from "react";

export function useFetch<T>(url: string, options?: RequestInit) {
    const [data, setData] = useState<T | null>(null);
    const [isLoading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const refresh = async (refreshOptions?: RequestInit) => {
        setLoading(true);
        setError("");
        try {
            const res = await fetch(url, refreshOptions || options);
            const data = (await res.json()) as T;
            setData(data);
            setLoading(false);
        } catch (e: any) {
            setError(e.message as string);
        }
    };

    useEffect(() => {
        refresh();
    }, [url]);

    return {
        data,
        isLoading,
        error,
        refresh,
    };
}
