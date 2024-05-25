import { defineConfig } from "vite";
import path from "path";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react()],
    resolve: {
        alias: {
            "@/components": path.resolve(__dirname, "./src/components"),
            "@/modules": path.resolve(__dirname, "./src/modules"),
            "@/hooks": path.resolve(__dirname, "./src/hooks"),
            "@/context": path.resolve(__dirname, "./src/context"),
            "@/theme": path.resolve(__dirname, "./src/theme.tsx"),
        },
    },
});
