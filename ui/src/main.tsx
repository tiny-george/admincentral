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
import { AppContext } from "./AppContext.tsx";
import { Watches } from "./components/Watches.tsx";
import { ExtensionsManager } from "./components/ExtensionsManager.tsx";

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
  {
    path: "watches",
    element: <>
      <Head/>
      <Watches/>
      <Footer/>
    </>,
  },
  {
    path: "extensions-manager",
    element: <>
      <Head/>
      <ExtensionsManager/>
      <Footer/>
    </>,
  },
]);

const value = { subscriptionId: "zpxvow3ismzwpot7", environment: "main" };

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <AppContext.Provider value={value}>
      <RouterProvider router={router} />
    </AppContext.Provider>
  </React.StrictMode>,
)
