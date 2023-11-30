import { useEffect, useState } from 'react'

export const Applications = () => {

  const [apps, setApps] = useState([])

  const fetchApps = () => {
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/applications")
      .then(response => {
        return response.json()
      })
      .then(data => {
        setApps(data)
      })
  }

  useEffect(() => {
    fetchApps()
  }, [])

  return (
    <div>
        {apps.length > 0 && (
        <ul>
          {apps.map(app => (
            <li key={app['name']}>{app['label']}</li>
          ))}
        </ul>
      )}
    </div>
  )
}
