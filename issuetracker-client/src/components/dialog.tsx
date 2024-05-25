import React, { useContext } from "react";
import {
    Root,
    Trigger,
    Portal,
    Overlay,
    Content,
    Title,
    Description,
    Close,
} from "@radix-ui/react-dialog";
import styles from "./styles/dialog.module.css";
import { AppContext } from "@/context";

export const Dialog = Root;

export const DialogClose = ({ children }: { children: React.ReactNode }) => {
    return <Close asChild>{children}</Close>;
};

export const DialogTrigger = ({ children }: { children: React.ReactNode }) => (
    <Trigger asChild>{children}</Trigger>
);

export const DialogContent = ({
    title,
    description,
    children,
}: {
    title?: string;
    description?: string;
    children?: React.ReactNode;
}) => {
    const appCtx = useContext(AppContext);

    if (!appCtx) return null;

    return (
        <Portal container={appCtx.appRootElement}>
            <Overlay className={styles.overlay} />
            <Content className={styles.content}>
                <div className={styles.header}>
                    <Title className={styles.title}>{title}</Title>
                    <Description className={styles.description}>
                        {description}
                    </Description>
                </div>
                {children}
            </Content>
        </Portal>
    );
};

export const DialogControls = ({ children }: { children: React.ReactNode }) => {
    return <div className={styles.controls}>{children}</div>;
};
