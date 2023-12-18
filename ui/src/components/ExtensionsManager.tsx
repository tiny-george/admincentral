import { useEffect, useState } from 'react'

export const ExtensionsManager = () => {

  const [extensions, setExtensions] = useState([])

  const fetchExtensions = () => {
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/extensions")
      .then(response => {
        return response.json()
      })
      .then(data => {
        setExtensions(data)
      })
  }

  const activateExtension = (extensionId: string) => {
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' }
    };
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/extensions/activate/" + extensionId, requestOptions)
        .then(response => response.json())
        .then(data => {
          setExtensions(data)
        })
        .catch(error => console.error(error));
  }

  useEffect(() => {
    fetchExtensions()
  }, [])

  return (
    <main>
      {extensions.length > 0 && (
        <>
          <h4>List of extensions</h4>
          <table className="table table-borderless">
            <thead>
              <tr>
                <th scope="col">Name</th>
                <th scope="col">Description</th>
                <th scope="col">Status</th>
                <th scope="col">Available</th>
                <th scope="col">Actions</th>
              </tr>
            </thead>
            <tbody>
            {extensions.map(extension => (
              <tr key={extension['id']}>
                <th scope="row">{extension['name']}</th>
                <td>{extension['description']}</td>
                <td>{extension['status']}</td>
                {extension['available'] == true && (
                    <td><button type="button" className="btn btn-secondary disabled">Available</button></td>
                )}
                {extension['available'] == false && (
                    <td><button type="button" className="btn btn-secondary disabled">Not Available</button></td>
                )}
                {extension['available'] == false && (
                    <td><button type="button" onClick={()=> activateExtension(extension['id'])} className="btn btn-primary">Mark as available</button></td>
                )}
                {extension['available'] == true && (
                    <td>Nothing yet</td>
                )}
              </tr>
            ))}
            </tbody>
          </table>
        </>
      )}
      {extensions.length == 0 && (
        <div className="alert alert-info" role="alert">
          There are no extensions
        </div>
      )}
    </main>
  )
}
