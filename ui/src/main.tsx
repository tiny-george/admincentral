import React from 'react'
import ReactDOM from 'react-dom/client'
import { Head } from './components/Head'
import { Footer } from './components/Footer'
import { Marketplace } from './components/Marketplace'
import { Applications } from './components/Applications'
import './index.css'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/",
    element: <>
      <Head/>
      <Applications/>
      <Footer/>
    </>,
  },
  {
    path: "marketplace",
    element: <>
      <Head/>
      <Marketplace/>
      <Footer/>
    </>,
  },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)
