'use client'

import { useState, useEffect } from 'react'
import { format, addDays, startOfWeek, endOfWeek, isSameDay, setDay, parseISO } from 'date-fns'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { toast } from "@/hooks/use-toast"

const HOURS = Array.from({ length: 24 }, (_, i) => i) // 0 to 23
const DAYS = ['Dll', 'Dm', 'Dc', 'Dj', 'Dv']

interface Event {
  id: string;
  day: string;
  startTime: string;
  endTime: string;
  technicalProfile: string;
  actName: string;
  actRoom: string;
  assignedEmployee: string;
  project: string;
}

const PROJECTS = ['Project A', 'Project B', 'Project C']

export default function PlanificationPage() {
  const [currentWeek, setCurrentWeek] = useState(new Date())
  const [events, setEvents] = useState<Event[]>([])
  const [selectedProject, setSelectedProject] = useState<string>('')
  const [formData, setFormData] = useState<Event>({
    id: '',
    day: '',
    startTime: '',
    endTime: '',
    technicalProfile: '',
    actName: '',
    actRoom: '',
    assignedEmployee: '',
    project: '',
  })

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleSelectChange = (name: string, value: string) => {
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const newEvent = { ...formData, id: Date.now().toString() }
    setEvents(prev => [...prev, newEvent])
    setFormData({
      id: '',
      day: '',
      startTime: '',
      endTime: '',
      technicalProfile: '',
      actName: '',
      actRoom: '',
      assignedEmployee: '',
      project: '',
    })
    toast({
      title: "Event added",
      description: "The event has been added to the timetable.",
    })
  }

  const weekDays = Array.from({ length: 5 }, (_, i) => addDays(setDay(startOfWeek(currentWeek), 1), i))

  const sendEventsToEndpoint = async () => {
    try {
      const response = await fetch('/api/events', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(events),
      })
      if (response.ok) {
        toast({
          title: "Events sent",
          description: "All events have been sent to the endpoint successfully.",
        })
      } else {
        throw new Error('Failed to send events')
      }
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to send events to the endpoint.",
        variant: "destructive",
      })
    }
  }

  const removeEvent = (eventId: string) => {
    setEvents(prev => prev.filter(event => event.id !== eventId))
    toast({
      title: "Event removed",
      description: "The event has been removed from the timetable.",
    })
  }

  const filteredEvents = selectedProject
    ? events.filter(event => event.project === selectedProject)
    : events

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-6">Planification</h1>
      <div className="mb-4">
        <Label htmlFor="projectFilter">Filter by Project</Label>
        <Select
          onValueChange={(value) => setSelectedProject(value)}
          value={selectedProject}
        >
          <SelectTrigger id="projectFilter">
            <SelectValue placeholder="Project A" />
          </SelectTrigger>
          <SelectContent>
            {PROJECTS.map(project => (
              <SelectItem key={project} value={project}>{project}</SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Timetable */}
        <Card>
          <CardHeader>
            <CardTitle>Timetable</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex justify-between items-center mb-4">
              <Button onClick={() => setCurrentWeek(addDays(currentWeek, -7))}>Previous Week</Button>
              <span>{format(startOfWeek(currentWeek), 'MMM d')} - {format(endOfWeek(currentWeek), 'MMM d, yyyy')}</span>
              <Button onClick={() => setCurrentWeek(addDays(currentWeek, 7))}>Next Week</Button>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full border-collapse">
                <thead>
                  <tr>
                    <th className="border p-2"></th>
                    {weekDays.map(day => (
                      <th key={day.toISOString()} className="border p-2">{format(day, 'EEE')}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {HOURS.map(hour => (
                    <tr key={hour}>
                      <td className="border p-2">{hour.toString().padStart(2, '0')}:00</td>
                      {weekDays.map(day => (
                        <td key={`${day.toISOString()}-${hour}`} className="border p-2">
                          {filteredEvents
                            .filter(event => 
                              isSameDay(parseISO(event.day), day) && 
                              parseInt(event.startTime.split(':')[0]) <= hour &&
                              parseInt(event.endTime.split(':')[0]) > hour
                            )
                            .map(event => (
                              <div 
                                key={event.id} 
                                className="text-xs p-1 mb-1 rounded bg-blue-200 cursor-pointer hover:bg-blue-300"
                                title={`${event.actName} - ${event.assignedEmployee}`}
                                onClick={() => removeEvent(event.id)}
                              >
                                {event.actName}
                              </div>
                            ))
                          }
                        </td>
                      ))}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Event Details</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <Label htmlFor="day">Day</Label>
                <Input
                  id="day"
                  name="day"
                  type="date"
                  value={formData.day}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="startTime">Start Time</Label>
                  <Input
                    id="startTime"
                    name="startTime"
                    type="time"
                    value={formData.startTime}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="endTime">End Time</Label>
                  <Input
                    id="endTime"
                    name="endTime"
                    type="time"
                    value={formData.endTime}
                    onChange={handleInputChange}
                    required
                  />
                </div>
              </div>
              <div>
                <Label htmlFor="technicalProfile">Technical Profile</Label>
                <Select
                  onValueChange={(value) => handleSelectChange('technicalProfile', value)}
                  value={formData.technicalProfile}
                  required
                >
                  <SelectTrigger id="technicalProfile">
                    <SelectValue placeholder="Select a profile" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="profile1">Profile 1</SelectItem>
                    <SelectItem value="profile2">Profile 2</SelectItem>
                    <SelectItem value="profile3">Profile 3</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div>
                <Label htmlFor="actName">Act Name</Label>
                <Input
                  id="actName"
                  name="actName"
                  value={formData.actName}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div>
                <Label htmlFor="actRoom">Act Room</Label>
                <Input
                  id="actRoom"
                  name="actRoom"
                  value={formData.actRoom}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div>
                <Label htmlFor="assignedEmployee">Assigned Employee</Label>
                <Select
                  onValueChange={(value) => handleSelectChange('assignedEmployee', value)}
                  value={formData.assignedEmployee}
                  required
                >
                  <SelectTrigger id="assignedEmployee">
                    <SelectValue placeholder="Select an employee" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="employee1">Employee 1</SelectItem>
                    <SelectItem value="employee2">Employee 2</SelectItem>
                    <SelectItem value="employee3">Employee 3</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div>
                <Label htmlFor="project">Project</Label>
                <Select
                  onValueChange={(value) => handleSelectChange('project', value)}
                  value={formData.project}
                  required
                >
                  <SelectTrigger id="project">
                    <SelectValue placeholder="Select a project" />
                  </SelectTrigger>
                  <SelectContent>
                    {PROJECTS.map(project => (
                      <SelectItem key={project} value={project}>{project}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <Button type="submit" className="w-full">Add Event</Button>
            </form>
          </CardContent>
        </Card>
      </div>
      <div className="mt-8">
        <Button onClick={sendEventsToEndpoint} className="w-full">
          Guardar Planificació
        </Button>
      </div>
    </div>
  )
}

