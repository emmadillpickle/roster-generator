import "./Layout.css"
import { Outlet } from "react-router"
import Navbar from "../Navbar/Navbar.jsx"

export default function Layout() {
    return (
        <div className="page">
            <aside className="page-navbar">
                <Navbar />
            </aside>
            <main className="page-content">
                <Outlet/>
            </main>
        </div>
    )
}