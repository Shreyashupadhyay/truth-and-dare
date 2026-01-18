import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import ErrorBoundary from './components/ErrorBoundary.jsx'
import './index.css'

console.log('main.jsx is loading...') // Debug log

// Verify root element exists
const rootElement = document.getElementById('root')
console.log('Root element:', rootElement) // Debug log

if (!rootElement) {
  console.error('Root element not found!')
  document.body.innerHTML = `
    <div style="padding: 20px; color: red; font-size: 18px;">
      <h1>Error: Root element not found!</h1>
      <p>Please check that index.html contains: &lt;div id="root"&gt;&lt;/div&gt;</p>
    </div>
  `
} else {
  console.log('Rendering React app...') // Debug log
  try {
    const root = ReactDOM.createRoot(rootElement)
    root.render(
      <React.StrictMode>
        <ErrorBoundary>
          <App />
        </ErrorBoundary>
      </React.StrictMode>
    )
    console.log('React app rendered successfully!') // Debug log
  } catch (error) {
    console.error('Error rendering React app:', error)
    rootElement.innerHTML = `
      <div style="padding: 20px; color: red; font-size: 18px;">
        <h1>Error Rendering React App</h1>
        <pre>${error.toString()}</pre>
        <p>Check the browser console for more details.</p>
      </div>
    `
  }
}