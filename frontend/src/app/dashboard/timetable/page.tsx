'use client';

import { useState, useEffect } from 'react'
import { format, addDays, startOfWeek, endOfWeek, isSameDay, setDay } from 'date-fns'
import { useEvents, Event } from '@/hooks/use-events'
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Tooltip } from 'react-tooltip'
import React from 'react'

const HOURS = Array.from({ length: 24 }, (_, i) => i) // 0 to 23

const SUBJECTS = ['Parlament', 'CCCB', 'Kings League', 'Goroka', 'Acte Ferrer']
const COLORS = ['#3b82f6', '#ef4444', '#10b981', '#f59e0b', '#8b5cf6']

export default function TimetablePage() {
  const [currentWeek, setCurrentWeek] = useState(new Date())
  const [isAddEventOpen, setIsAddEventOpen] = useState(false)
  const [draggedEvent, setDraggedEvent] = useState<Event | null>(null)
  const { events, addEvent, updateEvent, deleteEvent } = useEvents()
  const [newEvent, setNewEvent] = useState<Event>({
    id: '',
    title: '',
    date: new Date(),
    startTime: '',
    endTime: '',
    color: '#3b82f6',
    description: '',
    projectId: '' // Add this line
  })

  useEffect(() => {
    if (events.length === 0) {
      generateTestEvents()
    }
    setCurrentWeek(new Date(2023, 4, 1)) // Set to May 1, 2023
  }, [])

  const generateTestEvents = () => {
    const testEvents: Event[] = [];
    const weekStart = startOfWeek(currentWeek, { weekStartsOn: 1 }); // Ensure Monday is the start of the week
  
    for (let i = 0; i < 5; i++) { // Loop through Monday to Friday (5 days)
      const day = addDays(weekStart, i); // Calculate the specific day
      const numEvents = Math.floor(Math.random() * 3) + 3; // 3 to 5 events per day
  
      console.log(`Generating ${numEvents} events for ${format(day, 'EEEE')}`); // Debug: Verify day and event count
  
      for (let j = 0; j < numEvents; j++) {
        const startHour = Math.floor(Math.random() * 8) + 9; // Random hour between 9 AM and 4 PM
        const subject = SUBJECTS[Math.floor(Math.random() * SUBJECTS.length)];
        const color = COLORS[Math.floor(Math.random() * COLORS.length)];
  
        const event: Event = {
          id: `test-${i}-${j}`,
          title: subject,
          date: day,
          startTime: `${startHour.toString().padStart(2, '0')}:00`,
          endTime: `${(startHour + 1).toString().padStart(2, '0')}:00`,
          color: color,
          description: `Test event for ${subject}`,
          projectId: '', // Add this line
        };
  
        testEvents.push(event);
        console.log(`Event generated: ${event.title} on ${format(day, 'EEEE')} at ${event.startTime}`); // Debug each event
      }
    }
  
    // Add all generated events to the state
    testEvents.forEach(addEvent);
  
    console.log(`Total events generated: ${testEvents.length}`); // Debug total event count
  };
  

  const handleAddEvent = () => {
    if (newEvent.id) {
      updateEvent(newEvent)
    } else {
      addEvent(newEvent)
    }
    setNewEvent({ id: '', title: '', date: new Date(), startTime: '', endTime: '', color: '#3b82f6', description: '', projectId: '' })
    setIsAddEventOpen(false)
  }

  const handleEventClick = (event: Event) => {
    setNewEvent(event)
    setIsAddEventOpen(true)
  }

  const handleDeleteEvent = () => {
    if (newEvent.id) {
      deleteEvent(newEvent.id)
      setIsAddEventOpen(false)
      setNewEvent({ id: '', title: '', date: new Date(), startTime: '', endTime: '', color: '#3b82f6', description: '', projectId: '' })
    }
  }

  const handleDragStart = (event: Event) => {
    setDraggedEvent(event)
  }

  const handleDragOver = (e: React.DragEvent, date: Date, hour: number) => {
    e.preventDefault()
  }

  const handleDrop = (e: React.DragEvent, date: Date, hour: number) => {
    e.preventDefault()
    if (draggedEvent) {
      const updatedEvent = {
        ...draggedEvent,
        date: date,
        startTime: hour.toString().padStart(2, '0') + ':00',
        endTime: (hour + 1).toString().padStart(2, '0') + ':00'
      }
      updateEvent(updatedEvent)
      setDraggedEvent(null)
    }
  }

  // Start the week on Monday
  const weekDays = Array.from({ length: 7 }, (_, i) => addDays(setDay(startOfWeek(currentWeek), 1), i))

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold">Horari Setmanal</h2>
      <div className="flex justify-between items-center">
        <Button onClick={() => setCurrentWeek(addDays(currentWeek, -7))}>Setmana anterior</Button>
        <span>{format(startOfWeek(currentWeek), 'MMM d')} - {format(endOfWeek(currentWeek), 'MMM d, yyyy')}</span>
        <Button onClick={() => setCurrentWeek(addDays(currentWeek, 7))}>Setmana següent</Button>
      </div>
      <Card>
        <CardContent>
          {/* Add a scrollable container */}
          <div className="grid grid-cols-8 gap-2">
            {/* Sticky header for weekdays */}
            <div className="sticky top-0 bg-white z-10">
              <div className="font-bold"></div>
            </div>
          </div>

          {/* Scrollable timetable */}
          <div className="overflow-y-auto max-h-[calc(100vh-200px)]">
            <div className="grid grid-cols-8 gap-2">
              <div className="font-bold"></div>
              {weekDays.map(day => (
                <div key={day.toISOString()} className="font-bold text-center">
                  {format(day, 'EEE')}<br />{format(day, 'd')}
                </div>
              ))}
              {HOURS.map(hour => (
                <React.Fragment key={hour}>
                  <div className="text-sm text-right pr-2 sticky left-0 bg-white z-10">
                    {hour.toString().padStart(2, '0')}:00
                  </div>
                  {weekDays.map(day => (
                    <div
                      key={`${day.toISOString()}-${hour}`}
                      className="relative h-12 border-t"
                      onDragOver={(e) => handleDragOver(e, day, hour)}
                      onDrop={(e) => handleDrop(e, day, hour)}
                    >
                      <div className="absolute inset-0 flex flex-wrap content-start overflow-hidden">
                        {events
                          .filter(event =>
                            isSameDay(event.date, day) &&
                            parseInt(event.startTime.split(':')[0]) <= hour &&
                            parseInt(event.endTime.split(':')[0]) > hour
                          )
                          .map((event) => (
                            <button
                              key={event.id}
                              onClick={() => handleEventClick(event)}
                              onDragStart={() => handleDragStart(event)}
                              draggable
                              className="m-0.5 h-5 flex-grow overflow-hidden rounded p-1 text-xs text-white cursor-move"
                              style={{ backgroundColor: event.color }}
                              data-tooltip-id={`tooltip-${event.id}`}
                              data-tooltip-content={`${event.title}
${event.startTime} - ${event.endTime}
${event.description}`}
                            >
                              {event.title}
                            </button>
                          ))}
                      </div>
                    </div>
                  ))}
                </React.Fragment>
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
      <Dialog open={isAddEventOpen} onOpenChange={setIsAddEventOpen}>
        <DialogTrigger asChild>
          <Button>Add Event</Button>
        </DialogTrigger>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{newEvent.id ? 'Edit Event' : 'Add New Event'}</DialogTitle>
          </DialogHeader>
          <form onSubmit={(e) => { e.preventDefault(); handleAddEvent(); }} className="space-y-4">
            <div>
              <Label htmlFor="title">Event Title</Label>
              <Input
                id="title"
                value={newEvent.title}
                onChange={(e) => setNewEvent({ ...newEvent, title: e.target.value })}
                required
              />
            </div>
            <div>
              <Label htmlFor="date">Date</Label>
              <Input
                id="date"
                type="date"
                value={format(newEvent.date, 'yyyy-MM-dd')}
                onChange={(e) => setNewEvent({ ...newEvent, date: new Date(e.target.value) })}
                required
              />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <Label htmlFor="startTime">Start Time</Label>
                <Input
                  id="startTime"
                  type="time"
                  value={newEvent.startTime}
                  onChange={(e) => setNewEvent({ ...newEvent, startTime: e.target.value })}
                  required
                />
              </div>
              <div>
                <Label htmlFor="endTime">End Time</Label>
                <Input
                  id="endTime"
                  type="time"
                  value={newEvent.endTime}
                  onChange={(e) => setNewEvent({ ...newEvent, endTime: e.target.value })}
                  required
                />
              </div>
            </div>
            <div>
              <Label htmlFor="color">Event Color</Label>
              <Input
                id="color"
                type="color"
                value={newEvent.color}
                onChange={(e) => setNewEvent({ ...newEvent, color: e.target.value })}
              />
            </div>
            <div>
              <Label htmlFor="description">Description</Label>
              <Input
                id="description"
                value={newEvent.description}
                onChange={(e) => setNewEvent({ ...newEvent, description: e.target.value })}
              />
            </div>
            <div className="flex justify-between">
              <Button type="submit">{newEvent.id ? 'Update Event' : 'Add Event'}</Button>
              <Button onClick={generateTestEvents}>Regenerate Test Events</Button>
              {newEvent.id && (
                <Button type="button" variant="destructive" onClick={handleDeleteEvent}>
                  Delete Event
                </Button>
              )}
            </div>
          </form>
        </DialogContent>
      </Dialog>
      <Tooltip />
      <Button onClick={() => { generateTestEvents(); setCurrentWeek(new Date(2023, 4, 1)); }}>
        Regenerate Test Events
      </Button>
    </div>
  )
}

