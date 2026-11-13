import "./TutorialStep.css"

export default function TutorialStep({number, heading, paragraph}) {
    return (
        <section className="tutorial-step">
            <h1>{number}</h1>
            <div className="tutorial-step-detail">
                <h2>{heading}</h2>
                <p>{paragraph}</p>
            </div>
        </section>
    )
}