'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { useToast } from "@/hooks/use-toast"

type ProjectCost = {
  projectName: string
  totalCost: number
  tecnic_cost: { [key: string]: number }[]
}

type CostData = {
  cost_ma_obra: number
  cost_material: number
  cost_total: number
  projectes_cost: ProjectCost[]
}

export default function ProjectCostsPage() {
  const [costData, setCostData] = useState<CostData | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const { toast } = useToast()

  useEffect(() => {
    fetchCostData()
  }, [])

  const fetchCostData = async () => {
    setIsLoading(true)
    try {
      const response = await fetch('http://10.4.41.40:8080/costos')
      if (!response.ok) {
        throw new Error('Failed to fetch cost data')
      }
      const data = await response.json()
      setCostData(data)
    } catch (error) {
      console.error('Error fetching cost data:', error)
      toast({
        title: "Error",
        description: "No s'han pogut carregar les dades de cost. Torna-ho a provar.",
        variant: "destructive",
      })
    } finally {
      setIsLoading(false)
    }
  }

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('ca-ES', { style: 'currency', currency: 'EUR' }).format(amount)
  }

  return (
    <div className="container mx-auto py-10">
      <Card>
        <CardHeader>
          <CardTitle>Costos dels Projectes</CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="text-center mt-6">Carregant...</div>
          ) : costData ? (
            <>
              <div className="grid grid-cols-3 gap-4 mb-6">
                <Card>
                  <CardHeader>
                    <CardTitle>Cost Mà d&apos;Obra</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <p className="text-2xl font-bold">{formatCurrency(costData.cost_ma_obra)}</p>
                  </CardContent>
                </Card>
                <Card>
                  <CardHeader>
                    <CardTitle>Cost Material</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <p className="text-2xl font-bold">{formatCurrency(costData.cost_material)}</p>
                  </CardContent>
                </Card>
                <Card>
                  <CardHeader>
                    <CardTitle>Cost Total</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <p className="text-2xl font-bold">{formatCurrency(costData.cost_total)}</p>
                  </CardContent>
                </Card>
              </div>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Projecte</TableHead>
                    <TableHead>Tècnic</TableHead>
                    <TableHead>Cost</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {costData.projectes_cost.map((project) => (
                    <>
                      {project.tecnic_cost.map((tecnic, index) => {
                        const [username, cost] = Object.entries(tecnic)[0];
                        return (
                          <TableRow key={`${project.projectName}-${username}`}>
                            {index === 0 && (
                              <TableCell rowSpan={project.tecnic_cost.length + 1}>
                                {project.projectName}
                              </TableCell>
                            )}
                            <TableCell>{username}</TableCell>
                            <TableCell>{formatCurrency(cost)}</TableCell>
                          </TableRow>
                        );
                      })}
                      <TableRow key={`${project.projectName}-total`} className="font-bold">
                        <TableCell>Total</TableCell>
                        <TableCell>{formatCurrency(project.totalCost)}</TableCell>
                      </TableRow>
                    </>
                  ))}
                </TableBody>
              </Table>
            </>
          ) : (
            <div className="text-center mt-6">No hi ha dades disponibles</div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

