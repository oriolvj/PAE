'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { PlusCircle, Pencil, Trash2 } from 'lucide-react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
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
import { useToast } from "@/hooks/use-toast"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"

type Employee = {
  id: number
  nom: string
  hores_contracte: number
  sou: number
  posicio: string
  username: string
}

type Position = {
  id: number
  posicio: string
}

export default function EmployeesPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [employees, setEmployees] = useState<Employee[]>([])
  const [positions, setPositions] = useState<Position[]>([])
  const [usernames, setUsernames] = useState<string[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [searchTerm, setSearchTerm] = useState('')
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [newEmployee, setNewEmployee] = useState<Omit<Employee, 'id'>>({
    nom: '',
    hores_contracte: 0,
    sou: 0,
    posicio: '',
    username: ''
  })
  const [editingEmployee, setEditingEmployee] = useState<Employee | null>(null)

  useEffect(() => {
    fetchEmployees()
    fetchPositions()
    fetchUsernames()
  }, [])

  const fetchEmployees = async () => {
    setIsLoading(true)
    try {
      const response = await fetch('http://10.4.41.40:8080/tecnics')
      if (!response.ok) {
        throw new Error('Hi ha hagut un error en recuperar els empleats.')
      }
      const data = await response.json()
      setEmployees(data)
    } catch (err) {
      setError('Hi ha hagut un error en recuperar els empleats. Torna-ho a provar.')
    } finally {
      setIsLoading(false)
    }
  }

  const fetchPositions = async () => {
    try {
      const response = await fetch('http://10.4.41.40:8080/lloctreball')
      if (!response.ok) {
        throw new Error('Hi ha hagut un error en recuperar les posicions.')
      }
      const data = await response.json()
      setPositions(data)
    } catch (err) {
      console.error('Error fetching positions:', err)
      toast({
        title: "Error",
        description: "No s'han pogut carregar les posicions. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const fetchUsernames = async () => {
    try {
      const response = await fetch('http://10.4.41.40:8080/usernames')
      if (!response.ok) {
        throw new Error('Hi ha hagut un error en recuperar els noms d\'usuari.')
      }
      const data = await response.json()
      setUsernames(data)
    } catch (err) {
      console.error('Error fetching usernames:', err)
      toast({
        title: "Error",
        description: "No s'han pogut carregar els noms d'usuari. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleAddEmployee = async () => {
    try {
      const response = await fetch('http://10.4.41.40:8080/employees', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newEmployee),
      })

      if (!response.ok) {
        throw new Error('Failed to add employee')
      }

      toast({
        title: "Èxit",
        description: "Empleat afegit correctament",
      })

      setNewEmployee({
        nom: '',
        hores_contracte: 0,
        sou: 0,
        posicio: '',
        username: ''
      })
      setIsAddDialogOpen(false)
      fetchEmployees()
    } catch (error) {
      console.error('Error adding employee:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut afegir l'empleat. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleEditEmployee = async () => {
    if (!editingEmployee) return

    try {
      const response = await fetch(`http://10.4.41.40:8080/employees/${editingEmployee.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editingEmployee),
      })

      if (!response.ok) {
        throw new Error('Failed to update employee')
      }

      toast({
        title: "Èxit",
        description: "Empleat actualitzat correctament",
      })

      setEditingEmployee(null)
      fetchEmployees()
    } catch (error) {
      console.error('Error updating employee:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut actualitzar l'empleat. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleDeleteEmployee = async (id: number) => {
    try {
      const response = await fetch(`http://10.4.41.40:8080/employees/${id}`, {
        method: 'DELETE',
      })

      if (!response.ok) {
        throw new Error('Failed to delete employee')
      }

      toast({
        title: "Èxit",
        description: "Empleat eliminat correctament",
      })

      fetchEmployees()
    } catch (error) {
      console.error('Error deleting employee:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut eliminar l'empleat. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const filteredEmployees = employees.filter(employee =>
    employee.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    employee.posicio.toLowerCase().includes(searchTerm.toLowerCase()) ||
    employee.username.toLowerCase().includes(searchTerm.toLowerCase())
  )

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
          <CardTitle className="text-2xl font-bold">Gestió d'Empleats</CardTitle>
          <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
            <DialogTrigger asChild>
              <Button>
                <PlusCircle className="mr-2 h-4 w-4" /> Afegir Empleat
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
              <DialogHeader>
                <DialogTitle>Afegir Nou Empleat</DialogTitle>
                <DialogDescription>
                  Introdueix les dades del nou empleat aquí.
                </DialogDescription>
              </DialogHeader>
              <div className="grid gap-4 py-4">
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="nom" className="text-right">
                    Nom
                  </Label>
                  <Input
                    id="nom"
                    value={newEmployee.nom}
                    onChange={(e) => setNewEmployee({...newEmployee, nom: e.target.value})}
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="hores_contracte" className="text-right">
                    Hores Contracte
                  </Label>
                  <Input
                    id="hores_contracte"
                    type="number"
                    value={newEmployee.hores_contracte}
                    onChange={(e) => setNewEmployee({...newEmployee, hores_contracte: parseInt(e.target.value)})}
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="sou" className="text-right">
                    Sou
                  </Label>
                  <Input
                    id="sou"
                    type="number"
                    value={newEmployee.sou}
                    onChange={(e) => setNewEmployee({...newEmployee, sou: parseInt(e.target.value)})}
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="posicio" className="text-right">
                    Posició
                  </Label>
                  <Select
                    value={newEmployee.posicio}
                    onValueChange={(value) => setNewEmployee({...newEmployee, posicio: value})}
                  >
                    <SelectTrigger className="col-span-3">
                      <SelectValue placeholder="Selecciona una posició" />
                    </SelectTrigger>
                    <SelectContent>
                      {positions.map((position) => (
                        <SelectItem key={position.id} value={position.posicio}>
                          {position.posicio}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="username" className="text-right">
                    Nom d'usuari
                  </Label>
                  <Select
                    value={newEmployee.username}
                    onValueChange={(value) => setNewEmployee({...newEmployee, username: value})}
                  >
                    <SelectTrigger className="col-span-3">
                      <SelectValue placeholder="Selecciona un nom d'usuari" />
                    </SelectTrigger>
                    <SelectContent>
                      {usernames.map((username) => (
                        <SelectItem key={username} value={username}>
                          {username}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>
              <DialogFooter>
                <Button type="submit" onClick={handleAddEmployee}>Afegir</Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </CardHeader>
        <CardContent>
          <div className="mb-4">
            <Input
              placeholder="Cercar empleats..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="max-w-sm"
            />
          </div>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Nom</TableHead>
                <TableHead>Hores Contracte</TableHead>
                <TableHead>Sou</TableHead>
                <TableHead>Posició</TableHead>
                <TableHead>Nom d'usuari</TableHead>
                <TableHead className="text-right">Accions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {isLoading ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center">Carregant...</TableCell>
                </TableRow>
              ) : filteredEmployees.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center">No s'han trobat empleats</TableCell>
                </TableRow>
              ) : (
                filteredEmployees.map((employee) => (
                  <TableRow key={employee.id}>
                    <TableCell className="font-medium">{employee.nom}</TableCell>
                    <TableCell>{employee.hores_contracte}</TableCell>
                    <TableCell>{employee.sou}</TableCell>
                    <TableCell>{employee.posicio}</TableCell>
                    <TableCell>{employee.username}</TableCell>
                    <TableCell className="text-right">
                      <Dialog>
                        <DialogTrigger asChild>
                          <Button variant="outline" size="sm" className="mr-2" onClick={() => setEditingEmployee(employee)}>
                            <Pencil className="mr-2 h-4 w-4" />
                            Editar
                          </Button>
                        </DialogTrigger>
                        <DialogContent className="sm:max-w-[425px]">
                          <DialogHeader>
                            <DialogTitle>Editar Empleat</DialogTitle>
                            <DialogDescription>
                              Modifica les dades de l'empleat aquí.
                            </DialogDescription>
                          </DialogHeader>
                          <div className="grid gap-4 py-4">
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-nom" className="text-right">
                                Nom
                              </Label>
                              <Input
                                id="edit-nom"
                                value={editingEmployee?.nom || ''}
                                onChange={(e) => setEditingEmployee(editingEmployee ? { ...editingEmployee, nom: e.target.value } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-hores_contracte" className="text-right">
                                Hores Contracte
                              </Label>
                              <Input
                                id="edit-hores_contracte"
                                type="number"
                                value={editingEmployee?.hores_contracte || 0}
                                onChange={(e) => setEditingEmployee(editingEmployee ? { ...editingEmployee, hores_contracte: parseInt(e.target.value) } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-sou" className="text-right">
                                Sou
                              </Label>
                              <Input
                                id="edit-sou"
                                type="number"
                                value={editingEmployee?.sou || 0}
                                onChange={(e) => setEditingEmployee(editingEmployee ? { ...editingEmployee, sou: parseInt(e.target.value) } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-posicio" className="text-right">
                                Posició
                              </Label>
                              <Select
                                value={editingEmployee?.posicio || ''}
                                onValueChange={(value) => setEditingEmployee(editingEmployee ? { ...editingEmployee, posicio: value } : null)}
                              >
                                <SelectTrigger className="col-span-3">
                                  <SelectValue placeholder="Selecciona una posició" />
                                </SelectTrigger>
                                <SelectContent>
                                  {positions.map((position) => (
                                    <SelectItem key={position.id} value={position.posicio}>
                                      {position.posicio}
                                    </SelectItem>
                                  ))}
                                </SelectContent>
                              </Select>
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-username" className="text-right">
                                Nom d'usuari
                              </Label>
                              <Select
                                value={editingEmployee?.username || ''}
                                onValueChange={(value) => setEditingEmployee(editingEmployee ? { ...editingEmployee, username: value } : null)}
                              >
                                <SelectTrigger className="col-span-3">
                                  <SelectValue placeholder="Selecciona un nom d'usuari" />
                                </SelectTrigger>
                                <SelectContent>
                                  {usernames.map((username) => (
                                    <SelectItem key={username} value={username}>
                                      {username}
                                    </SelectItem>
                                  ))}
                                </SelectContent>
                              </Select>
                            </div>
                          </div>
                          <DialogFooter>
                            <Button type="submit" onClick={handleEditEmployee}>Desar Canvis</Button>
                          </DialogFooter>
                        </DialogContent>
                      </Dialog>
                      <Button variant="outline" size="sm" className="text-red-600" onClick={() => handleDeleteEmployee(employee.id)}>
                        <Trash2 className="mr-2 h-4 w-4" />
                        Eliminar
                      </Button>
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

