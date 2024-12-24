'use client'

import { useState } from 'react'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { X, Pencil } from 'lucide-react'

type Technician = {
  id: string
  nom: string
  horesContracte: number
  sou: number
  posicio: string
}

type Material = {
  id: string
  descripcio: string
  quantitat: number
  preuAlquiler: number
}

export default function TechnicalPage() {
  const [technicians, setTechnicians] = useState<Technician[]>([
    { id: "T1", nom: "Joan Pérez", horesContracte: 40, sou: 30000, posicio: "Tècnic de so" },
    { id: "T2", nom: "Maria García", horesContracte: 35, sou: 28000, posicio: "Tècnic de llums" },
  ])
  const [materials, setMaterials] = useState<Material[]>([
    { id: "M1", descripcio: "Micròfon Shure SM58", quantitat: 10, preuAlquiler: 20 },
    { id: "M2", descripcio: "Altaveu JBL EON612", quantitat: 4, preuAlquiler: 50 },
  ])

  const [newTechnician, setNewTechnician] = useState<Omit<Technician, 'id'>>({
    nom: '',
    horesContracte: 0,
    sou: 0,
    posicio: ''
  })

  const [newMaterial, setNewMaterial] = useState<Omit<Material, 'id'>>({
    descripcio: '',
    quantitat: 0,
    preuAlquiler: 0
  })

  const addTechnician = () => {
    const id = `T${technicians.length + 1}`
    setTechnicians([...technicians, { id, ...newTechnician }])
    setNewTechnician({ nom: '', horesContracte: 0, sou: 0, posicio: '' })
  }

  const addMaterial = () => {
    const id = `M${materials.length + 1}`
    setMaterials([...materials, { id, ...newMaterial }])
    setNewMaterial({ descripcio: '', quantitat: 0, preuAlquiler: 0 })
  }

  const deleteTechnician = (id: string) => {
    setTechnicians(technicians.filter(tech => tech.id !== id))
  }

  const deleteMaterial = (id: string) => {
    setMaterials(materials.filter(material => material.id !== id))
  }

  return (
    <div className="container mx-auto py-10 space-y-8">
      <div className="space-y-4">
        <h2 className="text-xl font-semibold">Tècnics:</h2>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>ID</TableHead>
              <TableHead>NOM</TableHead>
              <TableHead>HORES CONTRACTE</TableHead>
              <TableHead>SOU</TableHead>
              <TableHead>POSICIÓ</TableHead>
              <TableHead className="w-[100px]">Accions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {technicians.map((tech) => (
              <TableRow key={tech.id}>
                <TableCell>{tech.id}</TableCell>
                <TableCell>{tech.nom}</TableCell>
                <TableCell>{tech.horesContracte}</TableCell>
                <TableCell>{tech.sou}</TableCell>
                <TableCell>{tech.posicio}</TableCell>
                <TableCell className="flex gap-2">
                  <Button variant="destructive" size="icon" className="h-8 w-8" onClick={() => deleteTechnician(tech.id)}>
                    <X className="h-4 w-4" />
                  </Button>
                  <Button variant="outline" size="icon" className="h-8 w-8">
                    <Pencil className="h-4 w-4" />
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      <div className="space-y-4">
        <h2 className="text-xl font-semibold">Material:</h2>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>ID</TableHead>
              <TableHead>DESCRIPCIÓ</TableHead>
              <TableHead>QUANTITAT</TableHead>
              <TableHead>PREU ALQUILER</TableHead>
              <TableHead className="w-[100px]">Accions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {materials.map((material) => (
              <TableRow key={material.id}>
                <TableCell>{material.id}</TableCell>
                <TableCell>{material.descripcio}</TableCell>
                <TableCell>{material.quantitat}</TableCell>
                <TableCell>{material.preuAlquiler}</TableCell>
                <TableCell className="flex gap-2">
                  <Button variant="destructive" size="icon" className="h-8 w-8" onClick={() => deleteMaterial(material.id)}>
                    <X className="h-4 w-4" />
                  </Button>
                  <Button variant="outline" size="icon" className="h-8 w-8">
                    <Pencil className="h-4 w-4" />
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      <div className="flex gap-4">
        <Dialog>
          <DialogTrigger asChild>
            <Button className="bg-red-600 hover:bg-red-700">
              Afegir tècnic
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Afegir nou tècnic</DialogTitle>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <Input 
                placeholder="Nom" 
                value={newTechnician.nom} 
                onChange={(e) => setNewTechnician({...newTechnician, nom: e.target.value})}
              />
              <Input 
                type="number" 
                placeholder="Hores contracte" 
                value={newTechnician.horesContracte} 
                onChange={(e) => setNewTechnician({...newTechnician, horesContracte: Number(e.target.value)})}
              />
              <Input 
                type="number" 
                placeholder="Sou" 
                value={newTechnician.sou} 
                onChange={(e) => setNewTechnician({...newTechnician, sou: Number(e.target.value)})}
              />
              <Input 
                placeholder="Posició" 
                value={newTechnician.posicio} 
                onChange={(e) => setNewTechnician({...newTechnician, posicio: e.target.value})}
              />
              <Button onClick={addTechnician}>Afegir</Button>
            </div>
          </DialogContent>
        </Dialog>

        <Dialog>
          <DialogTrigger asChild>
            <Button className="bg-red-600 hover:bg-red-700">
              Afegir material
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Afegir nou material</DialogTitle>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <Input 
                placeholder="Descripció" 
                value={newMaterial.descripcio} 
                onChange={(e) => setNewMaterial({...newMaterial, descripcio: e.target.value})}
              />
              <Input 
                type="number" 
                placeholder="Quantitat" 
                value={newMaterial.quantitat} 
                onChange={(e) => setNewMaterial({...newMaterial, quantitat: Number(e.target.value)})}
              />
              <Input 
                type="number" 
                placeholder="Preu Alquiler" 
                value={newMaterial.preuAlquiler} 
                onChange={(e) => setNewMaterial({...newMaterial, preuAlquiler: Number(e.target.value)})}
              />
              <Button onClick={addMaterial}>Afegir</Button>
            </div>
          </DialogContent>
        </Dialog>
      </div>
    </div>
  )
}

