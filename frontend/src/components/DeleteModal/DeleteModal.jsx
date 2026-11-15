import "./DeleteModal.css"

export default function DeleteModal({onClose, onDelete, objectType, id}) {
    return (
        <div className="modal-overlay" onClick={onClose}>
            <div
                className="delete-modal"
                onClick={e => e.stopPropagation()}
            >

                <button
                    type="button"
                    onClick={onClose}
                >
                    &times;
                </button>
                
                <div className="delete-modal-content">
                    <h2>Delete {objectType}?</h2>
                    <p>Are you sure you want to delete this {objectType}? This action cannot be undone.</p>

                    <div className="delete-modal-actions">
                        <button
                            onClick={onClose}
                            className="close-button"
                        >
                            Cancel
                        </button>

                        <button 
                            onClick={() => onDelete(id)}
                            className="on-delete-button"
                        >
                            Delete
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}