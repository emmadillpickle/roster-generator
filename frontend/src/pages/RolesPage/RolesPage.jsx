import "./RolesPage.css"
import React from "react"
import { GoPlus } from "react-icons/go";
import { PiLightbulbFilamentLight } from "react-icons/pi";
import { getRoles, saveRole, editRole, deleteRole } from "../../api.js"
import RolesSummary from "./RolesSummary/RolesSummary.jsx"
import Banner from "../../components/Banner/Banner.jsx"
import RolesTable from "./RolesTable/RolesTable.jsx"
import RoleModal from "./SaveRoleModal/SaveRoleModal.jsx"
import DeleteModal from "../../components/DeleteModal/DeleteModal.jsx"

export default function Roles() {

    const [roles, setRoles] = React.useState([])
    const [isSaveModalOpen, setIsSaveModalOpen] = React.useState(false)
    const [isDeleteModalOpen, setIsDeleteModalOpen] = React.useState(false)
    const [selectedRole, setSelectedRole] = React.useState(null)

    function handleEditRole(role) {
        setSelectedRole(role)
        setIsSaveModalOpen(true)
    }
    
    function handleAddRole() {
        setSelectedRole(null)
        setIsSaveModalOpen(true)
    }

    function handleClickDeleteRole(role) {
        setSelectedRole(role)
        setIsDeleteModalOpen(true)
    }

    async function handleSaveRole(roleData) {
        try {
            let role

            if (selectedRole) {
                role = await editRole(roleData, selectedRole.id)
            } else {
                role = await saveRole(roleData)
            }

            setRoles((prevRoles) => {
                if (selectedRole) {
                    return prevRoles.map(r => 
                        r.id === role.id ? role : r
                    )
                }

                return [...prevRoles, role]
            })

            setIsSaveModalOpen(false)
            setSelectedRole(null)

        } catch (error) {
            console.error('Failed to save role:', error)
        }
    }

    async function handleDeleteRole(id) {
        try {
            await deleteRole(id)

            setRoles((prevRoles) => {
                return prevRoles.filter(r =>
                    r.id !== id 
                )
            })

            setIsDeleteModalOpen(false)
            setSelectedRole(null)
        } catch (error) {
            console.error('failed to delete role:', error)
        }
    }

    React.useEffect(() => {
        async function loadRoles() {
            try {
                const data = await getRoles()
                setRoles(data)
            } catch (err) {
                setError(err)
            }
        }

        loadRoles()
    }, [])

    return (
        <main className="roles-page-content">
            <header className="roles-page-header">
                <div className="roles-page-header-text">
                    <h1>Roles</h1>
                    <h2>Manage the roles for your roster</h2>
                </div>
                <button onClick={handleAddRole}> 
                    {<GoPlus className="add-role-icon"/>} 
                    Add Role
                </button>
            </header>

            <div className="roles-page-summary">
                <RolesSummary roles={roles}/>
            </div>

            <RolesTable roles={roles} onEdit={handleEditRole} onDelete={handleClickDeleteRole}/>

            <Banner 
                icon={PiLightbulbFilamentLight}
            >
                Individual roles are filled by one person, while paired roles can have more people working together
            </Banner>

            {isSaveModalOpen && (
                <RoleModal 
                    role={selectedRole}
                    onClose={() => setIsSaveModalOpen(false)}
                    onSave={handleSaveRole}
                />
            )}

            {isDeleteModalOpen && (
                <DeleteModal 
                    onClose={() => setIsDeleteModalOpen(false)}
                    onDelete={handleDeleteRole}
                    objectType="role"    
                    id={selectedRole.id}
                />
            )}
        </main>
    )
}