import { BsPeople } from "react-icons/bs";
import "./Logo.css";

export default function Logo() {
    return (
        <div>
            <BsPeople className="people-icon" />
            <h1>Roster Manager</h1>
        </div>
    )
}