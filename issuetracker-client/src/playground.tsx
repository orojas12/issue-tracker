import { Button } from "./components/button";
import { Card } from "./components/card";
import { Container } from "./components/container";
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogControls,
    DialogTrigger,
} from "./components/dialog";
import { TextField } from "./components/text-field";
import { NewIssueForm } from "./modules/issue/new-issue-form";

export function Playground() {
    return (
        <Container
            size="lg"
            style={{
                minHeight: "inherit",
                display: "flex",
                flexDirection: "column",
                alignItems: "flex-start",
                gap: "var(--space-8)",
                padding: "var(--space-8)",
            }}
        >
            <Card
                style={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "flex-start",
                    gap: "32px",
                }}
            >
                <p style={{ marginBottom: "32px" }}>
                    Lorem ipsum dolor sit amet, officia excepteur ex fugiat
                    reprehenderit enim labore culpa sint ad nisi Lorem pariatur
                    mollit ex esse exercitation amet. Nisi anim cupidatat
                    excepteur officia. Reprehenderit nostrud nostrud ipsum Lorem
                    est aliquip amet voluptate voluptate dolor minim nulla est
                    proident. Nostrud officia pariatur ut officia. Sit irure
                    elit esse ea nulla sunt ex occaecat reprehenderit commodo
                    officia dolor Lorem duis laboris cupidatat officia
                    voluptate. Culpa proident adipisicing id nulla nisi laboris
                    ex in Lorem sunt duis officia eiusmod. Aliqua reprehenderit
                    commodo ex non excepteur duis sunt velit enim. Voluptate
                    laboris sint cupidatat ullamco ut ea consectetur et est
                    culpa et culpa duis.
                </p>
                <div>
                    {["solid", "soft", "outline"].map((variant: any) => (
                        <div
                            style={{
                                display: "flex",
                                flexWrap: "wrap",
                                gap: "16px",
                                marginBottom: "16px",
                            }}
                        >
                            <Button variant={variant} color="neutral">
                                Submit
                            </Button>
                            <Button variant={variant} color="primary">
                                Submit
                            </Button>
                            <Button variant={variant} color="secondary">
                                Submit
                            </Button>
                            <Button variant={variant} color="destructive">
                                Submit
                            </Button>
                        </div>
                    ))}
                </div>
                <div>
                    {["solid", "soft", "outline"].map((variant: any) => (
                        <div
                            style={{
                                display: "flex",
                                flexWrap: "wrap",
                                gap: "16px",
                                marginBottom: "16px",
                            }}
                        >
                            <Button size="sm" variant={variant} color="neutral">
                                Submit
                            </Button>
                            <Button size="sm" variant={variant} color="primary">
                                Submit
                            </Button>
                            <Button
                                size="sm"
                                variant={variant}
                                color="secondary"
                            >
                                Submit
                            </Button>
                            <Button
                                size="sm"
                                variant={variant}
                                color="destructive"
                            >
                                Submit
                            </Button>
                        </div>
                    ))}
                </div>
                <div
                    style={{
                        display: "flex",
                        flexDirection: "column",
                        gap: "8px",
                        alignItems: "flex-start",
                    }}
                >
                    <TextField variant="normal" />
                    <TextField variant="soft" />
                    <TextField variant="inset" />
                    <TextField variant="shadow" />
                </div>
                <div
                    style={{
                        display: "flex",
                        flexDirection: "column",
                        gap: "8px",
                        alignItems: "flex-start",
                    }}
                >
                    <TextField size="sm" variant="normal" />
                    <TextField size="sm" variant="soft" />
                    <TextField size="sm" variant="inset" />
                    <TextField size="sm" variant="shadow" />
                </div>
                <NewIssueForm />
            </Card>
        </Container>
    );
}
