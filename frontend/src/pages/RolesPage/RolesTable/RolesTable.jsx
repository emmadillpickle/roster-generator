import "./RolesTable.css"
import React from "react"
import { FiEdit2, FiTrash2 } from "react-icons/fi"
import { GoChevronDown } from "react-icons/go"

export default function RolesTable({roles, onEdit, onDelete}) {

    const [typeFilter, setTypeFilter] = React.useState("ALL")
    const [searchQuery, setSearchQuery] = React.useState("")

    const filteredRoles = roles.filter(role => {
        const matchesSearch = searchQuery === "" || 
                                role.name.toLowerCase().includes(searchQuery.toLowerCase())
        
        const matchesType = typeFilter === "ALL" || 
                                role.roleType === typeFilter

        return matchesType && matchesSearch
    })

    return (
            <div className="roles-table-container">
                <form className="roles-table-toolbar">
                    <input 
                        type="text"
                        placeholder="Search roles..."
                        value={searchQuery}
                        onChange={e => setSearchQuery(e.target.value)}
                    />
                    
                    <div className="select-wrapper">
                        <select
                            value={typeFilter}
                            onChange={e => setTypeFilter(e.target.value)}
                        >
                            <option value="ALL">All Roles</option>
                            <option value="SOLO">Individual Roles</option>
                            <option value="PAIRED">Paired Roles</option>
                        </select>

                        <GoChevronDown className="select-arrow"/>
                    </div>
                </form>

                <table className="roles-table">

                    <thead>
                        <tr>
                            <th>Role</th>
                            <th>Type</th>
                            <th className="role-actions-column">Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        {filteredRoles.map(role => (
                            <tr key={role.id}>
                                <td>{role.name}</td>

                                <td>
                                    <span className={`role-type ${role.roleType.toLowerCase()}`}>
                                        {role.roleType === "SOLO" ? "Individual" : "Paired"}
                                    </span>
                                </td>

                                <td className="role-actions-column">
                                    <div className="role-actions">
                                        <button onClick={() => onEdit(role)}>
                                            <FiEdit2 className="role-actions-edit"/>
                                        </button>

                                        <button onClick={() => onDelete(role)} >
                                            <FiTrash2 className="role-actions-delete"/>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>

                </table>
            </div>
    )
}