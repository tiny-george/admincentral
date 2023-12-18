import { useEffect, useState } from 'react'
import { useContext } from 'react'
import { AppContext } from '../AppContext.tsx'

export const Watches = () => {

  const subscriptionId = useContext(AppContext).subscriptionId;

  const [properties, setProperties] = useState([])

  const fetchProperties = () => {
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/properties/watches/" + subscriptionId)
      .then(response => {
        return response.json()
      })
      .then(data => {
        setProperties(data)
      })
  }

  useEffect(() => {
    fetchProperties()
  }, [])

  return (
    <main>
      {properties.length > 0 && (
      <form>
        {properties.map(property => (
        <div className="form-group" key={property['name']}>
          {property['extensionName'] && (
              <iframe src={"https://" + property['extensionName'] + ".exp.magnolia-cloud.com/"}></iframe>
          )}
          {!property['extensionName'] && property['type'] === "string" && (
              <>
                <label htmlFor={property['name']}>{property['label']}</label>
                <input type="text" className="form-control" id={property['name']}/>
              </>)}
          {!property['extensionName'] && property['type'] === "textarea" && (
              <>
                <label htmlFor={property['name']}>{property['label']}</label>
                <textarea className="form-control" id={property['name']}></textarea>
              </>)}
        </div>
        ))}
        <button type="submit" className="btn btn-primary">Submit</button>
      </form>
      )}
      {properties.length == 0 && (
        <div className="alert alert-info" role="alert">
          There are no properties
        </div>
      )}
    </main>
  )
}
