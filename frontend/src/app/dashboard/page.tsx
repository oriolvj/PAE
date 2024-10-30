'use client'

import { useState } from 'react'
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"

type Event = {
  id: string
  title: string
  day: string
  startTime: string
  endTime: string
  color: string
}

const DAYS = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday']
const HOURS = Array.from({ length: 14 }, (_, i) => i + 7) // 7 AM to 8 PM

export default function TimetablePage() {
  const [events, setEvents] = useState<Event[]>([])
  const [newEvent, setNewEvent] = useState<Omit<Event, 'id'>>({
    title: '',
    day: '',
    startTime: '',
    endTime: '',
    color: '#3b82f6' // Default blue color
  })

  const handleAddEvent = () => {
    const id = Math.random().toString(36).substr(2, 9)
    setEvents([...events, { ...newEvent, id }])
    setNewEvent({ title: '', day: '', startTime: '', endTime: '', color: '#3b82f6' })
  }

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold">Timetable</h2>
      <Tabs defaultValue="view" className="w-full">
        <TabsList>
          <TabsTrigger value="view">View Timetable</TabsTrigger>
          <TabsTrigger value="add">Add Event</TabsTrigger>
        </TabsList>
        <TabsContent value="view">
          <Card>
            <CardContent>
              <div className="grid grid-cols-6 gap-2">
                <div className="font-bold">Time</div>
                {DAYS.map(day => (
                  <div key={day} className="font-bold">{day}</div>
                ))}
                {HOURS.map(hour => (
                  <>
                    <div key={hour} className="text-sm">
                      {hour % 12 || 12} {hour < 12 ? 'AM' : 'PM'}
                    </div>
                    {DAYS.map(day => (
                      <div key={`${day}-${hour}`} className="relative h-12 border-t">
                        {events
                          .filter(event => event.day === day && parseInt(event.startTime) <= hour && parseInt(event.endTime) > hour)
                          .map(event => (
                            <div
                              key={event.id}
                              className="absolute inset-0 m-1 overflow-hidden rounded p-1 text-xs"
                              style={{ backgroundColor: event.color }}
                            >
                              {event.title}
                            </div>
                          ))}
                      </div>
                    ))}
                  </>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
        <TabsContent value="add">
          <Card>
            <CardHeader>
              <CardTitle>Add New Event</CardTitle>
            </CardHeader>
            <CardContent>
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
                  <Label htmlFor="day">Day</Label>
                  <Select
                    value={newEvent.day}
                    onValueChange={(value) => setNewEvent({ ...newEvent, day: value })}
                  >
                    <SelectTrigger id="day">
                      <SelectValue placeholder="Select a day" />
                    </SelectTrigger>
                    <SelectContent>
                      {DAYS.map(day => (
                        <SelectItem key={day} value={day}>{day}</SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label htmlFor="startTime">Start Time</Label>
                    <Select
                      value={newEvent.startTime}
                      onValueChange={(value) => setNewEvent({ ...newEvent, startTime: value })}
                    >
                      <SelectTrigger id="startTime">
                        <SelectValue placeholder="Start time" />
                      </SelectTrigger>
                      <SelectContent>
                        {HOURS.map(hour => (
                          <SelectItem key={hour} value={hour.toString()}>
                            {hour % 12 || 12} {hour < 12 ? 'AM' : 'PM'}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                  <div>
                    <Label htmlFor="endTime">End Time</Label>
                    <Select
                      value={newEvent.endTime}
                      onValueChange={(value) => setNewEvent({ ...newEvent, endTime: value })}
                    >
                      <SelectTrigger id="endTime">
                        <SelectValue placeholder="End time" />
                      </SelectTrigger>
                      <SelectContent>
                        {HOURS.map(hour => (
                          <SelectItem key={hour} value={hour.toString()}>
                            {hour % 12 || 12} {hour < 12 ? 'AM' : 'PM'}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
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
                <Button type="submit">Add Event</Button>
              </form>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}