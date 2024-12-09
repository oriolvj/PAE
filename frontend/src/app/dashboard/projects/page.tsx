'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { PlusCircle } from 'lucide-react'
import { Skeleton } from "@/components/ui/skeleton"

type Project = {
  nom: string
  mes: string
  setmana: number
  dataInici: string
  dataFi: string
  numeroEmpleats: number
  ubicacio: string
}

export default function ProjectsPage() {
  const router = useRouter()
  const [searchTerm, setSearchTerm] = useState('')
  const [projects, setProjects] = useState<Project[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const response = await fetch('http://10.4.41.40:8080/projectes')
        if (!response.ok) {
          throw new Error('Hi ha hagut un error en recuperar els projectes.')
        }
        const data = await response.json()
        setProjects(data)
        setIsLoading(false)
      } catch (err) {
        setError('Hi ha hagut un error en recuperar els projectes. Torna-ho a provar.')
        setIsLoading(false)
      }
    }

    fetchProjects()
  }, [])

  const filteredProjects = projects.filter(project =>
    project.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    project.ubicacio.toLowerCase().includes(searchTerm.toLowerCase())
  )

  if (error) {
    return <div className="text-center text-red-500 mt-8">{error}</div>
  }

  return (
    <div className="container mx-auto py-10">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Project Management</h1>
        <Button onClick={() => router.push('/dashboard/projects/add')}>
          <PlusCircle className="mr-2 h-4 w-4" /> Add New Project
        </Button>
      </div>
      <div className="mb-4">
        <Input
          placeholder="Search projects..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="max-w-sm"
        />
      </div>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Nom</TableHead>
            <TableHead>Mes</TableHead>
            <TableHead>Setmana</TableHead>
            <TableHead>Data Inici</TableHead>
            <TableHead>Data Fi</TableHead>
            <TableHead>Numero empleats</TableHead>
            <TableHead>Ubicacio</TableHead>
            <TableHead className="text-right">Accions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {isLoading ? (
            Array(5).fill(0).map((_, index) => (
              <TableRow key={index}>
                <TableCell><Skeleton className="h-4 w-[200px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[50px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[50px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[150px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
              </TableRow>
            ))
          ) : (
            filteredProjects.map((project, index) => (
              <TableRow key={index}>
                <TableCell className="font-medium">{project.nom}</TableCell>
                <TableCell>{project.mes}</TableCell>
                <TableCell>{project.setmana}</TableCell>
                <TableCell>{project.dataInici}</TableCell>
                <TableCell>{project.dataFi}</TableCell>
                <TableCell>{project.numeroEmpleats}</TableCell>
                <TableCell>{project.ubicacio}</TableCell>
                <TableCell className="text-right">
                  <Button 
                    variant="ghost" 
                    size="sm" 
                    onClick={() => router.push(`/dashboard/projects/${encodeURIComponent(project.nom)}/edit`)}
                  >
                    Edit
                  </Button>
                  <Button variant="ghost" size="sm" className="text-red-600">Delete</Button>
                </TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>
    </div>
  )
}
