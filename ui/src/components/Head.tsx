export const Head = () => {

  return (
    <header className="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">
        <a href="/" className="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis text-decoration-none">
            <svg className="bi me-2" width="40" height="32"></svg>
            <span className="fs-4">MX Admincentral Extension</span>
        </a>
        <p className="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis">Subscription: some-subscription-id</p>
        <p className="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-body-emphasis">User: some.email@magnolia-cms.com</p>

        <ul className="nav nav-pills">
            <li className="nav-item"><a href="/">Home</a></li>
        </ul>
    </header>
  )
}
