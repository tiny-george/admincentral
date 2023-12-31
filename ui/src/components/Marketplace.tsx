import { useEffect, useState } from 'react'
import Modal from 'react-modal'
import { useContext } from 'react'
import { AppContext } from '../AppContext.tsx'

Modal.setAppElement('#root')

export const Marketplace = () => {

  const subscriptionId = useContext(AppContext).subscriptionId;

  const customStyles = {
    content: {
      top: '50%',
      left: '50%',
      right: 'auto',
      bottom: 'auto',
      marginRight: '-50%',
      transform: 'translate(-50%, -50%)'
    },
  };

  let subtitle: HTMLDivElement | null;
  const [modalData, setModalData] = useState(
    { open: false, extensionId: '', name: '', description: '', requiredConfig: [] as string[]});

  function openModal(extensionId: string, name: string, description: string, requiredConfig: string[]) {
    setModalData({ open: true, extensionId: extensionId, name: name, description: description, requiredConfig: requiredConfig});
  }

  function afterOpenModal() {
    // references are now sync'd and can be accessed.
    if (subtitle) {
      subtitle.style.color = '#f00';
    }
  }

  function closeModal() {
    setModalData({ open: false, extensionId: '', name: '', description: '', requiredConfig: []});
  }

  const [extensions, setExtensions] = useState([])

  const fetchExtensions = () => {
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/availableExtensions/" + subscriptionId)
      .then(response => {
        return response.json()
      })
      .then(data => {
        setExtensions(data)
      })
  }

  const enableExtension = (extensionId: string, requiredConfig: string[]) => {
    const configValues: any = {};
    if (requiredConfig && requiredConfig.length > 0) {
      requiredConfig.forEach(element => {
        const value = (document.getElementById(element) as HTMLInputElement).value;
        if (!value || value === '') {
          throw new Error("Cannot leave empty config value for " + element);
        }
        configValues[element] = value;
      });
    }
    const body = JSON.stringify({extensionId: extensionId, configValues: configValues})
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: body
    };
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/extensions/" + subscriptionId + "/enable", requestOptions)
      .then(response => response.json())
      .then(data => {
        closeModal()
        setExtensions(data)
      })
      .catch(error => console.error(error));
  }

  const disableExtension = (extensionId: string) => {
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' }
    };
    fetch(import.meta.env.VITE_SERVER_URL + "/admincentral/extensions/" + subscriptionId + "/disable/" + extensionId, requestOptions)
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
          <Modal
            isOpen={modalData.open}
            onAfterOpen={afterOpenModal}
            onRequestClose={closeModal}
            style={customStyles}
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
                  {modalData.requiredConfig && modalData.requiredConfig.length > 0 && (
                    <div className="modal-body">
                      <p>Required config values:</p>
                      {modalData.requiredConfig.map(value => (
                        <div className="row form-group" key={value}>
                          <label htmlFor={value}>{value}</label>
                          <input type="password" id={value} className="form-control"/>
                        </div>
                      ))}
                    </div> 
                  )}
                  {!modalData.requiredConfig || modalData.requiredConfig.length < 1 && (
                    <div className="modal-body">
                        <p>This extension doesn't requires config</p>
                    </div> 
                  )}
                  <div className="modal-footer btn-group">
                    <button type="button" className="btn btn-secondary" onClick={closeModal} data-bs-dismiss="modal">Close</button>
                    <button type="button" className="btn btn-primary" onClick={() => enableExtension(modalData.extensionId, modalData.requiredConfig)}>Activate</button>
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
                <th scope="col">Action</th>
              </tr>
            </thead>
            <tbody>
            {extensions.map(extension => (
              <tr key={extension['extensionId']}>
                <th scope="row">{extension['name']}</th>
                <td>{extension['description']}</td>
                {extension['enabled'] == true && (
                  <td><button type="button" className="btn btn-secondary disabled">Enabled</button></td>
                )}
                {extension['enabled'] == false && (
                    <td><button type="button" className="btn btn-secondary disabled">Disabled</button></td>
                )}
                {extension['enabled'] == false && (
                  <td><button type="button" onClick={() => openModal(extension['extensionId'], extension['name'], extension['description'], extension['requiredConfigKeys'])} className="btn btn-primary">Enable</button></td>
                )}
                {extension['enabled'] == true && (
                    <td><button type="button" onClick={() => disableExtension(extension['extensionId'])} className="btn btn-primary">Disable</button></td>
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
