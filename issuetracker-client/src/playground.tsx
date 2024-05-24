import { Button } from "./components/button";
import { Card } from "./components/card";
import { Container } from "./components/container";

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
            <Card style={{}}>
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
            </Card>
        </Container>
    );
}
