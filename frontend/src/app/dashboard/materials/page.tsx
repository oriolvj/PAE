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
import { Switch } from "@/components/ui/switch"

type Material = {
  id: number
  marca: string
  model: string
  quantitat: number
  preu_lloguer: number
  propietari: boolean
}

export default function MaterialsPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [materials, setMaterials] = useState<Material[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [searchTerm, setSearchTerm] = useState('')
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [newMaterial, setNewMaterial] = useState<Omit<Material, 'id'>>({
    marca: '',
    model: '',
    quantitat: 0,
    preu_lloguer: 0,
    propietari: false
  })
  const [editingMaterial, setEditingMaterial] = useState<Material | null>(null)

  useEffect(() => {
    fetchMaterials()
  }, [])

  const fetchMaterials = async () => {
    setIsLoading(true)
    try {
      const response = await fetch('http://10.4.41.40:8080/materials')
      if (!response.ok) {
        throw new Error('Hi ha hagut un error en recuperar els materials.')
      }
      const data = await response.json()
      setMaterials(data)
    } catch (err) {
      setError('Hi ha hagut un error en recuperar els materials. Torna-ho a provar.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleAddMaterial = async () => {
    try {
      const response = await fetch('http://10.4.41.40:8080/materials', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newMaterial),
      })

      if (!response.ok) {
        throw new Error('Failed to add material')
      }

      toast({
        title: "Èxit",
        description: "Material afegit correctament",
      })

      setNewMaterial({
        marca: '',
        model: '',
        quantitat: 0,
        preu_lloguer: 0,
        propietari: false
      })
      setIsAddDialogOpen(false)
      fetchMaterials()
    } catch (error) {
      console.error('Error adding material:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut afegir el material. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleEditMaterial = async () => {
    if (!editingMaterial) return

    try {
      const response = await fetch(`http://10.4.41.40:8080/materials/${editingMaterial.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editingMaterial),
      })

      if (!response.ok) {
        throw new Error('Failed to update material')
      }

      toast({
        title: "Èxit",
        description: "Material actualitzat correctament",
      })

      setEditingMaterial(null)
      fetchMaterials()
    } catch (error) {
      console.error('Error updating material:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut actualitzar el material. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const handleDeleteMaterial = async (id: number) => {
    try {
      const response = await fetch(`http://10.4.41.40:8080/materials/${id}`, {
        method: 'DELETE',
      })

      if (!response.ok) {
        throw new Error('Failed to delete material')
      }

      toast({
        title: "Èxit",
        description: "Material eliminat correctament",
      })

      fetchMaterials()
    } catch (error) {
      console.error('Error deleting material:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut eliminar el material. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  const filteredMaterials = materials.filter(material =>
    material.marca.toLowerCase().includes(searchTerm.toLowerCase()) ||
    material.model.toLowerCase().includes(searchTerm.toLowerCase())
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
          <CardTitle className="text-2xl font-bold">Gestió de Materials</CardTitle>
          <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
            <DialogTrigger asChild>
              <Button>
                <PlusCircle className="mr-2 h-4 w-4" /> Afegir Material
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
              <DialogHeader>
                <DialogTitle>Afegir Nou Material</DialogTitle>
                <DialogDescription>
                  Introdueix les dades del nou material aquí.
                </DialogDescription>
              </DialogHeader>
              <div className="grid gap-4 py-4">
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="marca" className="text-right">
                    Marca
                  </Label>
                  <Input
                    id="marca"
                    value={newMaterial.marca}
                    onChange={(e) => setNewMaterial({...newMaterial, marca: e.target.value})}
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="model" className="text-right">
                    Model
                  </Label>
                  <Input
                    id="model"
                    value={newMaterial.model}
                    onChange={(e) => setNewMaterial({...newMaterial, model: e.target.value})}
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="quantitat" className="text-right">
                    Quantitat
                  </Label>
                  <Input
                    id="quantitat"
                    type="number"
                    value={newMaterial.quantitat}
                    onChange={(e) => setNewMaterial({...newMaterial, quantitat: parseInt(e.target.value)})}
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="preu_lloguer" className="text-right">
                    Preu Lloguer
                  </Label>
                  <Input
                    id="preu_lloguer"
                    type="number"
                    value={newMaterial.preu_lloguer}
                    onChange={(e) => setNewMaterial({...newMaterial, preu_lloguer: parseInt(e.target.value)})}
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="propietari" className="text-right">
                    Propietari
                  </Label>
                  <Switch
                    id="propietari"
                    checked={newMaterial.propietari}
                    onCheckedChange={(checked) => setNewMaterial({...newMaterial, propietari: checked})}
                  />
                </div>
              </div>
              <DialogFooter>
                <Button type="submit" onClick={handleAddMaterial}>Afegir</Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </CardHeader>
        <CardContent>
          <div className="mb-4">
            <Input
              placeholder="Cercar materials..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="max-w-sm"
            />
          </div>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Marca</TableHead>
                <TableHead>Model</TableHead>
                <TableHead>Quantitat</TableHead>
                <TableHead>Preu Lloguer</TableHead>
                <TableHead>Propietari</TableHead>
                <TableHead className="text-right">Accions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {isLoading ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center">Carregant...</TableCell>
                </TableRow>
              ) : filteredMaterials.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} className="text-center">No s'han trobat materials</TableCell>
                </TableRow>
              ) : (
                filteredMaterials.map((material) => (
                  <TableRow key={material.id}>
                    <TableCell className="font-medium">{material.marca}</TableCell>
                    <TableCell>{material.model}</TableCell>
                    <TableCell>{material.quantitat}</TableCell>
                    <TableCell>{material.preu_lloguer}</TableCell>
                    <TableCell>{material.propietari ? 'Sí' : 'No'}</TableCell>
                    <TableCell className="text-right">
                      <Dialog>
                        <DialogTrigger asChild>
                          <Button variant="outline" size="sm" className="mr-2" onClick={() => setEditingMaterial(material)}>
                            <Pencil className="mr-2 h-4 w-4" />
                            Editar
                          </Button>
                        </DialogTrigger>
                        <DialogContent className="sm:max-w-[425px]">
                          <DialogHeader>
                            <DialogTitle>Editar Material</DialogTitle>
                            <DialogDescription>
                              Modifica les dades del material aquí.
                            </DialogDescription>
                          </DialogHeader>
                          <div className="grid gap-4 py-4">
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-marca" className="text-right">
                                Marca
                              </Label>
                              <Input
                                id="edit-marca"
                                value={editingMaterial?.marca || ''}
                                onChange={(e) => setEditingMaterial(editingMaterial ? { ...editingMaterial, marca: e.target.value } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-model" className="text-right">
                                Model
                              </Label>
                              <Input
                                id="edit-model"
                                value={editingMaterial?.model || ''}
                                onChange={(e) => setEditingMaterial(editingMaterial ? { ...editingMaterial, model: e.target.value } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-quantitat" className="text-right">
                                Quantitat
                              </Label>
                              <Input
                                id="edit-quantitat"
                                type="number"
                                value={editingMaterial?.quantitat || 0}
                                onChange={(e) => setEditingMaterial(editingMaterial ? { ...editingMaterial, quantitat: parseInt(e.target.value) } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-preu_lloguer" className="text-right">
                                Preu Lloguer
                              </Label>
                              <Input
                                id="edit-preu_lloguer"
                                type="number"
                                value={editingMaterial?.preu_lloguer || 0}
                                onChange={(e) => setEditingMaterial(editingMaterial ? { ...editingMaterial, preu_lloguer: parseInt(e.target.value) } : null)}
                                className="col-span-3"
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="edit-propietari" className="text-right">
                                Propietari
                              </Label>
                              <Switch
                                id="edit-propietari"
                                checked={editingMaterial?.propietari || false}
                                onCheckedChange={(checked) => setEditingMaterial(editingMaterial ? { ...editingMaterial, propietari: checked } : null)}
                              />
                            </div>
                          </div>
                          <DialogFooter>
                            <Button type="submit" onClick={handleEditMaterial}>Desar Canvis</Button>
                          </DialogFooter>
                        </DialogContent>
                      </Dialog>
                      <Button variant="outline" size="sm" className="text-red-600" onClick={() => handleDeleteMaterial(material.id)}>
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

