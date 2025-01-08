'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { PlusCircle, Edit, Trash2, Calendar, Users, MapPin } from 'lucide-react'
import { Skeleton } from "@/components/ui/skeleton"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { useToast } from "@/hooks/use-toast"
import { revalidatePath } from 'next/cache'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Label } from "@/components/ui/label"

type Project = {
  nom: string
  mes: string
  dataInici: string
  dataFi: string
  numeroEmpleats: number
  ubicacio: string
}

export default function ProjectsPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [searchTerm, setSearchTerm] = useState('')
  const [projects, setProjects] = useState<Project[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [editingProject, setEditingProject] = useState<Project | null>(null)

  useEffect(() => {
    fetchProjects()
  }, [])

  const fetchProjects = async () => {
    setIsLoading(true)
    try {
      const response = await fetch('http://10.4.41.40:8080/projectes')
      if (!response.ok) {
        throw new Error('Hi ha hagut un error en recuperar els projectes.')
      }
      const data = await response.json()
      setProjects(data)
    } catch (err) {
      console.error(err)
      setError('Hi ha hagut un error en recuperar els projectes. Torna-ho a provar.')
    } finally {
      setIsLoading(false)
    }
  }

  async function deleteProject(projectName: string) {
    try {
      const response = await fetch(`http://10.4.41.40:8080/projectes/${encodeURIComponent(projectName)}`, {
        method: 'DELETE',
      })
  
      if (!response.ok) {
        throw new Error('Failed to delete project')
      }
  
      revalidatePath('/dashboard/projects')
      return { success: true }
    } catch (error) {
      console.error('Error deleting project:', error)
      return { success: false, error: 'Failed to delete project' }
    }
  }

  const handleDeleteProject = async (projectName: string) => {
    try {
      const result = await deleteProject(projectName)
      if (result.success) {
        setProjects(projects.filter(project => project.nom !== projectName))
        toast({
          title: "Èxit",
          description: "Projecte eliminat correctament",
        })
      } else {
        throw new Error(result.error)
      }
    } catch (error) {
      console.error('Error esborrant el projecte:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut eliminar el projecte. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleEditProject = async () => {
    if (!editingProject) return

    try {
      const response = await fetch(`http://10.4.41.40:8080/projectes/${encodeURIComponent(editingProject.nom)}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editingProject),
      })

      if (!response.ok) {
        throw new Error('Failed to update project')
      }

      toast({
        title: "Èxit",
        description: "Projecte actualitzat correctament",
      })

      setEditingProject(null)
      fetchProjects()
    } catch (error) {
      console.error('Error updating project:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut actualitzar el projecte. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const filteredProjects = projects.filter(project =>
    project.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    project.ubicacio.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('ca-ES', { year: 'numeric', month: 'long', day: 'numeric' })
  }

  if (error) {
    return (
      <Card className="max-w-md mx-auto mt-8">
        <CardContent className="pt-6">
          <div className="text-center text-red-500">{error}</div>
        </CardContent>
      </Card>
    )
  }

  return (
    <div className="container mx-auto py-10">
      <Card className="mb-8">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-2xl font-bold">Project Management</CardTitle>
          <Button onClick={() => router.push('/dashboard/projects/add')}>
            <PlusCircle className="mr-2 h-4 w-4" /> Add New Project
          </Button>
        </CardHeader>
        <CardContent>
          <Input
            placeholder="Search projects..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="max-w-sm mb-4"
          />
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Nom</TableHead>
                <TableHead>Mes</TableHead>
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
                    <TableCell><Skeleton className="h-4 w-[150px]" /></TableCell>
                    <TableCell><Skeleton className="h-4 w-[150px]" /></TableCell>
                    <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
                    <TableCell><Skeleton className="h-4 w-[150px]" /></TableCell>
                    <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
                  </TableRow>
                ))
              ) : (
                filteredProjects.map((project, index) => (
                  <TableRow key={index}>
                    <TableCell className="font-medium">{project.nom}</TableCell>
                    <TableCell>
                      <Badge variant="outline">{project.mes}</Badge>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center">
                        <Calendar className="mr-2 h-4 w-4 text-muted-foreground" />
                        {formatDate(project.dataInici)}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center">
                        <Calendar className="mr-2 h-4 w-4 text-muted-foreground" />
                        {formatDate(project.dataFi)}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center">
                        <Users className="mr-2 h-4 w-4 text-muted-foreground" />
                        {project.numeroEmpleats}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center">
                        <MapPin className="mr-2 h-4 w-4 text-muted-foreground" />
                        {project.ubicacio}
                      </div>
                    </TableCell>
                    <TableCell className="text-right">
                      <Dialog>
                        <DialogTrigger asChild>
                          <Button variant="outline" size="sm" className="mr-2" onClick={() => setEditingProject(project)}>
                            <Edit className="mr-2 h-4 w-4" />
                            Edit
                          </Button>
                        </DialogTrigger>
                        <DialogContent className="sm:max-w-[425px]">
                          <DialogHeader>
                            <DialogTitle>Edit Project</DialogTitle>
                            <DialogDescription>
                              Fes els canvis en el projecte aquí.
                            </DialogDescription>
                          </DialogHeader>
                          <div className="grid gap-4 py-4">
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-nom" className="text-right">
                                Nom
                              </Label>
                              <Input
                                id="edit-nom"
                                value={editingProject?.nom || ''}
                                onChange={(e) => setEditingProject(editingProject ? { ...editingProject, nom: e.target.value } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-mes" className="text-right">
                                Mes
                              </Label>
                              <Input
                                id="edit-mes"
                                value={editingProject?.mes || ''}
                                onChange={(e) => setEditingProject(editingProject ? { ...editingProject, mes: e.target.value } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-dataInici" className="text-right">
                                Data Inici
                              </Label>
                              <Input
                                id="edit-dataInici"
                                type="date"
                                value={editingProject?.dataInici.split('T')[0] || ''}
                                onChange={(e) => setEditingProject(editingProject ? { ...editingProject, dataInici: e.target.value } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-dataFi" className="text-right">
                                Data Fi
                              </Label>
                              <Input
                                id="edit-dataFi"
                                type="date"
                                value={editingProject?.dataFi.split('T')[0] || ''}
                                onChange={(e) => setEditingProject(editingProject ? { ...editingProject, dataFi: e.target.value } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-numeroEmpleats" className="text-right">
                                Numero Empleats
                              </Label>
                              <Input
                                id="edit-numeroEmpleats"
                                type="number"
                                value={editingProject?.numeroEmpleats || 0}
                                onChange={(e) => setEditingProject(editingProject ? { ...editingProject, numeroEmpleats: parseInt(e.target.value) } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-ubicacio" className="text-right">
                                Ubicacio
                              </Label>
                              <Input
                                id="edit-ubicacio"
                                value={editingProject?.ubicacio || ''}
                                onChange={(e) => setEditingProject(editingProject ? { ...editingProject, ubicacio: e.target.value } : null)}
                                className="col-span-3"
                              />
                            </div>
                          </div>
                          <DialogFooter>
                            <Button type="submit" onClick={handleEditProject}>Save changes</Button>
                          </DialogFooter>
                        </DialogContent>
                      </Dialog>
                      <Dialog>
                        <DialogTrigger asChild>
                          <Button variant="ghost" size="sm" className="text-red-600">
                            <Trash2 className="mr-2 h-4 w-4" />
                            Delete
                          </Button>
                        </DialogTrigger>
                        <DialogContent>
                          <DialogHeader>
                            <DialogTitle>Are you sure you want to delete this project?</DialogTitle>
                            <DialogDescription>
                              This action cannot be undone. This will permanently delete the project
                              and remove all associated data from our servers.
                            </DialogDescription>
                          </DialogHeader>
                          <DialogFooter>
                            <Button variant="outline" onClick={() => {}}>Cancel</Button>
                            <Button variant="destructive" onClick={() => handleDeleteProject(project.nom)}>
                              Delete
                            </Button>
                          </DialogFooter>
                        </DialogContent>
                      </Dialog>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
}