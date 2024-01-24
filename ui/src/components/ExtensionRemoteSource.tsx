import { useEffect, useState } from 'react'

// @ts-ignore
export const ExtensionRemoteSource = ({subscriptionId, extension, label, name}) => {

  const [items, setItems] = useState([])

  const fetchItems = () => {
    const requestOptions = {
      method: 'GET',
      headers: { 'subscription-id': subscriptionId }
    };
    fetch("https://" + extension + ".exp.magnolia-cloud.com/items", requestOptions)
      .then(response => {
        return response.json()
      })
      .then(data => {
        setItems(data.items)
      })
  }

  useEffect(() => {
    fetchItems()
  }, [])

  return (
    <>
      {items && items.length > 0 && (
          <>
            <label htmlFor={name}>{label}</label>
            <select name={name} className="form-select">
              {items.map(item => (
                  <option key={item['id']} value={item['id']}>{item['title']}</option>
              ))}
            </select>
          </>
      )}
      {items && items.length == 0 && (
          <p>No items</p>
      )}
      {!items && (
          <>
            <label htmlFor={name}>{label}</label>
            <input type="text" className="form-control" id={name}/>
          </>
      )}
    </>
  )
}
