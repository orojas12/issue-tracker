import { type ReactNode, useState, useEffect } from "react";
import styles from "./styles/side-drawer.module.css";
import { Menu } from "lucide-react";
import { useProjectList } from "@/modules/project/use-project-list";

type SideDrawerProps = {
    open?: boolean;
    onOpenChange?: (open: boolean) => void;
    children?: ReactNode;
};

export function SideDrawer(props: SideDrawerProps) {
    const [open, setOpen] = useState(false);

    useEffect(() => {
        props.onOpenChange && props.onOpenChange(open);
    }, [open]);

    return (
        <div onKeyDown={(e) => e.key === "Escape" && setOpen(false)}>
            <button onClick={() => setOpen(true)} className={styles.trigger}>
                <Menu className={styles.triggerIcon} />
            </button>
            {open && (
                <>
                    <div className={styles.sideDrawer}>{props.children}</div>
                    <div
                        onClick={() => setOpen(false)}
                        className={styles.overlay}
                    ></div>
                </>
            )}
        </div>
    );
}
