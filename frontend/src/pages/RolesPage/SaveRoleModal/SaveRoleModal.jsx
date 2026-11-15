import "./SaveRoleModal.css"
import React from "react"
import { GoChevronDown } from "react-icons/go"

export default function SaveRoleModal({role, onClose, onSave}) {
    const [name, setName] = React.useState("")
    const [roleType, setRoleType] = React.useState("SOLO")

    React.useEffect(() => {

        if (role) {
            setName(role.name)
            setRoleType(role.roleType)
        }
    }, [role])

    function handleSubmit(e) {
        e.preventDefault()

        const roleData = {
            name,
            roleType
        }

        onSave(roleData)
    }
   
    return (
        <div className="modal-overlay" onClick={onClose}>
            <div
                className="role-modal"
                onClick={e => e.stopPropagation()}
            >

                <button
                    type="button"
                    onClick={onClose}
                >
                    &times;
                </button>
                
                <div className="role-modal-content">
                    <h2>{role ? 'Edit Role' : 'Add Role'}</h2>
                    <form onSubmit={handleSubmit}>
                        <label>
                            Role Name
                            <input
                                type="text"
                                value={name}
                                onChange={e => setName(e.target.value)}
                                placeholder="Enter Role Name"
                                required
                            />
                        </label>

                        <label>
                            Role Type
                            <div className="select-wrapper">
                                <select
                                    value={roleType}
                                    onChange={e => setRoleType(e.target.value)}
                                >
                                    <option value="SOLO">Individual</option>
                                    <option value="PAIRED">Paired</option>
                                </select>

                                <GoChevronDown className="select-arrow"/>
                            </div>
                        </label>

                        <div className="role-modal-actions">
                            <button
                                type="button"
                                onClick={onClose}
                            >
                                Cancel
                            </button>

                            <button type="submit">
                                {role ? 'Save Changes' : 'Create Role'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    )
}