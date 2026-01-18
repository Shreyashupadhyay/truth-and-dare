import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

/**
 * WebSocket service for real-time communication
 */
class WebSocketService {
  constructor() {
    this.client = null
    this.isConnected = false
    this.subscriptions = new Map()
  }

  /**
   * Connect to WebSocket server
   */
  connect(roomCode, onMessage, onError = null) {
    if (this.isConnected) {
      console.warn('Already connected to WebSocket')
      return
    }

    // WebSocket URL configuration
    // In production: Use VITE_WS_URL environment variable (e.g., wss://truth-and-dare-2.onrender.com/ws)
    // In development: Use localhost
    // Default: Use same protocol/host as current page + /ws
    const getWebSocketUrl = () => {
      const envUrl = import.meta.env.VITE_WS_URL
      if (envUrl) {
        return envUrl
      }
      
      // Detect if we're on localhost for development
      const isLocalhost = window.location.hostname === 'localhost' || 
                         window.location.hostname === '127.0.0.1'
      
      if (isLocalhost) {
        return 'http://localhost:8080/ws'
      }
      
      // For production: Use same protocol/host as current page
      // Automatically uses wss:// if page is https://
      const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:'
      const host = window.location.host
      return `${protocol}//${host}/ws`
    }
    
    const wsUrl = getWebSocketUrl()
    
    // Create STOMP client with SockJS
    // Note: When using webSocketFactory, do NOT use brokerURL
    this.client = new Client({
      connectHeaders: {},
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (str) => {
        console.log('STOMP:', str)
      },
      // Use SockJS as the underlying WebSocket factory
      webSocketFactory: () => new SockJS(wsUrl),
      onConnect: () => {
        console.log('WebSocket connected')
        this.isConnected = true
        
        // Subscribe to room topic
        const subscription = this.client.subscribe(
          `/topic/room/${roomCode}`,
          (message) => {
            try {
              const event = JSON.parse(message.body)
              console.log('Received WebSocket event:', event)
              if (onMessage) {
                onMessage(event)
              }
            } catch (error) {
              console.error('Error parsing WebSocket message:', error)
            }
          }
        )
        
        this.subscriptions.set(`/topic/room/${roomCode}`, subscription)
        
        // Subscribe to admin topic if needed
        const adminSubscription = this.client.subscribe(
          `/topic/room/${roomCode}/admin`,
          (message) => {
            try {
              const event = JSON.parse(message.body)
              console.log('Received admin WebSocket event:', event)
              if (onMessage) {
                onMessage(event)
              }
            } catch (error) {
              console.error('Error parsing admin WebSocket message:', error)
            }
          }
        )
        
        this.subscriptions.set(`/topic/room/${roomCode}/admin`, adminSubscription)
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame)
        this.isConnected = false
        if (onError) {
          onError(frame)
        }
      },
      onDisconnect: () => {
        console.log('WebSocket disconnected')
        this.isConnected = false
      },
    })
    
    this.client.activate()
  }

  /**
   * Disconnect from WebSocket server
   */
  disconnect() {
    if (this.client) {
      // Unsubscribe from all topics
      this.subscriptions.forEach((subscription) => {
        subscription.unsubscribe()
      })
      this.subscriptions.clear()
      
      this.client.deactivate()
      this.client = null
      this.isConnected = false
      console.log('WebSocket disconnected')
    }
  }

  /**
   * Send message to server
   */
  send(destination, body) {
    if (this.client && this.isConnected) {
      this.client.publish({
        destination,
        body: JSON.stringify(body),
      })
    } else {
      console.warn('Cannot send message: WebSocket not connected')
    }
  }
}

// Export singleton instance
export default new WebSocketService()