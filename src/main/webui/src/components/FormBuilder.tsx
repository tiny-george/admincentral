import { useEffect, useState } from 'react'
import { useContext } from 'react'
import { AppContext } from '../AppContext.tsx'
import AceEditor from "react-ace";

import "ace-builds/src-noconflict/mode-yaml";
import "ace-builds/src-noconflict/theme-github";
import "ace-builds/src-noconflict/ext-language_tools"

const default_content_type = `name: watch
properties:
  - name: id
    type: string
  - name: name
  - name: description
  - name: color
  - name: shopify-item
    type: datasource:shopify-multi`

const default_form = `properties:
  - name: description
    label: About
  - name: shopify-item
    label: Shopify
    component:
      type: select
      value: hello {{id}}
  - name: color
    label: Color
    component:
      type: extension
      value: warp-extensions-color-picker`;

export const FormBuilder = () => {

  const subscriptionId = useContext(AppContext).subscriptionId;

  const [fields, setFields] = useState([])
  const [error, setError] = useState('')

  const [input, setInput] = useState({
    type: default_content_type,
    form: default_form
  })

  const handleContentTypeChange = (newType: string) => {
    setInput({
      ...input,
      type: newType
    });
  };

  const handleFormChange = (newForm: string) => {
    setInput({
      ...input,
      form: newForm
    });
  };

  const fetchFields = (input: any) => {
    setError('')
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json',
                 'subscription-id': subscriptionId },
      body: JSON.stringify(input)
    };
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/formBuilder", requestOptions)
      .then(response => {
        if (response.ok) {
          return response.json()
        }
        response.text().then(value => setError(value))
      })
      .then(data => {
        if (data != null) {
          setFields(data.fields)
        }
      })
  }

  useEffect(() => {
    fetchFields(input)
  }, [input])

  return (
    <main>
      <div className="container">
        <div className="row">
          <div className="col">
            <div className="container">
              <div className="card mb-4 shadow-sm">
                <div className="card-header py-6">
                  <h5 className="my-0 fw-normal">Content-Type</h5>
                </div>
                <div className="card-body">
                  <AceEditor
                      mode="yaml"
                      theme="github"
                      onChange={handleContentTypeChange}
                      name="contentTypeEditor"
                      setOptions={{ useWorker: false }}
                      value={ input.type }
                      minLines={15}
                      maxLines={15}
                      editorProps={{ $blockScrolling: true }}
                  />
                </div>
              </div>
              <div className="card mb-4 shadow-sm">
                <div className="card-header py-6">
                  <h5 className="my-0 fw-normal">Form</h5>
                </div>
                <div className="card-body">
                  <AceEditor
                      mode="yaml"
                      theme="github"
                      onChange={handleFormChange}
                      setOptions={{ useWorker: false }}
                      name="formEditor"
                      value={ input.form }
                      minLines={10}
                      maxLines={15}
                      editorProps={{ $blockScrolling: true }}
                  />
                </div>
              </div>
            </div>
          </div>
          <div className="col">
            {error !== '' && (
                <div className="alert alert-danger" role="alert">{error}</div>
            )}
            {error === '' && fields.length > 0 && (
                <form>
                  {fields.map((property: any) => (
                      <div className="form-group" key={property['name']}>
                        {property['type'] === "string" && (
                            <>
                              <label htmlFor={property['name']}>{property['label']}</label>
                              <input type="text" className="form-control" id={property['name']}/>
                            </>)}
                        {property['type'] === "externalComponent" && (
                            <iframe src={"https://" + property['additionalData'] + ".exp.magnolia-cloud.com/"}></iframe>
                        )}
                        {property['type'] === "select" && (
                            <>
                              <label htmlFor={property['name']}>{property['label']}</label>
                              <select name={property['name']} className="form-select">
                                {property['additionalData'] !== null && property['additionalData'].map((item: any) => (
                                    <option key={item['key']} value={item['key']}>{item['value']}</option>
                                ))}
                              </select>
                            </>
                        )}
                        {property['type'] === "unknown" && (
                            <div className="alert alert-danger" role="alert">{property['additionalData']}</div>)}
                      </div>
                  ))}
                </form>
            )}
            {error === '' && fields.length == 0 && (
                <div className="alert alert-info" role="alert">
                  There are no properties
                </div>
            )}
          </div>
        </div>
      </div>

    </main>
  )
}
