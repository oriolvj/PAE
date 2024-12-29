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

type Tecnic = {
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

export default function TecnicsPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [Tecnics, setTecnics] = useState<Tecnic[]>([])
  const [positions, setPositions] = useState<Position[]>([])
  const [usernames, setUsernames] = useState<string[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [searchTerm, setSearchTerm] = useState('')
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [newTecnic, setNewTecnic] = useState<Omit<Tecnic, 'id'>>({
    nom: '',
    hores_contracte: 0,
    sou: 0,
    posicio: '',
    username: ''
  })
  const [editingTecnic, setEditingTecnic] = useState<Tecnic | null>(null)

  useEffect(() => {
    fetchTecnics()
    fetchPositions()
    fetchUsernames()
  }, [])

  const fetchTecnics = async () => {
    setIsLoading(true)
    try {
      const response = await fetch('http://10.4.41.40:8080/tecnics')
      if (!response.ok) {
        throw new Error('Hi ha hagut un error en recuperar els Tecnics.')
      }
      const data = await response.json()
      setTecnics(data)
    } catch (err) {
      setError('Hi ha hagut un error en recuperar els Tecnics. Torna-ho a provar.')
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
      const response = await fetch('http://10.4.41.40:8080/usuaris/usernames')
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

  const handleAddTecnic = async () => {
    try {
      const response = await fetch('http://10.4.41.40:8080/tecnics', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newTecnic),
      })

      if (!response.ok) {
        throw new Error('Failed to add Tecnic')
      }

      toast({
        title: "Èxit",
        description: "Tecnic afegit correctament",
      })

      setNewTecnic({
        nom: '',
        hores_contracte: 0,
        sou: 0,
        posicio: '',
        username: ''
      })
      setIsAddDialogOpen(false)
      fetchTecnics()
    } catch (error) {
      console.error('Error adding Tecnic:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut afegir l'Tecnic. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleEditTecnic = async () => {
    if (!editingTecnic) return

    try {
      const response = await fetch(`http://10.4.41.40:8080/tecnics/${editingTecnic.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editingTecnic),
      })

      if (!response.ok) {
        throw new Error('Failed to update Tecnic')
      }

      toast({
        title: "Èxit",
        description: "Tecnic actualitzat correctament",
      })

      setEditingTecnic(null)
      fetchTecnics()
    } catch (error) {
      console.error('Error updating Tecnic:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut actualitzar l'Tecnic. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleDeleteTecnic = async (id: number) => {
    try {
      const response = await fetch(`http://10.4.41.40:8080/tecnics/${id}`, {
        method: 'DELETE',
      })

      if (!response.ok) {
        throw new Error('Fallada al borrar el tècnic')
      }

      toast({
        title: "Èxit",
        description: "Tècnic eliminat correctament",
      })

      fetchTecnics()
    } catch (error) {
      console.error('Error esborrant el tècnic:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut eliminar el tècnic. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const filteredTecnics = Tecnics.filter(Tecnic =>
    Tecnic.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    Tecnic.posicio.toLowerCase().includes(searchTerm.toLowerCase()) ||
    Tecnic.username.toLowerCase().includes(searchTerm.toLowerCase())
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
          <CardTitle className="text-2xl font-bold">Gestió d'Tecnics</CardTitle>
          <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
            <DialogTrigger asChild>
              <Button>
                <PlusCircle className="mr-2 h-4 w-4" /> Afegir Tecnic
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
              <DialogHeader>
                <DialogTitle>Afegir Nou Tecnic</DialogTitle>
                <DialogDescription>
                  Introdueix les dades del nou Tecnic aquí.
                </DialogDescription>
              </DialogHeader>
              <div className="grid gap-4 py-4">
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="nom" className="text-right">
                    Nom
                  </Label>
                  <Input
                    id="nom"
                    value={newTecnic.nom}
                    onChange={(e) => setNewTecnic({...newTecnic, nom: e.target.value})}
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
                    value={newTecnic.hores_contracte}
                    onChange={(e) => setNewTecnic({...newTecnic, hores_contracte: parseInt(e.target.value)})}
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
                    value={newTecnic.sou}
                    onChange={(e) => setNewTecnic({...newTecnic, sou: parseInt(e.target.value)})}
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="posicio" className="text-right">
                    Posició
                  </Label>
                  <Select
                    value={newTecnic.posicio}
                    onValueChange={(value) => setNewTecnic({...newTecnic, posicio: value})}
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
                    value={newTecnic.username}
                    onValueChange={(value) => setNewTecnic({...newTecnic, username: value})}
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
                <Button type="submit" onClick={handleAddTecnic}>Afegir</Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </CardHeader>
        <CardContent>
          <div className="mb-4">
            <Input
              placeholder="Cercar Tecnics..."
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
              ) : filteredTecnics.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center">No s'han trobat Tecnics</TableCell>
                </TableRow>
              ) : (
                filteredTecnics.map((Tecnic) => (
                  <TableRow key={Tecnic.id}>
                    <TableCell className="font-medium">{Tecnic.nom}</TableCell>
                    <TableCell>{Tecnic.hores_contracte}</TableCell>
                    <TableCell>{Tecnic.sou}</TableCell>
                    <TableCell>{Tecnic.posicio}</TableCell>
                    <TableCell>{Tecnic.username}</TableCell>
                    <TableCell className="text-right">
                      <Dialog>
                        <DialogTrigger asChild>
                          <Button variant="outline" size="sm" className="mr-2" onClick={() => setEditingTecnic(Tecnic)}>
                            <Pencil className="mr-2 h-4 w-4" />
                            Editar
                          </Button>
                        </DialogTrigger>
                        <DialogContent className="sm:max-w-[425px]">
                          <DialogHeader>
                            <DialogTitle>Editar Tecnic</DialogTitle>
                            <DialogDescription>
                              Modifica les dades de l'Tecnic aquí.
                            </DialogDescription>
                          </DialogHeader>
                          <div className="grid gap-4 py-4">
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-nom" className="text-right">
                                Nom
                              </Label>
                              <Input
                                id="edit-nom"
                                value={editingTecnic?.nom || ''}
                                onChange={(e) => setEditingTecnic(editingTecnic ? { ...editingTecnic, nom: e.target.value } : null)}
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
                                value={editingTecnic?.hores_contracte || 0}
                                onChange={(e) => setEditingTecnic(editingTecnic ? { ...editingTecnic, hores_contracte: parseInt(e.target.value) } : null)}
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
                                value={editingTecnic?.sou || 0}
                                onChange={(e) => setEditingTecnic(editingTecnic ? { ...editingTecnic, sou: parseInt(e.target.value) } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-posicio" className="text-right">
                                Posició
                              </Label>
                              <Select
                                value={editingTecnic?.posicio || ''}
                                onValueChange={(value) => setEditingTecnic(editingTecnic ? { ...editingTecnic, posicio: value } : null)}
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
                                value={editingTecnic?.username || ''}
                                onValueChange={(value) => setEditingTecnic(editingTecnic ? { ...editingTecnic, username: value } : null)}
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
                            <Button type="submit" onClick={handleEditTecnic}>Desar Canvis</Button>
                          </DialogFooter>
                        </DialogContent>
                      </Dialog>
                      <Button variant="outline" size="sm" className="text-red-600" onClick={() => handleDeleteTecnic(Tecnic.id)}>
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

