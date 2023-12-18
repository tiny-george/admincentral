import { useContext } from 'react';
import { AppContext } from '../AppContext.tsx';

export const Head = () => {
  const state = useContext(AppContext);

  return (
    <header className="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">
        <a href="/" className="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis text-decoration-none">
            <svg className="bi me-2" width="40" height="32"></svg>
            <span className="fs-4">MX Admincentral Extension</span>
        </a>
        <p className="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis">Subscription: { state.subscriptionId }</p>
        <p className="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis">Environment: { state.environment }</p>
        <p className="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis">User: some.email@magnolia-cms.com</p>
    </header>
  )
}
