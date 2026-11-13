import './Home.css'
import { TbCalendarUser } from "react-icons/tb";
import { PiLightbulbFilamentLight } from "react-icons/pi";
import TutorialStep from "../../components/TutorialStep/TutorialStep.jsx"
import Banner from "../../components/Banner/Banner.jsx"

export default function Home() {
    return (
        // <section className="home-page">
        //     <aside className="home-page-sidebar">
        //         <Navbar/>
        //     </aside>
        //     <main className="home-page-main">
                <main className="home-page-content">
                    <section className="home-page-header">
                        <TbCalendarUser className="home-page-header-icon"/>
                        <h2>Welcome to</h2>
                        <h1>Roster Manager</h1>
                        <h3>Easily manage your people, dates, duties, and pairings to generate a balanced roster</h3>
                    </section>
                    <section className="home-page-tutorial">
                        <h4>How to use this tool</h4>
                        <TutorialStep 
                            number="1"
                            heading="Add and manage your people"
                            paragraph="Add people and set their availability, duties, limits, and preferences."
                        />
                        <TutorialStep 
                            number="2"
                            heading="Set up event dates"
                            paragraph="Add dates for the roster period and specify the duties needed."
                        />
                        <TutorialStep 
                            number="3"
                            heading="Configure pairings"
                            paragraph="Define pairings that can be rostered for paired roles."
                        />
                        <TutorialStep 
                            number="4"
                            heading="Generate the roster"
                            paragraph="Click 'Generate Roster' to create your roster automatically."
                        />
                        <Banner 
                            icon={PiLightbulbFilamentLight}
                        >
                            Tip: Make sure all data is up to date before generating the roster for best results.
                        </Banner>
                    </section>
                </main>
        //     </main>
        // </section>
    )
}