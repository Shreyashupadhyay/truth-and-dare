// Simple test component to verify React is working
export default function TestComponent() {
  return (
    <div style={{ 
      padding: '40px', 
      backgroundColor: '#fff', 
      borderRadius: '8px',
      margin: '20px',
      textAlign: 'center'
    }}>
      <h1 style={{ color: '#333' }}>🎲 Truth & Dare</h1>
      <p style={{ color: '#666' }}>If you see this, React is working!</p>
      <p style={{ color: '#999', fontSize: '12px' }}>Check console for any errors</p>
    </div>
  )
}