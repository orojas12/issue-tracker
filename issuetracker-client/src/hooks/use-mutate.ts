import { useState } from "react";

const defaultOptions: RequestInit = {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
    },
};

export function useMutate<T>(url: string, initOptions?: RequestInit) {
    const [data, setData] = useState<T | null>(null);
    const [isLoading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const mutate = async (data: any, mutateOptions?: RequestInit) => {
        const options = mutateOptions || initOptions || defaultOptions;
        options.body = JSON.stringify(data);
        setLoading(true);
        setError("");
        try {
            const res = await fetch(url, options);
            if (res.ok) {
                const data: T = await res.json();
                setData(data);
            } else {
                const error = await res.json();
                throw new Error(
                    error.message ||
                        `Server responded with error ${res.status}: ${res.statusText}`,
                );
            }
        } catch (e: any) {
            setError(e.message as string);
        } finally {
            setLoading(false);
        }
    };

    return {
        data,
        isLoading,
        error,
        mutate,
    };
}
