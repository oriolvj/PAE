'use client'

import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

type ProjectCost = {
  id: string
  name: string
  laborCost: number
  materialCost: number
  totalCost: number
}

export default function ProjectCostsPage() {
  const [projects] = useState<ProjectCost[]>([
    { id: '1', name: 'Projecte A', laborCost: 5000, materialCost: 2000, totalCost: 7000 },
    { id: '2', name: 'Projecte B', laborCost: 7500, materialCost: 3500, totalCost: 11000 },
    { id: '3', name: 'Projecte C', laborCost: 6000, materialCost: 1500, totalCost: 7500 },
  ])

  const totalLaborCost = projects.reduce((sum, project) => sum + project.laborCost, 0)
  const totalMaterialCost = projects.reduce((sum, project) => sum + project.materialCost, 0)
  const totalCost = projects.reduce((sum, project) => sum + project.totalCost, 0)

  return (
    <div className="container mx-auto py-10">
      <Card>
        <CardHeader>
          <CardTitle>Costos dels projectes</CardTitle>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Projecte</TableHead>
                <TableHead>Cost de mà d'obra</TableHead>
                <TableHead>Cost de material</TableHead>
                <TableHead>Cost total</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {projects.map((project) => (
                <TableRow key={project.id}>
                  <TableCell>{project.name}</TableCell>
                  <TableCell>{project.laborCost.toLocaleString()} €</TableCell>
                  <TableCell>{project.materialCost.toLocaleString()} €</TableCell>
                  <TableCell>{project.totalCost.toLocaleString()} €</TableCell>
                </TableRow>
              ))}
              <TableRow className="font-bold">
                <TableCell>Total</TableCell>
                <TableCell>{totalLaborCost.toLocaleString()} €</TableCell>
                <TableCell>{totalMaterialCost.toLocaleString()} €</TableCell>
                <TableCell>{totalCost.toLocaleString()} €</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
}

