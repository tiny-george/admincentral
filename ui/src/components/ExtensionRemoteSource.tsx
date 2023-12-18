import { useEffect, useState } from 'react'

// @ts-ignore
export const ExtensionRemoteSource = ({subscriptionId, extension, label, name}) => {

  const [items, setItems] = useState([])

  const fetchItems = () => {
    fetch("https://" + extension + ".exp.magnolia-cloud.com/items/all/" + subscriptionId)
      .then(response => {
        return response.json()
      })
      .then(data => {
        setItems(data)
      })
  }

  useEffect(() => {
    fetchItems()
  }, [])

  return (
    <>
      {items.length > 0 && (
          <>
            <label htmlFor={name}>{label}</label>
            <select name={name} className="form-select">
              {items.map(item => (
                  <option key={item['id']} value={item['id']}>{item['title']}</option>
              ))}
            </select>
          </>
      )}
      {items.length == 0 && (
          <p>No items</p>
      )}
    </>
  )
}
