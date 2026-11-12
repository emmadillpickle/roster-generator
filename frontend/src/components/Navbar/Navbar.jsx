import './Navbar.css'
import Logo from '../Logo/Logo.jsx'
import NavbarButton from './NavbarButton/NavbarButton.jsx'
import { IoHomeOutline, IoCalendarClearOutline } from "react-icons/io5";
import { GoPeople } from "react-icons/go";
import { FaRegHandshake } from "react-icons/fa";
import { LuFileSpreadsheet } from "react-icons/lu";

export default function Navbar() {
    return (
        <section className="nav-bar">
            <Logo/>
            <nav>
                <NavbarButton icon={IoHomeOutline} text="Home" pagePath="/"/>
                <NavbarButton icon={GoPeople} text="People" pagePath="/people"/>
                <NavbarButton icon={IoCalendarClearOutline} text="Cell Dates" pagePath="/cell-dates"/>
                <NavbarButton icon={FaRegHandshake} text="Pairings" pagePath="/pairings"/>
                <NavbarButton icon={LuFileSpreadsheet} text="View Roster" pagePath="/roster"/>
            </nav>
        </section>
    )
}