import { useEffect, useState } from 'react'
import Modal from 'react-modal'

Modal.setAppElement('#root')

const subscriptionId = 'todoExtensionsId';

export const Marketplace = () => {

  let subtitle: HTMLDivElement | null;
  const [modalData, setModalData] = useState(
    { open: false, name: '', description: '', requiredConfig: [] as string[]});

  function openModal(name: string, description: string, requiredConfig: string[]) {
    setModalData({ open: true, name: name, description: description, requiredConfig: requiredConfig});
  }

  function afterOpenModal() {
    // references are now sync'd and can be accessed.
    if (subtitle) {
      subtitle.style.color = '#f00';
    }
  }

  function closeModal() {
    setModalData({ open: false, name: '', description: '', requiredConfig: []});
  }

  const [extensions, setExtensions] = useState([])

  const fetchExtensions = () => {
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/extensions/" + subscriptionId)
      .then(response => {
        return response.json()
      })
      .then(data => {
        setExtensions(data)
      })
  }

  const activateExtension = (name: string, requiredConfig: string[]) => {
    const configValues = new Map<string, string>();
    if (requiredConfig.length > 0) {
      requiredConfig.forEach(element => {
        const value = (document.getElementById(element) as HTMLInputElement).value;
        if (!value || value === '') {
          throw new Error("Cannot leave empty config value for " + element);
        }
        configValues.set(element, value);
      });
    }
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({name: name, configValues: configValues})
    };
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/extensions/" + subscriptionId + "/activate", requestOptions)
      .then(response => response.json())
      .then(data => {
        closeModal()
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
          <Modal
            isOpen={modalData.open}
            onAfterOpen={afterOpenModal}
            onRequestClose={closeModal}
          >
            <div className='text-dark' ref={(_subtitle) => (subtitle = _subtitle)} tabIndex={-1}>
              <div className="modal-dialog">
                <div className="modal-content">
                  <div className="modal-header">
                    <h5 className="modal-title">{modalData.name}</h5>
                    <button type="button" className="btn-close" onClick={closeModal} data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div className="modal-body">
                    <p>{modalData.description}</p>
                  </div>
                  {modalData.requiredConfig.length > 0 && (
                    <div className="modal-body">
                      <p>Required config values:</p>
                      {modalData.requiredConfig.map(value => (
                        <div className="row" key={value}>
                          <div className="col-auto">
                            <label htmlFor={value}>{value}</label>
                          </div>
                          <div className="col-auto">
                            <input type="password" id={value} className="form-control"/>
                          </div>
                        </div>
                      ))}
                    </div> 
                  )}
                  {modalData.requiredConfig.length < 1 && (
                    <div className="modal-body">
                        <p>This extension doesn't requires config</p>
                    </div> 
                  )}
                  <div className="modal-footer">
                    <button type="button" className="btn btn-secondary" onClick={closeModal} data-bs-dismiss="modal">Close</button>
                    <button type="button" className="btn btn-primary" onClick={() => activateExtension(modalData.name, modalData.requiredConfig)}>Activate</button>
                  </div>
                </div>
              </div>
            </div>
          </Modal>
          <h4>List of extensions</h4>
          <table className="table table-borderless">
            <thead>
              <tr>
                <th scope="col">Name</th>
                <th scope="col">Description</th>
                <th scope="col">Status</th>
              </tr>
            </thead>
            <tbody>
            {extensions.map(extension => (
              <tr key={extension['name'] + Math.floor(Math.random() * 817267868)}>
                <th scope="row">{extension['name']}</th>
                <td>{extension['description']}</td>
                {extension['active'] == true && (
                  <td><button type="button" className="btn btn-secondary">Active</button></td>
                )}
                {extension['active'] == false && (
                  <td><button type="button" onClick={() => openModal(extension['name'], extension['description'], extension['requiredConfig'])} className="btn btn-primary">Activate</button></td>
                )}
              </tr>
            ))}
            </tbody>
          </table>
        </>
      )}
      {extensions.length == 0 && (
        <div className="alert alert-info" role="alert">
          There are no extensions for your subscription
        </div>
      )}
    </main>
  )
}
