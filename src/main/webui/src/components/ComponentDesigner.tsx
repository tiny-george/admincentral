import { useEffect, useState } from 'react'
import { IFrameComponent } from "./IFrameComponent.tsx";

export const ComponentDesigner = () => {

  const [fields, setFields] = useState<any[]>([])
  const [error, setError] = useState('')

  const [input, setInput] = useState({
    url: "https://local.de.magnolia-cloud.com:3100",
    fieldValue: "#d52b2b"
  })

  const handleUrlChange = (newUrl: string) => {
    setInput({
      ...input,
      url: newUrl
    });
  };

  const handleFieldValueChange = (newFieldValue: string) => {
    setInput({
      ...input,
      fieldValue: newFieldValue
    });
  };

  const fetchFields = (input: any) => {
    setError('');
    setFields([{
      name: "field",
      label: "",
      type: "externalComponent",
      value: input.fieldValue,
      additionalData: input.url
    }]);
  }

  useEffect(() => {
    fetchFields(input)
  }, [input])

  const inputStyle = {
    width: "100%"
  }

  return (
    <main>
      <div className="container">
        <div className="row">
          <div className="col">
            <div className="container">
              <div className="card mb-4 shadow-sm">
                <div className="card-header py-6">
                  <h5 className="my-0 fw-normal">Component Url</h5>
                </div>
                <div className="card-body">
                  <input
                      type={"text"}
                      name={"url"}
                      style={inputStyle}
                      defaultValue={ input.url }
                      onChange={e => handleUrlChange(e.target.value)}
                  />
                </div>
              </div>
              <div className="card mb-4 shadow-sm">
                <div className="card-header py-6">
                  <h5 className="my-0 fw-normal">Field value</h5>
                </div>
                <div className="card-body">
                  <textarea
                      onChange={e => handleFieldValueChange(e.target.value)}
                      name="formEditor"
                      style={inputStyle}
                      rows={5}
                      value={ input.fieldValue as string }
                  />
                </div>
              </div>
            </div>
          </div>
          <div className="col w-100">
            {error !== '' && (
                <div className="alert alert-danger" role="alert">{error}</div>
            )}
            {error === '' && fields.length > 0 && (
                <form className={"w-100 h-100"}>
                  {fields.map((property: any) => (
                      <div className="form-group h-100" key={property['name']}>
                        {property['type'] === "string" && (
                            <>
                              <label htmlFor={property['name']}>{property['label']}</label>
                              <input type="text" className="form-control" id={property['name']}/>
                            </>)}
                        {property['type'] === "externalComponent" && (
                            <IFrameComponent baseUrl={property['additionalData']}
                                             fieldName={property['name']}
                                             fieldValue={property['value']}/>
                        )}
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
