export const ExtensionsShopify = () => {

  return (
    <main>
      <h4>Shopify extensions recap</h4>
      <table className="table table-borderless">
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Setup/Tenant</th>
            <th scope="col">Tech</th>
            <th scope="col">Datatype</th>
            <th scope="col">URL's / Curl's</th>
          </tr>
        </thead>
        <tbody>
        <tr key={"original"}>
          <th scope="row">shopify</th>
          <td>Environment variables</td>
          <td>Java/Quarkus</td>
          <td>Shopify Products</td>
          <td>curl -v https://shopify.exp.magnolia-cloud.com/items</td>
        </tr>
        <tr key={"multi"}>
          <th scope="row">shopify-multi</th>
          <td>In UI/Endpoints</td>
          <td>Java/Quarkus Rest/GraphQL</td>
          <td>Shopify (also Cart) & Ecommerce. List/Id/Schema and Ecommerce traits.</td>
          <td><a href={"https://shopify-multi.exp.magnolia-cloud.com/schematic.json"}>Shopify Product Schema</a><br/>
            curl --url https://shopify-multi.exp.magnolia-cloud.com/items --header 'subscription-id: zpxvow3ismzwpot7'<br/>
            curl --url https://shopify-multi.exp.magnolia-cloud.com/products --header 'subscription-id: zpxvow3ismzwpot7'<br/>
            curl --url https://shopify-multi.exp.magnolia-cloud.com/collections --header 'subscription-id: zpxvow3ismzwpot7'<br/>
            curl --url https://shopify-multi.exp.magnolia-cloud.com/categories --header 'subscription-id: zpxvow3ismzwpot7'<br/>
            curl --url 'https://shopify-multi.exp.magnolia-cloud.com/items?searchTerm=HMS%20Victory' --header 'subscription-id: zpxvow3ismzwpot7'
          </td>
        </tr>
        <tr key={"multi-js"}>
          <th scope="row">shopify-multi-js</th>
          <td>In UI/Endpoints</td>
          <td>Typescript/NestJS</td>
          <td>Shopify Products</td>
          <td><a href={"https://shopify-multi-js.exp.magnolia-cloud.com"}>Js App</a><br/>curl --url https://shopify-multi-js.exp.magnolia-cloud.com/api/items --header 'subscription-id: zpxvow3ismzwpot7'</td>
        </tr>
        <tr key={"proxy"}>
          <th scope="row">hub</th>
          <td>In UI/Endpoints</td>
          <td>Java/Quarkus/Camel Rest/GraphQL</td>
          <td>Shopify Products</td>
          <td>Not deployed</td>
        </tr>
        </tbody>
      </table>
    </main>
  )
}
