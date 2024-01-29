import React from 'react';
import ReactDOM from 'react-dom/client';
import { Head } from './components/Head';
import { Footer } from './components/Footer';
import { Marketplace } from './components/Marketplace';
import { Applications } from './components/Applications';
import './index.css';
import {
    BrowserRouter as Router,
    Routes,
    Route,
} from 'react-router-dom';
import { AppContext } from './AppContext.tsx';
import { Watches } from './components/Watches.tsx';
import { FormBuilder } from './components/FormBuilder.tsx';
import { ExtensionsManager } from './components/ExtensionsManager.tsx';

const App = () => (
    <>
        <Head />
        <Routes>
            <Route path="/" element={<Applications />} />
            <Route path="marketplace" element={<Marketplace />} />
            <Route path="watches" element={<Watches />} />
            <Route path="formBuilder" element={<FormBuilder />} />
            <Route path="extensions-manager" element={<ExtensionsManager />} />
        </Routes>
        <Footer />
    </>
);

const value = { subscriptionId: 'zpxvow3ismzwpot7', environment: 'main' };

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <AppContext.Provider value={value}>
            <Router>
                <App />
            </Router>
        </AppContext.Provider>
    </React.StrictMode>,
);
