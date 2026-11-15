import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router'
import HomePage from './pages/HomePage/HomePage.jsx'
import Layout from './components/Layout/Layout.jsx'
import RolesPage from './pages/RolesPage/RolesPage.jsx'
import './index.css'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<HomePage />} />
          <Route path="/roles" element={<RolesPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
