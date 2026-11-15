import "./RolesSummary.css";
import { IoPersonOutline, IoListOutline, IoPeopleOutline } from "react-icons/io5";

export default function RolesSummary({roles}) {
    const totalRoles = roles.length

    const individualRoles = roles.filter(
        role => role.roleType === "SOLO"
    ).length

    const pairedRoles = roles.filter(
        role => role.roleType === "PAIRED"
    ).length
    
    return (
        <div className="roles-summary">
            <SummaryCard 
                icon={IoListOutline}
                title="All Roles"
                value={totalRoles}
            />
            <SummaryCard 
                icon={IoPersonOutline}
                title="Individual Roles"
                value={individualRoles}
            />
            <SummaryCard 
                icon={IoPeopleOutline}
                title="Paired Roles"
                value={pairedRoles}
            />
        </div>
    )
}

function SummaryCard({ icon: Icon, title, value }) {
    return (
        <div className="summary-card">
            <Icon className="summary-card-icon"/>
            <h2>{value}</h2>
            <p>{title}</p>
        </div>
    )
}