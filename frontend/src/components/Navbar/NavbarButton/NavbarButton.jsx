import './NavbarButton.css'
import { NavLink } from 'react-router'

export default function NavbarButton({icon: Icon, text, pagePath}) {
    return (
        <NavLink 
            to={pagePath}
            className="navbar-button"
        >
            <Icon className="icon"/>
            <h2 className="navbar-button-text">{text}</h2>
        </NavLink>
    )
}