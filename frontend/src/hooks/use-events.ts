import { useState, useEffect } from 'react'

export interface Event {
  id: string
  title: string
  date: Date
  startTime: string
  endTime: string
  color: string
  description: string
  projectId: string
}

export function useEvents() {
  const [events, setEvents] = useState<Event[]>([])

  useEffect(() => {
    const storedEvents = localStorage.getItem('events')
    if (storedEvents) {
      setEvents(JSON.parse(storedEvents, (key, value) => {
        if (key === 'date') return new Date(value)
        return value
      }))
    }
  }, [])

  useEffect(() => {
    localStorage.setItem('events', JSON.stringify(events))
  }, [events])

  const addEvent = (event: Event) => {
    const newEvent = { ...event, id: Date.now().toString(), projectId: event.projectId || '' }
    setEvents([...events, newEvent])
  }

  const updateEvent = (updatedEvent: Event) => {
    setEvents(events.map(event => event.id === updatedEvent.id ? updatedEvent : event))
  }

  const deleteEvent = (id: string) => {
    setEvents(events.filter(event => event.id !== id))
  }

  return { events, addEvent, updateEvent, deleteEvent }
}
