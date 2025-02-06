'use client'

import { useState, useEffect } from 'react'
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

type LlocTreball = {
  id: number
  posicio: string
}

export default function LlocsTreballPage() {
  const { toast } = useToast()
  const [llocsTreball, setLlocsTreball] = useState<LlocTreball[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [searchTerm, setSearchTerm] = useState('')
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [newPosicio, setNewPosicio] = useState('')
  const [editingLloc, setEditingLloc] = useState<LlocTreball | null>(null)

  useEffect(() => {
    fetchLlocsTreball()
  }, [])

  const fetchLlocsTreball = async () => {
    setIsLoading(true)
    try {
      const response = await fetch('http://10.4.41.33:8080/lloctreball')
      if (!response.ok) {
        throw new Error('Hi ha hagut un error en recuperar els llocs de treball.')
      }
      const data = await response.json()
      setLlocsTreball(data)
    } catch (err) {
      console.error(err)
      setError('Hi ha hagut un error en recuperar els llocs de treball. Torna-ho a provar.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleAddLlocTreball = async () => {
    try {
      const response = await fetch('http://10.4.41.33:8080/lloctreball', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ posicio: newPosicio }),
      })

      if (!response.ok) {
        throw new Error('Failed to add lloc de treball')
      }

      toast({
        title: "Èxit",
        description: "Lloc de treball afegit correctament",
      })

      setNewPosicio('')
      setIsAddDialogOpen(false)
      fetchLlocsTreball()
    } catch (error) {
      console.error('Error adding lloc de treball:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut afegir el lloc de treball. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleEditLlocTreball = async () => {
    if (!editingLloc) return

    try {
      const response = await fetch(`http://10.4.41.33:8080/lloctreball/${editingLloc.posicio}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editingLloc),
      })

      if (!response.ok) {
        throw new Error('Failed to update lloc de treball')
      }

      toast({
        title: "Èxit",
        description: "Lloc de treball actualitzat correctament",
      })

      setEditingLloc(null)
      fetchLlocsTreball()
    } catch (error) {
      console.error('Error updating lloc de treball:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut actualitzar el lloc de treball. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleDeleteLlocTreball = async (id: string) => {
    try {
      const response = await fetch(`http://10.4.41.33:8080/lloctreball/${id}`, {
        method: 'DELETE',
      })

      if (!response.ok) {
        throw new Error('Failed to delete lloc de treball')
      }

      toast({
        title: "Èxit",
        description: "Lloc de treball eliminat correctament",
      })

      fetchLlocsTreball()
    } catch (error) {
      console.error('Error deleting lloc de treball:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut eliminar el lloc de treball. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const filteredLlocsTreball = llocsTreball.filter(lloc =>
    lloc.posicio.toLowerCase().includes(searchTerm.toLowerCase())
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
          <CardTitle className="text-2xl font-bold">Llocs de Treball</CardTitle>
          <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
            <DialogTrigger asChild>
              <Button>
                <PlusCircle className="mr-2 h-4 w-4" /> Afegir Lloc de Treball
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
              <DialogHeader>
                <DialogTitle>Afegir Nou Lloc de Treball</DialogTitle>
                <DialogDescription>
                  Introdueix el nom de la nova posició laboral aquí.
                </DialogDescription>
              </DialogHeader>
              <div className="grid gap-4 py-4">
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="posicio" className="text-right">
                    Posició
                  </Label>
                  <Input
                    id="posicio"
                    value={newPosicio}
                    onChange={(e) => setNewPosicio(e.target.value)}
                    className="col-span-3"
                  />
                </div>
              </div>
              <DialogFooter>
                <Button type="submit" onClick={handleAddLlocTreball}>Afegir</Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </CardHeader>
        <CardContent>
          <div className="mb-4">
            <Input
              placeholder="Cercar llocs de treball..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="max-w-sm"
            />
          </div>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Posició</TableHead>
                <TableHead className="text-right">Accions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {isLoading ? (
                <TableRow>
                  <TableCell colSpan={2} className="text-center">Carregant...</TableCell>
                </TableRow>
              ) : filteredLlocsTreball.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={2} className="text-center">No s&apos;han trobat llocs de treball</TableCell>
                </TableRow>
              ) : (
                filteredLlocsTreball.map((lloc) => (
                  <TableRow key={lloc.posicio}>
                    <TableCell className="font-medium">{lloc.posicio}</TableCell>
                    <TableCell className="text-right">
                      <Dialog>
                        <DialogTrigger asChild>
                          <Button variant="outline" size="sm" className="mr-2" onClick={() => setEditingLloc(lloc)}>
                            <Pencil className="mr-2 h-4 w-4" />
                            Editar
                          </Button>
                        </DialogTrigger>
                        <DialogContent className="sm:max-w-[425px]">
                          <DialogHeader>
                            <DialogTitle>Editar Lloc de Treball</DialogTitle>
                            <DialogDescription>
                              Modifica el nom de la posició laboral aquí.
                            </DialogDescription>
                          </DialogHeader>
                          <div className="grid gap-4 py-4">
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-posicio" className="text-right">
                                Posició
                              </Label>
                              <Input
                                id="edit-posicio"
                                value={editingLloc?.posicio || ''}
                                onChange={(e) => setEditingLloc(editingLloc ? { ...editingLloc, posicio: e.target.value } : null)}
                                className="col-span-3"
                              />
                            </div>
                          </div>
                          <DialogFooter>
                            <Button type="submit" onClick={handleEditLlocTreball}>Desar Canvis</Button>
                          </DialogFooter>
                        </DialogContent>
                      </Dialog>
                      <Button variant="outline" size="sm" className="text-red-600" onClick={() => handleDeleteLlocTreball(lloc.posicio)}>
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

