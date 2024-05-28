import { useState } from "react";

const defaultOptions: RequestInit = {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
    },
};

export function useMutate<RequestData, ResponseData>(
    url: string,
    initOptions?: RequestInit,
) {
    const [isLoading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const mutate = async (
        data: RequestData,
        mutateOptions?: RequestInit,
    ): Promise<ResponseData | undefined> => {
        const options = mutateOptions || initOptions || defaultOptions;
        options.body = JSON.stringify(data);
        setLoading(true);
        setError("");
        try {
            const res = await fetch(url, options);
            if (res.ok) {
                return res.json();
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
        isLoading,
        error,
        mutate,
    };
}
