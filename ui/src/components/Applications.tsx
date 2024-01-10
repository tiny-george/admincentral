import { useEffect, useState } from 'react'
import { AppCard } from "./AppCard.tsx"

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
    <main>
        {apps.length > 0 && (
        <div className="row row-cols-1 row-cols-md-6 mb-6 text-center">
          {apps.map(app => (
              <div className="col" key={'watches'}>
                <AppCard
                  group={'Content App'}
                  label={app['label']}
                  icon={'content-app'}
                  link={'watches'}/>
              </div>
          ))}
        </div>
      )}
      <div className="row row-cols-1 row-cols-md-6 mb-6 text-center">
        <div className="col" key={'formBuilder'}>
          <AppCard
              group={'Developer'}
              label={'Form Builder'}
              icon={'content-app'}
              link={'formBuilder'}/>
        </div>
      </div>
      <div className="row row-cols-1 row-cols-md-6 mb-6 text-center">
        <div className="col" key={'marketplace'}>
          <AppCard
              group={'Extensions'}
              label={'Marketplace'}
              icon={'extension'}
              link={'marketplace'}/>
        </div>
        <div className="col" key={'extensions-manager'}>
          <AppCard
              group={'Extensions'}
              label={'Manage'}
              icon={'extension'}
              link={'extensions-manager'}/>
        </div>
      </div>
    </main>
  )
}
