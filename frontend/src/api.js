
export async function getRoles(id) {
    const url = id ? `/api/roles/${id}` : "/api/roles";

    const res = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    });

    if (!res.ok) {
        throw {
            message: "Failed to fetch roles",
            statusText: res.statusText,
            status: res.status
        };
    }

    const data = await res.json();
    return data;
}

export async function saveRole(role) {
    const url = "api/roles";

    const res = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(role)
    });

    if (!res.ok) {
        throw new Error(`Failed to save role: ${res.status}`);
    }

    const data = await res.json();
    return data;
}

export async function editRole(role, id) {
    const url = `api/roles/${id}`

    const res = await fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(role)
    });

    if (!res.ok) {
        throw new Error(`Failed to edit role: ${res.status}`);
    }

    const data = await res.json();
    return data;
}

export async function deleteRole(id) {
    const url = `api/roles/${id}`
    console.log(url)

    const res = await fetch(url, {
        method: 'DELETE'
    });

    if (!res.ok) {
        throw new Error(`Failed to delete role: ${res.status}`);
    }
}