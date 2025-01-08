"use client"

import { useState, useEffect } from 'react'
import { format, addDays, startOfWeek, isSameDay, parseISO } from 'date-fns'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { toast } from "@/hooks/use-toast"
import { useRouter } from 'next/navigation'

const HOURS = Array.from({ length: 24 }, (_, i) => i) // 0 to 23

interface Project {
  nom: string
  mes: string
  dataInici: string
  dataFi: string
  numeroEmpleats: number
  ubicacio: string
}

interface Requeriment {
  id: string;
  day: string;
  startTime: string;
  endTime: string;
  technicalProfile: string;
  actName: string;
  actRoom: string;
  nomProjecte: string;
}

export default function PlanificationPage() {
  const [currentWeek, setCurrentWeek] = useState(() => {
    const today = new Date()
    return startOfWeek(today, { weekStartsOn: 1 })
  })
  const [currentDay] = useState(() => {
    return new Date()
  })
  const [Requeriments, setRequeriments] = useState<Requeriment[]>([])
  const [selectedProject, setSelectedProject] = useState<string>("all")
  const [projects, setProjects] = useState<Project[]>([])
  const [llocsTreball, setLlocsTreball] = useState<{ posicio: string }[]>([])
  const [isLoadingProjects, setIsLoadingProjects] = useState(true)
  const [isLoadingLlocsTreball, setIsLoadingLlocsTreball] = useState(true)
  const [isLoadingRequeriments, setIsLoadingRequeriments] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [formData, setFormData] = useState<Requeriment>({
    id: '',
    day: '',
    startTime: '',
    endTime: '',
    technicalProfile: '',
    actName: '',
    actRoom: '',
    nomProjecte: '',
  })
  const [quantity, setQuantity] = useState<number>(1)
  const router = useRouter()
  useEffect(() => {
    const fetchProjects = async () => {
      setIsLoadingProjects(true)
      try {
        const response = await fetch('http://10.4.41.40:8080/projectes')
        if (!response.ok) {
          throw new Error('Failed to fetch projects')
        }
        const data = await response.json()
        setProjects(data)
      } catch (err) {
        console.error(err)
        setError('Failed to load projects. Please try again later.')
        toast({
          title: "Error",
          description: "Failed to load projects. Please try again later.",
          variant: "destructive",
        })
      } finally {
        setIsLoadingProjects(false)
      }
    }

    const fetchLlocsTreball = async () => {
      setIsLoadingLlocsTreball(true)
      try {
        const response = await fetch('http://10.4.41.40:8080/lloctreball')
        if (!response.ok) {
          throw new Error('Failed to fetch llocsTreball')
        }
        const data = await response.json()
        setLlocsTreball(data)
      } catch (err) {
        console.error(err)
        setError('Failed to load llocsTreball. Please try again later.')
        toast({
          title: "Error",
          description: "Failed to load llocsTreball. Please try again later.",
          variant: "destructive",
        })
      } finally {
        setIsLoadingLlocsTreball(false)
      }
    }

    const fetchRequeriments = async () => {
      setIsLoadingRequeriments(true)
      try {
        const response = await fetch('http://10.4.41.40:8080/requeriments')
        if (!response.ok) {
          throw new Error('Failed to fetch requeriments')
        }
        const data = await response.json()
        setRequeriments(data)
      } catch (err) {
        console.error(err)
        setError('Failed to load requeriments. Please try again later.')
        toast({
          title: "Error",
          description: "Failed to load requeriments. Please try again later.",
          variant: "destructive",
        })
      } finally {
        setIsLoadingRequeriments(false)
      }
    }

    fetchProjects()
    fetchLlocsTreball()
    fetchRequeriments()
  }, [])

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleSelectChange = (name: string, value: string) => {
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleQuantityChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setQuantity(Number(e.target.value));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const requests = Array.from({ length: quantity }, () =>
        fetch('http://10.4.41.40:8080/requeriments', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(formData),
        })
      );
      const responses = await Promise.all(requests);
      const allSuccessful = responses.every(response => response.ok);
      if (!allSuccessful) {
        throw new Error('Failed to save one or more requirements');
      }
      const newRequeriments = Array.from({ length: quantity }, (_, index) => ({ ...formData, id: `${formData.id}-${index}` }));
      setRequeriments(prev => [...prev, ...newRequeriments]);
      setFormData({
        id: '',
        day: '',
        startTime: '',
        endTime: '',
        technicalProfile: '',
        actName: '',
        actRoom: '',
        nomProjecte: '',
      });
      setQuantity(1);
      toast({
        title: "Requirements added",
        description: `${quantity} requirements have been added to the timetable and saved.`,
      });
    } catch (error) {
      console.error(error)
      toast({
        title: "Error",
        description: "Failed to save the requirements. Please try again.",
        variant: "destructive",
      });
    }
  }

  const compactRequeriments = (requeriments: Requeriment[]) => {
    const grouped = requeriments.reduce((acc, req) => {
      const key = `${req.day}-${req.startTime}-${req.endTime}-${req.technicalProfile}-${req.nomProjecte}`;
      if (!acc[key]) {
        acc[key] = { ...req, count: 1 };
      } else {
        acc[key].count += 1;
      }
      return acc;
    }, {} as Record<string, Requeriment & { count: number }>);
    return Object.values(grouped);
  };

  const weekDays = Array.from({ length: 7 }, (_, i) => addDays(currentWeek, i))

  const sendRequerimentsToEndpoint = async () => {

    let date = null
    if(currentDay > currentWeek) {
      date = format(currentDay, 'yyyy-MM-dd')
    } else {
      date = format(currentWeek, 'yyyy-MM-dd')
    }
    try {
      const response = await fetch(`http://10.4.41.40:8080/algorithm/${date}`, {
        method: 'GET',
      })
      if (response.ok) {
        const data = await response.json()

        if(data === true) {
          toast({
            title: "Assignació de personal realitzada correctament",
            description: "Tots els requeriments han estat satisfets correctament.",
          });
        } else {
          toast({
            title: "Warning",
            description: "No s'ha pogut assignar personal a tots els requeriments.",
          });
        }
      } else {
        throw new Error('Failed to send Requeriments')
      }
    } catch (error) {
      console.error(error)
      toast({
        title: "Error",
        description: "Failed to send Requeriments to the endpoint.",
        variant: "destructive",
      })
    }

    router.push('/dashboard/timetable')
  }

  const removeRequeriment = async (RequerimentId: string) => {
    try {
      const response = await fetch(`http://10.4.41.40:8080/requeriments/${RequerimentId}`, {
        method: 'DELETE',
      });
      if (!response.ok) {
        throw new Error('Failed to delete Requeriment');
      }
      setRequeriments(prev => prev.filter(Requeriment => Requeriment.id !== RequerimentId));
      toast({
        title: "Requeriment removed",
        description: "The Requeriment has been removed from the timetable and deleted.",
      });
    } catch (error) {
      console.error(error)
      toast({
        title: "Error",
        description: "Failed to delete the Requeriment. Please try again.",
        variant: "destructive",
      });
    }
  };

  const filteredRequeriments = selectedProject === "all"
  ? Requeriments
  : Requeriments.filter(req => req.nomProjecte === selectedProject);

  if (isLoadingProjects || isLoadingLlocsTreball || isLoadingRequeriments) {
    return <div className="p-8">Loading data...</div>
  }

  if (error) {
    return <div className="p-8">Error: {error}</div>
  }

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
            <SelectValue placeholder="Select a project" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Projects</SelectItem>
            {projects.map(project => (
              <SelectItem key={project.nom} value={project.nom}>{project.nom}</SelectItem>
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
              <span>{format(currentWeek, 'MMM d')} - {format(addDays(currentWeek, 4), 'MMM d, yyyy')}</span>
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
                      {weekDays.map(day => {
                        const requirementsForDayAndHour = compactRequeriments(filteredRequeriments)
                          .filter((Requeriment) =>
                            isSameDay(parseISO(Requeriment.day), day) &&
                            parseInt(Requeriment.startTime.split(':')[0]) <= hour &&
                            parseInt(Requeriment.endTime.split(':')[0]) > hour
                          );

                        console.log(`Requirements for ${format(day, 'yyyy-MM-dd')} at ${hour}:00:`, requirementsForDayAndHour);

                        return (
                          <td key={`${day.toISOString()}-${hour}`} className="border p-2">
                            {requirementsForDayAndHour.map((Requeriment) => (
                              <div
                                key={Requeriment.id}
                                className="text-xs p-1 mb-1 rounded bg-blue-200 cursor-pointer hover:bg-blue-300"
                                title={`Act: ${Requeriment.actName}, Room: ${Requeriment.actRoom}, Profile: ${Requeriment.technicalProfile}, Project: ${Requeriment.nomProjecte}, Count: ${Requeriment.count}`}
                                onClick={() => removeRequeriment(Requeriment.id)}
                              >
                                {`${Requeriment.actName} (${Requeriment.count})`}
                              </div>
                            ))}
                          </td>
                        );
                      })}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Requeriment Details</CardTitle>
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
                <Label htmlFor="technicalProfile">Lloc de Treball</Label>
                <Select
                  onValueChange={(value) => handleSelectChange('technicalProfile', value)}
                  value={formData.technicalProfile}
                  required
                >
                  <SelectTrigger id="technicalProfile">
                    <SelectValue placeholder="Select a technicalProfile" />
                  </SelectTrigger>
                  <SelectContent>
                    {llocsTreball.map((lloc, index) => (
                      <SelectItem key={lloc.posicio || index} value={lloc.posicio}>
                        {lloc.posicio}
                      </SelectItem>
                    ))}
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
                <Label htmlFor="quantity">Quantity</Label>
                <Input
                  id="quantity"
                  name="quantity"
                  type="number"
                  min="1"
                  value={quantity}
                  onChange={handleQuantityChange}
                  required
                />
              </div>
              <div>
                <Label htmlFor="project">Project</Label>
                <Select
                  onValueChange={(value) => handleSelectChange('nomProjecte', value)} // Correctly map to 'nomProjecte'
                  value={formData.nomProjecte} // Ensure it binds to 'nomProjecte'
                  required
                >
                  <SelectTrigger id="project">
                    <SelectValue placeholder="Select a project" />
                  </SelectTrigger>
                  <SelectContent>
                    {projects.map(project => (
                      <SelectItem key={project.nom} value={project.nom}>
                        {project.nom}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <Button type="submit" className="w-full">Add Requirements</Button>
            </form>
          </CardContent>
        </Card>
      </div>
      <div className="mt-8">
        <Button onClick={sendRequerimentsToEndpoint} className="w-full">
          Assginar treballadors setmana actual
        </Button>
      </div>
    </div>
  )
}
