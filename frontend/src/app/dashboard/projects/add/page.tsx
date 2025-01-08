'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from "@/components/ui/card"
import { ArrowLeft } from 'lucide-react'
import { useToast } from "@/hooks/use-toast"
import { Toaster } from "@/components/ui/toaster"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'

type Project = {
  nom: string
  mes: string
  dataInici: string
  dataFi: string
  numeroEmpleats: number
  ubicacio: string
}

const months = [
  "Gener", "Febrer", "Març", "Abril", "Maig", "Juny",
  "Juliol", "Agost", "Setembre", "Octubre", "Novembre", "Desembre"
]

export default function AddProjectPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [isLoading, setIsLoading] = useState(false)
  const [project, setProject] = useState<Project>({
    nom: '',
    mes: '',
    dataInici: '',
    dataFi: '',
    numeroEmpleats: 0,
    ubicacio: ''
  })

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = e.target
    setProject(prev => ({
      ...prev,
      [name]: type === 'number' ? Number(value) : value
    }))
  }

  const handleSelectChange = (value: string) => {
    setProject(prev => ({
      ...prev,
      mes: value
    }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    try {
      const response = await fetch('http://10.4.41.40:8080/projectes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(project),
      })
      if (!response.ok) {
        throw new Error('Failed to create project')
      }
      toast({
        title: "Success",
        description: "Project created successfully",
      })
      router.push('/dashboard/projects')
    } catch (error) {
      console.error('Error creating project:', error)
      toast({
        title: "Error",
        description: "Failed to create project. Please try again.",
        variant: "destructive",
      })
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="container mx-auto py-10">
      <Button variant="ghost" onClick={() => router.push('/dashboard/projects')} className="mb-6">
        <ArrowLeft className="mr-2 h-4 w-4" /> Anar a la llista de projectes
      </Button>
      <Card className="max-w-2xl mx-auto">
        <CardHeader>
          <CardTitle>Crear un nou projecte</CardTitle>
          <CardDescription>Introdueix els detalls del nou projecte.</CardDescription>
        </CardHeader>
        <form onSubmit={handleSubmit}>
          <CardContent className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="nom">Nom del Projecte</Label>
              <Input id="nom" name="nom" value={project.nom} onChange={handleInputChange} required />
            </div>
            <div className="space-y-2">
              <Label htmlFor="mes">Mes</Label>
              <Select onValueChange={handleSelectChange} value={project.mes}>
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Selecciona un mes" />
                </SelectTrigger>
                <SelectContent>
                  {months.map((month) => (
                    <SelectItem key={month} value={month}>
                      {month}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="dataInici">Data Inici</Label>
                <Input id="dataInici" name="dataInici" type="date" value={project.dataInici} onChange={handleInputChange} required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="dataFi">Data Fi</Label>
                <Input id="dataFi" name="dataFi" type="date" value={project.dataFi} onChange={handleInputChange} required />
              </div>
            </div>
            <div className="space-y-2">
              <Label htmlFor="numeroEmpleats">Numero d&apos;Empleats</Label>
              <Input id="numeroEmpleats" name="numeroEmpleats" type="number" value={project.numeroEmpleats} onChange={handleInputChange} required />
            </div>
            <div className="space-y-2">
              <Label htmlFor="ubicacio">Ubicació</Label>
              <Input id="ubicacio" name="ubicacio" value={project.ubicacio} onChange={handleInputChange} required />
            </div>
          </CardContent>
          <CardFooter>
            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? 'Afegint el projecte...' : 'Afegeix el projecte'} 
            </Button>
          </CardFooter>
        </form>
      </Card>
      <Toaster />
    </div>
  )
}

